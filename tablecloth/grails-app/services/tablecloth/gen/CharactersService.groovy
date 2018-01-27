package tablecloth.gen

import tablecloth.DatabaseService
import tablecloth.exceptions.TableclothDomainException
import tablecloth.model.domain.creatures.PlayerCharacter
import tablecloth.model.domain.users.User
import tablecloth.viewmodel.PersonViewmodel

class CharactersService {

    DatabaseService databaseService

    List<PersonViewmodel> getCharsOfUser(String username) {
        User user = User.findByUsername(username)
        if (!user) {
            return null
        }
        List<PlayerCharacter> chars = user.characters?.toArray() as List<PlayerCharacter> ?: []
        return chars.collect {
            PersonViewmodel.fromDomain(it)
        }
    }

    void deleteCharacter(Integer id) {
        PlayerCharacter pc = PlayerCharacter.get(id)
        if (!pc) {
            throw new TableclothDomainException("Failed to delete character with id: $id" +
                ", no character with that id exists.")
        }
        databaseService.delete(pc)
    }
}
