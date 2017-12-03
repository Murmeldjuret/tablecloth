package tablecloth.gen

import tablecloth.gen.exceptions.TableclothDomainException
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
        return chars.collect {
            playerCharacterToViewmodel(it)
        }
    }

    void deleteCharacter(Integer id) {
        PlayerCharacter pc = PlayerCharacter.findById(id)
        if (!pc) {
            throw new TableclothDomainException("Failed to delete character with id: $id" +
                ", no character with that id exists.")
        }
        databaseService.delete(pc)
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
