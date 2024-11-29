package edu.skku.cs.skkumarket

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.skku.cs.skkumarket.data.model.ChatMessageRequest
import edu.skku.cs.skkumarket.data.model.ChatMessageResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.*

interface ChatApi {
    @GET("chat/load_chat/{chat_id}")
    suspend fun getMessages(@Path("chat_id") chatId: Int): Response<List<ChatMessageResponse>>

    @POST("chat/send/{chat_id}")
    suspend fun sendMessage(@Path("chat_id") chatId: Int, @Body message: ChatMessageRequest): Response<Void>
}

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var chatApi: ChatApi
    private var chatRoomId: Int? = null
    private var senderId: Int = 0
    private lateinit var adapter: ChatMessageAdapter
    private val messagesList = mutableListOf<ChatMessageResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        val sharedPreferences = this.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        senderId = sharedPreferences.getInt("userId", 0)

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)

        chatRoomId = intent.getIntExtra("chatRoomId", 0)

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://backend-url.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        chatApi = retrofit.create(ChatApi::class.java)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ChatMessageAdapter(messagesList, 100)
        chatRecyclerView.adapter = adapter

        loadMessages()

        sendButton.setOnClickListener {
            val messageText = messageInput.text.toString()

            if (messageText.isNotEmpty()) {
                val newMessage = ChatMessageResponse(
                    id = messagesList.size + 1,
                    chatId = chatRoomId ?: 0,
                    senderId = 100,
                    message = messageText,
                    send_at = getCurrentTime()
                )

                messagesList.add(newMessage)
                adapter.notifyItemInserted(messagesList.size - 1)
                chatRecyclerView.scrollToPosition(messagesList.size - 1)

                messageInput.text.clear()
            }
        }


//        sendButton.setOnClickListener {
//            val messageText = messageInput.text.toString()
//
//            if (messageText.isNotEmpty()) {
//                val newMessage = ChatMessageRequest(sender_id = senderId, message = messageText)
//
//                lifecycleScope.launchWhenStarted {
//                    try {
//                        chatApi.sendMessage(chatRoomId!!, newMessage)
//
//                        val response = chatApi.getMessages(chatRoomId!!)
//
//                        if (response.isSuccessful && response.body() != null) {
//                            val responseMessages = response.body()!! // List<ChatMessageResponse>
//
//                            messagesList.addAll(responseMessages)
//
//                            adapter.notifyItemRangeInserted(messagesList.size - responseMessages.size, responseMessages.size)
//                            chatRecyclerView.scrollToPosition(messagesList.size - 1)
//                        } else {
//                            val errorMessage = response.errorBody()?.string() ?: "Unknown error"
//                            Toast.makeText(applicationContext, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        Toast.makeText(applicationContext, "Network error", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                messageInput.text.clear()
//            }
//        }

    }

    private fun generateMockMessages(): List<ChatMessageResponse> {
        return listOf(
            ChatMessageResponse(id = 1, chatId = 101, senderId = 2, message = "안녕하세요!", send_at = "10:00"),
            ChatMessageResponse(id = 2, chatId = 101, senderId = 100, message = "test", send_at = "10:01"),
            ChatMessageResponse(id = 3, chatId = 101, senderId = 2, message = "hi", send_at = "10:02"),
            ChatMessageResponse(id = 4, chatId = 101, senderId = 100, message = "hihi", send_at = "10:02")
        )
    }

    private fun loadMessages() {
        val mockMessages = generateMockMessages()

        messagesList.clear()
        messagesList.addAll(mockMessages)
        adapter.notifyDataSetChanged()
    }

    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return String.format("%02d:%02d", hour, minute)
    }



//    private fun loadMessages() {
//        lifecycleScope.launchWhenStarted {
//            try {
//                val response = chatApi.getMessages(chatRoomId!!)
//
//                if (response.isSuccessful && response.body() != null) {
//                    val messagesFromServer = response.body()!!
//
//                    val convertedMessages = messagesFromServer.map {
//                        ChatMessageResponse(
//                            id = it.id,
//                            chatId = it.chatId,
//                            senderId = it.senderId,
//                            message = it.message,
//                            send_at = it.send_at
//                        )
//                    }
//
//                    messagesList.clear()
//                    messagesList.addAll(convertedMessages)
//                    adapter.notifyDataSetChanged()
//                } else {
//                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
//                    Toast.makeText(applicationContext, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Toast.makeText(applicationContext, "Network error", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

}
