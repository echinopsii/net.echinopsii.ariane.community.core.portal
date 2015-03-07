/**
 * Portal base bundle
 * Mail Service implementation
 * Copyright (C) 2015 Mathilde Ffrench
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

package net.echinopsii.ariane.community.core.portal.base.plugin.iPojo;

import net.echinopsii.ariane.community.core.portal.base.model.MailSenderEntity;
import net.echinopsii.ariane.community.core.portal.base.plugin.MailService;
import org.apache.felix.ipojo.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

@Component(managedservice="net.echinopsii.ariane.community.core.PortalMailService")
@Provides
@Instantiate
public class MailServiceImpl implements MailService {

    private static final String MAIL_SERVICE_NAME = "Ariane Portal Mail Service";
    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    private String smtpConfPath;
    private File smtpConfFile;
    private MailSenderEntity smtpConf;

    @Validate
    public void validate() throws Exception {
        log.info("{} is started", new Object[]{MAIL_SERVICE_NAME});
    }

    @Invalidate
    public void invalidate(){ log.info("{} is stopped", new Object[]{MAIL_SERVICE_NAME}); }

    @Updated
    public synchronized void updated(final Dictionary properties) {
        log.debug("{} is being updated by {}", new Object[]{MAIL_SERVICE_NAME, Thread.currentThread().toString()});
        smtpConf = new MailSenderEntity();
        smtpConf.setSubjectPrefix("");
        smtpConf.setSmtpAUTH(false);
        smtpConf.setSmtpPort("25");
        smtpConf.setSmtpSSL(false);
        smtpConf.setSmtpSSL(false);
        smtpConf.setSmtpTLS(false);

        Enumeration<String> dicEnum = properties.keys();
        boolean validProperties = false;

        while (dicEnum.hasMoreElements())
            if (dicEnum.nextElement().equals("net.echinopsii.ariane.community.core.PortalMailService.path")) {
                validProperties = true;
                break;
            }

        dicEnum = properties.keys();
        if (validProperties) {
            log.debug("valid configuration!");
            while (dicEnum.hasMoreElements()) {
                String key = (String) dicEnum.nextElement();
                String value = (String) properties.get(key);

                if (key.equals("net.echinopsii.ariane.community.core.PortalMailService.path")) {
                    this.smtpConfPath = value;
                    this.smtpConfFile = new File(this.smtpConfPath);
                    if (!this.smtpConfFile.exists() || !this.smtpConfFile.canRead() || !this.smtpConfFile.canWrite()) {
                        log.error("Problem with Mail Service Conf File. Update of conf will not work !");
                        this.smtpConfFile = null;
                    }
                } else {
                    switch (key) {
                        case "mail.user":
                            smtpConf.setSmtpUser(value);
                            break;
                        case "mail.password":
                            smtpConf.setSmtpPassword(value);
                            break;
                        case "mail.from":
                            smtpConf.setFrom(value);
                            break;
                        case "mail.subject.prefix":
                            smtpConf.setSubjectPrefix(value);
                            break;
                        case "mail.smtp.host":
                            smtpConf.setSmtpHost(value);
                            break;
                        case "mail.smtp.port":
                            smtpConf.setSmtpPort(value);
                            break;
                        case "mail.smtp.auth":
                            smtpConf.setSmtpAUTH(new Boolean(value));
                            break;
                        case "mail.smtp.tls":
                            smtpConf.setSmtpTLS(new Boolean(value));
                            break;
                        case "mail.smtp.ssl":
                            smtpConf.setSmtpSSL(new Boolean(value));
                            break;
                        case "component":
                            break;
                        default:
                            log.error("({}:{}) incorrect key", new Object[]{key, value});
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void send(List<String> to, String subject, String body) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpConf.getSmtpHost());
        properties.put("mail.smtp.port", smtpConf.getSmtpPort());
        if (smtpConf.isSmtpAUTH()) properties.put("mail.smtp.auth", "true");
        if (smtpConf.isSmtpTLS()) properties.put("mail.smtp.starttls.enable", "true");
        if (smtpConf.isSmtpSSL()) {
            properties.put("mail.smtp.socketFactory.port", smtpConf.getSmtpPort());
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.socketFactory.fallback", "false");
        }

        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpConf.getSmtpUser(),smtpConf.getSmtpPassword());
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(smtpConf.getFrom()));
        for (String toMail : to)
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
        message.setSubject("[" + smtpConf.getSubjectPrefix() + "] " + subject);
        message.setText(body);
        Transport.send(message);
    }

    @Override
    public MailSenderEntity getSmtpConf() {
        return smtpConf;
    }

    @Override
    public void setSmtpConf(MailSenderEntity smtpConf) {
        this.smtpConf = smtpConf;
    }

    @Override
    public void storeSmtpConf() throws IOException {
        if (smtpConfFile!=null && smtpConf!=null) {
            Properties propsToSave = new Properties();
            propsToSave.setProperty("net.echinopsii.ariane.community.core.PortalMailService.path", smtpConfPath);
            propsToSave.setProperty("mail.user", smtpConf.getSmtpUser());
            propsToSave.setProperty("mail.password", smtpConf.getSmtpPassword());
            propsToSave.setProperty("mail.subject.prefix", smtpConf.getSubjectPrefix());
            propsToSave.setProperty("mail.smtp.host", smtpConf.getSmtpHost());
            propsToSave.setProperty("mail.smtp.port", smtpConf.getSmtpPort());
            propsToSave.setProperty("mail.smtp.auth", (smtpConf.isSmtpAUTH()) ? "true" : "false");
            propsToSave.setProperty("mail.smtp.tls", (smtpConf.isSmtpTLS()) ? "true" : "false");
            propsToSave.setProperty("mail.smtp.ssl", (smtpConf.isSmtpSSL()) ? "true" : "false");

            log.debug("save loaded props");
            OutputStream out = ((OutputStream) new FileOutputStream(smtpConfFile));
            propsToSave.store(out, "");
            out.flush();
            out.close();
        }
    }
}