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
import json
import os
from tools.AConfParamNotNone import AConfParamNotNone
from tools.AConfUnit import AConfUnit

__author__ = 'mffrench'


class cpPortalMailServiceConfPath(AConfParamNotNone):

    name = "##portalMailConfPath"
    description = "Portal Mail Service Configuration Path"
    hide = False

    def __init__(self):
        self.value = None


class cpPortalMailServiceUser(AConfParamNotNone):

    name = "##portalMailUser"
    description = "Portal Mail User"
    hide = False

    def __init__(self):
        self.value = None


class cpPortalMailServicePassword(AConfParamNotNone):

    name = "##portalMailPassword"
    description = "Portal Mail Password"
    hide = False

    def __init__(self):
        self.value = None



class cpPortalMailServiceFrom(AConfParamNotNone):

    name = "##portalMailFrom"
    description = "Portal Mail From"
    hide = False

    def __init__(self):
        self.value = None


class cpPortalMailServiceSMTPHost(AConfParamNotNone):

    name = "##portalMailSMTPHost"
    description = "Portal Mail SMTP Host"
    hide = False

    def __init__(self):
        self.value = None


class cpPortalMailServiceSMTPPort(AConfParamNotNone):

    name = "##portalMailSMTPPort"
    description = "Portal Mail SMTP Port"
    hide = False

    def __init__(self):
        self.value = None


class cpPortalMailServiceSMTPAuth(AConfParamNotNone):

    name = "##portalMailSMTPAuth"
    description = "Portal Mail SMTP AUTH"
    hide = False

    def __init__(self):
        self.value = None


class cpPortalMailServiceSMTPSSL(AConfParamNotNone):

    name = "##portalMailSMTPSSL"
    description = "Portal Mail SMTP SSL"
    hide = False

    def __init__(self):
        self.value = None


class cpPortalMailServiceSMTPTLS(AConfParamNotNone):

    name = "##portalMailSMTPTLS"
    description = "Portal Mail SMTP TLS"
    hide = False

    def __init__(self):
        self.value = None


class cuPortalMailServiceProcessor(AConfUnit):

    def __init__(self, targetConfDir):
        self.confUnitName = "Portal Mail Service"
        self.confTemplatePath = os.path.abspath("resources/templates/components/net.echinopsii.ariane.community.core.PortalMailService.properties.tpl")
        self.confFinalPath = targetConfDir + "net.echinopsii.ariane.community.core.PortalMailService.properties"

        portalMailServiceConfPath = cpPortalMailServiceConfPath()
        portalMailServiceUser = cpPortalMailServiceUser()
        portalMailServicePassword = cpPortalMailServicePassword()
        portalMailServiceFrom = cpPortalMailServiceFrom()
        portalMailServiceSMTPHost = cpPortalMailServiceSMTPHost()
        portalMailServiceSMTPPort = cpPortalMailServiceSMTPPort()
        portalMailServiceSMTPAuth = cpPortalMailServiceSMTPAuth()
        portalMailServiceSMTPSSL = cpPortalMailServiceSMTPSSL()
        portalMailServiceSMTPTLS = cpPortalMailServiceSMTPTLS()

        self.paramsDictionary = {
            portalMailServiceConfPath.name: portalMailServiceConfPath,
            portalMailServiceUser.name: portalMailServiceUser,
            portalMailServicePassword.name: portalMailServicePassword,
            portalMailServiceFrom.name: portalMailServiceFrom,
            portalMailServiceSMTPHost.name: portalMailServiceSMTPHost,
            portalMailServiceSMTPPort.name: portalMailServiceSMTPPort,
            portalMailServiceSMTPAuth.name: portalMailServiceSMTPAuth,
            portalMailServiceSMTPSSL.name: portalMailServiceSMTPSSL,
            portalMailServiceSMTPTLS.name: portalMailServiceSMTPTLS
        }


class portalMailServiceSyringe:

    def __init__(self, targetConfDir, silent):
        self.silent = silent
        self.targetConfDir = targetConfDir
        self.portalMailServiceProcessor = cuPortalMailServiceProcessor(targetConfDir)
        portalMailServiceCUJSON = open("resources/configvalues/components/cuPortalMailService.json")
        self.portalMailServiceCUValues = json.load(portalMailServiceCUJSON)
        portalMailServiceCUJSON.close()

    def shootBuilder(self):
            self.portalMailServiceProcessor.setKeyParamValue(cpPortalMailServiceConfPath.name, self.targetConfDir+"net.echinopsii.ariane.community.core.PortalMailService.properties")

            userDefault = self.portalMailServiceCUValues[cpPortalMailServiceUser.name]
            userDefaultUI = "[default - " + userDefault + "] "

            username = ""
            if not self.silent:
                username = input("%-- >> Define mail service username " + userDefaultUI + ": ")
                if username == "" or username is None:
                    username = userDefault
            else:
                username = userDefault

            self.portalMailServiceProcessor.setKeyParamValue(cpPortalMailServiceUser.name, username)

            passwordDefault = self.portalMailServiceCUValues[cpPortalMailServicePassword.name]
            passwordDefaultUI = "[default - " + passwordDefault + "] "

            password = ""
            if not self.silent:
                password = input("%-- >> Define mail service password " + passwordDefaultUI + ": ")
                if password == "" or password is None:
                    password = passwordDefault
            else:
                password = passwordDefault

            self.portalMailServiceProcessor.setKeyParamValue(cpPortalMailServicePassword.name, password)

            self.portalMailServiceProcessor.setKeyParamValue(cpPortalMailServiceFrom.name, self.portalMailServiceCUValues[cpPortalMailServiceFrom.name])

            hostnameDefault = self.portalMailServiceCUValues[cpPortalMailServiceSMTPHost.name]
            hostnameDefaultUI = "[default - " + hostnameDefault + "] "

            hostname = ""
            if not self.silent:
                hostname = input("%-- >> Define mail service hostname " + hostnameDefaultUI + ": ")
                if hostname == "" or hostname is None:
                    hostname = hostnameDefault
            else:
                hostname = hostnameDefault

            self.portalMailServiceProcessor.setKeyParamValue(cpPortalMailServiceSMTPHost.name, hostname)

            portIsValid = False
            portDefault = self.portalMailServiceCUValues[cpPortalMailServiceSMTPPort.name]
            portDefaultUI = "[default - " + str(portDefault) + "] "

            port = 0
            while not portIsValid:
                if not self.silent:
                    portStr = input("%-- >> Define mail service port " + portDefaultUI + ": ")
                    if portStr == "" or portStr is None:
                        port = portDefault
                        portIsValid = True
                    else:
                        try:
                            port = int(portStr)
                            if (port <= 0) and (port > 65535):
                                print("%-- !! Invalid mail service port " + str(port) + ": not in port range")
                            else:
                                portIsValid = True
                        except ValueError:
                            print("%-- !! Invalid mail service port " + portStr + " : not a number")
                else:
                    port = portDefault
                    portIsValid = True

            self.portalMailServiceProcessor.setKeyParamValue(cpPortalMailServiceSMTPPort.name, port)

            authIsValid = False
            authDefault = self.portalMailServiceCUValues[cpPortalMailServiceSMTPAuth.name]
            authDefaultUI = "[default - " + str(authDefault) + "] "

            auth = authDefault
            while not authIsValid:
                if not self.silent:
                    authStr = input("%-- >> Define mail service authentication " + authDefaultUI + " (true or false): ")
                    if authStr == "" or authStr is None:
                        authIsValid = True
                    else:
                        if authStr != "true" and authStr != "false":
                            print("%-- !! Invalid authentication value. Must be true or false")
                        else:
                            if authStr == "true":
                                auth = True
                            else:
                                auth = False
                            authIsValid = True

            self.portalMailServiceProcessor.setKeyParamValue(cpPortalMailServiceSMTPAuth.name, auth)

            sslIsValid = False
            sslDefault = self.portalMailServiceCUValues[cpPortalMailServiceSMTPSSL.name]
            sslDefaultUI = "[default - " + str(sslDefault) + "] "

            ssl = sslDefault
            while not sslIsValid:
                if not self.silent:
                    sslStr = input("%-- >> Define mail service SSL " + sslDefaultUI + " (true or false): ")
                    if sslStr == "" or sslStr is None:
                        sslIsValid = True
                    else:
                        if sslStr != "true" and sslStr != "false":
                            print("%-- !! Invalid SSL value. Must be true or false")
                        else:
                            if sslStr == "true":
                                ssl = True
                            else:
                                ssl = False
                            sslIsValid = True

            self.portalMailServiceProcessor.setKeyParamValue(cpPortalMailServiceSMTPSSL.name, ssl)

            tlsIsValid = False
            tlsDefault = self.portalMailServiceCUValues[cpPortalMailServiceSMTPTLS.name]
            tlsDefaultUI = "[default - " + str(tlsDefault) + "] "

            tls = tlsDefault
            while not tlsIsValid:
                if not self.silent:
                    tlsStr = input("%-- >> Define mail service TLS " + tlsDefaultUI + " (true or false): ")
                    if tlsStr == "" or tlsStr is None:
                        tlsIsValid = True
                    else:
                        if tlsStr != "true" and tlsStr != "false":
                            print("%-- !! Invalid TLS value. Must be true or false")
                        else:
                            if tlsStr == "true":
                                tls = True
                            else:
                                tls = False
                            tlsIsValid = True

            self.portalMailServiceProcessor.setKeyParamValue(cpPortalMailServiceSMTPTLS.name, tls)

    def inject(self):
        portalMailServiceCUJSON = open("resources/configvalues/components/cuPortalMailService.json", "w")
        jsonStr = json.dumps(self.portalMailServiceCUValues, sort_keys=True, indent=4, separators=(',', ': '))
        portalMailServiceCUJSON.write(jsonStr)
        portalMailServiceCUJSON.close()
        self.portalMailServiceProcessor.process()
