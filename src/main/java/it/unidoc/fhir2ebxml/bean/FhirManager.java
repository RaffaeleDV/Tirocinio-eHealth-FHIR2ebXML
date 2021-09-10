package it.unidoc.fhir2ebxml.bean;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.unidoc.fhir2ebxml.conf.Fhir2ebXmlConfiguration;

@Component
public class FhirManager {

	@Autowired
	private Fhir2ebXmlConfiguration conf;

	@PostConstruct
	private void init() {
		String author = conf.getAuthorCodes();

		System.out.println(author);
	}

}
