package tablecloth.gen.viewmodel

import tablecloth.gen.model.domain.creatures.CharacterSheet
import tablecloth.gen.model.domain.creatures.PlayerCharacter

class PersonViewmodel {

    String name
    int intelligence, strength, dexterity
    int charId

    static PersonViewmodel fromDomain(PlayerCharacter playerCharacter) {
        CharacterSheet sheet = playerCharacter.sheet
        return new PersonViewmodel(
            charId: playerCharacter.id,
            name: playerCharacter.name,
            strength: sheet.strength,
            intelligence: sheet.intelligence,
            dexterity: sheet.dexterity,
        )
    }
}
