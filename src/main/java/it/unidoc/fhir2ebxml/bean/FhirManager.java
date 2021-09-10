package it.unidoc.fhir2ebxml.bean;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import it.unidoc.fhir2ebxml.conf.Fhir2ebXmlConfiguration;

@Component
public class FhirManager {

	@Autowired
	private Fhir2ebXmlConfiguration conf;
	@Autowired
	private ApplicationContext cont;
	
	//public static int id = 3;
	
	Document doc = null;
	
	@PostConstruct
	private void init() {
		String author = conf.getAuthorCodes();
		
		//System.out.println(cont.getBean(FhirManager.class).id);

		//System.out.println(author);	
		
	}
	
	public void traslateJSON(String urlFile){
		JSONTokener jT = null;
		try {
			jT = new JSONTokener(new FileReader(urlFile));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JSONObject oJ = new JSONObject(jT);
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    doc = docBuilder.newDocument();
		
		readAuthor(oJ);
		
		createXML(doc);
		
	}
	
	private void readAuthor(JSONObject oJ) {
		// TODO Auto-generated method stub
		JSONArray arr = oJ.getJSONArray("author");
		String authorPerson = arr.getJSONObject(0).getString("authorPerson");
		String authorInstitution = arr.getJSONObject(0).getString("authorInstitution");
		addAuthor(authorPerson, authorInstitution);
	}

	private void addAuthor(String authorPerson, String authorInstitution) {
		// TODO Auto-generated method stub
		Element root = doc.createElement("rim:Classification");
		root.setAttribute("classificationScheme", "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d");
		root.setAttribute("classifiedObject", "theDocument");
		root.setAttribute("nodeRepresentation", "");	    
		doc.appendChild(root);

		Element authorname = doc.createElement("rim:Slot");
		authorname.setAttribute("name", "authorPerson");
		authorname.setTextContent(authorPerson);

		root.appendChild(authorname);
		
		Element authorinst = doc.createElement("rim:Slot");
		authorinst.setAttribute("name", "authorInstitution");
		authorinst.setTextContent(authorInstitution);

		root.appendChild(authorinst);
		
	}

	/*public void traslateJSON(String s) throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	    
	    doc = docBuilder.newDocument();
	    Element root = doc.createElement("rim:Classification");
	    root.setAttribute("classificationScheme", "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d");
	    root.setAttribute("classifiedObject", "theDocument");
	    root.setAttribute("nodeRepresentation", "");	    
	    doc.appendChild(root);
	    
	    Element authorname = doc.createElement("rim:Slot");
	    authorname.setAttribute("name", "authorPerson");
	    authorname.setTextContent("Raffaele");
	    
	    root.appendChild(authorname);
	    
	    try {
			FileOutputStream out = new FileOutputStream("C:\\Users\\Raffaele\\Documents\\Università\\tirocinioUniDoc\\test.xml");
			createXML(doc, out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	}*/

	private void createXML(Document doc){

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(doc);
		
		FileOutputStream out = null;
		try {
			out = new FileOutputStream("testfile\\test.xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StreamResult result = new StreamResult(out);

		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Creato file XML ");
		
	}
	
	

}
