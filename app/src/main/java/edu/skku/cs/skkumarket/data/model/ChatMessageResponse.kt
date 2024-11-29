package edu.skku.cs.skkumarket.data.model

data class ChatMessageResponse(
    val id: Int,
    val chatId: Int,
    val senderId: Int,
    val message: String,
    val send_at: String
)
