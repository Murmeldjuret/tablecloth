package tablecloth.gen.model.domain.campaign

import tablecloth.gen.model.domain.creatures.PlayerCharacter
import tablecloth.gen.model.domain.users.User
import tablecloth.gen.modelData.CampaignPermission

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

    static belongsTo = [
        owner: User
    ]

    static constraints = {
    }
}
