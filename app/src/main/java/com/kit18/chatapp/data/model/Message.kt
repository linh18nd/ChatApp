package com.kit18.chatapp.data.model

class Message {
    var messageId: String? = null
    var createAt: String? = null
    var type: String? = null
    var content: String? = null
    var senderId: String? = null
    var isRead: Boolean? = null

    constructor() {}

    constructor(
        messageId: String?,
        createAt: Long,
        type: String?,
        content: String?,
        senderId: String?,

        isRead: Boolean?
    ) {
        this.messageId = messageId
        this.createAt = createAt.toString()
        this.type = type
        this.content = content
        this.senderId = senderId

        this.isRead = isRead
    }
}