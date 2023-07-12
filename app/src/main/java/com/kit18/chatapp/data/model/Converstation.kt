package com.kit18.chatapp.data.model

class Converstation {
    var converstationId: String? = null
    var createAt: String? = null
    var type: String? = null
    var lastMessage: String? = "Cuộc trò chuyện mới"
    var lastMessageAt: Long? = null
    var participants: ArrayList<String> = ArrayList()

    constructor() {}

    constructor(
        converstationId: String?,
        createAt: String?,
        type: String?,
        lastMessage: String?,
        lastMessageAt: Long?,
        participants: ArrayList<String>
    ) {
        this.converstationId = converstationId
        this.createAt = createAt
        this.type = type
        this.lastMessage = lastMessage
        this.lastMessageAt = lastMessageAt
        this.participants = participants
    }
}