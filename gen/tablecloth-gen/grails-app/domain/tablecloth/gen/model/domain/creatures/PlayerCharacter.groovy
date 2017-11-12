package tablecloth.gen.model.domain.creatures

import tablecloth.gen.model.domain.users.User

class PlayerCharacter extends StattedCreature {

    static belongsTo = [
            owner: User
    ]

    static constraints = {
    }
}
