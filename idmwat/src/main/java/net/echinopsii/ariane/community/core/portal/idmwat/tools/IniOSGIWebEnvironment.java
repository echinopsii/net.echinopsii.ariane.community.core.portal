/**
 * Portal IDM wat bundle
 * Shiro Resource Web Environment loader
 * Copyright (C) 2013 Mathilde Ffrench
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.echinopsii.ariane.community.core.portal.idmwat.tools;

import net.echinopsii.ariane.community.core.portal.idmwat.plugin.SecurityManagerProxyConsumer;
import org.apache.shiro.ShiroException;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.IniFactorySupport;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.Destroyable;
import org.apache.shiro.util.Initializable;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.apache.shiro.web.env.ResourceBasedWebEnvironment;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * This class extends the Shiro ResourceBasedWebEnvironment in order to enable Shiro ini config file splitting accross different war which share the same Shiro environment
 * (which is loaded by web security manager proxy).<br/>
 * Then each war can add some specific shiro web configuration (containing only shiro webapp filter and urls).<br/><br/>
 *
 * Example of valid war shiro configuration :
 * <pre>
 *     [urls]
 *      /index.html = anon
 *      /views/** = authc
 *      /rest/** = authc, rest
 * </pre><br/><br/>
 *
 * Example of Shiro war configuration (web.xml) to use this Shiro web environment implementation :
 * <pre>
 *  <!-- SHIRO CONFIGURATION -->
 *  <context-param>
 *      <param-name>shiroEnvironmentClass</param-name>
 *      <param-value>net.echinopsii.ariane.core.portal.idm.tools.IniOSGIWebEnvironment</param-value>
 *  </context-param>
 *  <listener>
 *      <listener-class>org.apache.shiro.web.env.EnvironmentLoaderListener</listener-class>
 *  </listener>
 *  <filter>
 *      <filter-name>ShiroFilter</filter-name>
 *      <filter-class>org.apache.shiro.web.servlet.ShiroFilter</filter-class>
 *  </filter>
 *  <filter-mapping>
 *      <filter-name>ShiroFilter</filter-name>
 *      <url-pattern>/*</url-pattern>
 *      <dispatcher>REQUEST</dispatcher>
 *      <dispatcher>FORWARD</dispatcher>
 *      <dispatcher>INCLUDE</dispatcher>
 *      <dispatcher>ERROR</dispatcher>
 *  </filter-mapping>
 * </pre><br/><br/>
 *
 * NOTE:  the splitted Shiro web configuration is working well on war bundle startup but there is a bad behavior when stopping a war bundle as it stop the entire shared
 * Shiro environment and then access to other running war bundle are not working anymore. MUST BE FIXED.
 */
public class IniOSGIWebEnvironment extends ResourceBasedWebEnvironment implements Initializable, Destroyable {
    public static final String DEFAULT_WEB_INI_RESOURCE_PATH = "/WEB-INF/shiro.ini";
    private static final Logger log = LoggerFactory.getLogger(IniOSGIWebEnvironment.class);
    /*
     * this ini extends the main WebSecurityManager ini. It should contains only webapp filters & urls
     */
    private Ini ini;

    /**
     * Init the shared securityManager for the underlying web context and add the war shiro configuration to shared securityManageer...
     *
     * @throws ShiroException
     */
    @Override
    public void init() throws ShiroException {
        String[] configLocations = getConfigLocations();
        if (CollectionUtils.isEmpty(ini)) {
            log.debug("Checking any specified config locations.");
            ini = getSpecifiedIni(configLocations);
        }
        if (CollectionUtils.isEmpty(ini)) {
            log.debug("No INI instance or config locations specified.  Trying default config locations.");
            ini = getDefaultIni();
        }
        /*
        if (CollectionUtils.isEmpty(ini)) {
            String msg = "Shiro INI configuration was either not found or discovered to be empty/unconfigured.";
            throw new ConfigurationException(msg);
        }
        */

        //TODO : check a better way to start war after OSGI layer
        while(SecurityManagerProxyConsumer.getInstance().getWebSecurityManagerProxy()==null)
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        //SecurityManagerProxyConsumer.getInstance().getWebSecurityManagerProxy().init();
        WebSecurityManager securityManager = SecurityManagerProxyConsumer.getInstance().getWebSecurityManagerProxy().getWebSecurityManager();
        setWebSecurityManager(securityManager);
        Map<String, ?> beans = SecurityManagerProxyConsumer.getInstance().getWebSecurityManagerProxy().getWebIniSecurityManagerFactory().getBeans();
        if (!CollectionUtils.isEmpty(beans)) {
            this.objects.putAll(beans);
        }

        log.debug("Web Security Manager {} retrieved from SecurityManagerProxyConsumer...", new Object[]{getWebSecurityManager().toString()});
        if (ini!=null) {
            log.debug("Load webapp ini file...");
            FilterChainResolver resolver = createFilterChainResolver();
            if (resolver != null) {
                setFilterChainResolver(resolver);
            }
        }
    }

    public Ini getIni() {
        return ini;
    }

    public void setIni(Ini ini) {
        this.ini = ini;
    }

    protected FilterChainResolver createFilterChainResolver() {
        FilterChainResolver resolver = null;
        if (!CollectionUtils.isEmpty(ini)) {
            //only create a resolver if the 'filters' or 'urls' sections are defined:
            Ini.Section urls = ini.getSection(IniFilterChainResolverFactory.URLS);
            Ini.Section filters = ini.getSection(IniFilterChainResolverFactory.FILTERS);
            Ini.Section main = ini.getSection(IniSecurityManagerFactory.MAIN_SECTION_NAME);
            if (!CollectionUtils.isEmpty(urls) || !CollectionUtils.isEmpty(main) || !CollectionUtils.isEmpty(filters)) {
                //either the urls section or the filters section was defined.  Go ahead and create the resolver:
                IniFilterChainResolverFactory factory = new IniFilterChainResolverFactory(ini, this.objects);
                resolver = factory.getInstance();
                /*
                if (resolver instanceof  PathMatchingFilterChainResolver) {
                    for (Entry entry : main.entrySet())
                    ((PathMatchingFilterChainResolver) resolver).getFilterChainManager();
                }
                */
            }
        }

        return resolver;
    }

    protected Ini getSpecifiedIni(String[] configLocations) throws ConfigurationException {
        Ini ini = null;

        if (configLocations != null && configLocations.length > 0) {
            if (configLocations.length > 1) {
                log.warn("More than one Shiro .ini config location has been specified.  Only the first will be " +
                                 "used for configuration as the {} implementation does not currently support multiple " +
                                 "files.  This may be supported in the future however.", IniOSGIWebEnvironment.class.getName());
            }
            //required, as it is user specified:
            ini = createIni(configLocations[0], true);
        }

        return ini;
    }

    protected Ini getDefaultIni() {
        Ini ini = null;

        String[] configLocations = getDefaultConfigLocations();
        if (configLocations != null) {
            for (String location : configLocations) {
                ini = createIni(location, false);
                if (!CollectionUtils.isEmpty(ini)) {
                    log.debug("Discovered non-empty INI configuration at location '{}'.  Using for configuration.",
                                     location);
                    break;
                }
            }
        }

        return ini;
    }

    protected Ini createIni(String configLocation, boolean required) throws ConfigurationException {
        Ini ini = null;

        if (configLocation != null) {
            ini = convertPathToIni(configLocation, required);
        }
        if (required && CollectionUtils.isEmpty(ini)) {
            String msg = "Required configuration location '" + configLocation + "' does not exist or did not " +
                                 "contain any INI configuration.";
            throw new ConfigurationException(msg);
        }

        return ini;
    }

    private Ini convertPathToIni(String path, boolean required) {
        //TODO - this logic is ugly - it'd be ideal if we had a Resource API to polymorphically encaspulate this behavior
        Ini ini = null;

        if (StringUtils.hasText(path)) {
            InputStream is = null;
            //SHIRO-178: Check for servlet context resource and not only resource paths:
            if (!ResourceUtils.hasResourcePrefix(path)) {
                is = getServletContextResourceStream(path);
            } else {
                try {
                    is = ResourceUtils.getInputStreamForPath(path);
                } catch (IOException e) {
                    if (required) {
                        throw new ConfigurationException(e);
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("Unable to load optional path '" + path + "'.", e);
                        }
                    }
                }
            }
            if (is != null) {
                ini = new Ini();
                ini.load(is);
            } else {
                if (required) {
                    throw new ConfigurationException("Unable to load resource path '" + path + "'");
                }
            }
        }

        return ini;
    }

    //TODO - this logic is ugly - it'd be ideal if we had a Resource API to polymorphically encaspulate this behavior
    private InputStream getServletContextResourceStream(String path) {
        InputStream is = null;

        path = WebUtils.normalize(path);
        ServletContext sc = getServletContext();
        if (sc != null) {
            is = sc.getResourceAsStream(path);
        }

        return is;
    }

    protected String[] getDefaultConfigLocations() {
        return new String[]{
                                   DEFAULT_WEB_INI_RESOURCE_PATH,
                                   IniFactorySupport.DEFAULT_INI_RESOURCE_PATH
        };
    }
}