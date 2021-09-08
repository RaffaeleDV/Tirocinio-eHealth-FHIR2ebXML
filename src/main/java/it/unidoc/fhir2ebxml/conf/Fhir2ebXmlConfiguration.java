package it.unidoc.fhir2ebxml.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 * @author b.amoruso
 */

@ConfigurationProperties(prefix="fhir2ebxml")
public class Fhir2ebXmlConfiguration {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
