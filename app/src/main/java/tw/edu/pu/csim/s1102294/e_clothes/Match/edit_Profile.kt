package tw.edu.pu.csim.s1102294.e_clothes.Match

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import tw.edu.pu.csim.s1102294.e_clothes.Community.Friends
import tw.edu.pu.csim.s1102294.e_clothes.Community.Liked_Post
import tw.edu.pu.csim.s1102294.e_clothes.Community.Personal_Page
import tw.edu.pu.csim.s1102294.e_clothes.R
import tw.edu.pu.csim.s1102294.e_clothes.Setting
import tw.edu.pu.csim.s1102294.e_clothes.change_password
import tw.edu.pu.csim.s1102294.e_clothes.home

class edit_Profile : AppCompatActivity() {

    lateinit var Match: ImageView
    lateinit var Home: ImageView
    lateinit var Friend: ImageView
    lateinit var Clothes: ImageView
    lateinit var Personal_page: ImageView

    lateinit var ok: Button
    lateinit var txv_change: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val userUid = FirebaseAuth.getInstance().currentUser?.uid // 取得使用者 UID

        val user_name: EditText = findViewById(R.id.user_name)
        val birthday: EditText = findViewById(R.id.birthday)
        val gender: EditText = findViewById(R.id.gender)
        val sign: EditText = findViewById(R.id.sign)
        val db = FirebaseFirestore.getInstance()
        val id = FirebaseAuth.getInstance().currentUser?.uid // 取得使用者 UID

        Home = findViewById(R.id.Home)
        Home.setOnClickListener {
//            Home.text = ""
            val intent1 = Intent(this, home::class.java)
            startActivity(intent1)
            finish()
        }

        Match = findViewById(R.id.Match)
        Match.setOnClickListener {
//            Match.text = ""
            val intent2 = Intent(this, Match_home::class.java)
            startActivity(intent2)
            finish()
        }

        Clothes = findViewById(R.id.Clothes)
        Clothes.setOnClickListener {
//            textView9.text = "123"
//            checkPermission()
        }

        Friend = findViewById(R.id.Friend)
        Friend.setOnClickListener {
            val intent1 = Intent(this, Friends::class.java)
            startActivity(intent1)
            finish()
        }

        Personal_page = findViewById(R.id.Personal_page)
        Personal_page.setOnClickListener {
            val intent1 = Intent(this, Personal_Page::class.java)
            startActivity(intent1)
            finish()
        }

        ok = findViewById(R.id.ok)
        ok.setOnClickListener {
            // 确保用户已登录，再执行更新操作
            val id = FirebaseAuth.getInstance().currentUser?.uid
            val newName = user_name.text.toString()

            if (id != null) {
                val user = hashMapOf(
                    "使用者名稱" to newName,
                    "生日" to birthday.text.toString(),
                    "性別" to gender.text.toString(),
                    "個性簽名" to sign.text.toString()
                )

                // 使用用户名称和"個人資料"来确定文档ID
                val documentId = "$newName 個人資料"

                db.collection(id) // 假设集合名称是用户的 UID
                    .document(documentId) // 生成新的文档 ID
                    .set(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "更新成功", Toast.LENGTH_LONG).show()
                        // 更新 EditText 显示的内容（如果需要）
                        user_name.setText(newName)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "更新失败: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "用户未登录", Toast.LENGTH_LONG).show()
            }
        }


        txv_change = findViewById(R.id.txv_change)
        txv_change.setOnClickListener {
            val intent2 = Intent(this, change_password::class.java)
            startActivity(intent2)
            finish()
        }





        val menu = findViewById<ImageView>(R.id.menu)
        menu.setOnClickListener {
            val popup = PopupMenu(this, menu)
            popup.menuInflater.inflate(R.menu.menu_share, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.add -> {
                        Toast.makeText(this, "編輯個人資料", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(this, edit_Profile::class.java)
                        startActivity(intent2)
                        finish()
                        true
                    }
                    R.id.check -> {
                        Toast.makeText(this, "編輯精選穿搭", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(this, edit_Chosen_Match::class.java)
                        startActivity(intent2)
                        finish()
                        true
                    }
                    R.id.share -> {
                        Toast.makeText(this, "分享搭配", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(this, share_Match::class.java)
                        startActivity(intent2)
                        finish()
                        true
                    }
                    R.id.like -> {
                        Toast.makeText(this, "喜歡的貼文", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(this, Liked_Post::class.java)
                        startActivity(intent2)
                        finish()
                        true
                    }
                    R.id.settings -> {
                        Toast.makeText(this, "設定", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(this, Setting::class.java)
                        startActivity(intent2)
                        finish()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }
}