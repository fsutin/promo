package com.google.drive

import static org.junit.Assert.*

import org.junit.*
import org.sonar.wsclient.services.Measure

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.Drive.Files.Insert
import com.google.gdata.client.GoogleService.InvalidCredentialsException
import com.google.gdata.client.spreadsheet.FeedURLFactory
import com.google.gdata.client.spreadsheet.SpreadsheetQuery
import com.google.gdata.client.spreadsheet.SpreadsheetService
import com.google.gdata.data.spreadsheet.ListEntry
import com.google.gdata.data.spreadsheet.SpreadsheetEntry
import com.google.gdata.data.spreadsheet.SpreadsheetFeed
import com.google.gdata.data.spreadsheet.WorksheetEntry
import com.google.gdata.data.spreadsheet.WorksheetFeed
import com.google.gdata.util.InvalidEntryException
import com.sun.media.sound.InvalidFormatException

class GoogleDriveTests {

	private static String CLIENT_ID = "743045243915-52v5f1jkmjjisfbuvv54prt2dlikg33j@developer.gserviceaccount.com";
	private static String CLIENT_SECRET = "k9z9C6AZQQQxOZtXK6P3S5ng";
	//private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
	
	/** The URL for spread sheets*/
	String SPREADSHEET_FEED_URL = "https://spreadsheets.google.com/feeds/spreadsheets/private/full"
	
	/** The name of the spreadsheet service */
	String SPREADSHEET_SERVICE = "TheSpreadsheetService"
	
	/** The URL for spread sheet*/
	String SPREADHSEET_METRIC= "metricSpreadSheet"
	
	/** The header of the metric */
	String METRIC_HEADER = "METRICA"
	
	/** The header of the value */
	String VALUE_HEADER = "VALOR"
	
	/** The spring security service injection */
	def springSecurityService
	
	/** The google spreadsheet service*/
	def googleSpreadsheetService
	
	/** The sonar service injection */
	def sonarExtractorService
	
	
	@Before
	void setUp() {
		// Setup logic here
	}

	@After
	void tearDown() {
		// Tear down logic here
	}
	
	@Test
	void testSomething1(){
		HttpTransport httpTransport = new NetHttpTransport();
		JacksonFactory jsonFactory = new JacksonFactory();
		
		GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport).setJsonFactory(jsonFactory)
		.setServiceAccountId('743045243915-bfhqqtmr7jc4sr4djsq9ic7frli9tg61.apps.googleusercontent.com').setServiceAccountScopes(DriveScopes.DRIVE)
		.setServiceAccountPrivateKeyFromP12File(new File(System.getProperty("user.home")+"/568fe94cd505281e4903a8aefbb1b4fc24fff8a5-privatekey.p12"))
		.setServiceAccountUser("federico.sutin@globallogic.com")
		.build();

		Drive drive = new Drive.Builder(httpTransport, jsonFactory, credential).build();
		com.google.api.services.drive.model.File  file = new com.google.api.services.drive.model.File();
		file.setTitle("test");
		file.setMimeType("application/vnd.google-apps.spreadsheet");
		Insert insert = drive.files().insert(file);
		file = insert.execute();
		assertNotNull(file.getId())
	}
	
	/**
	 * Authenticates into the google
	 * drive service
	 */
	def authenticate(def service){
		
		try {
			service.setUserCredentials('federico.sutin@globallogic.com', 'Enable2013');
		} catch (InvalidCredentialsException e) {
			throw new IllegalAccessException(e.getMessage())
		}
	}
	
	/**
	 * @return the spread sheet service
	 */
	def getSpreadsheetService(){
		if(this.googleSpreadsheetService == null) {
			this.googleSpreadsheetService = new SpreadsheetService(this.SPREADSHEET_SERVICE)
		}
		this.authenticate(this.googleSpreadsheetService)
		return this.googleSpreadsheetService
	}
	
	/**
	 * @return a list of spreadsheets
	 */
	def listSpreadSheets(){
		def result = [:]
		for (SpreadsheetEntry sheet : this.getAll()) {
			result.put(sheet.getTitle().getPlainText(), sheet.getEditLink()?.getHref())
		}
		return result
	}
	
	private def getAll(){
		URL url = new URL(this.SPREADSHEET_FEED_URL);
		SpreadsheetFeed feed = this.getSpreadsheetService().getFeed(url, SpreadsheetFeed.class);
		feed.getEntries();
	}
	
	def export(String docKey, Measure metric) throws InvalidFormatException{
		def entry = this.getSpreadsheet(docKey)
		def feedUrl = this.getFeedUrl(entry)
		this.exportMetric(metric, entry, feedUrl)
	}
	
	def getSpreadsheet(def spreadSheetKey){
		FeedURLFactory factory = FeedURLFactory.getDefault()
		def service = this.getSpreadsheetService()
		SpreadsheetQuery spreadsheetQuery = new SpreadsheetQuery(factory.getSpreadsheetsFeedUrl());
		spreadsheetQuery.setTitleQuery(spreadSheetKey);
		spreadsheetQuery.setTitleExact(true);
		SpreadsheetFeed spreadsheetFeed = service.query(spreadsheetQuery, SpreadsheetFeed.class);
		
		List<SpreadsheetEntry> spreadsheets = spreadsheetFeed.getEntries();
		
		return spreadsheets.get(0);
	}
	
	def getFeedUrl(def entry){
		def service = this.getSpreadsheetService()
		WorksheetFeed worksheetFeed = service.getFeed(entry.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry worksheet = worksheets.get(0);
		return worksheet.getListFeedUrl();
	}
	
	def exportMetric(metric, entry, feedUrl){
		def service = this.getSpreadsheetService()
		ListEntry row = new ListEntry();
		row.getCustomElements().setValueLocal(METRIC_HEADER,metric.getMetricKey());
		row.getCustomElements().setValueLocal(VALUE_HEADER,metric.getFormattedValue());
		try{
			row = service.insert(feedUrl, row);
		} catch (InvalidEntryException e){
			throw new InvalidFormatException("Imposible de exporta. Asegurese de que su documento, tiene las columnas METRICA y VALOR")
		}
	}
	
	@Test
	void testSomething(){
		this.export('hola',sonarExtractorService.extractApacheMavenRCIMeasure())
		assert 1 ==1
	}
	
   /* @Test
    void testSomething() {
		OAuthToken googleOAuth = OAuthToken.findByUserId('federico.sutin@globallogic.com')
		GoogleTokenResponse token
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();
		if(googleOAuth != null){
			token = new GoogleTokenResponse();
			token.setAccessToken(googleOAuth.getAccessToken());
			token.setRefreshToken(googleOAuth.getRefreshToken());
			token.setScope(googleOAuth.getScope());
			token.setExpiresInSeconds(googleOAuth.getExpireTime());
			token.setTokenType(googleOAuth.getTokenType())
			token.setFactory(new JacksonFactory());

		}
		
		GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(jsonFactory).setClientSecrets(CLIENT_ID, CLIENT_SECRET).setTransport(httpTransport).build();
		credential.setFromTokenResponse(token)
		credential.refreshToken();
		
		//Create a new authorized API client
		Drive service = new Drive.Builder(httpTransport, jsonFactory, credential).build();
	
		//Insert a file
		File body = new File();
		body.setTitle("My document");
		body.setDescription("A test document");
		body.setMimeType("text/plain");
		
		java.io.File fileContent = new java.io.File("document.txt");
		FileContent mediaContent = new FileContent("text/plain", fileContent);
	
		File file = service.files().insert(body, mediaContent).execute();
		System.out.println("File ID: " + file.getId());
		
		assert 1==1
    }*/
	
	
	
}

