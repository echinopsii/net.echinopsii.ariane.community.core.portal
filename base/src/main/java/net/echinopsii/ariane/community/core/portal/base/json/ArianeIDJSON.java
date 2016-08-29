package net.echinopsii.ariane.community.core.portal.base.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.echinopsii.ariane.community.core.portal.base.model.ArianeDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ArianeIDJSON {
    private final static Logger log = LoggerFactory.getLogger(ArianeIDJSON.class);

    static class ArianeID {
        String delivery_date;
        String deployment_type;
        String version;

        public ArianeID() {

        }

        public String getDelivery_date() {
            return delivery_date;
        }

        public void setDelivery_date(String delivery_date) {
            this.delivery_date = delivery_date;
        }

        public String getDeployment_type() {
            return deployment_type;
        }

        public void setDeployment_type(String deployment_type) {
            this.deployment_type = deployment_type;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

    public static ArianeDefinition getArianeIDFromJSON() {
        ArianeDefinition ret = null;
        String idFilePath = System.getProperty("ariane.id.path");
        File idFile = new File(idFilePath);
        if (idFile.canRead()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                ArianeID idJson = mapper.readValue(idFile, ArianeID.class);
                ret = new ArianeDefinition();
                String reformattedDeliveryDate;
                if (idJson.getDelivery_date().split("/").length == 3) {
                    reformattedDeliveryDate = "20" + idJson.getDelivery_date().split("/")[2] + "." +
                            idJson.getDelivery_date().split("/")[0] + "." + idJson.getDelivery_date().split("/")[1];
                } else {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                    reformattedDeliveryDate = dateFormat.format(new Date());
                }
                ret.setDeliveryDate(reformattedDeliveryDate);
                ret.setDeliveryYear(reformattedDeliveryDate.split("\\.")[0]);
                ret.setDeploymentType(idJson.getDeployment_type());
                ret.setVersion(idJson.getVersion());
            } catch (IOException e) {
                log.error("Parsing Ariane ID file {} failed ! Reason : {} ", new Object[]{idFilePath, e.getMessage()});
            }
        } else {
            log.error("Ariane ID file {} is not readable !", new Object[]{idFilePath});
        }
        return ret;
    }
}
