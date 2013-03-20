package extractor

class QualityMeasureExtractorLauncherController {

	def sonarExtractorService
	
    def apacheMavenExtractor() {
		render sonarExtractorService.extractApacheMavenRCIMeasure()
	}
}
