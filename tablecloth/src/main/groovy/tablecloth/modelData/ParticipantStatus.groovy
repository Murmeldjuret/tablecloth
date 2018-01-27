package tablecloth.modelData

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
enum ParticipantStatus {

    PENDING_INVITATION('Invited Player'),
    ACCEPTED_INVITATION('Player without Character'),
    CURRENT_PLAYER('Player'),
    OWNER('Campaign Owner'),
    FORMER_PLAYER('Former player')

    String desc

    ParticipantStatus(String desc) {
        this.desc = desc
    }
}
