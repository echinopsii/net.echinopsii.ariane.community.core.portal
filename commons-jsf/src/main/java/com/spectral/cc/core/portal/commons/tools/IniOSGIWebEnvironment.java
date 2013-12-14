/**
 * Portal Commons JSF bundle
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

package com.spectral.cc.core.portal.commons.tools;

import com.spectral.cc.core.portal.commons.consumer.SecurityManagerProxyConsumer;
import org.apache.shiro.ShiroException;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.IniFactorySupport;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.Destroyable;
import org.apache.shiro.util.Initializable;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.apache.shiro.web.env.ResourceBasedWebEnvironment;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class IniOSGIWebEnvironment extends ResourceBasedWebEnvironment implements Initializable, Destroyable {
    public static final String DEFAULT_WEB_INI_RESOURCE_PATH = "/WEB-INF/shiro.ini";
    private static final Logger log = LoggerFactory.getLogger(IniOSGIWebEnvironment.class);
    /*
     * this ini extends the main WebSecurityManager ini. It should contains only webapp filters & urls
     */
    private Ini ini;

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
            if (!CollectionUtils.isEmpty(urls) || !CollectionUtils.isEmpty(filters)) {
                //either the urls section or the filters section was defined.  Go ahead and create the resolver:
                IniFilterChainResolverFactory factory = new IniFilterChainResolverFactory(ini, this.objects);
                resolver = factory.getInstance();
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