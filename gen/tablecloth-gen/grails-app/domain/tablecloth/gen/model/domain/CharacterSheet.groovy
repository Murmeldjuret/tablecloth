package tablecloth.gen.model.domain

import tablecloth.gen.model.domain.users.User

class CharacterSheet {

    String name

    int strength

    int dexterity

    int intelligence

    static belongsTo = [
            user: User
    ]
}
