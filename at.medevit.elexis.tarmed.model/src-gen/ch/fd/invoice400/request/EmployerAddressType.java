//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.18 at 01:35:39 PM CET 
//

package ch.fd.invoice400.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for employerAddressType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="employerAddressType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="ean_party" type="{http://www.xmlData.ch/xmlInvoice/XSD}eanPartyType"/>
 *         &lt;element name="company" type="{http://www.xmlData.ch/xmlInvoice/XSD}employerCompanyType"/>
 *         &lt;element name="person" type="{http://www.xmlData.ch/xmlInvoice/XSD}employerPersonType"/>
 *         &lt;element name="reg_number" type="{http://www.xmlData.ch/xmlInvoice/XSD}stringType1_35"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "employerAddressType", propOrder = {
	"eanParty", "company", "person", "regNumber"
})
public class EmployerAddressType {
	
	@XmlElement(name = "ean_party")
	protected String eanParty;
	protected EmployerCompanyType company;
	protected EmployerPersonType person;
	@XmlElement(name = "reg_number")
	protected String regNumber;
	
	/**
	 * Gets the value of the eanParty property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEanParty(){
		return eanParty;
	}
	
	/**
	 * Sets the value of the eanParty property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEanParty(String value){
		this.eanParty = value;
	}
	
	/**
	 * Gets the value of the company property.
	 * 
	 * @return possible object is {@link EmployerCompanyType }
	 * 
	 */
	public EmployerCompanyType getCompany(){
		return company;
	}
	
	/**
	 * Sets the value of the company property.
	 * 
	 * @param value
	 *            allowed object is {@link EmployerCompanyType }
	 * 
	 */
	public void setCompany(EmployerCompanyType value){
		this.company = value;
	}
	
	/**
	 * Gets the value of the person property.
	 * 
	 * @return possible object is {@link EmployerPersonType }
	 * 
	 */
	public EmployerPersonType getPerson(){
		return person;
	}
	
	/**
	 * Sets the value of the person property.
	 * 
	 * @param value
	 *            allowed object is {@link EmployerPersonType }
	 * 
	 */
	public void setPerson(EmployerPersonType value){
		this.person = value;
	}
	
	/**
	 * Gets the value of the regNumber property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRegNumber(){
		return regNumber;
	}
	
	/**
	 * Sets the value of the regNumber property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRegNumber(String value){
		this.regNumber = value;
	}
	
}
