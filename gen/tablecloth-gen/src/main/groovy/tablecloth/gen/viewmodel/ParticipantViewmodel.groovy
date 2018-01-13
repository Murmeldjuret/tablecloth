package tablecloth.gen.viewmodel

import tablecloth.gen.model.domain.campaign.Participant
import tablecloth.gen.modelData.CampaignPermission
import tablecloth.gen.modelData.ParticipantStatus

class ParticipantViewmodel {

    String username

    ParticipantStatus status

    List<CampaignPermission> permissions

    static ParticipantViewmodel fromDomain(Participant participant) {
        return new ParticipantViewmodel(
            username: participant.user.username,
            status: participant.status,
            permissions: participant.permissions.toList()
        )
    }
}
