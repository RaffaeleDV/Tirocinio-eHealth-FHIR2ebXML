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
import org.json.JSONException;
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
		
		//Creazione XML
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    doc = docBuilder.newDocument();
	    
	    Element root = createExtrinsicObject(doc);   
		
		readAuthor(oJ, root);
		
		createXML(doc);
		
	}
	
	private Element createExtrinsicObject(Document doc) {
		
		final String stableDocument = "urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1";
		final String onDemandDocument = "urn:uuid:34268e47-fdf5-41a6-ba33-82133c465248";		
		
		Element root = doc.createElement("rim:ExtrinsicObject");
	    root.setAttribute("id", "");
	    root.setAttribute("objectType", stableDocument);	
	    
	    doc.appendChild(root);
	    
	    return root;
	}

	private void readAuthor(JSONObject oJ, Element root) {
		
		JSONArray author = oJ.getJSONArray("author");
		/*String authorReference = arr.getJSONObject(0).getString("reference");
		System.out.println(authorReference);
		String authorReference2 = arr.getJSONObject(1).getString("reference");
		System.out.println(authorReference2);*/
		String aID = "";
		String aName = "";
		String aSurname = "";
		
		for (int i = 0; true; i++) {//Search authors
			try {
				String authorReference = author.getJSONObject(i).getString("reference");
				System.out.println(authorReference);
				if (authorReference.contains("#")) {
					JSONArray contained = oJ.getJSONArray("contained");
					for (int j = 0; true; j++) {//Search author's data in contained
						if (!authorReference.contains(contained.getJSONObject(j).getString("id")))
							continue;
						aID = contained.getJSONObject(j).getString("id");
						aName = contained.getJSONObject(j).getJSONArray("name").getJSONObject(0).getJSONArray("given").getString(0);
						aSurname = contained.getJSONObject(j).getJSONArray("name").getJSONObject(0).getString("family");
						
						addAuthor(aID, aName+" "+aSurname," ", root);
						break;
					}
				}
			} catch (JSONException e) {
				System.out.printf("Exit i = %d\n", i);
				break;
			}
		}
		//System.out.printf("Numero autori: %d\n", i);
		return;
		
		
		
		//String resType = "";
	
		/*
		 * while(true) { if(authorReference.contains("#")){//Reference contained in the
		 * document
		 * 
		 * JSONArray cont = oJ.getJSONArray("contained"); resType =
		 * cont.getJSONObject(0).getString("resourceType");
		 * if(!resType.equals("Practitioner")) continue; id =
		 * cont.getJSONObject(0).getString("id"); if(!id.equals("#id1")) continue; gName
		 * = cont.getJSONObject(0).getJSONArray("name").getJSONObject(0).getJSONArray(
		 * "given").getString(0); fName =
		 * cont.getJSONObject(0).getJSONArray("name").getJSONObject(0).getString(
		 * "family"); }
		 */
		//}
		//String authorInstitution = " ";
				//arr.getJSONObject(0).getString("authorInstitution");
		//addAuthor(resType, id, gName+fName,authorInstitution, root);
	}

	private void addAuthor(String id, String authorPerson, String authorInstitution, Element mRoot) {
		System.out.println("Addauthor");
		final String authorClass = "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d";
		
		Element root = doc.createElement("rim:Classification");
		root.setAttribute("classificationScheme", authorClass);
		root.setAttribute("classifiedObject", "theDocument");
		root.setAttribute("id", id);
		root.setAttribute("objectType", "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification");
		root.setAttribute("nodeRepresentation", ""); 
		mRoot.appendChild(root);

		Element authorname = doc.createElement("rim:Slot");
		authorname.setAttribute("name", "authorPerson");		
		Element aValueList = doc.createElement("rim:ValueList");
		Element aValue = doc.createElement("rim:Value");		
		aValue.setTextContent(authorPerson);

		root.appendChild(authorname);
		authorname.appendChild(aValueList);
		aValueList.appendChild(aValue);
		
		Element authorinst = doc.createElement("rim:Slot");
		authorinst.setAttribute("name", "authorInstitution");
		Element iValueList = doc.createElement("rim:ValueList");
		Element iValue = doc.createElement("rim:Value");		
		iValue.setTextContent(authorInstitution);

		root.appendChild(authorinst);
		authorinst.appendChild(iValueList);
		iValueList.appendChild(iValue);
		
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
