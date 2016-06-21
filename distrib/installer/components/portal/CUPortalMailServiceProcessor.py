# installer mapping rim managed service configuration unit
#
# Copyright (C) 2014 Mathilde Ffrench
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
import getpass
import json
import os
from tools.AConfParamNotNone import AConfParamNotNone
from tools.AConfUnit import AConfUnit

__author__ = 'mffrench'


class CPPortalMailServiceConfPath(AConfParamNotNone):

    name = "##portalMailConfPath"
    description = "Portal Mail Service Configuration Path"
    hide = False

    def __init__(self):
        self.value = None

    def is_valid(self):
        return super(CPPortalMailServiceConfPath, self).is_valid()


class CPPortalMailServiceUser(AConfParamNotNone):

    name = "##portalMailUser"
    description = "Portal Mail User"
    hide = False

    def __init__(self):
        self.value = None

    def is_valid(self):
        return super(CPPortalMailServiceUser, self).is_valid()


class CPPortalMailServicePassword(AConfParamNotNone):

    name = "##portalMailPassword"
    description = "Portal Mail Password"
    hide = False

    def __init__(self):
        self.value = None

    def is_valid(self):
        return super(CPPortalMailServicePassword, self).is_valid()


class CPPortalMailServiceFrom(AConfParamNotNone):

    name = "##portalMailFrom"
    description = "Portal Mail From"
    hide = False

    def __init__(self):
        self.value = None

    def is_valid(self):
        return super(CPPortalMailServiceFrom, self).is_valid()


class CPPortalMailServiceSMTPHost(AConfParamNotNone):

    name = "##portalMailSMTPHost"
    description = "Portal Mail SMTP Host"
    hide = False

    def __init__(self):
        self.value = None

    def is_valid(self):
        return super(CPPortalMailServiceSMTPHost, self).is_valid()


class CPPortalMailServiceSMTPPort(AConfParamNotNone):

    name = "##portalMailSMTPPort"
    description = "Portal Mail SMTP Port"
    hide = False

    def __init__(self):
        self.value = None

    def is_valid(self):
        return super(CPPortalMailServiceSMTPPort, self).is_valid()


class CPPortalMailServiceSMTPAuth(AConfParamNotNone):

    name = "##portalMailSMTPAuth"
    description = "Portal Mail SMTP AUTH"
    hide = False

    def __init__(self):
        self.value = None

    def is_valid(self):
        return super(CPPortalMailServiceSMTPAuth, self).is_valid()


class CPPortalMailServiceSMTPSSL(AConfParamNotNone):

    name = "##portalMailSMTPSSL"
    description = "Portal Mail SMTP SSL"
    hide = False

    def __init__(self):
        self.value = None

    def is_valid(self):
        return super(CPPortalMailServiceSMTPSSL, self).is_valid()


class CPPortalMailServiceSMTPTLS(AConfParamNotNone):

    name = "##portalMailSMTPTLS"
    description = "Portal Mail SMTP TLS"
    hide = False

    def __init__(self):
        self.value = None

    def is_valid(self):
        return super(CPPortalMailServiceSMTPTLS, self).is_valid()


class CUPortalMailServiceProcessor(AConfUnit):

    def __init__(self, target_conf_dir):
        self.confUnitName = "Portal Mail Service"
        self.confTemplatePath = os.path.abspath(
            "resources/templates/components/net.echinopsii.ariane.community.core.PortalMailService.properties.tpl"
        )
        self.confFinalPath = target_conf_dir + "net.echinopsii.ariane.community.core.PortalMailService.properties"

        conf_path = CPPortalMailServiceConfPath()
        user = CPPortalMailServiceUser()
        password = CPPortalMailServicePassword()
        mail_from = CPPortalMailServiceFrom()
        smtp_host = CPPortalMailServiceSMTPHost()
        smtp_port = CPPortalMailServiceSMTPPort()
        smtp_auth = CPPortalMailServiceSMTPAuth()
        smtp_ssl = CPPortalMailServiceSMTPSSL()
        smtp_tls = CPPortalMailServiceSMTPTLS()

        self.paramsDictionary = {
            conf_path.name: conf_path,
            user.name: user,
            password.name: password,
            mail_from.name: mail_from,
            smtp_host.name: smtp_host,
            smtp_port.name: smtp_port,
            smtp_auth.name: smtp_auth,
            smtp_ssl.name: smtp_ssl,
            smtp_tls.name: smtp_tls
        }

    def process(self):
        return super(CUPortalMailServiceProcessor, self).process()

    def get_param_from_key(self, key):
        return super(CUPortalMailServiceProcessor, self).get_param_from_key(key)

    def get_params_keys_list(self):
        return super(CUPortalMailServiceProcessor, self).get_params_keys_list()

    def set_key_param_value(self, key, value):
        return super(CUPortalMailServiceProcessor, self).set_key_param_value(key, value)


class PortalMailServiceSyringe:

    def __init__(self, target_conf_dir, silent):
        self.silent = silent
        self.targetConfDir = target_conf_dir
        self.portalMailServiceProcessor = CUPortalMailServiceProcessor(target_conf_dir)
        cujson = open("resources/configvalues/components/cuPortalMailService.json")
        self.portalMailServiceCUValues = json.load(cujson)
        cujson.close()

    def shoot_builder(self):
            self.portalMailServiceProcessor.set_key_param_value(
                CPPortalMailServiceConfPath.name,
                self.targetConfDir+"net.echinopsii.ariane.community.core.PortalMailService.properties"
            )

            user_default = self.portalMailServiceCUValues[CPPortalMailServiceUser.name]
            user_default_ui = "[default - " + user_default + "] "

            if not self.silent:
                username = input("%-- >> Define mail service username " + user_default_ui + ": ")
                if username == "" or username is None:
                    username = user_default
            else:
                username = user_default

            self.portalMailServiceCUValues[CPPortalMailServiceUser.name] = username
            self.portalMailServiceProcessor.set_key_param_value(CPPortalMailServiceUser.name, username)

            password_default = self.portalMailServiceCUValues[CPPortalMailServicePassword.name]

            if not self.silent:
                password = getpass.getpass("%-- >> Define mail service password : ")
                if password == "" or password is None:
                    password = password_default
            else:
                password = password_default

            self.portalMailServiceCUValues[CPPortalMailServicePassword.name] = password
            self.portalMailServiceProcessor.set_key_param_value(CPPortalMailServicePassword.name, password)

            self.portalMailServiceProcessor.set_key_param_value(
                CPPortalMailServiceFrom.name,
                self.portalMailServiceCUValues[CPPortalMailServiceFrom.name]
            )

            hostname_default = self.portalMailServiceCUValues[CPPortalMailServiceSMTPHost.name]
            hostname_default_ui = "[default - " + hostname_default + "] "

            if not self.silent:
                hostname = input("%-- >> Define mail service hostname " + hostname_default_ui + ": ")
                if hostname == "" or hostname is None:
                    hostname = hostname_default
            else:
                hostname = hostname_default

            self.portalMailServiceCUValues[CPPortalMailServiceSMTPHost.name] = hostname
            self.portalMailServiceProcessor.set_key_param_value(CPPortalMailServiceSMTPHost.name, hostname)

            port_is_valid = False
            port_default = self.portalMailServiceCUValues[CPPortalMailServiceSMTPPort.name]
            port_default_ui = "[default - " + str(port_default) + "] "

            port = 0
            while not port_is_valid:
                if not self.silent:
                    port_str = input("%-- >> Define mail service port " + port_default_ui + ": ")
                    if port_str == "" or port_str is None:
                        port = port_default
                        port_is_valid = True
                    else:
                        try:
                            port = int(port_str)
                            if (port <= 0) or (port > 65535):
                                print("%-- !! Invalid mail service port " + str(port) + ": not in port range")
                            else:
                                port_is_valid = True
                        except ValueError:
                            print("%-- !! Invalid mail service port " + port_str + " : not a number")
                else:
                    port = port_default
                    port_is_valid = True

            self.portalMailServiceCUValues[CPPortalMailServiceSMTPPort.name] = port
            self.portalMailServiceProcessor.set_key_param_value(CPPortalMailServiceSMTPPort.name, port)

            auth_is_valid = False
            auth_default = self.portalMailServiceCUValues[CPPortalMailServiceSMTPAuth.name]
            auth_default_ui = "[default - " + str(auth_default) + "] "

            auth = auth_default
            while not auth_is_valid:
                if not self.silent:
                    auth_str = input("%-- >> Define mail service authentication " + auth_default_ui +
                                     " (true or false): ")
                    if auth_str == "" or auth_str is None:
                        auth_is_valid = True
                    else:
                        if auth_str != "true" and auth_str != "false":
                            print("%-- !! Invalid authentication value. Must be true or false")
                        else:
                            if auth_str == "true":
                                auth = True
                            else:
                                auth = False
                            auth_is_valid = True
                else:
                    auth_is_valid = True

            self.portalMailServiceCUValues[CPPortalMailServiceSMTPAuth.name] = auth
            self.portalMailServiceProcessor.set_key_param_value(CPPortalMailServiceSMTPAuth.name, auth)

            ssl_is_valid = False
            ssl_default = self.portalMailServiceCUValues[CPPortalMailServiceSMTPSSL.name]
            ssl_default_ui = "[default - " + str(ssl_default) + "] "

            ssl = ssl_default
            while not ssl_is_valid:
                if not self.silent:
                    ssl_str = input("%-- >> Define mail service SSL " + ssl_default_ui + " (true or false): ")
                    if ssl_str == "" or ssl_str is None:
                        ssl_is_valid = True
                    else:
                        if ssl_str != "true" and ssl_str != "false":
                            print("%-- !! Invalid SSL value. Must be true or false")
                        else:
                            if ssl_str == "true":
                                ssl = True
                            else:
                                ssl = False
                            ssl_is_valid = True
                else:
                    ssl_is_valid = True

            self.portalMailServiceCUValues[CPPortalMailServiceSMTPSSL.name] = ssl
            self.portalMailServiceProcessor.set_key_param_value(CPPortalMailServiceSMTPSSL.name, ssl)

            tls_is_valid = False
            tls_default = self.portalMailServiceCUValues[CPPortalMailServiceSMTPTLS.name]
            tls_default_ui = "[default - " + str(tls_default) + "] "

            tls = tls_default
            while not tls_is_valid:
                if not self.silent:
                    tls_str = input("%-- >> Define mail service TLS " + tls_default_ui + " (true or false): ")
                    if tls_str == "" or tls_str is None:
                        tls_is_valid = True
                    else:
                        if tls_str != "true" and tls_str != "false":
                            print("%-- !! Invalid TLS value. Must be true or false")
                        else:
                            if tls_str == "true":
                                tls = True
                            else:
                                tls = False
                            tls_is_valid = True
                else:
                    tls_is_valid = True

            self.portalMailServiceCUValues[CPPortalMailServiceSMTPTLS.name] = tls
            self.portalMailServiceProcessor.set_key_param_value(CPPortalMailServiceSMTPTLS.name, tls)

    def inject(self):
        portal_mail_service_cujson = open("resources/configvalues/components/cuPortalMailService.json", "w")
        json_str = json.dumps(self.portalMailServiceCUValues, sort_keys=True, indent=4, separators=(',', ': '))
        portal_mail_service_cujson.write(json_str)
        portal_mail_service_cujson.close()
        self.portalMailServiceProcessor.process()
