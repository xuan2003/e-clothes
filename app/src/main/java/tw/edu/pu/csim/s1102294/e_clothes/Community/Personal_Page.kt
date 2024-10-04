package tw.edu.pu.csim.s1102294.e_clothes.Community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import tw.edu.pu.csim.s1102294.e_clothes.R

class Personal_Page : AppCompatActivity() {

    lateinit var nameTextView: TextView
    lateinit var birthdayTextView: TextView
    lateinit var signatureTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_page)

        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        nameTextView = findViewById(R.id.nameTextView)
        birthdayTextView = findViewById(R.id.birthdayTextView)
        signatureTextView = findViewById(R.id.signatureTextView)

        // 確保 userId 不為 null
        if (userId != null) {
            // 指定文件名稱為 "個人資料"
            val documentRef = db.collection(userId).document("個人資料")

            documentRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        // 獲取文件中的資料
                        val name = document.getString("使用者名稱")
                        val birthday = document.getString("生日")
                        val signature = document.getString("個性簽名")
                        val gender = document.getString("性別")

                        // 顯示資料
                        nameTextView.text = "@${name ?: "Name not found"}"
                        birthdayTextView.text = "生日：${birthday ?: "Birthday not found"} _ ${gender ?: "Gender not found"}"
                        signatureTextView.text = "個性簽名：${signature ?: "Signature not found"}"
                    } else {
                        Log.d("Firestore", "Document does not exist")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("Firestore", "Error getting document: ", exception)
                }
        } else {
            Log.w("Firestore", "User ID is null")
        }
    }
}
