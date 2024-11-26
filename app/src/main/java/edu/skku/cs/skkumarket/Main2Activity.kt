package edu.skku.cs.skkumarket

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        findViewById<Button>(R.id.signInBtn).setOnClickListener { showSignInDialog() }
        findViewById<Button>(R.id.logInBtn).setOnClickListener { showLogInDialog() }
    }

    private fun showSignInDialog() {
        val signInView = layoutInflater.inflate(R.layout.activity_sign_in_std, null)
        val signInDialog = createDialog(signInView)

        signInView.findViewById<Button>(R.id.stdVerifyBtn).setOnClickListener {
            signInDialog.dismiss()
            showSignInInfoDialog()
        }

        signInDialog.show()
    }

    private fun showSignInInfoDialog() {
        val signInInfoView = layoutInflater.inflate(R.layout.activity_sign_in_info, null)
        val infoDialog = createDialog(signInInfoView)

        signInInfoView.findViewById<Button>(R.id.infoVerifyBtn).setOnClickListener {
            infoDialog.dismiss()
            navigateToActivity(Main2Activity::class.java)
        }

        infoDialog.show()
    }

    private fun showLogInDialog() {
        val logInView = layoutInflater.inflate(R.layout.activity_log_in, null)
        val logInDialog = createDialog(logInView)

        logInView.findViewById<Button>(R.id.logInDoneBtn).setOnClickListener {
            logInDialog.dismiss()
            navigateToActivity(HomeActivity::class.java)
        }

        logInDialog.show()
    }

    private fun createDialog(view: android.view.View): AlertDialog {
        return AlertDialog.Builder(this).apply {
            setView(view)
        }.create().also { dialog ->
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    private fun navigateToActivity(destination: Class<*>) {
        val intent = Intent(this, destination)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
}
