package tw.edu.pu.csim.s1102294.e_clothes.clothes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import tw.edu.pu.csim.s1102294.e_clothes.R

class Wardrobe : AppCompatActivity() {
    lateinit var hat1: ImageView
    lateinit var btn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wardrobe)

        hat1 = findViewById(R.id.hat1)
        btn = findViewById(R.id.btn)
        btn.setOnClickListener {
            downloadImage()
        }
    }
    private fun downloadImage() {
        val storage = FirebaseStorage.getInstance()
        // 使用相對於 Firebase Storage 的路徑
        val storageRef = storage.reference.child("images/706da137-998a-4dbc-84d6-207be8a817dc")

        storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
            val bmp: Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            hat1.setImageBitmap(bmp)
        }.addOnFailureListener { exception ->
            Log.e("Firebase", "Error downloading image", exception)
        }
    }

}