package it.unidoc.fhir2ebxml.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.logging.Logger;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import it.unidoc.fhir2ebxml.bean.FhirManager;

@RestController
public class TranslateController {
	
	private static final Logger logger = Logger.getLogger(TranslateController.class.getName());
	
	@Autowired
	private FhirManager m;
	
	
	//COMMAND curl -X POST -F "file=@PATH" http://localhost:8080/add
	@PostMapping("/add")
	public void add(@RequestParam("file") MultipartFile file) throws IOException {
		logger.info("POST");
		if (file == null) {
			throw new RuntimeException("You must select a file");
		}
		
		InputStream inputStream = file.getInputStream();
		String originalName = file.getOriginalFilename();
		String name = file.getName();
		String contentType = file.getContentType();
		long size = file.getSize();
		String id = UUID.randomUUID().toString();

		logger.info("inputStream: " + inputStream);
		logger.info("originalName: " + originalName);
		logger.info("name: " + name);
		logger.info("contentType: " + contentType);
		logger.info("size: " + size);
		logger.info("id: " + id); // ID used for GET retrieve		
		
		byte[] buffer = new byte[inputStream.available()];
		inputStream.read(buffer);

		File targetFile = new File("testfile/inputfile.json");

		try (OutputStream outStream = new FileOutputStream(targetFile)) {
		    outStream.write(buffer);
		}
		 
		File out = new File("testfile/"+id+".xml");//Stored with the id
		
		try {
			m.translateJSON(targetFile, out);
		} catch (Exception e) {			
			e.printStackTrace();
		}	
		
	}
	//COMMAND curl -X GET -F "id=ID" http://localhost:8080/retrieve --output file.xml
	@GetMapping(value = "/retrieve",
			produces = MediaType.TEXT_XML_VALUE )
	public FileSystemResource retrieve(@RequestParam("id") String id) {
		logger.info("GET");		
		
		return new FileSystemResource("testfile/"+id+".xml");//File with the id requested		
	}

}
