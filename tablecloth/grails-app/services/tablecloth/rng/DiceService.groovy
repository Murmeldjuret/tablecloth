package tablecloth.rng

import groovy.transform.CompileStatic
import tablecloth.commands.DiceCommand

@CompileStatic
class DiceService {

    RandomService randomService

    List<Integer> rollDice(DiceCommand cmd = DiceCommand.singleDie()) {
        List<Integer> retList = []
        (1..cmd.nrOfRolls).each {
            retList += singleDiceGroup(cmd)
        }
        return retList
    }

    private Integer singleDiceGroup(DiceCommand cmd) {
        Integer result = 0
        List<Integer> rolls = (1..cmd.nrOfDice).collect {
            return rollSingleDie(cmd)
        }.sort()
        if (cmd.dropNHighest > 0 || cmd.dropNLowest > 0) {
            rolls = rolls.subList(0 + cmd.dropNLowest, rolls.size() - cmd.dropNHighest)
        }
        rolls.each { result += it }
        return result
    }

    private Integer rollSingleDie(DiceCommand cmd) {
        if (!cmd.exploding) {
            return randomService.getInt(cmd.sides)
        } else {
            int roll = randomService.getInt(cmd.sides)
            if (roll == cmd.sides) {
                return rollSingleDie(cmd) + rollSingleDie(cmd)
            }
            return roll
        }
    }
}
