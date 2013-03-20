grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()
		mavenRepo "https://its-subversion.usc.edu/maven2/public"
		mavenRepo "http://google-api-client-libraries.appspot.com/mavenrepo"
		mavenRepo "http://mavenrepo.google-api-java-client.googlecode.com/hg"
		
		

        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.20'
		compile 'org.codehaus.sonar:sonar-ws-client:2.11'
		compile 'commons-httpclient:commons-httpclient:3.1'
		compile 'org.apache.httpcomponents:httpclient:4.1.3'
		
		compile 'com.google.apis:google-api-services-drive:v2-rev60-1.13.2-beta'
		compile 'com.google.api-client:google-api-client:1.5.0-beta'
		compile 'com.google.http-client:google-http-client:1.13.1-beta'
		compile ('com.google.http-client:google-http-client-jackson2:1.11.0-beta'){excludes "xpp3", "slf4j-api", "slf4j-log4j12", "jul-to-slf4j", "jcl-over-slf4j"}
		
		compile 'com.google.gdata:gdata-client:1.12.0'
    }

    plugins {
        runtime ":hibernate:$grailsVersion"
        runtime ":jquery:1.7.2"
        runtime ":resources:1.1.6"

        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0"
        //runtime ":cached-resources:1.0"
        //runtime ":yui-minify-resources:0.1.4"

        build ":tomcat:$grailsVersion"

        runtime ":database-migration:1.1"

        compile ':cache:1.0.0'
    }
}
