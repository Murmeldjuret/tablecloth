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

    boolean isOwner() {
        return status == ParticipantStatus.OWNER
    }
}
