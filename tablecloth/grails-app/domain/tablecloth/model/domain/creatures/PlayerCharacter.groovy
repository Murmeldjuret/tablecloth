package tablecloth.model.domain.creatures

import tablecloth.model.domain.users.User

class PlayerCharacter extends Creature {

    static belongsTo = [
        owner: User
    ]

    static constraints = {
    }
}
