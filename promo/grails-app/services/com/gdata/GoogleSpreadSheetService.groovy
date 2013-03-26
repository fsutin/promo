package com.gdata

import java.text.SimpleDateFormat;

import org.sonar.wsclient.services.Measure

import com.google.gdata.client.GoogleService.InvalidCredentialsException
import com.sun.media.sound.InvalidFormatException
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


class GoogleSpreadSheetService {

    /** The URL for spread sheets*/
	String SPREADSHEET_FEED_URL = "https://spreadsheets.google.com/feeds/spreadsheets/private/full"
	
	/** The name of the spreadsheet service */
	String SPREADSHEET_SERVICE = "SpreadSheetServiceIntegrationV2"
	
	String SPREADHSEET_FILE_NAME= "projectReport"
	
	String DATE_HEADER = "fecha"
	
	String PROYECT_HEADER = "proyecto"
	
	String QI_VALUE_HEADER = "quality-index"
	
	private static googleSpreadsheetService
	
	
   def authenticate(def service){
	   try {
		   User usuario = User.findByMail('federico.sutin@globallogic.com')
		   service.setUserCredentials(usuario.mail, usuario.password);
	   } catch (InvalidCredentialsException e) {
		   throw new IllegalAccessException(e.getMessage())
	   }
   }
   
   
   def getSpreadsheetService(){
	   if(this.googleSpreadsheetService == null) {
		   this.googleSpreadsheetService = new SpreadsheetService(this.SPREADSHEET_SERVICE)
	   }
	   this.authenticate(this.googleSpreadsheetService)
	   return this.googleSpreadsheetService
   }
   
   
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
   
   def getSpreadsheet(def spreadSheetKey){
	   FeedURLFactory factory = FeedURLFactory.getDefault()
	   def service = this.getSpreadsheetService()
	   SpreadsheetQuery spreadsheetQuery = new SpreadsheetQuery(factory.getSpreadsheetsFeedUrl());
	   spreadsheetQuery.setTitleQuery(spreadSheetKey);
	   spreadsheetQuery.setTitleExact(true);
	   SpreadsheetFeed spreadsheetFeed = service.query(spreadsheetQuery, SpreadsheetFeed.class);
	   
	   List<SpreadsheetEntry> spreadsheets = spreadsheetFeed.getEntries();
	   
	   return spreadsheets.size() == 0 ? null : spreadsheets.get(0);
   }
   
   def getFeedUrl(def entry){
	   def service = this.getSpreadsheetService()
	   WorksheetFeed worksheetFeed = service.getFeed(entry.getWorksheetFeedUrl(), WorksheetFeed.class);
	   List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
	   WorksheetEntry worksheet = worksheets.get(0);
	   return worksheet.getListFeedUrl();
   }
   
   def export(def resources){
	   resources.each{
		   exportResourceMeasure(it)
	   }
   }
   
   def exportResourceMeasure(def resource){
	   SimpleDateFormat sdf = new SimpleDateFormat('dd/MM/yyyy')
	   def entry = this.getSpreadsheet(SPREADHSEET_FILE_NAME)
	   if(entry == null)
	   	throw new FileNotFoundException("No existe un documento con el nombre ${SPREADHSEET_FILE_NAME} en GoogleDrive.")
	   def feedUrl = this.getFeedUrl(entry)
	   def service = this.getSpreadsheetService()
	   ListEntry row = new ListEntry();
	   row.getCustomElements().setValueLocal(DATE_HEADER,sdf.format(new Date()));
	   row.getCustomElements().setValueLocal(PROYECT_HEADER,resource.getKey());
	   row.getCustomElements().setValueLocal(QI_VALUE_HEADER,resource.measures[0].value.toString());
	   try{
		   row = service.insert(feedUrl, row);
	   } catch (InvalidEntryException e){
		   throw new InvalidFormatException("Documento mal formado. El Spreadsheet debe tener las columnas ${DATE_HEADER}, ${PROYECT_HEADER} y ${QI_VALUE_HEADER}")
	   }
   }
   
}
