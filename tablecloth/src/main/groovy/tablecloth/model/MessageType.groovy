package tablecloth.model

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
enum MessageType {

    // Invitee has been sent but not responded
    INVITATION,

    // Invitee has accepted but not deleted
    ACCEPTED_INVITATION,

    // Invitee has rejected but not deleted
    REJECTED_INVITATION,

    // Invitation was deleted by sender
    DELETED_INVITATION,

    // User to user
    PRIVATE_MESSAGE,

    // Server admin to all other users
    SERVER_MESSAGE

    boolean isInvitation() {
        return this in [INVITATION, ACCEPTED_INVITATION, REJECTED_INVITATION, DELETED_INVITATION]
    }

    boolean isServerMessage() {
        return this == SERVER_MESSAGE
    }

}