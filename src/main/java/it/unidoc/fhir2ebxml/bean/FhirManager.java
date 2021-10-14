package it.unidoc.fhir2ebxml.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashMap;

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
	HashMap<String, String> translationMap;
	final private static String translationFilePath = "src\\main\\resources\\translation";

	@PostConstruct
	private void init() {
		//String author = conf.getAuthorCodes();

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

		//Create structure XML
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		doc = docBuilder.newDocument();
		
		//Read translation file
		translationMap = readFile(translationFilePath);
		
		 /*for (HashMap.Entry<String, String> entry :
             translationMap.entrySet()) {
            System.out.println(entry.getKey() + " : "
                               + entry.getValue());
        }*/
		//Create ExtrinsicObkect 
		Element root = createExtrinsicObject(oJ, doc);   
		
		//Read resource and translate
		readAuthor(oJ, root);
		readUniqueID(oJ, root);
		readTypeCode(oJ, root);
		readClassCode(oJ, root);
		readPatientID(oJ, root);
		readConfidentialityCode(oJ, root);
		readLanguageCode(oJ, root);
		readCreationTime(oJ, root);
		readTitle(oJ, root);
		readFormatCode(oJ, root);
		readEventCodeList(oJ, root);
		readServiceTime(oJ, root);
		readFacilityTypeCode(oJ, root);
		readPracticeSettingCode(oJ, root);
		readSourcePatient(oJ, root);

		createXML(doc);

	}	


	private void readSourcePatient(JSONObject oJ, Element root) {
		String sourcePID = oJ.getJSONObject("context").getJSONObject("sourcePatientInfo").getString("reference");
		
		createSlot("sourcePatientId", sourcePID.substring(1), root);
		
		
		for(int i = 0; true; i++) {
			try {
				JSONObject patient = oJ.getJSONArray("contained").getJSONObject(i);
				if(!patient.getString("id").equals(sourcePID)) continue;
				
			} catch (JSONException e) {
				System.out.println("Patient non found.");
				break;
				
			}
		}
		
		
	}

	private void readPracticeSettingCode(JSONObject oJ, Element root) {
		JSONObject setting = oJ.getJSONObject("context").getJSONObject("practiceSetting").getJSONArray("coding").getJSONObject(0);
		
		String code = setting.getString("system");
		String nodeC = setting.getString("code");
		String name = setting.getString("display");
		
		createClassification("ClassificationSchemePracticeSettingCode", nodeC, code, name, root, false);
	}

	private void readFacilityTypeCode(JSONObject oJ, Element root) {
		JSONObject facility = oJ.getJSONObject("context").getJSONObject("facilityType").getJSONArray("coding").getJSONObject(0);
		
		String code = facility.getString("system");
		String nodeC = facility.getString("code");
		String name = facility.getString("display");
		
		createClassification("ClassificationSchemeFacilityTypeCode", nodeC, code, name, root, false);
	}

	private void readServiceTime(JSONObject oJ, Element root) {
		JSONObject time = oJ.getJSONObject("context").getJSONObject("period");
		
		String start = time.getString("start");
		
		createSlot("serviceStartTime", start, root);
		
		String stop = time.getString("end");
		
		createSlot("serviceStopTime", stop, root);		
	}

	private void readEventCodeList(JSONObject oJ, Element root) {
		
		JSONObject event = oJ.getJSONObject("context").getJSONArray("event").getJSONObject(0).getJSONArray("coding").getJSONObject(0);
		
		String code = event.getString("system");
		String nodeC = event.getString("code");
		String name = event.getString("display");
		
		createClassification("ClassificationSchemeEventCodeList", nodeC, code, name, root, false);
		
	}

	private void readFormatCode(JSONObject oJ, Element root) {

		JSONObject format = oJ.getJSONArray("content").getJSONObject(0).getJSONObject("format");
		
		String code = format.getString("system");
		String nodeC = format.getString("code");
		String name = format.getString("display");
		
		createClassification("ClassificationSchemeFormatCode", nodeC, code, name, root, false);
		
	}

	private void readTitle(JSONObject oJ, Element root) {
		
		String title = oJ.getJSONArray("content").getJSONObject(0).getJSONObject("attachment").getString("title");
		
		Element nameE = doc.createElement("rim:Name");
		Element lString = doc.createElement("rim:LocalizedString");		
		lString.setAttribute("value",title);
		
		nameE.appendChild(lString);	
		
		root.appendChild(nameE);
	}

	private void readCreationTime(JSONObject oJ, Element root) {
		
		String time = oJ.getJSONArray("content").getJSONObject(0).getJSONObject("attachment").getString("creation");		
		createSlot("creationTime", time, root); 
		
	}

	private void readLanguageCode(JSONObject oJ, Element root) {
		
		String lang = oJ.getJSONArray("content").getJSONObject(0).getJSONObject("attachment").getString("language");		
		createSlot("languageCode", lang, root); 
	}

	private void readConfidentialityCode(JSONObject oJ, Element root) {
		JSONObject security = oJ.getJSONArray("securityLabel").getJSONObject(0).getJSONArray("coding").getJSONObject(0);
		
		String code = security.getString("system");
		String nodeC = security.getString("code");
		String name = security.getString("display");
		
		createClassification("ClassificationSchemeConfidentialityCode", nodeC, code, name, root, false);
		
	}

	private void readPatientID(JSONObject oJ, Element root) {
		String patient = oJ.getJSONObject("subject").getString("reference");
		
		createExternalIdentifier("IdentificationSchemePatienID", patient.substring(8), "XDSDocumentEntry.patientId", root);
		
	}

	private void readClassCode(JSONObject oJ, Element root) {
		JSONArray category = oJ.getJSONArray("category");
		
		String code = category.getJSONObject(0).getJSONArray("coding").getJSONObject(0).getString("system");
		String nodeC = category.getJSONObject(0).getJSONArray("coding").getJSONObject(0).getString("code");
		String name = category.getJSONObject(0).getJSONArray("coding").getJSONObject(0).getString("display");
		
		createClassification("ClassificationSchemeClassCode", nodeC, code, name, root, false);
	}

	private void readTypeCode(JSONObject oJ, Element root) {
		JSONObject type = oJ.getJSONObject("type").getJSONArray("coding").getJSONObject(0);
		
		String code = type.getString("system");
		String nodeC = type.getString("code");
		String name = type.getString("display");
		
		createClassification("ClassificationSchemeTypeCode", nodeC, code, name, root, false);
		
		
	}

	private void readUniqueID(JSONObject oJ, Element root) {
		JSONArray mID = oJ.getJSONArray("masterIdentifier");
		
		String id = mID.getString(0);
		
		createExternalIdentifier("IdentificationSchemeUniqueID", id, "XDSDocumentEntry.uniqueId", root);		
	}	

	private void readAuthor(JSONObject oJ, Element root) {

		JSONArray author = oJ.getJSONArray("author");
		
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
		

		
		//String authorInstitution = " ";
		//arr.getJSONObject(0).getString("authorInstitution");
		//addAuthor(resType, id, gName+fName,authorInstitution, root);
	}

	private void addAuthor(String id, String authorPerson, String authorInstitution, Element mRoot) {
		
		Element root = doc.createElement("rim:Classification");
		root.setAttribute("classificationScheme", translationMap.get("ClassificationSchemeAuthor"));
		root.setAttribute("classifiedObject", "theDocument");
		root.setAttribute("id", id);
		root.setAttribute("objectType", translationMap.get("objectTypeClassification"));
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
	
	private void createClassification(String cScheme, String nodeC, String code, String name, Element mRoot, boolean author) {
		
		Element root = doc.createElement("rim:Classification");
		root.setAttribute("classificationScheme", translationMap.get(cScheme));
		root.setAttribute("classifiedObject", "theDocument");
		root.setAttribute("id", "id_x");
		root.setAttribute("objectType", translationMap.get("objectTypeClassification"));
		root.setAttribute("nodeRepresentation", nodeC);
		mRoot.appendChild(root);
		
		Element slot = doc.createElement("rim:Slot");
		slot.setAttribute("name", "codingScheme");		
		Element valueList = doc.createElement("rim:ValueList");
		Element valueE = doc.createElement("rim:Value");
		valueE.setTextContent(code);
		
		slot.appendChild(valueList);
		valueList.appendChild(valueE);
		
		Element nameE = doc.createElement("rim:Name");
		Element lString = doc.createElement("rim:LocalizedString");		
		lString.setAttribute("value",name);
		
		nameE.appendChild(lString);	
		
		root.appendChild(slot);	
		root.appendChild(nameE);
		
	}
	
	private void createExternalIdentifier(String iScheme, String value, String name, Element mRoot) {
		
		Element root = doc.createElement("rim:ExternalIdentifier");
		root.setAttribute("classificationScheme", translationMap.get(iScheme));
		root.setAttribute("value", value);
		root.setAttribute("id", "id_x");
		root.setAttribute("objectType", translationMap.get("objectTypeExternal"));	
		root.setAttribute("registryObject", "theDocument");
		mRoot.appendChild(root);
		
		Element nameE = doc.createElement("rim:Name");
		Element lString = doc.createElement("rim:LocalizedString");		
		lString.setAttribute("value",name);
		nameE.appendChild(lString);		
		root.appendChild(nameE);
		
	}
	
	private void createSlot(String name, String value, Element mRoot) {
		
		Element slot = doc.createElement("rim:Slot");
		slot.setAttribute("name", name);		
		Element valueList = doc.createElement("rim:ValueList");
		Element valueE = doc.createElement("rim:Value");
		valueE.setTextContent(value);
		
		slot.appendChild(valueList);
		valueList.appendChild(valueE);
		
		mRoot.appendChild(slot);
	}
	
	private HashMap<String, String> readFile(String translationfilepath) {
		HashMap<String, String> map
		= new HashMap<String, String>();
		BufferedReader br = null;

		try {


			File file = new File(translationfilepath);


			br = new BufferedReader(new FileReader(file));

			String l = null;


			while ((l = br.readLine()) != null) {

				if(l.isEmpty()) continue;
				String[] parts = l.split("=");

				String key = parts[0].trim();
				String value  = parts[1].trim();


				//*if (!name.equals("") && !number.equals(""))
				map.put(key, value);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {

			if (br != null) {
				try {
					br.close();
				}
				catch (Exception e) {
				};
			}
		}

		return map;
	}
	

	private Element createExtrinsicObject(JSONObject oJ, Document doc) {	
		
		Element root = doc.createElement("rim:ExtrinsicObject");
		root.setAttribute("id", "theDocument");		
		root.setAttribute("mimeType", oJ.getJSONArray("content").getJSONObject(0).getJSONObject("attachment").getString("contentType"));
		root.setAttribute("objectType", translationMap.get("ExtrinsicObjectStable"));
		doc.appendChild(root);

		return root;
	}


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
