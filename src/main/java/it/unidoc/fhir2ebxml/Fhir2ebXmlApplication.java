package it.unidoc.fhir2ebxml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import it.unidoc.fhir2ebxml.conf.Fhir2ebXmlConfiguration;

/**
 * 
 * @author b.amoruso
 */

@SpringBootApplication
@EnableConfigurationProperties(Fhir2ebXmlConfiguration.class)
public class Fhir2ebXmlApplication {

	public static void main(String[] args) {
		SpringApplication.run(Fhir2ebXmlApplication.class, args);
	}

}
