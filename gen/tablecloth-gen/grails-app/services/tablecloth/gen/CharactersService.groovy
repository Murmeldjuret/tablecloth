package tablecloth.gen

import tablecloth.gen.model.domain.creatures.CharacterSheet
import tablecloth.gen.model.domain.creatures.PlayerCharacter
import tablecloth.gen.model.domain.users.User
import tablecloth.gen.viewmodel.PersonViewmodel

class CharactersService {

    DatabaseService databaseService

    List<PersonViewmodel> getCharsOfUser(String username) {
        User user = User.findByUsername(username)
        if (!user) {
            return null
        }
        List<PlayerCharacter> chars = user.characters?.toArray() as List<PlayerCharacter> ?: []
        return chars?.collect {
            playerCharacterToViewmodel(it)
        }
    }

    boolean deleteCharacter(int id) {
        PlayerCharacter pc = PlayerCharacter.findById(id)
        if (!pc) {
            return false
        }
        databaseService.delete(pc)
        return true
    }

    private static PersonViewmodel playerCharacterToViewmodel(PlayerCharacter playerCharacter) {
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
