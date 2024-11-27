package edu.skku.cs.skkumarket

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

class FilterActivity : AppCompatActivity() {
    private var lastCheckedMap = mutableMapOf<Int, Int?>() // 각 RadioGroup의 마지막 선택 상태 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        
        // 뒤로 가기 클릭
        val backIcon = findViewById<ImageView>(R.id.back_icon)
        
        backIcon.setOnClickListener {
            finish()
        }

        // 필터 적용 하기 버튼 클릭
        val applyFilterButton: Button = findViewById(R.id.apply_filter_button)

        applyFilterButton.setOnClickListener {
            validatePrices()
        }

        // RadioGroup 설정
        setupRadioGroups()
    }

    // 각 RadioGroup에 대한 처리
    private fun setupRadioGroups() {
        // RadioGroup ID 목록
        val radioGroupIds = listOf(
            R.id.transaction_method_group, // 희망 거래 방식
            R.id.transaction_location_group, // 희망 거래 장소
            R.id.product_state_group // 상품 상태
        )

        radioGroupIds.forEach { groupId ->
            val radioGroup = findViewById<RadioGroup>(groupId)
            lastCheckedMap[groupId] = null // 초기값 설정

            for (i in 0 until radioGroup.childCount) {
                val radioButton = radioGroup.getChildAt(i) as RadioButton
                radioButton.setOnClickListener {
                    handleRadioButtonClick(radioGroup, radioButton)
                }
            }
        }
    }

    // RadioGroup 클릭 이벤트 처리
    private fun handleRadioButtonClick(group: RadioGroup, button: RadioButton) {
        val groupId = group.id
        val currentCheckedId = button.id

        if (lastCheckedMap[groupId] == currentCheckedId) {
            // 동일 버튼 클릭 시 체크 해제
            group.clearCheck()
            lastCheckedMap[groupId] = null
        } else {
            // 새로운 버튼 선택
            lastCheckedMap[groupId] = currentCheckedId
            button.isChecked = true // 선택 유지
        }
    }

    // 최소 가격이 최대 가격보다 클 경우 handling
    private fun validatePrices() {
        val minPriceEditText: EditText = findViewById(R.id.min_price)
        val maxPriceEditText: EditText = findViewById(R.id.max_price)

        val minPrice = minPriceEditText.text.toString().toIntOrNull()
        val maxPrice = maxPriceEditText.text.toString().toIntOrNull()

        if (minPrice != null && maxPrice != null) {
            if (minPrice > maxPrice) {
                showWarningDialog()
            }
        }
        else {
            showSuccessDialog()
        }
    }

    // Warning Dialog
    private fun showWarningDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Notice")
        builder.setMessage("최소 가격은 최대 가격보다 작거나 같아야 합니다.")
        builder.setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    // Success Dialog
    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Success")
        builder.setMessage("필터가 성공적으로 적용되었습니다.")
        builder.setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()
            finish()
        }
        builder.show()
    }
}