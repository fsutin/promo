package extractor

class QualityMeasureExportLauncherController {

	def sonarExtractorService
	
	def googleSpreadSheetService
	
	String QUALITY_INDEX_METRIC = 'qi-quality-index'
	
    def exportQualityIndexMetric() {
		try {
			googleSpreadSheetService.export(sonarExtractorService.getMetricFromAllResources(QUALITY_INDEX_METRIC))
			render 'Done!'
		} catch (Exception e) {
			render "Export Failed. Error: ${e.getCause()}"
		}
			
		
	}
}
