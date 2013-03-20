package org.sonar

import org.sonar.wsclient.Sonar
import org.sonar.wsclient.services.Resource
import org.sonar.wsclient.services.ResourceQuery;

class SonarExtractorService {

    def extractApacheMavenRCIMeasure() {
		Sonar sonar = Sonar.create("https://analysis.apache.org/")
		Resource apacheMaven = sonar.find(ResourceQuery.createForMetrics("org.apache.maven:maven", "coverage", "lines", "violations"));
		return apacheMaven.getMeasure("coverage");
    }
}
