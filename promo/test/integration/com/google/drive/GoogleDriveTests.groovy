package com.google.drive

import static org.junit.Assert.*

import org.junit.*


class GoogleDriveTests {
	
	def googleSpreadSheetService
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
	void testGoogleSpreadSheetExportReport(){
		googleSpreadSheetService.export(sonarExtractorService.getMetricFromAllResources('qi-quality-index'))
		assert 1 ==1
	}
	
	
	
}

