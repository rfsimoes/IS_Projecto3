
package artifact;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "AddNews", targetNamespace = "http://ws/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface AddNews {


    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "addNews", targetNamespace = "http://ws/", className = "artifact.AddNews_Type")
    @ResponseWrapper(localName = "addNewsResponse", targetNamespace = "http://ws/", className = "artifact.AddNewsResponse")
    public String addNews(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

}
