package tablecloth.gen

import grails.compiler.GrailsCompileStatic
import tablecloth.DatabaseService
import tablecloth.exceptions.TableclothDomainReferenceException
import tablecloth.model.domain.creatures.PlayerCharacter
import tablecloth.model.domain.users.User
import tablecloth.viewmodel.PersonViewmodel

@GrailsCompileStatic
class CharactersService {

    DatabaseService databaseService

    List<PersonViewmodel> getCharsOfUser(String username) {
        User user = User.getUserByNameAssertExists(username)
        Set<PlayerCharacter> chars = user.characters ?: [].toSet() as Set<PlayerCharacter>
        return chars.collect {
            PersonViewmodel.fromDomain(it)
        }
    }

    void deleteCharacter(Long charId) {
        PlayerCharacter pc = PlayerCharacter.get(charId)
        if (!pc) {
            throw new TableclothDomainReferenceException("Failed to delete character with id: $charId" +
                ", no character with that id exists.")
        }
        databaseService.delete(pc)
    }
}
