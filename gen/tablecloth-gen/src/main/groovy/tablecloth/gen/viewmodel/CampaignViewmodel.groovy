package tablecloth.gen.viewmodel

import tablecloth.gen.model.domain.campaign.Campaign

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
            party: camp.party.collect { PersonViewmodel.fromDomain(it) }.toList(),
            participants: camp.participants.collect { ParticipantViewmodel.fromDomain(it) }.toList(),
        )
    }
}
