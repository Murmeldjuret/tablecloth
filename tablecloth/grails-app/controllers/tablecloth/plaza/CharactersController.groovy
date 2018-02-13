package tablecloth.plaza

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.annotation.Secured
import tablecloth.commands.DeletePersonCommand
import tablecloth.commands.SingleStringCommand
import tablecloth.gen.CharactersService
import tablecloth.security.UserService
import tablecloth.utils.ValidatableResponseUtil

@GrailsCompileStatic
@Secured('ROLE_USER')
class CharactersController {

    UserService userService
    CharactersService charactersService

    def index() {
        def user = userService.getCurrentUserViewmodel()
        def characters = charactersService.getCharsOfUser(user.name)
        render view: 'characters', model: [user: user, chars: characters]
    }

    @Secured('ROLE_ADMIN')
    def forUser(String username) {
        def user = userService.getCurrentUserViewmodel(username)
        if (!user) {
            flash.message = "Could not find user by username $username"
            redirect controller: 'user'
            return
        }
        def characters = charactersService.getCharsOfUser(username)
        render view: 'characters', model: [user: user, chars: characters, readonly: true]
    }

    def addPerson(SingleStringCommand cmd) {
        if (!cmd.validate()) {
            flash.message = "Failed to add character, reason: ${ValidatableResponseUtil.errorcount(cmd)}"
            render view: 'characters'
            return
        }
        redirect controller: 'generator', action: 'person', params: [name: cmd.name]
    }

    def delete(DeletePersonCommand cmd) {
        if (!cmd.validate()) {
            flash.message = "Failed to add character, reason: ${ValidatableResponseUtil.errorcount(cmd)}"
            redirect action: 'index'
            return
        }
        charactersService.deleteCharacter(cmd.charId)
        flash.message = "Deleted character ${cmd.characterName ?: ''}!"
        if (cmd.returntoUserAfterDeletion) {
            redirect action: 'forUser', params: [username: cmd.returntoUserAfterDeletion]
            return
        }
        redirect action: 'index'
    }
}
