//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.18 at 03:48:09 PM CET 
//


package ch.fd.invoice440.request;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for xtraStationaryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="xtraStationaryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="admission_type" type="{http://www.forum-datenaustausch.ch/invoice}grouperDataType"/>
 *         &lt;element name="discharge_type" type="{http://www.forum-datenaustausch.ch/invoice}grouperDataType"/>
 *         &lt;element name="provider_type" type="{http://www.forum-datenaustausch.ch/invoice}grouperDataType"/>
 *         &lt;element name="bfs_residence_before_admission" type="{http://www.forum-datenaustausch.ch/invoice}bfsDataType"/>
 *         &lt;element name="bfs_admission_type" type="{http://www.forum-datenaustausch.ch/invoice}bfsDataType"/>
 *         &lt;element name="bfs_decision_for_discharge" type="{http://www.forum-datenaustausch.ch/invoice}bfsDataType"/>
 *         &lt;element name="bfs_residence_after_discharge" type="{http://www.forum-datenaustausch.ch/invoice}bfsDataType"/>
 *         &lt;element name="case_detail" type="{http://www.forum-datenaustausch.ch/invoice}caseDetailType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="hospitalization_date" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="treatment_days" use="required" type="{http://www.w3.org/2001/XMLSchema}duration" />
 *       &lt;attribute name="hospitalization_type" default="regular">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="regular"/>
 *             &lt;enumeration value="emergency"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="hospitalization_mode" default="cantonal">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="cantonal"/>
 *             &lt;enumeration value="noncantonal_indicated"/>
 *             &lt;enumeration value="noncantonal_nonindicated"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="class" default="general">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="private"/>
 *             &lt;enumeration value="semi_private"/>
 *             &lt;enumeration value="general"/>
 *             &lt;enumeration value="hospital_comfort"/>
 *             &lt;enumeration value="md_free_choice"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="section_major" type="{http://www.forum-datenaustausch.ch/invoice}stringType1_6" />
 *       &lt;attribute name="has_expense_loading" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="do_cost_assessment" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "xtraStationaryType", propOrder = {
    "admissionType",
    "dischargeType",
    "providerType",
    "bfsResidenceBeforeAdmission",
    "bfsAdmissionType",
    "bfsDecisionForDischarge",
    "bfsResidenceAfterDischarge",
    "caseDetail"
})
public class XtraStationaryType {

    @XmlElement(name = "admission_type", required = true)
    protected GrouperDataType admissionType;
    @XmlElement(name = "discharge_type", required = true)
    protected GrouperDataType dischargeType;
    @XmlElement(name = "provider_type", required = true)
    protected GrouperDataType providerType;
    @XmlElement(name = "bfs_residence_before_admission", required = true)
    protected BfsDataType bfsResidenceBeforeAdmission;
    @XmlElement(name = "bfs_admission_type", required = true)
    protected BfsDataType bfsAdmissionType;
    @XmlElement(name = "bfs_decision_for_discharge", required = true)
    protected BfsDataType bfsDecisionForDischarge;
    @XmlElement(name = "bfs_residence_after_discharge", required = true)
    protected BfsDataType bfsResidenceAfterDischarge;
    @XmlElement(name = "case_detail")
    protected List<CaseDetailType> caseDetail;
    @XmlAttribute(name = "hospitalization_date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar hospitalizationDate;
    @XmlAttribute(name = "treatment_days", required = true)
    protected Duration treatmentDays;
    @XmlAttribute(name = "hospitalization_type")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String hospitalizationType;
    @XmlAttribute(name = "hospitalization_mode")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String hospitalizationMode;
    @XmlAttribute(name = "class")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String clazz;
    @XmlAttribute(name = "section_major")
    protected String sectionMajor;
    @XmlAttribute(name = "has_expense_loading")
    protected Boolean hasExpenseLoading;
    @XmlAttribute(name = "do_cost_assessment")
    protected Boolean doCostAssessment;

    /**
     * Gets the value of the admissionType property.
     * 
     * @return
     *     possible object is
     *     {@link GrouperDataType }
     *     
     */
    public GrouperDataType getAdmissionType() {
        return admissionType;
    }

    /**
     * Sets the value of the admissionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link GrouperDataType }
     *     
     */
    public void setAdmissionType(GrouperDataType value) {
        this.admissionType = value;
    }

    /**
     * Gets the value of the dischargeType property.
     * 
     * @return
     *     possible object is
     *     {@link GrouperDataType }
     *     
     */
    public GrouperDataType getDischargeType() {
        return dischargeType;
    }

    /**
     * Sets the value of the dischargeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link GrouperDataType }
     *     
     */
    public void setDischargeType(GrouperDataType value) {
        this.dischargeType = value;
    }

    /**
     * Gets the value of the providerType property.
     * 
     * @return
     *     possible object is
     *     {@link GrouperDataType }
     *     
     */
    public GrouperDataType getProviderType() {
        return providerType;
    }

    /**
     * Sets the value of the providerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link GrouperDataType }
     *     
     */
    public void setProviderType(GrouperDataType value) {
        this.providerType = value;
    }

    /**
     * Gets the value of the bfsResidenceBeforeAdmission property.
     * 
     * @return
     *     possible object is
     *     {@link BfsDataType }
     *     
     */
    public BfsDataType getBfsResidenceBeforeAdmission() {
        return bfsResidenceBeforeAdmission;
    }

    /**
     * Sets the value of the bfsResidenceBeforeAdmission property.
     * 
     * @param value
     *     allowed object is
     *     {@link BfsDataType }
     *     
     */
    public void setBfsResidenceBeforeAdmission(BfsDataType value) {
        this.bfsResidenceBeforeAdmission = value;
    }

    /**
     * Gets the value of the bfsAdmissionType property.
     * 
     * @return
     *     possible object is
     *     {@link BfsDataType }
     *     
     */
    public BfsDataType getBfsAdmissionType() {
        return bfsAdmissionType;
    }

    /**
     * Sets the value of the bfsAdmissionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link BfsDataType }
     *     
     */
    public void setBfsAdmissionType(BfsDataType value) {
        this.bfsAdmissionType = value;
    }

    /**
     * Gets the value of the bfsDecisionForDischarge property.
     * 
     * @return
     *     possible object is
     *     {@link BfsDataType }
     *     
     */
    public BfsDataType getBfsDecisionForDischarge() {
        return bfsDecisionForDischarge;
    }

    /**
     * Sets the value of the bfsDecisionForDischarge property.
     * 
     * @param value
     *     allowed object is
     *     {@link BfsDataType }
     *     
     */
    public void setBfsDecisionForDischarge(BfsDataType value) {
        this.bfsDecisionForDischarge = value;
    }

    /**
     * Gets the value of the bfsResidenceAfterDischarge property.
     * 
     * @return
     *     possible object is
     *     {@link BfsDataType }
     *     
     */
    public BfsDataType getBfsResidenceAfterDischarge() {
        return bfsResidenceAfterDischarge;
    }

    /**
     * Sets the value of the bfsResidenceAfterDischarge property.
     * 
     * @param value
     *     allowed object is
     *     {@link BfsDataType }
     *     
     */
    public void setBfsResidenceAfterDischarge(BfsDataType value) {
        this.bfsResidenceAfterDischarge = value;
    }

    /**
     * Gets the value of the caseDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the caseDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCaseDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CaseDetailType }
     * 
     * 
     */
    public List<CaseDetailType> getCaseDetail() {
        if (caseDetail == null) {
            caseDetail = new ArrayList<CaseDetailType>();
        }
        return this.caseDetail;
    }

    /**
     * Gets the value of the hospitalizationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getHospitalizationDate() {
        return hospitalizationDate;
    }

    /**
     * Sets the value of the hospitalizationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setHospitalizationDate(XMLGregorianCalendar value) {
        this.hospitalizationDate = value;
    }

    /**
     * Gets the value of the treatmentDays property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getTreatmentDays() {
        return treatmentDays;
    }

    /**
     * Sets the value of the treatmentDays property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setTreatmentDays(Duration value) {
        this.treatmentDays = value;
    }

    /**
     * Gets the value of the hospitalizationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHospitalizationType() {
        if (hospitalizationType == null) {
            return "regular";
        } else {
            return hospitalizationType;
        }
    }

    /**
     * Sets the value of the hospitalizationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHospitalizationType(String value) {
        this.hospitalizationType = value;
    }

    /**
     * Gets the value of the hospitalizationMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHospitalizationMode() {
        if (hospitalizationMode == null) {
            return "cantonal";
        } else {
            return hospitalizationMode;
        }
    }

    /**
     * Sets the value of the hospitalizationMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHospitalizationMode(String value) {
        this.hospitalizationMode = value;
    }

    /**
     * Gets the value of the clazz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClazz() {
        if (clazz == null) {
            return "general";
        } else {
            return clazz;
        }
    }

    /**
     * Sets the value of the clazz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClazz(String value) {
        this.clazz = value;
    }

    /**
     * Gets the value of the sectionMajor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSectionMajor() {
        return sectionMajor;
    }

    /**
     * Sets the value of the sectionMajor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSectionMajor(String value) {
        this.sectionMajor = value;
    }

    /**
     * Gets the value of the hasExpenseLoading property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isHasExpenseLoading() {
        if (hasExpenseLoading == null) {
            return true;
        } else {
            return hasExpenseLoading;
        }
    }

    /**
     * Sets the value of the hasExpenseLoading property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHasExpenseLoading(Boolean value) {
        this.hasExpenseLoading = value;
    }

    /**
     * Gets the value of the doCostAssessment property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isDoCostAssessment() {
        if (doCostAssessment == null) {
            return false;
        } else {
            return doCostAssessment;
        }
    }

    /**
     * Sets the value of the doCostAssessment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDoCostAssessment(Boolean value) {
        this.doCostAssessment = value;
    }

}
