package edu.skku.cs.skkumarket

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PostActivity : AppCompatActivity() {
    private lateinit var imageContainer: LinearLayout
    private val PICK_IMAGE_REQUEST = 1
    private val maxImageCount = 5  //사진 최대 개수
    private val lastCheckedMap = mutableMapOf<Int, Int?>() // 각 RadioGroup의 마지막으로 선택된 버튼 ID를 저장하는 Map

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        // 사진 추가 버튼
        imageContainer = findViewById(R.id.image_container)
        val addImageButton: ImageView = findViewById(R.id.add_image_button)

        addImageButton.setOnClickListener {
            if (imageContainer.childCount - 1 < maxImageCount) { // 첫 번째 버튼 제외
                openGallery()
            } else {
                showWarningMessage()
            }
        }
        
        // 뒤로 가기 버튼
        val backIcon: ImageView = findViewById(R.id.back_icon)
        backIcon.setOnClickListener {
            finish()
        }

        // 작성 완료 버튼
        val submitButton: Button = findViewById(R.id.submit_button)

        // 작성 완료 버튼을 누를 시 조건 체크
        submitButton.setOnClickListener {
            if(isFormValid()){
                showSuccessDialog()

                /**
                 * 물픔을 DB에 저장하는 코드 작성
                 */
                
            }
            else{
                showWarningDialog()
            }
        }

        // RadioGroup 설정
        setupRadioGroups()
    }
    
    // 휴대폰 갤러리 접근
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            if (selectedImageUri != null) {
                addImageToScrollView(selectedImageUri)
            }
        }
    }
    
    // ScrollView에 이미지 추가하는 함수
    private fun addImageToScrollView(imageUri: Uri) {
        val imageView = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                240, // 너비 (dp 값을 픽셀로 변환 후 사용)
                240  // 높이
            ).apply {
                setMargins(8, 0, 8, 0)
            }
            setImageURI(imageUri)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        // 'Add Image' 버튼의 오른쪽에 추가
        val addButtonIndex = imageContainer.indexOfChild(findViewById(R.id.add_image_button))
        imageContainer.addView(imageView, addButtonIndex + 1)
    }

    // Warning Dialog (이미지를 5개 이상 등록하려고 할 때)
    fun showWarningMessage() {
        val builder = AlertDialog.Builder(this) // 'this'는 현재 Context
        builder.setTitle("알림")
        builder.setMessage("이미지는 최대 ${maxImageCount}개까지 추가할 수 있습니다.")

        builder.setPositiveButton("확인") { dialog, which ->
            // 확인 버튼 클릭 시 동작
            dialog.dismiss()
        }

        // 다이얼로그 표시
        builder.show()
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
            lastCheckedMap[groupId] = null // 초기화

            // 각 RadioButton에 개별 리스너 설정
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
            // 동일 버튼 클릭 시 선택 해제
            group.clearCheck()
            lastCheckedMap[groupId] = null
        } else {
            // 새로운 버튼 선택
            lastCheckedMap[groupId] = currentCheckedId
            button.isChecked = true // 선택 상태 유지
        }
    }

    //등록 조건 체크하는 함수
    private fun isFormValid(): Boolean {
        val imageContainer: LinearLayout = findViewById(R.id.image_container)
        val productTitle: TextView = findViewById(R.id.product_title)
        val productPrice: TextView = findViewById(R.id.product_price)
        val categorySpinner: Spinner  = findViewById(R.id.category_spinner)
        val transactionMethodGroup: RadioGroup = findViewById(R.id.transaction_method_group)
        val transactionLocationGroup: RadioGroup = findViewById(R.id.transaction_location_group)
        val productStateGroup: RadioGroup = findViewById(R.id.product_state_group)
        val submitButton: Button = findViewById(R.id.submit_button)

        // 이미지를 1개 이상 등록했는 지 확인
        if (imageContainer.childCount - 1 < 1) { // Subtracting the add image button
            return false
        }

        // title을 작성했는 지 확인
        if (productTitle.text.isNullOrBlank()) {
            return false
        }

        // 가격을 작성했는 지 확인
        if (productPrice.text.isNullOrBlank()) {
            return false
        }

        // 없음을 제외한 카테고리를 선택했는 지 확인
        if (categorySpinner.selectedItemPosition == 0) {
            return false
        }

        // 희망 거래 방식을 선택했는 지 확인
        if (transactionMethodGroup.checkedRadioButtonId == -1) {
            return false
        }

        // 상품 거래 장소를 선택했는 지 확인
        if (transactionLocationGroup.checkedRadioButtonId == -1) {
            return false
        }

        // 상품 상태를 선택했는 지 확인
        if (productStateGroup.checkedRadioButtonId == -1) {
            return false
        }

        return true
    }

    // Warning Dialog
    private fun showWarningDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Notice")
        builder.setMessage("모든 필수 항목을 작성해주세요.")
        builder.setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    // Success dialog
    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Success")
        builder.setMessage("상품 등록이 완료되었습니다.")
        builder.setPositiveButton("확인") { dialog, _ ->
            dialog.dismiss()
            finish()
        }
        builder.show()
    }
}