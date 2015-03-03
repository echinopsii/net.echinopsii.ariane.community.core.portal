/**
 * Portal base bundle
 * Portal mail service interface
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

package net.echinopsii.ariane.community.core.portal.base.plugin;

import net.echinopsii.ariane.community.core.portal.base.model.MailSenderEntity;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.util.List;

public interface MailService {
    public void send(List<String> to, String subject, String body) throws MessagingException;
    public MailSenderEntity getSmtpConf();
    public void setSmtpConf(MailSenderEntity smtpConf);
}