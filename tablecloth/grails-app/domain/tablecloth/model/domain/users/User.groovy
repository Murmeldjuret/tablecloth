package tablecloth.model.domain.users

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import tablecloth.exceptions.TableclothDomainReferenceException
import tablecloth.model.domain.campaign.Campaign
import tablecloth.model.domain.creatures.PlayerCharacter
import tablecloth.model.domain.messages.Inbox

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@EqualsAndHashCode(includes = 'username')
@ToString(includes = 'username', includeNames = true, includePackage = false)
class User implements Serializable {

    private static final long serialVersionUID = 1

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    String username
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    Set<Role> getAuthorities() {
        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
    }

    static hasMany = [
        characters: PlayerCharacter,
        campaigns : Campaign
    ]

    static hasOne = [
        inbox: Inbox
    ]

    static constraints = {
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
    }

    static mapping = {
        password column: '`password`'
        characters column: 'chars'
        inbox cascade: "all-delete-orphan"
    }

    static User getUserByNameAssertExists(String username) {
        User user = findByUsername(username)
        if (!user) {
            throw new TableclothDomainReferenceException("User with username $username not found")
        }
        return user
    }
}
