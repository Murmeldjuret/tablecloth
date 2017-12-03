package tablecloth.gen

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification
import tablecloth.gen.exceptions.TableclothDomainException
import tablecloth.gen.model.domain.creatures.CharacterSheet
import tablecloth.gen.model.domain.creatures.PlayerCharacter
import tablecloth.gen.model.domain.users.User
import tablecloth.gen.viewmodel.PersonViewmodel

class CharactersServiceTest extends Specification implements ServiceUnitTest<CharactersService>, DataTest {

    void setupSpec() {
        mockDomain User
        mockDomain PlayerCharacter
        mockDomain CharacterSheet
    }

    void setup() {
        service.databaseService = Mock(DatabaseService)
    }

    void "test getCharsOfUser"() {
        given:
        saveDummy()
        PersonViewmodel viewmodel = new PersonViewmodel(
            name: 'myPC',
            intelligence: 10,
            strength: 11,
            dexterity: 12,
        )

        when:
        List<PersonViewmodel> views = service.getCharsOfUser('user')

        then:
        views.size() == 1
        views.first().dexterity == viewmodel.dexterity
        views.first().name == viewmodel.name
        views.first().intelligence == viewmodel.intelligence
        views.first().strength == viewmodel.strength
        views.first().charId != 0
    }

    void "test getCharsOfUser user not found"() {
        when:
        List<PersonViewmodel> views = service.getCharsOfUser('invalid')

        then:
        views == null
    }

    void "test getCharsOfUser user has no characters"() {
        given:
        new User(
            username: 'user',
            password: 'supersecure101',
            characters: null
        ).save()

        when:
        List<PersonViewmodel> views = service.getCharsOfUser('user')

        then:
        views == []
    }

    void "test deleteCharacter"() {
        given:
        saveDummy()

        when:
        service.deleteCharacter(1)

        then:
        1 * service.databaseService.delete(_)
    }

    void "test deleteCharacter with invalid id"() {
        given:
        saveDummy()

        when:
        service.deleteCharacter(-5)

        then:
        User.count == 1
        PlayerCharacter.count == 1
        CharacterSheet.count == 1
        thrown TableclothDomainException
    }

    private static void saveDummy() {
        User user = new User(
            username: 'user',
            password: 'supersecure101',
            characters: [].toSet()
        )
        PlayerCharacter character = new PlayerCharacter(
            name: 'myPC',
            owner: user,
        )
        CharacterSheet sheet = new CharacterSheet(
            owner: character,
            intelligence: 10,
            strength: 11,
            dexterity: 12,
        )
        character.sheet = sheet
        user.characters.add(character)
        user.save()
    }
}
