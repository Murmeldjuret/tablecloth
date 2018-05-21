package tablecloth.rng

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification
import spock.lang.Unroll

class RandomServiceTest extends Specification implements ServiceUnitTest<RandomService> {

    void "test getInt"() {
        when:
            int i = service.getInt(100)
        then:
            i < 100
    }

    @Unroll
    void "test rollPercent outside bounds"() {
        when:
            boolean response = service.rollPercent(val)
        then:
            response == expected
        where:
            val  || expected
            -1.2 || false
            0.0  || false
            1.2  || true
            1.0  || true
    }
}
