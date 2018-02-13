package tablecloth.viewmodel

import grails.compiler.GrailsCompileStatic
import tablecloth.model.domain.campaign.Campaign
import tablecloth.modelData.CampaignPermission
import tablecloth.modelData.ParticipantStatus

@GrailsCompileStatic
class CampaignViewmodel {

    long id

    String name
    String desc

    List<PersonViewmodel> party
    List<ParticipantViewmodel> participants

    static CampaignViewmodel fromDomain(Campaign camp) {
        return new CampaignViewmodel(
            id: camp.id,
            name: camp.name,
            desc: camp.description,
            party: camp.party?.collect { PersonViewmodel.fromDomain(it) } ?: [] as List<PersonViewmodel>,
            participants: camp.participants.collect { ParticipantViewmodel.fromDomain(it) }.toList(),
        )
    }

    boolean hasInviteRights(String username) {
        participants.any {
            it.username == username && (
                it.permissions?.contains(CampaignPermission.EDIT_CAMPAIGN) ||
                    it.status == ParticipantStatus.OWNER
            )
        }
    }

    boolean isOwner(String username) {
        participants.any {
            it.username == username &&
                it.status == ParticipantStatus.OWNER
        }
    }
}
