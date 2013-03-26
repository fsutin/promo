package org.sonar

import org.sonar.wsclient.Sonar
import org.sonar.wsclient.services.MetricQuery;
import org.sonar.wsclient.services.Resource
import org.sonar.wsclient.services.ResourceQuery;
import org.springframework.context.support.StaticApplicationContext;

class SonarExtractorService {
	
	private static Sonar instance

    private Sonar getSonarInstance() {
		if(instance == null)
		 instance = Sonar.create("https://analysis.apache.org/")
		 //instance = Sonar.create(url, user, pass)
		return instance
    }
	
	def getSonarResources(){
		return getSonarInstance().findAll(new ResourceQuery())
	}
	
	def getSonarMetrics(){
		return getSonarInstance().findAll(new MetricQuery())
	}
	
	def getResource(Long resourceId){
		return getSonarInstance().find(new ResourceQuery().setResourceId(resourceId))
	}
	
	def getResource(String resourceKey){
		return getSonarInstance().find(new ResourceQuery().setResourceKeyOrId(resourceKey))
	}
	
	def getMetricFromResource(String resourceKey,String metric){
		return getSonarInstance().find(ResourceQuery.createForMetrics(resourceKey,metric))
	}
	
	def getMetricFromAllResources(String metric){
		return getSonarInstance().findAll(new ResourceQuery().setMetrics(metric))
	}
	
	
}
