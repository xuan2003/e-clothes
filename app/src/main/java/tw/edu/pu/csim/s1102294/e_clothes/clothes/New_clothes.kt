package tw.edu.pu.csim.s1102294.e_clothes.clothes

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.marginLeft
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tw.edu.pu.csim.s1102294.e_clothes.Community.Friends
import tw.edu.pu.csim.s1102294.e_clothes.Community.Personal_Page
import tw.edu.pu.csim.s1102294.e_clothes.Match.Match_home
import tw.edu.pu.csim.s1102294.e_clothes.Match.New_Match
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
    var imageUrl: String? = null // 新增用来存储图片的URL
    private val labelTexts = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_clothes)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            // 用户未登录，显示提示并跳转到登录页面
            Toast.makeText(this, "用戶未登入，請先登入", Toast.LENGTH_LONG).show()

            // 跳转到登录页面
            val loginIntent = Intent(this, login::class.java)
            startActivity(loginIntent)
            finish() // 关闭当前 Activity，避免用户返回
            return // 阻止后续代码执行
        } else {
            userId = currentUser.uid // 获取用户的 UID
        }

        val db = FirebaseFirestore.getInstance()

        clothes = findViewById(R.id.clothes)
        val imageBitmap = intent.getParcelableExtra<Bitmap>("capturedPhoto")
        imageBitmap?.let {
            clothes.setImageBitmap(it)
        } ?: run {
            Toast.makeText(this, "加載失敗", Toast.LENGTH_LONG).show()
        }

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

        addClothes = findViewById(R.id.addClothes)
        addClothes.setOnClickListener {
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

        finish = findViewById(R.id.finish)
        firebaseHelper = FirebaseHelper()

        finish.setOnClickListener {
            // 从 ImageView 获取 Bitmap
            val bitmap = (clothes.drawable as? BitmapDrawable)?.bitmap
            if (bitmap != null) {
                val imageUri = getImageUriFromBitmap(this, bitmap)
                if (imageUri != null) {
                    // 上传图片并获取URL
                    firebaseHelper.uploadImage(this, imageUri, onSuccess = { url ->
                        imageUrl = url // 保存图片URL
                        saveDataToFirestore(db) // 上传成功后保存到 Firestore
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
            val intent1 = Intent(this, New_Match::class.java)
            startActivity(intent1)
            finish()
        }
        Classification_name = findViewById(R.id.Classification_name)

        val Classification: ImageView = findViewById(R.id.Classification)
        Classification.setOnClickListener { view ->
            showPopupMenu(view)
        }
        label = findViewById(R.id.label)
        add_label = findViewById<ImageView>(R.id.add_label)
        val label_layout = findViewById<LinearLayout>(R.id.label_layout) // 确保有一个父布局来容纳新按钮
        add_label.setOnClickListener {
            if (label.text.isNotEmpty()) {
                val newtxv = TextView(this)
                newtxv.id = View.generateViewId()
                newtxv.setPadding(10, 10, 10, 10)
                newtxv.text = label.text
                newtxv.textSize = 20f
                newtxv.setTextColor(Color.parseColor("#000000"))

                label_layout.addView(newtxv)
                label.text.clear()  // 清空输入框

                // 将文本添加到列表中
                labelTexts.add(newtxv.text.toString())
            } else {
                Toast.makeText(this, "標籤不能為空", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 保存数据到 Firestore
    private fun saveDataToFirestore(db: FirebaseFirestore) {
        val id = FirebaseAuth.getInstance().currentUser?.uid
        if (id != null) {
            val user = hashMapOf(
                "服裝種類" to Classification_name.text.toString(),
                "圖片網址" to imageUrl, // 将图片 URL 保存到 Firestore
                "標籤" to labelTexts // 将文本列表保存到 Firestore
            )

            val documentId = Classification_name.text.toString()

            db.collection(id) // 假设集合名称是用户的 UID
                .document(documentId)
                .set(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "搭配完成", Toast.LENGTH_LONG).show()
                    val intent1 = Intent(this, home::class.java)
                    startActivity(intent1)
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "搭配失敗: ${e.message}", Toast.LENGTH_LONG).show()
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

    // 将 Bitmap 转换为 URI
    private fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri? {
        // 创建一个临时文件用于保存Bitmap
        val file = File(context.cacheDir, "${UUID.randomUUID()}.jpg")
        return try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream) // 将Bitmap压缩为JPEG格式
            outputStream.flush()
            outputStream.close()
            Uri.fromFile(file) // 返回文件的Uri
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
