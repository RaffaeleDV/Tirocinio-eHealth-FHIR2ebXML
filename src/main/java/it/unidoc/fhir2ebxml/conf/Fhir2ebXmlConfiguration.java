package it.unidoc.fhir2ebxml.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 * @author b.amoruso
 */

@ConfigurationProperties(prefix="fhir2ebxml")
public class Fhir2ebXmlConfiguration {

	private String name;
	private String classificationSchemeAuthor;
	private String classificationSchemeTypeCode;
	private String classificationSchemeClassCode;
	private String classificationSchemeConfidentialityCode;
	private String classificationSchemeFormatCode;
	private String classificationSchemeEventCodeList;
	private String classificationSchemeFacilityTypeCode;
	private String classificationSchemePracticeSettingCode;
	private String identificationSchemeUniqueId;
	private String identificationSchemePatienId;
	private String extrinsicObjectStable;
	private String extrinsicObjectonDemand;
	private String objectTypeClassification;
	private String objectTypeExternal;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassificationSchemeAuthor() {
		return classificationSchemeAuthor;
	}

	public void setClassificationSchemeAuthor(String classificationSchemeAuthor) {
		this.classificationSchemeAuthor = classificationSchemeAuthor;
	}

	public String getClassificationSchemeTypeCode() {
		return classificationSchemeTypeCode;
	}

	public void setClassificationSchemeTypeCode(String classificationSchemeTypeCode) {
		this.classificationSchemeTypeCode = classificationSchemeTypeCode;
	}

	public String getClassificationSchemeClassCode() {
		return classificationSchemeClassCode;
	}

	public void setClassificationSchemeClassCode(String classificationSchemeClassCode) {
		this.classificationSchemeClassCode = classificationSchemeClassCode;
	}

	public String getClassificationSchemeConfidentialityCode() {
		return classificationSchemeConfidentialityCode;
	}

	public void setClassificationSchemeConfidentialityCode(String classificationSchemeConfidentialityCode) {
		this.classificationSchemeConfidentialityCode = classificationSchemeConfidentialityCode;
	}

	public String getClassificationSchemeFormatCode() {
		return classificationSchemeFormatCode;
	}

	public void setClassificationSchemeFormatCode(String classificationSchemeFormatCode) {
		this.classificationSchemeFormatCode = classificationSchemeFormatCode;
	}

	public String getClassificationSchemeEventCodeList() {
		return classificationSchemeEventCodeList;
	}

	public void setClassificationSchemeEventCodeList(String classificationSchemeEventCodeList) {
		this.classificationSchemeEventCodeList = classificationSchemeEventCodeList;
	}

	public String getClassificationSchemeFacilityTypeCode() {
		return classificationSchemeFacilityTypeCode;
	}

	public void setClassificationSchemeFacilityTypeCode(String classificationSchemeFacilityTypeCode) {
		this.classificationSchemeFacilityTypeCode = classificationSchemeFacilityTypeCode;
	}

	public String getClassificationSchemePracticeSettingCode() {
		return classificationSchemePracticeSettingCode;
	}

	public void setClassificationSchemePracticeSettingCode(String classificationSchemePracticeSettingCode) {
		this.classificationSchemePracticeSettingCode = classificationSchemePracticeSettingCode;
	}

	public String getIdentificationSchemeUniqueId() {
		return identificationSchemeUniqueId;
	}

	public void setIdentificationSchemeUniqueId(String identificationSchemeUniqueId) {
		this.identificationSchemeUniqueId = identificationSchemeUniqueId;
	}

	public String getIdentificationSchemePatienId() {
		return identificationSchemePatienId;
	}

	public void setIdentificationSchemePatienId(String identificationSchemePatienId) {
		this.identificationSchemePatienId = identificationSchemePatienId;
	}

	public String getExtrinsicObjectStable() {
		return extrinsicObjectStable;
	}

	public void setExtrinsicObjectStable(String extrinsicObjectStable) {
		this.extrinsicObjectStable = extrinsicObjectStable;
	}

	public String getExtrinsicObjectonDemand() {
		return extrinsicObjectonDemand;
	}

	public void setExtrinsicObjectonDemand(String extrinsicObjectonDemand) {
		this.extrinsicObjectonDemand = extrinsicObjectonDemand;
	}

	public String getObjectTypeClassification() {
		return objectTypeClassification;
	}

	public void setObjectTypeClassification(String objectTypeClassification) {
		this.objectTypeClassification = objectTypeClassification;
	}

	public String getObjectTypeExternal() {
		return objectTypeExternal;
	}

	public void setObjectTypeExternal(String objectTypeExternal) {
		this.objectTypeExternal = objectTypeExternal;
	}

}
