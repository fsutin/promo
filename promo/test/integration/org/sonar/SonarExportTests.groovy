package org.sonar

import static org.junit.Assert.*
import org.junit.*
import org.sonar.wsclient.services.Resource;

class SonarExportTests {

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
    void testGetAllResources() {
		def resources = sonarExtractorService.getSonarResources()
		assert resources != null
    }
	
	@Test
	void testGetAllMetrics(){
		def metrics = sonarExtractorService.getSonarMetrics()
		assert metrics != null
	}
	
	@Test
	void testGetMetricFromResource(){
		def result = sonarExtractorService.getMetricFromResource('org.apache.maven:maven','qi-quality-index')
		assert result != null
	}
	
	@Test
	void testGetMetricFromAllResources(){
		def result = sonarExtractorService.getMetricFromAllResources('qi-quality-index')
		result.each{
			println "Project:${it.key} - ${it.measures[0].metricKey} : ${it.measures[0].value}"
		}
		assert result != null
	}
}
