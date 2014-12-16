
package artifact;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the artifact package. 
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

    private final static QName _AddNews_QNAME = new QName("http://ws/", "addNews");
    private final static QName _AddNewsResponse_QNAME = new QName("http://ws/", "addNewsResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: artifact
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddNews_Type }
     * 
     */
    public AddNews_Type createAddNews_Type() {
        return new AddNews_Type();
    }

    /**
     * Create an instance of {@link AddNewsResponse }
     * 
     */
    public AddNewsResponse createAddNewsResponse() {
        return new AddNewsResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddNews_Type }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws/", name = "addNews")
    public JAXBElement<AddNews_Type> createAddNews(AddNews_Type value) {
        return new JAXBElement<AddNews_Type>(_AddNews_QNAME, AddNews_Type.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddNewsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws/", name = "addNewsResponse")
    public JAXBElement<AddNewsResponse> createAddNewsResponse(AddNewsResponse value) {
        return new JAXBElement<AddNewsResponse>(_AddNewsResponse_QNAME, AddNewsResponse.class, null, value);
    }

}
