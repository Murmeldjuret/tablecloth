package tablecloth.gen

import tablecloth.gen.model.domain.campaign.Campaign
import tablecloth.gen.model.domain.campaign.Participant
import tablecloth.gen.model.domain.creatures.CharacterSheet
import tablecloth.gen.model.domain.creatures.PlayerCharacter
import tablecloth.gen.model.domain.users.User
import tablecloth.gen.modelData.CampaignPermission
import tablecloth.gen.modelData.ParticipantStatus

class MockObjects {

    static User genericUser() {
        User user = new User(
            username: 'user',
            password: 'supersecure101',
            characters: [].toSet(),
            campaigns: [].toSet()
        )
        user.save(flush: true)
        return user
    }

    static User campaignOwner() {
        User user = new User(
            username: 'owner',
            password: 'supersecure101',
            characters: [].toSet(),
            campaigns: [].toSet()
        )
        user.save(flush: true)
        return user
    }

    static Campaign genericCampaign() {
        User owner = campaignOwner()
        Campaign camp = new Campaign(
            owner: owner,
            name: 'Middle Earth 2.0',
            description: 'Not stolen from Tolkien',
            defaultPermissions: CampaignPermission.defaultPermissions(),
            participants: [
                new Participant(
                    status: ParticipantStatus.OWNER,
                    user: owner,
                    permissions: CampaignPermission.masterPermissions()
                )
            ]
        )
        owner.campaigns.add(camp)
        owner.save()
        camp.save(flush: true)
        return camp
    }

    static PlayerCharacter genericPC() {
        User user = genericUser()
        PlayerCharacter character = new PlayerCharacter(
            name: 'myPC',
            owner: user,
        )
        CharacterSheet sheet = new CharacterSheet(
            owner: character,
            intelligence: 10,
            strength: 11,
            dexterity: 12,
        )
        character.sheet = sheet
        user.characters.add(character)
        user.save(flush: true)
        return character
    }
}
