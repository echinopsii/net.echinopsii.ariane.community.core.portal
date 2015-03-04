/**
 * Portal wat bundle
 * Mail Service Controller
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

package net.echinopsii.ariane.community.core.portal.wat.controller;

import net.echinopsii.ariane.community.core.portal.base.model.MailSenderEntity;
import net.echinopsii.ariane.community.core.portal.wat.plugin.MailServiceConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class MailServiceController implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(MainMenuController.class);

    MailSenderEntity senderEntity = MailServiceConsumer.getInstance().getMailService().getSmtpConf();

    String mailTestTO;
    String mailTestSubject;
    String mailTestBody;

    public MailSenderEntity getSenderEntity() {
        return senderEntity;
    }

    public String getMailTestTO() {
        return mailTestTO;
    }

    public void setMailTestTO(String mailTestTO) {
        this.mailTestTO = mailTestTO;
    }

    public String getMailTestSubject() {
        return mailTestSubject;
    }

    public void setMailTestSubject(String mailTestSubject) {
        this.mailTestSubject = mailTestSubject;
    }

    public String getMailTestBody() {
        return mailTestBody;
    }

    public void setMailTestBody(String mailTestBody) {
        this.mailTestBody = mailTestBody;
    }

    public void testSenderEntity() {
        ArrayList<String> tol = new ArrayList<>(); tol.add(mailTestTO);
        try {
            MailServiceConsumer.getInstance().getMailService().send(tol, mailTestSubject, mailTestBody);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                "Message sent successfully !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (MessagingException e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Fail sending message !",
                                                       e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            e.printStackTrace();
        }
    }

    public void storeSenderEntity() {
        try {
            MailServiceConsumer.getInstance().getMailService().storeSmtpConf();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                       "Configuration saved successfully !", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (IOException e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                       "Fail saving configuration !",
                                                       e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            e.printStackTrace();
        }
    }
}