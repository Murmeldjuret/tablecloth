package tablecloth.gen.country

import grails.test.hibernate.HibernateSpec
import grails.testing.services.ServiceUnitTest
import tablecloth.gen.ConfigService

class CountryGeneratorSpec extends HibernateSpec implements ServiceUnitTest<CountryGeneratorService> {

    List<Class> getDomainClasses() { [] }

    void setup() {
        service.configService = Mock(ConfigService)
    }

    void "test dummy service"() {
        given:
            service.configService.getGovernment() >> mockGovConfig()
        when:
            def response = service.generate()
        then:
            response == "Success"
    }


    private static GovConfig mockGovConfig() {
        return new GovConfig(
            version: 1,
            tags: [
                "DUMMY": 1,
            ],
            data: [
                new GovData()
            ],
        )
    }
}
