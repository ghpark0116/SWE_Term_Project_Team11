package edu.skku.cs.skkumarket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import edu.skku.cs.skkumarket.data.model.ChatRoom
import edu.skku.cs.skkumarketimport.ChatListAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatListApi {
    @GET("chat/rooms/{user_id}")
    suspend fun getChatRooms(@Path("user_id") userId: String): List<ChatRoom>
}

class ChatListFragment : Fragment() {

    private lateinit var chatListView: ListView
    private lateinit var chatListApi: ChatListApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_list, container, false)
        chatListView = view.findViewById(R.id.chatListView)

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://backend-url.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        chatListApi = retrofit.create(ChatListApi::class.java)

        loadChatRooms()

        return view
    }

    private fun loadChatRooms() {
        lifecycleScope.launchWhenStarted {
            try {
                val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getString("userId", null) ?: throw Exception("User ID not found")

                val chatRooms = chatListApi.getChatRooms(userId)
                val adapter = ChatListAdapter(requireContext(), chatRooms)
                chatListView.adapter = adapter

                chatListView.setOnItemClickListener { _, _, position, _ ->
                    val selectedChatRoom = chatRooms[position]
                    val intent = Intent(requireContext(), ChatRoomActivity::class.java)
                    intent.putExtra("chatRoomId", selectedChatRoom.id)
                    startActivity(intent)
                }
            } catch (e: Exception) {
                e.printStackTrace()

                val mockChatRooms = listOf(
                    ChatRoom(1, 10, 100, 3, "Hi", "11-28 10:10"),
                    ChatRoom(2, 20, 100, 2, "test message", "11-28 10:05"),
                    ChatRoom(3, 30, 2, 100, "test message 2", "11-28 09:10")
                )
                val adapter = ChatListAdapter(requireContext(), mockChatRooms)
                chatListView.adapter = adapter

                chatListView.setOnItemClickListener { _, _, position, _ ->
                    val selectedChatRoom = mockChatRooms[position]
                    val intent = Intent(requireContext(), ChatRoomActivity::class.java)
                    intent.putExtra("chatRoomId", selectedChatRoom.id)
                    startActivity(intent)
                }
            }
        }
    }


    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): ChatListFragment {
            return ChatListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        }
    }
}
