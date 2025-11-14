package businessLogic;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

public class BLFactory {

    public BLFacade getBusinessLogic(boolean isLocal) {
        ConfigXML c = ConfigXML.getInstance();
        try {
            if (isLocal) { // Local: Crea DataAccess 
                DataAccess da = new DataAccess();
                return new BLFacadeImplementation(da);
            } else { //remoto
                String serviceName = "http://" + c.getBusinessLogicNode() + ":" + c.getBusinessLogicPort() + "/ws/" + c.getBusinessLogicName() + "?wsdl";
                URL url = new URL(serviceName);
                QName qname = new QName("http://businessLogic/", "BLFacadeImplementationService");
                Service service = Service.create(url, qname);
                return service.getPort(BLFacade.class);
            }
        } catch (Exception e) {
            System.out.println("Error in BLFactory: " + e.toString());
            return null;
        }
    }
}