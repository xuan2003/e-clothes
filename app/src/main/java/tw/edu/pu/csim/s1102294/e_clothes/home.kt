package tw.edu.pu.csim.s1102294.e_clothes

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import tw.edu.pu.csim.s1102294.e_clothes.Community.Friends
import tw.edu.pu.csim.s1102294.e_clothes.Community.Personal_Page
import tw.edu.pu.csim.s1102294.e_clothes.Match.Match_home
import tw.edu.pu.csim.s1102294.e_clothes.clothes.New_clothes

class home : AppCompatActivity() {
    lateinit var Match: ImageView
    lateinit var Home: ImageView
    lateinit var Friend: ImageView
    lateinit var Clothes: ImageView
    lateinit var Personal_page: ImageView

    private val takePictureResult =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            val intent = Intent(this, New_clothes::class.java)
            intent.putExtra("capturedPhoto", bitmap) // 传递 Bitmap 对象而不是资源标识符
            startActivity(intent)
            finish()
        }

    private val permissionsResultCallback = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){
        when (it) {
            true -> {
//                txv.text = "您允許拍照權限，歡迎使用拍照功能！"
                takePictureResult.launch(null)
            }
            false -> {
//                txv.text = "抱歉，您尚未允許拍照權限，無法使用相機功能。"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
            checkPermission()
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

    }

    private fun checkPermission() {
        val permission = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            permissionsResultCallback.launch(android.Manifest.permission.CAMERA)
        } else {
//            txv.text = "您先前已允許拍照權限，歡迎使用拍照功能！"
            takePictureResult.launch(null)
        }
    }

}