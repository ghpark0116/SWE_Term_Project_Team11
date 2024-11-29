package edu.skku.cs.skkumarketimport
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.BaseAdapter
import edu.skku.cs.skkumarket.R
import edu.skku.cs.skkumarket.data.model.ChatRoom

class ChatListAdapter(private val context: Context, private val chatRooms: List<ChatRoom>) : BaseAdapter() {
    override fun getCount() = chatRooms.size
    override fun getItem(position: Int) = chatRooms[position]
    override fun getItemId(position: Int) = position.toLong()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_chat_list, parent, false)
        val chatRoom = chatRooms[position]

        val chatRoomName = view.findViewById<TextView>(R.id.chatRoomName)
        val chatRoomTimeStamp = view.findViewById<TextView>(R.id.chatRoomTimeStamp)
        val lastMessage = view.findViewById<TextView>(R.id.lastMessage)

        chatRoomName.text = "Chat Room " + chatRoom.productId.toString()
        chatRoomTimeStamp.text = chatRoom.timestamp
        lastMessage.text = chatRoom.lastMessage

        return view
    }
}
