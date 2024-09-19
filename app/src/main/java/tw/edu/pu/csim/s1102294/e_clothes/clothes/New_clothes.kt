package tw.edu.pu.csim.s1102294.e_clothes.clothes

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import tw.edu.pu.csim.s1102294.e_clothes.Community.Friends
import tw.edu.pu.csim.s1102294.e_clothes.Community.Personal_Page
import tw.edu.pu.csim.s1102294.e_clothes.Match.Match_home
import tw.edu.pu.csim.s1102294.e_clothes.R
import tw.edu.pu.csim.s1102294.e_clothes.home
import tw.edu.pu.csim.s1102294.e_clothes.login
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class New_clothes : AppCompatActivity() {

    lateinit var Match: ImageView
    lateinit var Home: ImageView
    lateinit var Friend: ImageView
    lateinit var addClothes: ImageView
    lateinit var Personal_page: ImageView

    lateinit var finish: ImageView
    lateinit var previous: ImageView
    lateinit var clothes: ImageView
    lateinit var Classification_name: TextView
    lateinit var add_label: ImageView
    lateinit var label: EditText
    var userId: String? = null
    lateinit var firebaseHelper: FirebaseHelper
    var imageUrl: String? = null // 新增用來存儲圖片的URL
    private val labelTexts = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_clothes)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(this, "用戶未登入，請先登入", Toast.LENGTH_LONG).show()
            val loginIntent = Intent(this, login::class.java)
            startActivity(loginIntent)
            finish()
            return
        } else {
            userId = currentUser.uid
        }

        val db = FirebaseFirestore.getInstance()

        clothes = findViewById(R.id.clothes)

        // 获取传递过来的图片URI或Bitmap
        val imageUriString = intent.getStringExtra("selectedImageUri")
        val imageBitmap = intent.getParcelableExtra<Bitmap>("capturedPhoto")

        // 根据传递的内容设置ImageView
        when {
            imageUriString != null -> {
                val imageUri = Uri.parse(imageUriString)
                clothes.setImageURI(imageUri)
            }
            imageBitmap != null -> {
                clothes.setImageBitmap(imageBitmap)
            }
            else -> {
                Toast.makeText(this, "加載失敗", Toast.LENGTH_LONG).show()
            }
        }

        Home = findViewById(R.id.Home)
        Home.setOnClickListener {
            val intent1 = Intent(this, home::class.java)
            startActivity(intent1)
            finish()
        }

        Match = findViewById(R.id.Match)
        Match.setOnClickListener {
            val intent2 = Intent(this, Match_home::class.java)
            startActivity(intent2)
            finish()
        }

        addClothes = findViewById(R.id.addClothes)
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

        finish = findViewById(R.id.finish)
        firebaseHelper = FirebaseHelper()

        finish.setOnClickListener {
            val bitmap = (clothes.drawable as? BitmapDrawable)?.bitmap
            if (bitmap != null) {
                val imageUri = getImageUriFromBitmap(this, bitmap)
                if (imageUri != null) {
                    firebaseHelper.uploadImage(this, imageUri, onSuccess = { url ->
                        imageUrl = url
                        saveDataToFirestore(db)
                    }, onFailure = { e ->
                        Toast.makeText(this, "上傳失敗: ${e.message}", Toast.LENGTH_LONG).show()
                    })
                } else {
                    Toast.makeText(this, "無法取得URL", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "加載失敗", Toast.LENGTH_SHORT).show()
            }
        }

        previous = findViewById(R.id.previous)
        previous.setOnClickListener {
            val intent1 = Intent(this, home::class.java)
            startActivity(intent1)
            finish()
        }

        Classification_name = findViewById(R.id.Classification_name)

        val Classification: ImageView = findViewById(R.id.Classification)
        Classification.setOnClickListener { view ->
            showPopupMenu(view)
        }

        label = findViewById(R.id.label)
        add_label = findViewById(R.id.add_label)
        val label_layout = findViewById<LinearLayout>(R.id.label_layout)
        add_label.setOnClickListener {
            if (label.text.isNotEmpty()) {
                val newtxv = TextView(this)
                newtxv.id = View.generateViewId()
                newtxv.setPadding(10, 10, 10, 10)
                newtxv.text = label.text
                newtxv.textSize = 20f
                newtxv.setTextColor(Color.parseColor("#000000"))

                label_layout.addView(newtxv)
                label.text.clear()
                labelTexts.add(newtxv.text.toString())
            } else {
                Toast.makeText(this, "標籤不能為空", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveDataToFirestore(db: FirebaseFirestore) {
        val id = FirebaseAuth.getInstance().currentUser?.uid
        if (id != null) {
            db.collection(id)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val docCount = task.result?.size() ?: 0
                        val newDocumentName = Classification_name.text.toString() + "${docCount + 1}"

                        val user = hashMapOf(
                            "服裝種類" to Classification_name.text.toString(),
                            "圖片網址" to imageUrl,
                            "標籤" to labelTexts
                        )

                        db.collection(id)
                            .document(newDocumentName)
                            .set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this, "新增完成", Toast.LENGTH_LONG).show()
                                val intent1 = Intent(this, home::class.java)
                                startActivity(intent1)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "新增失敗: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    } else {
                        Toast.makeText(this, "獲取文件數量失敗", Toast.LENGTH_LONG).show()
                    }
                }
        } else {
            Toast.makeText(this, "用戶未登入", Toast.LENGTH_LONG).show()
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.classification_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.hat -> Classification_name.text = "帽子"
                R.id.hair_ornaments -> Classification_name.text = "髪飾"
                R.id.clothes -> Classification_name.text = "上衣"
                R.id.pants -> Classification_name.text = "褲子"
                R.id.shoes -> Classification_name.text = "鞋子"
                R.id.dress -> Classification_name.text = "洋裝"
                else -> false
            }
            true
        }
        popupMenu.show()
    }

    private fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri? {
        val file = File(context.cacheDir, "${UUID.randomUUID()}.jpg")
        return try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
