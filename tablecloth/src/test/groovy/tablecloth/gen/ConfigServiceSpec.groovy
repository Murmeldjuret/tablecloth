package tablecloth.gen

import grails.test.hibernate.HibernateSpec
import grails.testing.services.ServiceUnitTest

class ConfigServiceSpec extends HibernateSpec implements ServiceUnitTest<ConfigService> {

    void "init loads all configs"() {
        when:
            service.init()
        then:
            service.government != null
    }
}
