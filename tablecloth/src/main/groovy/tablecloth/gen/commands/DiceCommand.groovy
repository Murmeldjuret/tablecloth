package tablecloth.gen.commands

import grails.validation.Validateable

class DiceCommand implements Validateable {

    Integer sides
    Integer nrOfDice
    Integer nrOfRolls = 1

    boolean exploding = false
    Integer dropNLowest = 0
    Integer dropNHighest = 0

    static constraints = {
        sides nullable: false, range: 1..10000
        nrOfDice nullable: false, range: 1..100
        nrOfRolls nullable: true, range: 1..100, validator: { val ->
            return val > dropNHighest + dropNLowest
        }
        exploding nullable: true
        dropNLowest nullable: true, range: 0..99
        dropNHighest nullable: true, range: 0..99
    }

    static DiceCommand singleDie() {
        return new DiceCommand(
            sides: 6,
            nrOfDice: 1
        )
    }

}
