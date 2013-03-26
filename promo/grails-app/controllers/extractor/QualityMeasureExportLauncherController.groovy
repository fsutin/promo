package extractor

import com.gdata.User
import com.globallogic.Ctx

class QualityMeasureExportLauncherController {

	def sonarExtractorService
	
	def googleSpreadSheetService
	
	
	
    def exportQualityIndexMetric() {
		try {
		
		if(params.username == null || params.password == null)
			throw new IllegalArgumentException('No se recibieron username y password como parametros')
		String username = params.username
		String password = params.password
		
		Ctx.put(Thread.currentThread().getName(),new User(mail: username, password:password))
		
		
			def resourceMeasures = sonarExtractorService.getQualityIndexFromAllResources()
			googleSpreadSheetService.export(resourceMeasures)
			render 'Done!'
		} catch (Exception e) {
			render "Export Failed. Error: ${e.getMessage()}"
		}

			
		
	}
}
