package com.kit18.chatapp.data.model

class UserModel {
    var uid: String? = null
    var name: String? = null
    var email: String? = null
    var profileImageUrl: String? = null


    constructor() {}
    constructor(
        uid: String?,
        name: String?,
        email: String?,
        profileImageUrl: String?,

        ) {
        this.uid = uid
        this.name = name
        this.email = email
        this.profileImageUrl = profileImageUrl

    }

    override fun toString(): String {
        return "UserModel(uid=$uid, name=$name, email=$email, profileImageUrl=$profileImageUrl)"
    }
}