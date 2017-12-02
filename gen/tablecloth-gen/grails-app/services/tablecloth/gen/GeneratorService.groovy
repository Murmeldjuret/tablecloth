package tablecloth.gen

import tablecloth.gen.commands.DiceCommand
import tablecloth.gen.commands.GenerateCharacterCommand
import tablecloth.gen.model.domain.creatures.CharacterSheet
import tablecloth.gen.model.domain.creatures.PlayerCharacter
import tablecloth.gen.model.domain.users.User
import tablecloth.gen.rng.DiceService

class GeneratorService {

    DatabaseService databaseService
    DiceService diceService

    boolean generatePerson(GenerateCharacterCommand cmd, User user) {
        assert User
        if (!cmd?.validate()) {
            return false
        }
        DiceCommand attributeDice = new DiceCommand(
            nrOfDice: 4,
            nrOfRolls: 1,
            sides: 6,
            dropNLowest: 1,
        )
        CharacterSheet sheet = new CharacterSheet(
            name: cmd.name,
            intelligence: diceService.rollDice(attributeDice).first(),
            strength: diceService.rollDice(attributeDice).first(),
            dexterity: diceService.rollDice(attributeDice).first()
        )
        PlayerCharacter pc = new PlayerCharacter(name: cmd.name, sheet: sheet, owner: user)
        databaseService.save(pc, user)
        return true
    }
}
