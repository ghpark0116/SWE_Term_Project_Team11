package edu.skku.cs.skkumarket

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProductDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // View 연결
        val titleTextView: TextView = findViewById(R.id.product_title)
        val priceTextView: TextView = findViewById(R.id.product_price)
        val dateTextView: TextView = findViewById(R.id.product_date)
        val imageView: ImageView = findViewById(R.id.product_image)
        val descriptionTextView: TextView = findViewById(R.id.product_description)

        // Intent로부터 데이터 가져오기
        val title = intent.getStringExtra("title")
        val price = intent.getStringExtra("price")
        val date = intent.getStringExtra("date")
        val imageResId = intent.getIntExtra("imageResId", 0)
        val description = intent.getStringExtra("description")

        // View에 데이터 설정
        titleTextView.text = title
        priceTextView.text = price
        dateTextView.text = date
        imageView.setImageResource(imageResId)
        descriptionTextView.text = description

        //back icon을 누르면 전 main page로 이동
        val prevIcon: ImageView = findViewById(R.id.back_icon)
        prevIcon.setOnClickListener {
            finish()
        }

        //채팅하기 버튼을 누르면 채팅방 생성 및 main page로 이동
        val chatButton: Button = findViewById(R.id.chat_button)
        chatButton.setOnClickListener {
            /**
             * 채팅방 생성 로직 추가
             */

            showSuccessDialog()
        }
    }

    // Success Dialog
    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Success")
        builder.setMessage("채팅방이 생성되었습니다!")
        builder.setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()
            finish()
        }
        builder.show()
    }
}
