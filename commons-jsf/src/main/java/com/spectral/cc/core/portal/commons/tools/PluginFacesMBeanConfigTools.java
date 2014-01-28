/**
 * [DEFINE YOUR PROJECT NAME/MODULE HERE]
 * [DEFINE YOUR PROJECT DESCRIPTION HERE] 
 * Copyright (C) 15/01/14 echinopsii
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

import com.spectral.cc.core.portal.commons.facesplugin.iPojo.InjectorPluginFacesMBeanRegistryImpl;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.ApplicationResourceBundle;
import com.sun.faces.config.DbfFactory;
import com.sun.faces.el.ELUtils;
import com.sun.faces.mgbean.BeanManager;
import com.sun.faces.mgbean.ManagedBeanInfo;
import com.sun.faces.util.TypedCollections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

public class PluginFacesMBeanConfigTools {

    private static final Logger log = LoggerFactory.getLogger(InjectorPluginFacesMBeanRegistryImpl.class);

    public static void registerFromDocument(Document dom, ServletContext servletContext) {
        String namespace = dom.getDocumentElement().getNamespaceURI();
        NodeList managedBeans = dom.getDocumentElement().getElementsByTagNameNS(namespace, "managed-bean");
        log.debug("get {} managed beans to add", managedBeans.getLength());
        BeanManager beanManager = ApplicationAssociate.getInstance(servletContext).getBeanManager();
        if ((managedBeans != null) && (managedBeans.getLength() > 0)) {
            for (int m = 0;  m < managedBeans.getLength();  m++) {
                addManagedBean(beanManager, managedBeans.item(m));
            }
        }
    }

    public static void unregisterFromDocument(Document dom, ServletContext servletContext) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        String namespace = dom.getDocumentElement().getNamespaceURI();
        log.debug("Namespaces : {}", namespace);
        NodeList managedBeans = dom.getDocumentElement().getElementsByTagNameNS(namespace, "managed-bean");
        log.debug("get {} managed beans to remove", managedBeans.getLength());
        BeanManager beanManager = ApplicationAssociate.getInstance(servletContext).getBeanManager();
        if ((managedBeans != null) && (managedBeans.getLength() > 0)) {
            for (int m = 0;  m < managedBeans.getLength();  m++) {
                delManagedBean(beanManager, managedBeans.item(m));
            }
        }
    }

    public static Document parseXML(URL facesConfig) {
        Document dom = null;
        // Make an  instance of the DocumentBuilderFactory
        try {
            // use the factory to take an instance of the document builder
            DocumentBuilder db = getNonValidatingBuilder();
            // parse using the builder to get the DOM mapping of the
            // XML file

            String configPath = facesConfig.toURI().toASCIIString();
            log.debug("get w3c document from {}", configPath);
            dom = db.parse(configPath);
            dom.getDocumentElement().normalize();
            log.debug("Root element :" + dom.getDocumentElement().getNodeName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dom;
    }

    /*
     * Bellow are some private tools coming from com.sun.faces.config.processor.ManagedBeanConfigProcessor (CDDLv1 License)
     */
    private static final String MGBEAN_NAME = "managed-bean-name";
    private static final String MGBEAN_CLASS = "managed-bean-class";
    private static final String MGBEAN_SCOPE = "managed-bean-scope";
    private static final String DEFAULT_SCOPE = "request";
    private static final String LIST_ENTRIES = "list-entries";
    private static final String MAP_ENTRIES = "map-entries";
    private static final String MG_PROPERTY = "managed-property";
    private static final String MG_PROPERTY_NAME = "property-name";
    private static final String MG_PROPERTY_TYPE = "property-class";
    private static final String DESCRIPTION = "description";
    private static final String VALUE_CLASS = "value-class";
    private static final String VALUE = "value";
    private static final String NULL_VALUE = "null-value";
    private static final String MAP_KEY_CLASS = "key-class";
    private static final String MAP_ENTRY = "map-entry";
    private static final String KEY = "key";

    private static DocumentBuilder getNonValidatingBuilder() throws Exception {
        DocumentBuilderFactory tFactory = DbfFactory.getFactory();
        tFactory.setValidating(false);
        DocumentBuilder tBuilder = tFactory.newDocumentBuilder();
        tBuilder.setEntityResolver(DbfFactory.FACES_ENTITY_RESOLVER);
        tBuilder.setErrorHandler(DbfFactory.FACES_ERROR_HANDLER);
        return tBuilder;
    }

    private static void delManagedBean(BeanManager beanManager, Node managedBean) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        NodeList children = managedBean.getChildNodes();
        String beanName = null;
        String beanClass = null;
        String beanScope = null;
        ManagedBeanInfo.ListEntry listEntry = null;
        ManagedBeanInfo.MapEntry mapEntry = null;
        List<Node> managedProperties = null;
        List<Node> descriptions = null;

        for (int i = 0, size = children.getLength(); i < size; i++) {
            Node n = children.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                if (MGBEAN_NAME.equals(n.getLocalName())) {
                    beanName = getNodeText(n);
                } else if (MGBEAN_CLASS.equals(n.getLocalName())) {
                    beanClass = getNodeText(n);
                }
            }
        }

        log.debug("Begin unregistring managed bean {}", beanName);

        beanManager.destroy(beanName, Class.forName(beanClass).newInstance());
    }

    private static void addManagedBean(BeanManager beanManager, Node managedBean) {
        NodeList children = managedBean.getChildNodes();
        String beanName = null;
        String beanClass = null;
        String beanScope = null;
        ManagedBeanInfo.ListEntry listEntry = null;
        ManagedBeanInfo.MapEntry mapEntry = null;
        List<Node> managedProperties = null;
        List<Node> descriptions = null;

        for (int i = 0, size = children.getLength(); i < size; i++) {
            Node n = children.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                if (MGBEAN_NAME.equals(n.getLocalName())) {
                    beanName = getNodeText(n);
                } else if (MGBEAN_CLASS.equals(n.getLocalName())) {
                    beanClass = getNodeText(n);
                } else if (MGBEAN_SCOPE.equals(n.getLocalName())) {
                    beanScope = getNodeText(n);
                    if (beanScope == null) {
                        beanScope = DEFAULT_SCOPE;
                    }
                } else if (LIST_ENTRIES.equals(n.getLocalName())) {
                    listEntry = buildListEntry(n);
                } else if (MAP_ENTRIES.equals(n.getLocalName())) {
                    mapEntry = buildMapEntry(n);
                } else if (MG_PROPERTY.equals(n.getLocalName())) {
                    if (managedProperties == null) {
                        managedProperties = new ArrayList<Node>(size);
                    }
                    managedProperties.add(n);
                } else if (DESCRIPTION.equals(n.getLocalName())) {
                    if (descriptions == null) {
                        descriptions = new ArrayList<Node>(4);
                    }
                    descriptions.add(n);
                }
            }
        }

        log.debug("Begin processing managed bean {}", beanName);
        List<ManagedBeanInfo.ManagedProperty> properties = null;
        if (managedProperties != null && !managedProperties.isEmpty()) {
            properties = new ArrayList<ManagedBeanInfo.ManagedProperty>(managedProperties.size());
            for (Node managedProperty : managedProperties) {
                properties.add(buildManagedProperty(managedProperty));
            }
        }

        beanManager.register(new ManagedBeanInfo(beanName, beanClass, beanScope, isEager(managedBean, beanName, beanScope),
                                                        mapEntry, listEntry, properties, getTextMap(descriptions)));
        log.debug("Completed processing bean {}", beanName);
    }

    private static ManagedBeanInfo.ListEntry buildListEntry(Node listEntry) {
        if (listEntry != null) {
            String valueClass = "java.lang.String";
            List<String> values = null;
            NodeList children = listEntry.getChildNodes();
            for (int i = 0, size = children.getLength(); i < size; i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    if (VALUE_CLASS.equals(child.getLocalName())) {
                        valueClass = getNodeText(child);
                    } else if (VALUE.equals(child.getLocalName())) {
                        if (values == null) {
                            values = new ArrayList<String>(size);
                        }
                        values.add(getNodeText(child));
                    } else if (NULL_VALUE.equals(child.getLocalName())) {
                        if (values == null) {
                            values = new ArrayList<String>(size);
                        }
                        values.add(ManagedBeanInfo.NULL_VALUE);
                    }
                }
            }

            log.debug(MessageFormat.format("Created ListEntry valueClass={}, values={}", new Object[]{valueClass, (values != null && !values.isEmpty()) ? values.toString() : "none"}));
            return (new ManagedBeanInfo.ListEntry(valueClass, (values == null) ? TypedCollections.dynamicallyCastList(Collections.emptyList(), String.class) : values));
        }
        return null;
    }


    private static ManagedBeanInfo.MapEntry buildMapEntry(Node mapEntry) {

        if (mapEntry != null) {
            String valueClass = "java.lang.String";
            String keyClass = "java.lang.String";
            Map<String, String> entries = null;
            NodeList children = mapEntry.getChildNodes();
            for (int i = 0, size = children.getLength(); i < size; i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    if (VALUE_CLASS.equals(child.getLocalName())) {
                        valueClass = getNodeText(child);
                    } else if (MAP_KEY_CLASS.equals(child.getLocalName())) {
                        keyClass = getNodeText(child);
                    } else if (MAP_ENTRY.equals(child.getLocalName())) {
                        if (entries == null) {
                            entries = new LinkedHashMap<String,String>(8, 1.0f);
                        }
                        NodeList c = child.getChildNodes();
                        String key = null;
                        String value = null;
                        for (int j = 0, jsize = c.getLength(); j < jsize; j++) {
                            Node node = c.item(j);
                            if (node.getNodeType() == Node.ELEMENT_NODE) {
                                if (KEY.equals(node.getLocalName())) {
                                    key = getNodeText(node);
                                } else if (VALUE.equals(node.getLocalName())) {
                                    value = getNodeText(node);
                                } else
                                if (NULL_VALUE.equals(node.getLocalName())) {
                                    value = ManagedBeanInfo.NULL_VALUE;
                                }
                            }
                        }
                        entries.put(key, value);
                    }
                }
            }
            log.debug(MessageFormat.format("Created MapEntry keyClass={}, valueClass={}, entries={}", new Object[]{keyClass, valueClass, (entries != null) ? entries.toString() : "none"}));
            return (new ManagedBeanInfo.MapEntry(keyClass, valueClass, entries));
        }

        return null;
    }

    private static boolean isEager(Node managedBean, String beanName, String scope)
    {
        NamedNodeMap attributes = managedBean.getAttributes();
        Node eagerNode = attributes.getNamedItem("eager");
        boolean eager = false;
        if (eagerNode != null) {
            eager = Boolean.valueOf(getNodeText(eagerNode)).booleanValue();
            if ((eager) && ((scope == null) || (!ELUtils.Scope.APPLICATION.toString().equals(scope)))) {
                log.debug("jsf.configuration.illegal.eager.bean", new Object[]{beanName, scope});
                eager = false;
            }
        }

        return eager;
    }

    private static ManagedBeanInfo.ManagedProperty buildManagedProperty(Node managedProperty) {
        if (managedProperty != null) {
            String propertyName = null;
            String propertyClass = null;
            String value = null;
            ManagedBeanInfo.MapEntry mapEntry = null;
            ManagedBeanInfo.ListEntry listEntry = null;
            NodeList children = managedProperty.getChildNodes();
            for (int i = 0, size = children.getLength(); i < size; i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    if (MG_PROPERTY_NAME.equals(child.getLocalName())) {
                        propertyName = getNodeText(child);
                    } else if (MG_PROPERTY_TYPE.equals(child.getLocalName())) {
                        propertyClass = getNodeText(child);
                    } else if (VALUE.equals(child.getLocalName())) {
                        value = getNodeText(child);
                    } else if (NULL_VALUE.equals(child.getLocalName())) {
                        value = ManagedBeanInfo.NULL_VALUE;
                    } else if (LIST_ENTRIES.equals(child.getLocalName())) {
                        listEntry = buildListEntry(child);
                    } else if (MAP_ENTRIES.equals(child.getLocalName())) {
                        mapEntry = buildMapEntry(child);
                    }
                }
            }
            log.debug("Adding ManagedProperty propertyName={}, propertyClass={}, propertyValue={}, hasMapEntry={}, hasListEntry={}",
                             new Object[]{
                                                 propertyName,
                                                 ((propertyClass != null) ? propertyClass : "inferred"), value, (mapEntry != null), (listEntry != null)});
            return new ManagedBeanInfo.ManagedProperty(propertyName, propertyClass, value, mapEntry, listEntry);
        }

        return null;
    }

    private static String getNodeText(Node node) {
        String res = null;
        if (node != null) {
            res = node.getTextContent();
            if (res != null) {
                res = res.trim();
            }
        }

        return ((res != null && res.length() != 0) ? res : null);
    }

    private static Map<String, String> getTextMap(List<Node> list) {
        if (list != null && !list.isEmpty()) {
            int len = list.size();
            HashMap<String, String> names =
                    new HashMap<String, String>(len, 1.0f);
            for (int i = 0; i < len; i++) {
                Node node = list.get(i);
                String textValue = getNodeText(node);
                if (textValue != null) {
                    if (node.hasAttributes()) {
                        NamedNodeMap attributes = node
                                                          .getAttributes();
                        String lang
                                = getNodeText(attributes.getNamedItem(
                                                                             "lang"));
                        if (lang == null) {
                            lang =
                                    getNodeText(attributes.getNamedItem(
                                                                               "xml:lang"));
                        }
                        if (lang != null) {
                            names.put(lang, textValue);
                        } else {
                            names.put(ApplicationResourceBundle.DEFAULT_KEY,
                                             textValue);
                        }
                    } else {
                        names.put(ApplicationResourceBundle.DEFAULT_KEY,
                                         textValue);
                    }
                }
            }

            return names;
        }
        return null;
    }
}