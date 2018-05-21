package tablecloth.gen.country

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import tablecloth.gen.ConfigService

@Transactional
@GrailsCompileStatic
class CountryGeneratorService {

    ConfigService configService

    String generate() {
        CountryConfig cont = configService.country
        GovConfig gov = configService.government

        return "Success"
    }

}
