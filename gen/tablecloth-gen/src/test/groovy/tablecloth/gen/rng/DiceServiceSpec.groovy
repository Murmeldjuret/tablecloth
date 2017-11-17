package tablecloth.gen.rng

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification
import spock.lang.Unroll
import tablecloth.gen.commands.DiceCommand

class DiceServiceSpec extends Specification implements ServiceUnitTest<DiceService> {

    def setup() {
        service.randomService = Mock(RandomService)
    }

    @Unroll('Testing #nrOfDice rolls and expecting a sum of #expectedSum')
    def "test simple dice rolls"() {
        given:
        DiceCommand cmd = new DiceCommand(
            sides: sides,
            nrOfDice: nrOfDice
        )

        when:
        List<Integer> sum = service.rollDice(cmd)

        then:
        expectedRolls * service.randomService.getInt(_) >>> rollTable
        sum.first() == expectedSum

        where:
        rollTable       | sides | nrOfDice || expectedSum | expectedRolls
        [4]             | 6     | 1        || 4           | 1
        [4, 5, 1]       | 6     | 3        || 10          | 3
        [9, 7, 2]       | 10    | 3        || 18          | 3
        [1, 1, 1, 1, 1] | 6     | 5        || 5           | 5
        [12, 12, 12]    | 12    | 3        || 36          | 3
    }

    def "test exploding dice explode"() {
        given:
        DiceCommand cmd = new DiceCommand(
            sides: 6,
            nrOfDice: 1,
            nrOfRolls: 1,
            exploding: true,
        )
        7 * service.randomService.getInt(_) >>> [6, 6, 6, 1, 1, 1, 1]

        when:
        List<Integer> sum = service.rollDice(cmd)

        then:
        sum.first() == 4
    }

    @Unroll('Testing #nrOfDice dice where drop #dropNLowest low and drop #dropNHighest high for a sum of #expectedSum')
    def "test drop of N dice rolls"() {
        given:
        DiceCommand cmd = new DiceCommand(
            sides: 6,
            dropNLowest: dropNLowest,
            dropNHighest: dropNHighest,
            nrOfDice: 4
        )

        when:
        List<Integer> sum = service.rollDice(cmd)

        then:
        service.randomService.getInt(_) >>> rollTable
        sum.first() == expectedSum

        where:
        rollTable    | dropNLowest | dropNHighest || expectedSum
        [6, 2, 3, 1] | 1           | 1            || 5
        [6, 2, 3, 1] | 2           | 0            || 9
        [6, 2, 3, 1] | 0           | 2            || 3
        [6, 2, 3, 1] | 2           | 1            || 3
        [6, 2, 3, 1] | 1           | 2            || 2
        [6, 2, 3, 1] | 3           | 0            || 6
        [6, 2, 3, 1] | 0           | 3            || 1
    }
}
