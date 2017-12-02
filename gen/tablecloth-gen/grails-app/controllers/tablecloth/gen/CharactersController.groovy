package tablecloth.gen

import grails.plugin.springsecurity.annotation.Secured
import tablecloth.gen.commands.AddPersonCommand
import tablecloth.gen.commands.DeletePersonCommand
import tablecloth.gen.security.SecurityService
import tablecloth.gen.security.UserService
import tablecloth.gen.utils.ValidatableResponseUtil

@Secured('ROLE_USER')
class CharactersController {

    SecurityService securityService
    UserService userService
    CharactersService charactersService

    def index() {
        def user = userService.getUser(securityService.user)
        def characters = charactersService.getCharsOfUser(user.name)
        render view: 'characters', model: [user: user, chars: characters]
    }

    @Secured('ROLE_ADMIN')
    def forUser(String username) {
        def user = userService.getUser(username)
        if (!user) {
            flash.message = "Could not find user by username $username"
            redirect controller: 'user'
            return
        }
        def characters = charactersService.getCharsOfUser(username)
        render view: 'characters', model: [user: user, chars: characters, readonly: true]
    }

    def addPerson(AddPersonCommand cmd) {
        if (!cmd.validate()) {
            flash.message = "Failed to add character, reason: ${ValidatableResponseUtil.errorcount(cmd)}"
            render view: 'characters'
            return
        }
        redirect controller: 'generator', action: 'person', params: [name: cmd.characterName]
    }

    def delete(DeletePersonCommand cmd) {
        if (!cmd.validate()) {
            flash.message = "Failed to add character, reason: ${ValidatableResponseUtil.errorcount(cmd)}"
            redirect action: 'index'
            return
        }

        charactersService.deleteCharacter(cmd.charId)

        flash.message = "Deleted character ${cmd.characterName ?: ''}!"
        if (cmd.returntoUser) {
            redirect action: 'forUser', params: [username: cmd.returntoUser]
            return
        }
        redirect action: 'index'
    }
}
