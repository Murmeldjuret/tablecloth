package tablecloth.model.domain.campaign

import tablecloth.model.domain.creatures.PlayerCharacter
import tablecloth.modelData.CampaignPermission

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    String name

    String description

    static hasMany = [
        party: PlayerCharacter,
        participants: Participant,
        defaultPermissions: CampaignPermission
    ]

    static constraints = {
    }
}
