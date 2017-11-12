package tablecloth.gen

import tablecloth.gen.model.domain.creatures.CharacterSheet
import tablecloth.gen.model.domain.creatures.PlayerCharacter
import tablecloth.gen.model.domain.users.User

class GeneratorService {

    DatabaseService databaseService

    boolean generatePerson(String name, User user) {
        assert User
        if (!name || name == '') {
            return false
        }
        CharacterSheet sheet = new CharacterSheet(
                name: name,
                intelligence: 10, strength: 10, dexterity: 10
        )
        PlayerCharacter pc = new PlayerCharacter(name: name, sheet: sheet, owner: user)
        databaseService.save(pc, user)
    }
}
