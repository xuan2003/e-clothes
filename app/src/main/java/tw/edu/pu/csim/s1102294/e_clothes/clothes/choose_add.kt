package tw.edu.pu.csim.s1102294.e_clothes.clothes

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import tw.edu.pu.csim.s1102294.e_clothes.R

class choose_add : AppCompatActivity() {

    // Step 1: 新增变量用于处理相机结果
    private val takePictureResult =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                val intent = Intent(this, New_clothes::class.java)
                intent.putExtra("capturedPhoto", bitmap) // 传递 Bitmap 对象
                startActivity(intent)
                finish()
            } else {
                // Step 2: 处理取消操作
                Toast.makeText(this, "取消拍照", Toast.LENGTH_SHORT).show()
            }
        }

    private val permissionsResultCallback = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) {
        when (it) {
            true -> takePictureResult.launch(null)
            false -> Toast.makeText(this, "相機權限未授權", Toast.LENGTH_SHORT).show()
        }
    }

    private val PICK_IMAGE_REQUEST = 1
    private var img: ImageView? = null

    lateinit var camera: Button
    lateinit var photo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_add)

        camera = findViewById(R.id.camera)
        camera.setOnClickListener {
            checkPermission() // Step 3: 检查权限并启动相机
        }
        photo = findViewById(R.id.photo)
        photo.setOnClickListener {
            openPhotoAlbum()
        }
    }

    private fun checkPermission() {
        val permission = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            permissionsResultCallback.launch(android.Manifest.permission.CAMERA)
        } else {
            takePictureResult.launch(null)
        }
    }

    private fun openPhotoAlbum() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            val intent = Intent(this, New_clothes::class.java)
            intent.putExtra("selectedImageUri", selectedImageUri.toString()) // 修改为 String 类型
            startActivity(intent)
            finish()
        }
    }
}
