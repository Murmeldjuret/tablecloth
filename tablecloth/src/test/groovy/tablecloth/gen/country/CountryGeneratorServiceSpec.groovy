package tablecloth.gen.country

import grails.test.hibernate.HibernateSpec
import grails.testing.services.ServiceUnitTest
import tablecloth.gen.ConfigService
import tablecloth.rng.RandomService

class CountryGeneratorServiceSpec extends HibernateSpec implements ServiceUnitTest<CountryGeneratorService> {

    List<Class> getDomainClasses() { [] }

    void setup() {
        service.configService = Mock(ConfigService)
        service.randomService = Mock(RandomService)
        service.randomService.noise(*_) >> 1.0
        service.randomService.chooseBucket(*_) >> { args ->
            return (args[0] as Map<Object, String>).keySet().first()
        }
    }

    void "test dummy service"() {
        given:
            service.configService.getCfg() >> mockCfg()
        when:
            def response = service.generate([])
        then:
            response.totalPop == 50
            response.totalFood == 2500
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
                new GovStruct(
                    name: 'govname',
                    has: [
                        GovType.HEAD_OF_STATE,
                    ],
                )
            ],
        )
    }

    private static GovConfig mockGovConfig() {
        return new GovConfig(
            version: 1,
            data: [
                new GovData(
                    type: GovType.HEAD_OF_STATE,
                    name: 'supreme leader',
                    baseweight: 50,
                )
            ],
        )
    }

    private static ClassesConfig mockClassesConfig() {
        return new ClassesConfig(
            version: 1,
            data: [
                mockClsData()
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

    private static ClassesData mockClsData() {
        return new ClassesData(
            type: CountryType.CLASSES,
            basesize: 50,
            mandatory: true,
            urbanization: 1.0,
            popPerHousehold: 10,
            basechance: 1.0,
            baseweight: 1.0,
            militarization: 1.0,
            food: 5,
        )
    }
}
