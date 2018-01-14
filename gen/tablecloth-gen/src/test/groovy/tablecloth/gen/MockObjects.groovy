package tablecloth.gen

import tablecloth.gen.model.domain.campaign.Campaign
import tablecloth.gen.model.domain.campaign.Participant
import tablecloth.gen.model.domain.creatures.CharacterSheet
import tablecloth.gen.model.domain.creatures.PlayerCharacter
import tablecloth.gen.model.domain.messages.Inbox
import tablecloth.gen.model.domain.messages.Message
import tablecloth.gen.model.domain.users.User
import tablecloth.gen.modelData.CampaignPermission
import tablecloth.gen.modelData.MessageType
import tablecloth.gen.modelData.ParticipantStatus

class MockObjects {

    static User genericUser() {
        User user = new User(
            username: 'user',
            password: 'supersecure101',
            characters: [].toSet(),
            campaigns: [].toSet()
        )
        Inbox inbox = new Inbox(
            owner: user,
            messages: [].toSet()
        )
        user.inbox = inbox
        user.save(flush: true, failOnError: true)
        return user
    }

    static User genericOtherUser() {
        User user = new User(
            username: 'user2',
            password: 'supersecure101',
            characters: [].toSet(),
            campaigns: [].toSet()
        )
        Inbox inbox = new Inbox(
            owner: user,
            messages: [].toSet()
        )
        user.inbox = inbox
        user.save(flush: true, failOnError: true)
        return user
    }

    static User genericAdmin() {
        User user = new User(
            username: 'admin',
            password: 'supersecure101',
            characters: [].toSet(),
            campaigns: [].toSet()
        )
        Inbox inbox = new Inbox(
            owner: user,
            messages: [].toSet()
        )
        user.inbox = inbox
        user.save(flush: true, failOnError: true)
        return user
    }

    static User campaignOwner() {
        User user = new User(
            username: 'username',
            password: 'supersecure101',
            characters: [].toSet(),
            campaigns: [].toSet()
        )
        Inbox inbox = new Inbox(
            owner: user,
            messages: [].toSet()
        )
        user.inbox = inbox
        user.save(flush: true, failOnError: true)
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
        owner.addToCampaigns(camp)
        owner.save()
        camp.save(flush: true, failOnError: true)
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
        user.addToCharacters(character)
        user.save(flush: true, failOnError: true)
        return character
    }

    static Inbox genericInbox() {
        User user = genericUser()
        User sender = genericOtherUser()
        def inbox = user.inbox
        Message msg = new Message(
            sent: new Date().minus(1),
            sender: sender,
            messageType: MessageType.PRIVATE_MESSAGE,
            body: 'testname',
            inbox: inbox,
            read: false,
        )
        Message msg2 = new Message(
            sent: new Date().minus(1),
            sender: sender,
            messageType: MessageType.PRIVATE_MESSAGE,
            body: 'othermessage',
            inbox: inbox,
            read: false,
        )
        Message msg3 = new Message(
            sent: new Date().minus(1),
            sender: sender,
            messageType: MessageType.INVITATION,
            body: 'invitation',
            inbox: inbox,
            read: false,
            invitationId: 66
        )
        inbox.addToMessages(msg)
        inbox.addToMessages(msg2)
        inbox.addToMessages(msg3)
        inbox.save(flush: true, failOnError: true)
        return inbox
    }
}
