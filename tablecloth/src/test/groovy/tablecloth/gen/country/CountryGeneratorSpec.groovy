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
            service.configService.getTags() >> mockTags()
            service.configService.getCls() >> mockClassesConfig()
            service.configService.getCountry() >> mockCountryConfig()
        when:
            def response = service.generate()
        then:
            response == []
    }

    private static TagConfig mockTags() {
        return new TagConfig(
            version: 1,
            size: [:],
            environment: [:],
            fortunes: [:],
            ages: [:],
            generic: [
                test0: 1.1,
                test1: 1.1,
                test2: 1.1,
            ],
        )
    }

    private static GovConfig mockGovConfig() {
        return new GovConfig(
            version: 1,
            data: [
                new GovData()
            ],
        )
    }

    private static ClassesConfig mockClassesConfig() {
        return new ClassesConfig(
            version: 1,
            data: [
                new ClassesData()
            ],
        )
    }

    private static CountryConfig mockCountryConfig() {
        return new CountryConfig(
            version: 1,
            baseFoodEfficiency: [:],
            baseSizeModifiers: [:],
        )
    }
}
