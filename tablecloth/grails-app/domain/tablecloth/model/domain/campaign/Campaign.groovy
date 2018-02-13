package tablecloth.model.domain.campaign

import tablecloth.commands.AddCampaignCommand
import tablecloth.exceptions.TableclothDomainReferenceException
import tablecloth.model.domain.creatures.PlayerCharacter
import tablecloth.model.domain.users.User
import tablecloth.modelData.CampaignPermission
import tablecloth.modelData.ParticipantStatus

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    String name

    String description

    static hasMany = [
        party             : PlayerCharacter,
        participants      : Participant,
        defaultPermissions: CampaignPermission
    ]

    static constraints = {
    }

    static Campaign fromAddCommand(AddCampaignCommand cmd) {
        return new Campaign(
            name: cmd.name,
            description: cmd.description,
            defaultPermissions: CampaignPermission.defaultPermissions(),
            participants: [].toSet(),
            party: [].toSet(),
        )
    }

    void addUserAsParticipant(User user) {
        if (!defaultPermissions) {
            log.error("No default permissions assigned to campaign $name")
            defaultPermissions = CampaignPermission.defaultPermissions()
        }
        addToParticipants(
            new Participant(
                user: user,
                status: ParticipantStatus.PENDING_INVITATION,
                permissions: defaultPermissions.collect()
            )
        )
    }

    void removeParticipantByNameAsserted(String username) {
        Participant participant = participants.find { it.user.username == username }
        if (!participant) {
            throw new TableclothDomainReferenceException(
                "User with $username is not participating in campaign with id $id")
        }
        removeFromParticipants(participant)
    }

    Participant getParticipantByUsernameAsserted(String username) {
        Participant participant = participants.find { it.user.username == username }
        if (!participant) {
            throw new TableclothDomainReferenceException(
                "User with $username is not participating in campaign with id $id")
        }
        return participant
    }
}
