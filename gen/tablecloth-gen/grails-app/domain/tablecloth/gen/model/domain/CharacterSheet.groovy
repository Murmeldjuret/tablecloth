package tablecloth.gen.model.domain

class CharacterSheet {

    String name

    int strength

    int dexterity

    int intelligence

    static belongsTo = [
            user: User
    ]
}
