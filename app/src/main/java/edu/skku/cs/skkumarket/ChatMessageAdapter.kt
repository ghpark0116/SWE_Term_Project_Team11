package edu.skku.cs.skkumarket
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import edu.skku.cs.skkumarket.data.model.ChatMessageResponse

class ChatMessageAdapter(
    private val messages: List<ChatMessageResponse>,
    private val senderId: Int
) : RecyclerView.Adapter<ChatMessageAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]

        holder.messageTextView.text = message.message
        holder.timestampTextView.text = message.send_at

        val messageParams = holder.messageTextView.layoutParams as ConstraintLayout.LayoutParams
        val timestampParams = holder.timestampTextView.layoutParams as ConstraintLayout.LayoutParams

        if (message.senderId == senderId) {
            messageParams.startToStart = ConstraintLayout.LayoutParams.UNSET
            messageParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID

            timestampParams.startToEnd = ConstraintLayout.LayoutParams.UNSET
            timestampParams.endToStart = holder.messageTextView.id
        } else {
            messageParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            messageParams.endToEnd = ConstraintLayout.LayoutParams.UNSET

            timestampParams.startToEnd = holder.messageTextView.id
            timestampParams.endToStart = ConstraintLayout.LayoutParams.UNSET
        }

        holder.messageTextView.layoutParams = messageParams
        holder.timestampTextView.layoutParams = timestampParams
    }


    override fun getItemCount(): Int {
        return messages.size
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        val timestampTextView: TextView = itemView.findViewById(R.id.messageTimestamp)
    }
}
