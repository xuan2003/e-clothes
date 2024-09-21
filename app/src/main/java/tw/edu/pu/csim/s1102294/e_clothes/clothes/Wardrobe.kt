package tw.edu.pu.csim.s1102294.e_clothes.clothes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import tw.edu.pu.csim.s1102294.e_clothes.R

class Wardrobe : AppCompatActivity() {
    lateinit var imagesContainer: LinearLayout // 用于存放图片的容器
    private val hatImageViews = mutableListOf<ImageView>() // 存储 ImageView 的列表

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wardrobe)

        imagesContainer = findViewById(R.id.imagesContainer) // 初始化图片容器
        initializeImageViews() // 初始化 ImageView
        downloadImages() // 调用方法下载图片
    }

    private fun initializeImageViews() {
        // 创建 ImageView 并添加到容器中
        for (i in 1..4) {
            val imageView = ImageView(this)
            val layoutParams = LinearLayout.LayoutParams(
                (100 * resources.displayMetrics.density).toInt(),
                (100 * resources.displayMetrics.density).toInt()
            )
            imageView.layoutParams = layoutParams
            imagesContainer.addView(imageView) // 添加到容器中
            hatImageViews.add(imageView) // 将 ImageView 添加到列表中
        }
    }

    private fun downloadImages() {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            db.collection(userId)
                .whereEqualTo("服裝種類", "帽子")
                .orderBy(FieldPath.documentId()) // 根据文档 ID 排序
                .limit(4) // 限制获取前 4 条数据
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        var index = 0
                        for (document in documents) {
                            val imageUrl = document.getString("圖片網址") // 假设字段名为"圖片網址"
                            if (imageUrl != null && index < hatImageViews.size) {
                                addImageView(imageUrl, hatImageViews[index]) // 下载并设置图片
                                index++
                            }
                        }
                    } else {
                        Log.d("Firebase", "没有找到帽子文件")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Firebase", "获取文档时出错", exception)
                }
        } else {
            Log.e("Firebase", "用户未登录")
        }
    }

    private fun addImageView(imageUrl: String, imageView: ImageView) {
        downloadFromFirebaseStorage(imageUrl, imageView) // 下载并设置图片
    }

    private fun downloadFromFirebaseStorage(relativePath: String, imageView: ImageView) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child(relativePath)

        storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
            val bmp: Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            imageView.setImageBitmap(bmp) // 设置图片
        }.addOnFailureListener { exception ->
            Log.e("Firebase", "下载图片时出错", exception)
        }
    }
}
