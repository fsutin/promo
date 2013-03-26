// Place your Spring DSL code here
beans = {
	
	sonarExtractorService(org.sonar.SonarExtractorService){
		sonarUrl = 'https://analysis.apache.org/'
		qualityIndexMetric= 'qi-quality-index'
	}
	
}
