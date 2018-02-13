package tablecloth.exceptions

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class TableclothDomainValidationException extends TableclothDomainException {

    TableclothDomainValidationException(String message, Exception cause = null) {
        super(message, cause)
    }
}
