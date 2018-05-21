package tablecloth.exceptions

class TableclothDataException extends Exception {

    TableclothDataException(String message, Exception cause = null) {
        super(message, cause)
    }
}
