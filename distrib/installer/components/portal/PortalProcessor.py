# installer portal processor
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
import os
from components.portal.CUPortalMailServiceProcessor import PortalMailServiceSyringe
from components.portal.DBIDMMySQLPopulator import DBIDMMySQLPopulator


__author__ = 'mffrench'


class PortalProcessor:
    def __init__(self, home_dir_path, dist_dep_type, directory_db_conf, idm_db_conf, bus_processor, silent):
        print("\n%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--%--"
              "%--%--%--%--%--%--%--%--%--\n")
        print("%-- Portal configuration : \n")
        self.homeDirPath = home_dir_path
        self.dist_dep_type = dist_dep_type
        self.idmDBConfig = idm_db_conf
        self.directoryDBConfig = directory_db_conf
        self.busProcessor = bus_processor

        kernel_repository_dir_path = self.homeDirPath + "/repository/ariane-core/"
        if not os.path.exists(kernel_repository_dir_path):
            os.makedirs(kernel_repository_dir_path, 0o755)

        self.portalIDMSQLPopulator = DBIDMMySQLPopulator(idm_db_conf)
        self.portalMailServiceSyringe = PortalMailServiceSyringe(kernel_repository_dir_path, silent)
        self.portalMailServiceSyringe.shoot_builder()

    def process(self):
        self.portalIDMSQLPopulator.process()
        self.portalMailServiceSyringe.inject()
        return self
