package it.unidoc.fhir2ebxml;


import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import it.unidoc.fhir2ebxml.bean.FhirManager;
import it.unidoc.fhir2ebxml.conf.Fhir2ebXmlConfiguration;

/**
 * 
 * @author b.amoruso
 */

@SpringBootApplication
@EnableConfigurationProperties(Fhir2ebXmlConfiguration.class)
public class Fhir2ebXmlApplication /*implements ApplicationRunner */{

	@Autowired
	private FhirManager m;

	public static void main(String[] args) throws FileNotFoundException {
		SpringApplication.run(Fhir2ebXmlApplication.class, args);

		/*JSONTokener jT = new JSONTokener(new FileReader("C:\\Users\\Raffaele\\Documents\\Università\\tirocinioUniDoc\\DocumentReference-ex-DocumentReferenceMinimal.json"));
		
		JSONObject oJ = new JSONObject(jT);
		
		System.out.println(oJ.toString()+"\n");
		
		JSONArray arr = oJ.getJSONArray("identifier");
		
		String s = arr.getJSONObject(0).getString("system");
		
		System.out.println(s);
		
		String s2 = oJ.getJSONObject("masterIdentifier").getString("value");
		
		System.out.println(s2);
		
		String s3 = oJ.getString("id");
		
		System.out.println(s3);
		
		String s4 = oJ.getJSONObject("meta").getJSONArray("profile").getString(0);
		
		System.out.println(s4);*/
		
		//System.out.println(oJ.getString("masterIdentifier").to);
	}
	/*
	 * @Override public void run(ApplicationArguments args) throws Exception { if
	 * (args.getSourceArgs().length < 2) throw new
	 * Exception("Invalid parameters. Specify input and output file");
	 * 
	 * File in = new File(args.getSourceArgs()[0]); File out = new
	 * File(args.getSourceArgs()[1]);
	 * 
	 * m.translateJSON(in, out); }
	 */

}
