package com.kit18.chatapp.data.model

class Conversation {
    var conversationId: String? = null
    var createAt: String? = null
    var type: String? = null
    var lastMessage: String? = "Cuộc trò chuyện mới"
    var lastMessageAt: Long? = null
    var participants: ArrayList<String?> = ArrayList()


    constructor() {}

    constructor(
        conversationId: String?,
        createAt: Long,
        type: String?,
        lastMessage: String?,
        lastMessageAt: Long?,
        participants: ArrayList<String?>
    ) {
        this.conversationId = conversationId
        this.createAt = createAt.toString()
        this.type = type
        this.lastMessage = lastMessage
        this.lastMessageAt = lastMessageAt
        this.participants = participants
    }
}