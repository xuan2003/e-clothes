package tw.edu.pu.csim.s1102294.e_clothes.Match

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import tw.edu.pu.csim.s1102294.e_clothes.Community.Friends
import tw.edu.pu.csim.s1102294.e_clothes.Community.Liked_Post
import tw.edu.pu.csim.s1102294.e_clothes.Community.Personal_Page
import tw.edu.pu.csim.s1102294.e_clothes.R
import tw.edu.pu.csim.s1102294.e_clothes.Setting
import tw.edu.pu.csim.s1102294.e_clothes.change_password
import tw.edu.pu.csim.s1102294.e_clothes.home

class edit_Profile : AppCompatActivity() {

    private lateinit var Match: ImageView
    private lateinit var Home: ImageView
    private lateinit var Friend: ImageView
    private lateinit var Clothes: ImageView
    private lateinit var Personal_page: ImageView
    private lateinit var ok: Button
    private lateinit var txv_change: TextView
    private lateinit var circularImageView: ShapeableImageView

    private var imageUri: Uri? = null // Variable to hold the selected image URI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val user_name: EditText = findViewById(R.id.user_name)
        val birthday: EditText = findViewById(R.id.birthday)
        val gender: EditText = findViewById(R.id.gender)
        val sign: EditText = findViewById(R.id.sign)
        val db = FirebaseFirestore.getInstance()

        circularImageView = findViewById(R.id.circularImageView)

        circularImageView.setOnClickListener {
            openGallery() // Open the gallery
        }

        Home = findViewById(R.id.Home)
        Home.setOnClickListener {
            startActivity(Intent(this, home::class.java))
            finish()
        }

        Match = findViewById(R.id.Match)
        Match.setOnClickListener {
            startActivity(Intent(this, Match_home::class.java))
            finish()
        }

        Clothes = findViewById(R.id.Clothes)
        Clothes.setOnClickListener {
            // Implement related logic here
        }

        Friend = findViewById(R.id.Friend)
        Friend.setOnClickListener {
            startActivity(Intent(this, Friends::class.java))
            finish()
        }

        Personal_page = findViewById(R.id.Personal_page)
        Personal_page.setOnClickListener {
            startActivity(Intent(this, Personal_Page::class.java))
            finish()
        }

        ok = findViewById(R.id.ok)
        ok.setOnClickListener {
            val id = FirebaseAuth.getInstance().currentUser?.uid

            val newName = user_name.text.toString()

            if (id != null) {
                val user = hashMapOf(
                    "使用者名稱" to newName,
                    "生日" to birthday.text.toString(),
                    "性別" to gender.text.toString(),
                    "個性簽名" to sign.text.toString()
                )

                // Use user ID and "個人資料" to identify the document ID
                val documentId = "個人資料"

                // Update Firestore with user data
                db.collection(id)
                    .document(documentId)
                    .set(user)
                    .addOnSuccessListener {
                        // After updating user data, upload the image if it's selected
                        if (imageUri != null) {
                            uploadImageToFirebase(imageUri!!)
                        } else {
                            Toast.makeText(this, "更新成功", Toast.LENGTH_LONG).show()
                        }
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
            startActivity(Intent(this, change_password::class.java))
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
                        startActivity(Intent(this, edit_Profile::class.java))
                        finish()
                        true
                    }
                    R.id.check -> {
                        Toast.makeText(this, "編輯精選穿搭", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, edit_Chosen_Match::class.java))
                        finish()
                        true
                    }
                    R.id.share -> {
                        Toast.makeText(this, "分享搭配", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, share_Match::class.java))
                        finish()
                        true
                    }
                    R.id.like -> {
                        Toast.makeText(this, "喜歡的貼文", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, Liked_Post::class.java))
                        finish()
                        true
                    }
                    R.id.settings -> {
                        Toast.makeText(this, "設定", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, Setting::class.java))
                        finish()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*" // Only select images
        }
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            imageUri = data?.data // Save the selected image URI
            circularImageView.setImageURI(imageUri) // Set the selected image
        }
    }

    // Upload the selected image to Firebase Storage
    private fun uploadImageToFirebase(imageUri: Uri) {
        val storageReference = FirebaseStorage.getInstance().reference.child("profile_images/${System.currentTimeMillis()}.jpg")

        storageReference.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Get the download URL
                storageReference.downloadUrl.addOnSuccessListener { fullUrl ->
                    // Extract the relative path
                    val relativePath = "/" + fullUrl.toString().substringAfter("/o/").substringBefore("?alt=media")
                        .replace("%2F", "/")

                    // Now `relativePath` stores the cleaned relative path with a leading /
                    saveImageUrlToFirestore(relativePath) // Pass the cleaned URL to Firestore saving method
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Save the image URL to Firestore
    private fun saveImageUrlToFirestore(imageUrl: String) {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val documentRef = db.collection(userId).document("個人資料")
            documentRef.update("頭貼圖片", imageUrl)
                .addOnSuccessListener {
                    Toast.makeText(this, "Image URL saved to Firestore", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to save URL: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
