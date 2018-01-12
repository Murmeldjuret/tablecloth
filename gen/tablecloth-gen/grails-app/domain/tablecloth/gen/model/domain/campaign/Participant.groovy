package tablecloth.gen.model.domain.campaign

import tablecloth.gen.model.domain.users.User
import tablecloth.gen.modelData.CampaignPermission
import tablecloth.gen.modelData.ParticipantStatus

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
}
