package tablecloth.commands

import grails.util.Holders
import grails.validation.Validateable

class SendMessageCommand implements Validateable {

    static int MAX_MSG_LENGTH = Holders.config.tablecloth?.messaging?.messagelength ?: 1024i

    String username
    String body

    static constraints = {
        username nullable: false, blank: false
        body nullable: false, blank: false, maxSize: MAX_MSG_LENGTH
    }
}
