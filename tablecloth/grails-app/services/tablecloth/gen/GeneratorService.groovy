package tablecloth.gen

import tablecloth.DatabaseService
import tablecloth.commands.DiceCommand
import tablecloth.commands.PersonNameCommand
import tablecloth.gen.country.CountryGeneratorService
import tablecloth.model.domain.creatures.CharacterSheet
import tablecloth.model.domain.creatures.PlayerCharacter
import tablecloth.model.domain.users.User
import tablecloth.rng.DiceService
import tablecloth.viewmodel.gen.CountryDataViewmodel

class GeneratorService {

    DatabaseService databaseService
    DiceService diceService

    CountryGeneratorService countryGeneratorService

    boolean generatePerson(PersonNameCommand cmd, User user) {
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

    CountryDataViewmodel generateCountry(Collection<String> tags) {
        countryGeneratorService.generate(tags)
    }
}
