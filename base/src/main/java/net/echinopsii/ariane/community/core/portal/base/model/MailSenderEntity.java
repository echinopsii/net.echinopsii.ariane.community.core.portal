/**
 * Portal base bundle
 * Mail Sender Entity
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

package net.echinopsii.ariane.community.core.portal.base.model;

public class MailSenderEntity {

    private String from;
    private String subjectPrefix;

    private String smtpHost;
    private String smtpPort;
    private boolean smtpAUTH;
    private String smtpUser;
    private String smtpPassword;
    private boolean smtpSSL;
    private boolean smtpTLS;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubjectPrefix() {
        return subjectPrefix;
    }

    public void setSubjectPrefix(String subjectPrefix) {
        this.subjectPrefix = subjectPrefix;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public boolean isSmtpAUTH() {
        return smtpAUTH;
    }

    public void setSmtpAUTH(boolean smtpAUTH) {
        this.smtpAUTH = smtpAUTH;
    }

    public String getSmtpUser() {
        return smtpUser;
    }

    public void setSmtpUser(String smtpUser) {
        this.smtpUser = smtpUser;
    }

    public String getSmtpPassword() {
        return smtpPassword;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    public boolean isSmtpSSL() {
        return smtpSSL;
    }

    public void setSmtpSSL(boolean smtpSSL) {
        this.smtpSSL = smtpSSL;
    }

    public boolean isSmtpTLS() {
        return smtpTLS;
    }

    public void setSmtpTLS(boolean smtpTLS) {
        this.smtpTLS = smtpTLS;
    }
}