package tablecloth.gen.country

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import tablecloth.exceptions.TableclothDataException
import tablecloth.gen.ConfigService

@Transactional
@GrailsCompileStatic
class CountryGeneratorService {

    ConfigService configService

    String generate() {
        GovConfig cfg = configService.government
        return "Success"
    }

}
