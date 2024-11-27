package edu.skku.cs.skkumarket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        // RecyclerView 초기화
        val recyclerView: RecyclerView = findViewById(R.id.notification_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        /**
         * 임시 더미 데이터 (추후에 DB에서 받아 올 데이터(content, time))
         */
        val notifications = listOf(
            NotificationData(R.drawable.notification, "상품 등록 완료 알림", "10:30 AM"),
            NotificationData(R.drawable.notification, "채팅 알림: 새로운 채팅방 생성", "11:00 AM"),
            NotificationData(R.drawable.notification, "채팅 알림: 답변 알림", "1:45 PM"),
            NotificationData(R.drawable.notification, "상품 거래 완료 알림", "4:00 PM")
        )

        // RecyclerView Adapter
        val adapter = NotificationAdapter(notifications)
        recyclerView.adapter = adapter

        // 뒤로 가기 버튼 클릭
        val closeIcon: ImageView = findViewById(R.id.close_icon)
        closeIcon.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}

// Notification 데이터 클래스
data class NotificationData(
    val iconResId: Int,
    val content: String,
    val time: String
)

// RecyclerView Adapter 클래스
class NotificationAdapter(private val notifications: List<NotificationData>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.notificationIcon.setImageResource(notification.iconResId)
        holder.notificationContent.text = notification.content
        holder.notificationTime.text = notification.time
    }

    override fun getItemCount(): Int = notifications.size

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notificationIcon: ImageView = itemView.findViewById(R.id.notification_icon)
        val notificationContent: TextView = itemView.findViewById(R.id.notification_content)
        val notificationTime: TextView = itemView.findViewById(R.id.notification_time)
    }
}
