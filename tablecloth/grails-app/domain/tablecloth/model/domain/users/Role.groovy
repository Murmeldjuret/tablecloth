package tablecloth.model.domain.users

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@EqualsAndHashCode(includes = 'authority')
@ToString(includes = 'authority', includeNames = true, includePackage = false)
class Role implements Serializable {

    private static final long serialVersionUID = 1

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id

    String authority

    static constraints = {
        authority nullable: false, blank: false, unique: true
    }

    static mapping = {
        cache true
    }
}
