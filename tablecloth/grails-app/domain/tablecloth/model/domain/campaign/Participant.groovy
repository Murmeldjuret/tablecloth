package tablecloth.model.domain.campaign

import tablecloth.model.domain.users.User
import tablecloth.modelData.CampaignPermission
import tablecloth.modelData.ParticipantStatus

class Participant {

    ParticipantStatus status = ParticipantStatus.PENDING_INVITATION

    static hasMany = [
        permissions: CampaignPermission
    ]

    static belongsTo = [
        user: User
    ]

    static constraints = {
    }

    static Participant fromMasterUser(User user) {
        return new Participant(
            user: user,
            status: ParticipantStatus.OWNER,
            permissions: CampaignPermission.masterPermissions(),
        )
    }

    boolean isOwner() {
        return status == ParticipantStatus.OWNER
    }
}
