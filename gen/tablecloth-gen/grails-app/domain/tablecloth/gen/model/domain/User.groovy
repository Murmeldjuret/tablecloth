package tablecloth.gen.model.domain

class User {

    String name

    String login

    String pwd

    static hasMany = [
            characterSheets: CharacterSheet
    ]

}
