package tablecloth.exceptions

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class TableclothAccessException extends Exception {

    TableclothAccessException(String message, Exception cause = null) {
        super(message, cause)
    }
}
