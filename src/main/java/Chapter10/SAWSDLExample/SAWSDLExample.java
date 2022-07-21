package Chapter10.SAWSDLExample;

import edu.uga.cs.lsdis.sawsdl.util.SAWSDLUtility;
import edu.uga.cs.lsdis.sawsdl.*;

import javax.xml.namespace.QName;
import java.io.File;
import java.util.Map;
import java.util.List;


public class SAWSDLExample {
 
    public static void main(String[] args) throws Exception{
        if (args.length >1 || args.length <1){
            System.out.println("Need file name");
            return;
        }
        
        System.setProperty("javax.wsdl.factory.WSDLFactory",
                "edu.uga.cs.lsdis.sawsdl.impl.factory.WSDLFactoryImpl");

        File wsdlURI = new File(args[0]);
        Definition definition = SAWSDLUtility.getDefinitionFromFile(wsdlURI);

        assert(definition!=null);
        System.out.println("Definition created Successfully!");
 
        Map portTypes = definition.getPortTypes();
        for (Object key:portTypes.keySet()){
            PortType semanticPortType = definition.getSemanticPortType((QName)key);

            System.out.println("Porttype QName ->" + semanticPortType.getQName());
            System.out.println("Model References ->" + semanticPortType.getModelReferences() );

            List operations = semanticPortType.getOperations();

            for (Object operation : operations) {
                System.out.println("Operation ->" + ((Operation) operation).getName());
            }

        }

        Map messages = definition.getMessages();
        for (Object key:messages.keySet()){
            Message semanticMessage = definition.getSemanticMessage((QName)key);

            System.out.println("Message QName ->" + semanticMessage.getQName());

            Map parts = semanticMessage.getParts();

            for (Object partKey : parts.keySet()) {
                Part semanticPart = semanticMessage.getSemanticPart((String) partKey);
                System.out.println("part ->" + semanticPart);
                System.out.println("part model references ->" + semanticPart.getModelReferences());
            }
        }
    }
}
