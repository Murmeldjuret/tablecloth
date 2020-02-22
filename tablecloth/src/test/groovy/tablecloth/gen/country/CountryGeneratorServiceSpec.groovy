package tablecloth.gen.country

import grails.test.hibernate.HibernateSpec
import grails.testing.services.ServiceUnitTest
import tablecloth.gen.ConfigService

class CountryGeneratorServiceSpec extends HibernateSpec implements ServiceUnitTest<CountryGeneratorService> {

    List<Class> getDomainClasses() { [] }

    void setup() {
        service.configService = Mock(ConfigService)
    }

    void "test dummy service"() {
        given:
            service.configService.getCfg() >> mockCfg()
        when:
            def response = service.generate()
        then:
            response == []
    }

    private static GeneratorConfiguration mockCfg() {
        return new GeneratorConfiguration(
            govStructs: mockGovStructConfig(),
            govCats: mockGovConfig(),
            classes: mockClassesConfig(),
            tags: mockTags(),
            countryConfig: mockCountryConfig(),
        )
    }

    private static TagConfig mockTags() {
        return new TagConfig(
            version: 1,
            size: [:] as Map<String, Double>,
            environment: [:] as Map<String, Double>,
            fortunes: [:] as Map<String, Double>,
            ages: [:] as Map<String, Double>,
            generic: [
                test0: 1.1,
                test1: 1.1,
                test2: 1.1,
            ],
        )
    }

    private static GovStructConfig mockGovStructConfig() {
        return new GovStructConfig(
            version: 1,
            structs: [
                new GovStruct()
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
            baseFoodEfficiency: [:] as Map<String, Double>,
            baseSizeModifiers: [:] as Map<String, Double>,
        )
    }
}
