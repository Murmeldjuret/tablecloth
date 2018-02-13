package tablecloth.exceptions

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class TableclothDomainReferenceException extends TableclothDomainException {

    TableclothDomainReferenceException(String message, Exception cause = null) {
        super(message, cause)
    }
}
