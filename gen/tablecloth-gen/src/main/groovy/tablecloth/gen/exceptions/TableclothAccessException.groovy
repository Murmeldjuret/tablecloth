package tablecloth.gen.exceptions

class TableclothAccessException extends Exception {

    TableclothAccessException(String message, Exception cause = null) {
        super(message, cause)
    }
}
