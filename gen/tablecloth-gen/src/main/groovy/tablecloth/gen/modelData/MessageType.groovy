package tablecloth.gen.modelData

enum MessageType {

    INVITATION,
    ACCEPTED_INVITATION,
    REJECTED_INVITATION,
    PRIVATE_MESSAGE,
    SERVER_MESSAGE

    boolean isInvitation() {
        return this in [INVITATION, ACCEPTED_INVITATION, REJECTED_INVITATION]
    }

}