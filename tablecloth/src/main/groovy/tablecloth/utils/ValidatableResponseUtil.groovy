package tablecloth.utils

import grails.validation.Validateable

class ValidatableResponseUtil {

    // TODO temp dummy, fix in gsp in final implementation
    static String errorcount(Validateable v) {
        return "has $v.errors.errorCount errors"
    }
}
