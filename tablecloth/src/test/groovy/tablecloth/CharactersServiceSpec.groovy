package tablecloth

import grails.test.hibernate.HibernateSpec
import grails.testing.services.ServiceUnitTest
import tablecloth.gen.CharactersService
import tablecloth.exceptions.TableclothDomainException
import tablecloth.model.domain.creatures.CharacterSheet
import tablecloth.model.domain.creatures.PlayerCharacter
import tablecloth.model.domain.users.User
import tablecloth.viewmodel.PersonViewmodel

class CharactersServiceSpec extends HibernateSpec implements ServiceUnitTest<CharactersService> {

    List<Class> getDomainClasses() { [User, PlayerCharacter, CharacterSheet] }

    void setup() {
        service.databaseService = Mock(DatabaseService)
        service.databaseService.save(_) >> { args -> args[0].each { it.save(failOnError: true, flush: true) } }
        service.databaseService.delete(_) >> { args -> args[0].each { it.delete(failOnError: true, flush: true) } }
    }

    void "test getCharsOfUser"() {
        given:
        MockObjects.genericPC()
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
        MockObjects.genericUser()

        when:
        List<PersonViewmodel> views = service.getCharsOfUser('user')

        then:
        views == []
    }

    void "test deleteCharacter"() {
        given:
        int id = MockObjects.genericPC().id

        when:
        service.deleteCharacter(id)

        then:
        1 * service.databaseService.delete(_)
    }

    void "test deleteCharacter with invalid id"() {
        given:
        MockObjects.genericPC()

        when:
        service.deleteCharacter(-5)

        then:
        User.count == 1
        PlayerCharacter.count == 1
        CharacterSheet.count == 1
        thrown TableclothDomainException
    }
}
