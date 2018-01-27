package tablecloth.exceptions

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class TableclothDomainException extends Exception {

    TableclothDomainException(String message, Exception cause = null) {
        super(message, cause)
    }
}
