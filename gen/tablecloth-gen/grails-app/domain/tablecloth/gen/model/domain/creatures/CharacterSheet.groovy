package tablecloth.gen.model.domain.creatures

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class CharacterSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    int strength

    int dexterity

    int intelligence

    static belongsTo = [
        owner: Creature
    ]

}
