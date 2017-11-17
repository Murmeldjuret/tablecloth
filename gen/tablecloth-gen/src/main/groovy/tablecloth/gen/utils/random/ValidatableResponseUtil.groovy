package tablecloth.gen.utils.random

import grails.validation.Validateable

class ValidatableResponseUtil {

    static String errorcount(Validateable v) {
        return "has $v.errors.errorCount errors"
    }
}
