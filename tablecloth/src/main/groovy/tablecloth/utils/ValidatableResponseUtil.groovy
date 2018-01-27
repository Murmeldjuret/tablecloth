package tablecloth.utils

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

@GrailsCompileStatic
class ValidatableResponseUtil {

    // TODO temp dummy, fix in gsp in final implementation
    static String errorcount(Validateable v) {
        return "has $v.errors.errorCount errors"
    }
}
