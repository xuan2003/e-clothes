package tw.edu.pu.csim.s1102294.e_clothes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class register : AppCompatActivity() {
    lateinit var btn_register: Button
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var password_again: EditText
    lateinit var firebaseAuth: FirebaseAuth

    private val errorTranslations = mapOf(
        "The email address is badly formatted." to "Email格式不正確",
        "The given password is invalid. [ Password should be at least 6 characters ]" to "密碼無效（密碼應至少為6個字符）",
        "The email address is already in use by another account." to "該電子郵件地址已被使用",
        "The given password is too weak." to "密碼太弱"
        // 可根據需要添加其他錯誤消息的翻譯
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        password_again = findViewById(R.id.password_again)

        // 初始化 Firebase 身分驗證
        firebaseAuth = FirebaseAuth.getInstance()

        val userUid = intent.getStringExtra("userUid")
        btn_register = findViewById(R.id.btn_register)
        btn_register.setOnClickListener {

            val email = email.text.toString().trim()
            val password = password.text.toString().trim()
            val confirmPassword = password_again.text.toString().trim()

            if (validateInputs(email, password, confirmPassword)){
                registerUser(email, password)
            }
        }

        }

    private fun validateInputs(email: String, password: String, confirmPassword: String): Boolean {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "請輸入帳號和密碼", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "密碼和確認密碼不相符", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun registerUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val id = FirebaseAuth.getInstance().currentUser?.uid
                    val db = FirebaseFirestore.getInstance()

                    if (id != null) {
                        val user = hashMapOf(
                            "頭貼圖片" to "",
                            "使用者名稱" to "",
                            "生日" to "",
                            "性別" to "",
                            "個性簽名" to ""
                        )

                    } else {
                        Toast.makeText(this, "用户未登录", Toast.LENGTH_LONG).show()
                    }

                    Toast.makeText(this, "註冊成功！請登入帳號", Toast.LENGTH_SHORT).show()
                    clearUserAuthState() // 清除用戶驗證狀態
                    // 執行註冊成功後的相應操作，例如導航到登入畫面
                    navigateToLoginScreen()
                } else {
                    val errorMessage = task.exception?.message
                    val translatedError = errorTranslations[errorMessage]
                    val displayMessage = translatedError ?: "註冊失敗：$errorMessage"
                    Toast.makeText(this, displayMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun clearUserAuthState() {
        firebaseAuth.signOut()
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(this, login::class.java)
        startActivity(intent)
        finish()
    }

}