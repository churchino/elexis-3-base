//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b52-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.13 at 12:17:21 PM MEZ 
//

package ch.fd.invoice400.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for annulmentType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="annulmentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="explanation" type="{http://www.xmlData.ch/xmlInvoice/XSD}stringType1_700" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="notAccepted" type="{http://www.xmlData.ch/xmlInvoice/XSD}xtraElementType"/>
 *           &lt;element name="accepted" type="{http://www.xmlData.ch/xmlInvoice/XSD}xtraElementType"/>
 *           &lt;element name="invoiceNotFound" type="{http://www.xmlData.ch/xmlInvoice/XSD}xtraElementType"/>
 *           &lt;element name="invoiceRejected" type="{http://www.xmlData.ch/xmlInvoice/XSD}xtraElementType"/>
 *           &lt;element name="invoicePaid" type="{http://www.xmlData.ch/xmlInvoice/XSD}invoicePaidType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" fixed="final" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "annulmentType", propOrder = {
	"explanation", "notAccepted", "accepted", "invoiceNotFound", "invoiceRejected", "invoicePaid"
})
public class AnnulmentType {
	
	@XmlElement(namespace = "http://www.xmlData.ch/xmlInvoice/XSD")
	protected String explanation;
	@XmlElement(namespace = "http://www.xmlData.ch/xmlInvoice/XSD")
	protected XtraElementType notAccepted;
	@XmlElement(namespace = "http://www.xmlData.ch/xmlInvoice/XSD")
	protected XtraElementType accepted;
	@XmlElement(namespace = "http://www.xmlData.ch/xmlInvoice/XSD")
	protected XtraElementType invoiceNotFound;
	@XmlElement(namespace = "http://www.xmlData.ch/xmlInvoice/XSD")
	protected XtraElementType invoiceRejected;
	@XmlElement(namespace = "http://www.xmlData.ch/xmlInvoice/XSD")
	protected InvoicePaidType invoicePaid;
	@XmlAttribute
	protected String type;
	
	/**
	 * Gets the value of the explanation property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getExplanation(){
		return explanation;
	}
	
	/**
	 * Sets the value of the explanation property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setExplanation(String value){
		this.explanation = value;
	}
	
	/**
	 * Gets the value of the notAccepted property.
	 * 
	 * @return possible object is {@link XtraElementType }
	 * 
	 */
	public XtraElementType getNotAccepted(){
		return notAccepted;
	}
	
	/**
	 * Sets the value of the notAccepted property.
	 * 
	 * @param value
	 *            allowed object is {@link XtraElementType }
	 * 
	 */
	public void setNotAccepted(XtraElementType value){
		this.notAccepted = value;
	}
	
	/**
	 * Gets the value of the accepted property.
	 * 
	 * @return possible object is {@link XtraElementType }
	 * 
	 */
	public XtraElementType getAccepted(){
		return accepted;
	}
	
	/**
	 * Sets the value of the accepted property.
	 * 
	 * @param value
	 *            allowed object is {@link XtraElementType }
	 * 
	 */
	public void setAccepted(XtraElementType value){
		this.accepted = value;
	}
	
	/**
	 * Gets the value of the invoiceNotFound property.
	 * 
	 * @return possible object is {@link XtraElementType }
	 * 
	 */
	public XtraElementType getInvoiceNotFound(){
		return invoiceNotFound;
	}
	
	/**
	 * Sets the value of the invoiceNotFound property.
	 * 
	 * @param value
	 *            allowed object is {@link XtraElementType }
	 * 
	 */
	public void setInvoiceNotFound(XtraElementType value){
		this.invoiceNotFound = value;
	}
	
	/**
	 * Gets the value of the invoiceRejected property.
	 * 
	 * @return possible object is {@link XtraElementType }
	 * 
	 */
	public XtraElementType getInvoiceRejected(){
		return invoiceRejected;
	}
	
	/**
	 * Sets the value of the invoiceRejected property.
	 * 
	 * @param value
	 *            allowed object is {@link XtraElementType }
	 * 
	 */
	public void setInvoiceRejected(XtraElementType value){
		this.invoiceRejected = value;
	}
	
	/**
	 * Gets the value of the invoicePaid property.
	 * 
	 * @return possible object is {@link InvoicePaidType }
	 * 
	 */
	public InvoicePaidType getInvoicePaid(){
		return invoicePaid;
	}
	
	/**
	 * Sets the value of the invoicePaid property.
	 * 
	 * @param value
	 *            allowed object is {@link InvoicePaidType }
	 * 
	 */
	public void setInvoicePaid(InvoicePaidType value){
		this.invoicePaid = value;
	}
	
	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getType(){
		if (type == null) {
			return "final";
		} else {
			return type;
		}
	}
	
	/**
	 * Sets the value of the type property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setType(String value){
		this.type = value;
	}
	
}
