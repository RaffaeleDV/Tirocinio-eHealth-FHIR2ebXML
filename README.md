**Introduzione**

Il tirocinio si svolgerà presso l’azienda UniDoc, spinoff dell’Università degli studi di Salerno, che si occupa della progettazione e produzione di soluzioni informatiche e della successiva commercializzazione.

**Scopo**

Il tirocinio si pone nell’ambito dell’eHealth, più precisamente nella condivisione di documentazione di carattere diagnostico. Questa integrazione è fatta rispettando gli standard informatici posti dalla IHE (Integrating the Healthcare Enterprise).

Il prototipo da realizzare si occuperà nello specifico della comunicazione tra due tipi differenti di profili di integrazione, XDS e MHD.

XDS (Cross-Enterprise Document Sharing) nella sua seconda versione XDS.b, è un profilo di comunicazione basato sul protocollo SOAP con documenti in formato ebXML.

MHD (Mobile access to Health Documents) è un profilo di comunicazione basato sull’architettura RESTful, la leggerezza di questa tecnologia lo rende adatto ad operare su dispositivi con risorse limitate come i dispositivi mobili (tablet, smartphone, ecc.…).

L’applicazione da sviluppare si occuperà si prelevare i documenti creati dal profilo MHD in formato JSON e tradurli nel formato ebXML consumabile dal profilo XDS.b come specificato nel proprio Technical Framework, nello specifico il formato JSON si atterrà allo standard FHIR dell’organizzazione HL7.

Il progetto sarà sviluppato seguendo secondo l’approccio dei microservizi, questa architettura prevede che l’applicazione venga sviluppata tramite una collezione di servizi indipendenti (o debolmente accoppiati) di piccole dimensioni, questi servizi sono a grana-fine e utilizzano protocolli leggeri. I microservizi possono comunicare tra di loro tramite API e hanno la caratteristica di essere autonomi, quindi, il funzionamento del singolo servizio non dipende dagli altri e di essere specializzati in compiti singoli in modo da semplificarne lo sviluppo. L’adottare questa tipologia di architettura porta con sé una serie di vantaggi:

- scalabilità e flessibilità

Data l’indipendenza dei microservizi questa permette una scalabilità in base alle richieste dell’applicazione

- agilità

I microservizi possono essere sviluppati da gruppi indipendenti e di dimensioni ridotte

- distribuzione

Supportano una integrazione e distribuzione continua, permettendo una facile manutenzione e un immediato aggiornamento

- codice riutilizzabile

Essendo formati da codice di ridotte dimensioni e altamente specializzato il suo riutilizzo è estremamente facilitato

**MHD (Mobile access to Health Documents)**

Il profilo MHD prevede l’utilizzo di diversi attori, transazioni e risorse aderenti allo standard HL7 FHIR R4.

Gli attori che intervengono nella comunicazione sono 4:

- Document Source
- Document Recipient
- Document Consumer
- Document Responder

Il Document Source si occupa di fornire tramite la transazione ITI-65 documenti o metadati al Document Recipient che invece si occupa di ricevere questi dati.

Il Document Consumer interroga tramite ITI-66/ITI-67/ITI-68 il Document Responder che si occupa di rispondere con la risorsa richiesta.

Le 4 transazioni sopraccitate:

- ITI-65 - Provide Document Bundle
- ITI-66 - Find Document Lists
- ITI-67 - Find Document References
- ITI-68 - Retrieve Document

Sono tutte transazioni richieste dal TF di MHD quindi gli attori sono tenute a supportarle.

ITI-65 è la transazione che permette la comunicazione tra Document Source e Document Recipient, questa aderisce allo standard FHIR e utilizza il metodo POST dell’HTTP e un file di tipo FHIR-JSON oppure FHIR-XML.

ITI-66/ITI-67/ITI-68 sono transazioni utilizzate dal Document Consumer per richiedere al Document Responder rispettivamente una lista di risorse, una lista di metadati e un singolo documento, queste interrogano grazie a un set di parametri tramite i metodi GET o POST dell’HTTP.

Il profilo MHD utilizza una serie di risorse che aderiscono allo standard HL7 FHIR R4:

- DocumentReference: contiene i metadati di un qualsiasi documento in modo che possa essere trovato e gestito.
- DocumentManifest: è un insieme di documenti in un solo pacchetto con dei metadati che descrivono la collezione.
- List: descrive una collezione di risorse, possibilmente ordinate, queste possono essere sia omogenee, un solo tipo di risorsa, oppure eterogenee e contenere una varietà di risorse.
- Patient: contiene la demografica e altre informazioni amministrative associate ad un individuo in cura oppure collegato a servizi sanitari.
- Practitioner: contiene informazioni di una persona coinvolta in attività sanitarie (chirurghi, dentisti, volontari, segretari, informatici, ecc.…).
- OperationOutcome: è una collezione di errori, avvisi o messaggi di informazione relativi ad un’azione di sistema.
- Bundle: è un insieme di risorse impiegate in operazioni di trasporto o di persistenza.

**Bundle**

Il Bundle è una risorsa che assume particolare valore all’interno del profilo MHD, lo standard FHIR lo definisce come un “raggruppamento” di risorse. Queste risorse raggruppate sono utili per vari utilizzi:

- restituire un set di risorse che corrispondono ad un criterio dato,
- restituire un set di versioni di risorse prese dallo storico delle operazioni
- inviare un set di risorse tramite un messaggio
- creare/aggiornare/cancellare un set di risorse tramite una singola operazione
- Memorizzare una collezione di risorse

**Traduzione tra FHIR ed eb.XML**

La codifica delle informazioni contenute nei documenti varia tra gli standard che il progetto andrà ad utilizzare. In questa sezione si mostreranno le possibili “equivalenze” tra i due standard (dove possibili) e si descriverà lo scheletro delle due strutture.

Lo standard FHIR utilizza documenti JSON e XML (il nostro progetto prenderà in esame file JSON) con le risorse precedentemente descritte.

Lo standard eb.XML utilizza una struttura XML con extra attributi. Questi extra attributi possono essere codificati in degli oggetti definiti dallo standard:

- ebRIM Classification
- ebRIM Slot
- ebRIM ExternalIdentifier
- ebRIM Name
- ebRIM Description

![Diagram

Figura - Struttura file eb.XML

| Oggetto eb.XML | Attributo | Molt. | Descrizione | Oggetto FHIR | Attributo | Molt. | Note |
| --- | --- | --- | --- | --- | --- | --- | --- |
| ExtrinsicObject | uniqueId | 1   | Identificatore globale assegnato al documento | DocumentReference | masterIdentifier | 0..1 | In eb.XML è contenuto in un ebRIM ExternalIdentifier |
| mimeType | 1   | Formato documento | content.attachment | 0..1 |     |
| availabilityStatus | 0..1 | Disponibilità del documento | status | 1   |     |
| objectType | 1   |     | Corrispondenza non univoca |     |     |     |
| author | 0..N | Autore del documento | DocumentReference | author | 0..N | In eb.XML è contenuto in un ebRIM Classification |
| classCode | 1   | Specifica il tipo di documento di alto livello | category | 0..N |
| patientId | 1   | Identificatore del paziente | subject | 0..1 |
| creationTime | 1   | Tempo di creazione | Content.attachmennt | 0..1 | In eb.XML è contenuto in un ebRIM Slot |
| confidentialityCode | 1   | Sicurezza e privacy del documento | securityLabel | 0..N | In eb.XML è contenuto in un ebRIM Classification |
| entryUUID | 1   | Identificatore unico globale | identifier | 0..N |     |
| formatCode | 1   | Dettagli tecnici | Content.format | 0..1 | In eb.XML è contenuto in un ebRIM Classification |
| healthcareFacility<br><br>TypeCode | 1   | Tipo di organizzazione | Context.facilityType | 0..1 |
| languageCode | 1   | Linguaggio umano dei documenti | Content.attachmennt | 1   | In eb.XML è contenuto in un ebRIM Slot |
| practiceSettingCode | 1   | Specializzazione clinica | Context.practiceSetting | 0..1 | In eb.XML è contenuto in un ebRIM Classification |
| serviceStartTime | 1   | Tempo di inizio e di fine del servizio | Context.period | 0..1 | In eb.XML è contenuto in un ebRIM Slot |
| serviceStopTime | 1   |
| sourcePatientId | 1   |     | Context.sourcePatientInfo | 0..1 |
| sourcePatientInfo |     |
| typeCode | 1   | Tipo preciso del documento dalla vista dell’utente | Type | 0..1 | In eb.XML è contenuto in un ebRIM Classification |
| RegistryPackage | uniqueId | 1   | Identificatore globale assegnato al documento | List | Identifier | 0..1 | In eb.XML è contenuto in un ebRIM ExternalIdentifier |
| limitedMetadata | 0   | Indica che è stati utilizzati dei requisiti meno rigorosi | Meta.profile | 0..N | In eb.XML è contenuto in un ebRIM Classification |
| Title | 0..1 | Titolo del RegistryPackage | Title | 0..1 | In eb.XML è contenuto in un ebRIM Name |
| lastUpdateTime/ submissionTime | 1   | Data del documento | date | 0..1 | In eb.XML è contenuto in un ebRIM Slot |

**Architettura REST**

Il profilo MHD su cui si concentrerà il tirocinio si basa su un’architettura di tipo REST. Questa architettura si lega al meglio a MHD grazie alla sua estrema semplicità e leggerezza. Nel nostro prototipo si andrà a costruire questa architettura grazie agli strumenti messi a disposizione dal framework Spring, si andrà a creare un controller REST che andrà a gestire due metodi HTTP:

- GET: mappato sul percorso “/retrieve”, che riceverà in ingresso un ID relativo a un documento tradotto da restituire
- POST: mappato sul percorso “/add”, che si occuperà di accettare un file JSON da tradurre

**Tecnologie adottate**

Il prototipo sarà sviluppato in ambiente Java tramite il framework Spring. Si utilizzerà l’ambiente di sviluppo Eclipse e si farà uso di un sistema di controllo di versione (GIT) la cui repository verrà ospitata in remoto su Bitbucket.
