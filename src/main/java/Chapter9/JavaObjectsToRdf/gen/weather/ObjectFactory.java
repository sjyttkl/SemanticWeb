//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-646 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.10.19 at 03:12:50 PM EDT 
//


package Chapter9.JavaObjectsToRdf.gen.weather;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.semwebprogramming.chapter9.weather package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.semwebprogramming.chapter9.weather
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ImageType }
     * 
     */
    public ImageType createImageType() {
        return new ImageType();
    }

    /**
     * Create an instance of {@link CurrentObservation }
     * 
     */
    public CurrentObservation createCurrentObservation() {
        return new CurrentObservation();
    }

}