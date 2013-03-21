package com.google.drive

import static org.junit.Assert.*
import org.junit.*

import org.springframework.aop.aspectj.RuntimeTestWalker.ThisInstanceOfResidueTestVisitor;

//import com.globallogic.sonar.exception.GoogleConnectionException
import com.google.gdata.client.GoogleService.InvalidCredentialsException
import com.google.gdata.client.spreadsheet.SpreadsheetService
import com.google.gdata.data.spreadsheet.SpreadsheetEntry
import com.google.gdata.data.spreadsheet.SpreadsheetFeed

class GoogleDriveTests {

//	private static String CLIENT_ID = "743045243915.apps.googleusercontent.com";
//	private static String CLIENT_SECRET = "zxeqm_w1LYGJVVsk9KT89Q2Q";
//	private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
	
	/** The URL for spread sheets*/
	String SPREADSHEET_FEED_URL = "https://spreadsheets.google.com/feeds/spreadsheets/private/full"
	
	/** The name of the spreadsheet service */
	String SPREADSHEET_SERVICE = "TheSpreadsheetService"
	
	/** The spring security service injection */
	def springSecurityService
	
	/** The google spreadsheet service */
	def googleSpreadsheetService
	
	/**
	 * Authenticates into the google
	 * drive service
	 */
	
    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testSomething() {
		def result =  listSpreadSheets()
		assert result != null
    }
	
	/**
	 * Authenticates into the google
	 * drive service
	 */
	def authenticate(def service){
//		GoogleCredentials credentials = User.get(springSecurityService.getPrincipal().id).googleCredentials
		try {
			service.setUserCredentials('federico.sutin@globallogic.com', '');
		} catch (InvalidCredentialsException e) {
			throw new IllegalAccessError(e.getMessage());
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
		URL url = new URL(this.SPREADSHEET_FEED_URL);
		SpreadsheetFeed feed = this.getSpreadsheetService().getFeed(url, SpreadsheetFeed.class);
		
//		SpreadsheetsService spreedSheetService = new SpreadsheetsService("MySpreadsheetIntegration-v1");
		this.getSpreadsheetService().RequestFactory
		
		
//		List<SpreadsheetEntry> spreadsheets = feed.getEntries();
//		def result = [:]
//		for (SpreadsheetEntry sheet : spreadsheets) {
//			result.put(sheet.getTitle().getPlainText(), sheet.getEditLink()?.getHref())
//		}
		return result
	}
}

