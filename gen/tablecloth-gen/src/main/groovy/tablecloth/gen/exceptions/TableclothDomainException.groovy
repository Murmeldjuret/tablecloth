package tablecloth.gen.exceptions

class TableclothDomainException extends Exception {

    TableclothDomainException(String message, Exception cause = null) {
        super(message, cause)
    }
}