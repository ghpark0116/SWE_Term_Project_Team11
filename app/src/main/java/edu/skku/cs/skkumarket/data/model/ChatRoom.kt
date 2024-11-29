package edu.skku.cs.skkumarket.data.model

data class ChatRoom(
    val id: Int,
    val productId: Int,
    val sellerId: Int,
    val buyerId: Int,
    val lastMessage: String,
    val timestamp: String
)
