package tablecloth.gen.model.domain.creatures

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Creature {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id

    String name

    static constraints = {
    }
}
