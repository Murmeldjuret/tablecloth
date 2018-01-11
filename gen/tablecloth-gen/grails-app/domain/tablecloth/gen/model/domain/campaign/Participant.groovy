package tablecloth.gen.model.domain.campaign

import tablecloth.gen.model.domain.users.User
import tablecloth.gen.modelData.CampaignPermission

class Participant {

    static hasMany = [
        permissions: CampaignPermission
    ]

    static belongsTo = [
        user: User
    ]

    static constraints = {
    }
}
