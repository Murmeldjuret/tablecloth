package tablecloth.viewmodel

import grails.compiler.GrailsCompileStatic
import tablecloth.model.domain.creatures.CharacterSheet
import tablecloth.model.domain.creatures.PlayerCharacter

@GrailsCompileStatic
class PersonViewmodel {

    String name
    int intelligence, strength, dexterity
    long charId

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
