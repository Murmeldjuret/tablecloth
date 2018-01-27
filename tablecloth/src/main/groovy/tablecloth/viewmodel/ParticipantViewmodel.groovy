package tablecloth.viewmodel

import grails.compiler.GrailsCompileStatic
import tablecloth.model.domain.campaign.Participant
import tablecloth.modelData.CampaignPermission
import tablecloth.modelData.ParticipantStatus

@GrailsCompileStatic
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

    boolean isOwner() {
        return status == ParticipantStatus.OWNER
    }
}
