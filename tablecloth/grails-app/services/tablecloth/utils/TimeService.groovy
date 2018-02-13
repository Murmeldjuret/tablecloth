package tablecloth.utils

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional

@GrailsCompileStatic
@Transactional
class TimeService {

    Date getNow() {
        return new Date()
    }
}
