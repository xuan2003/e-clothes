package tw.edu.pu.csim.s1102294.e_clothes.clothes

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import tw.edu.pu.csim.s1102294.e_clothes.Community.Friends
import tw.edu.pu.csim.s1102294.e_clothes.Match.Match_home
import tw.edu.pu.csim.s1102294.e_clothes.Match.New_Match
import tw.edu.pu.csim.s1102294.e_clothes.Match.edit_Profile
import tw.edu.pu.csim.s1102294.e_clothes.Match.share_Match
import tw.edu.pu.csim.s1102294.e_clothes.R
import tw.edu.pu.csim.s1102294.e_clothes.Setting
import tw.edu.pu.csim.s1102294.e_clothes.home

class ImageAdapter(private val context: Context, private val imageUrls: List<String>) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        downloadImage(imageUrl, holder.imageView)
    }

    override fun getItemCount() = imageUrls.size

    private fun downloadImage(imageUrl: String, imageView: ImageView) {
        val storageRef = FirebaseStorage.getInstance().reference.child(imageUrl)

        storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            imageView.setImageBitmap(bitmap)
        }.addOnFailureListener { exception ->
            Log.e("ImageAdapter", "Error loading image: ${exception.message}")
            // Optionally set a placeholder image or handle error state
        }
    }
}

class add_hat : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private val firestore = FirebaseFirestore.getInstance()
    lateinit var add: ImageView

    lateinit var Match: ImageView
    lateinit var Home: ImageView
    lateinit var Friend: ImageView
    lateinit var Clothes: ImageView
    lateinit var set: ImageView

    lateinit var menu: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hat)


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
            val intent = Intent(this, choose_add::class.java)
            startActivity(intent)
            finish()
        }

        Friend = findViewById(R.id.Friend)
        Friend.setOnClickListener {
            val intent1 = Intent(this, Friends::class.java)
            startActivity(intent1)
            finish()
        }

        set = findViewById(R.id.set)
        set.setOnClickListener {
            val intent1 = Intent(this, Setting::class.java)
            startActivity(intent1)
            finish()
        }

        add = findViewById(R.id.add)
        add.setOnClickListener {
            val intent1 = Intent(this, choose_add::class.java)
            startActivity(intent1)
            finish()
        }

        // 修改為使用 ContextThemeWrapper 來套用自訂樣式
        menu = findViewById(R.id.menu)
        menu.setOnClickListener {
            // 使用 ContextThemeWrapper 應用自訂樣式
            val popupMenu = PopupMenu(ContextThemeWrapper(this, R.style.CustomPopupMenu), menu)
            popupMenu.inflate(R.menu.wardrobe_menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.manage_clothes -> {
                        Toast.makeText(this, "管理所友衣服", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(this, Wardrobe::class.java)
                        startActivity(intent2)
                        finish()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()  // 顯示 PopupMenu
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        // Load images from Firestore
        loadImagesFromFirestore()
    }

//    private fun loadImagesFromFirestore() {
//        val imageUrls = mutableListOf<String>()
//
//        // 代替從 Firestore 獲取圖片的固定網址
//        imageUrls.add("/images/c5fbabe9-4aa2-4af6-9e26-d23a1c8ac3af")
//        imageUrls.add("/images/0eb6ebb5-bfcf-462f-af3e-cbc411ec60c1")
//        imageUrls.add("/images/d39ccd08-c7b9-4db1-9471-c63627ba1a80")
//        imageUrls.add("/images/41b09662-5355-494e-952b-7b628d9137e9")
//
//        // 創建並設置 Adapter
//        imageAdapter = ImageAdapter(this, imageUrls)
//        recyclerView.adapter = imageAdapter
//    }

    private fun loadImagesFromFirestore() {
        val imageUrls = mutableListOf<String>()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Retrieve image URLs from Firestore (assuming the collection name is "hats")
        if (userId != null) {
            firestore.collection(userId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        // 確保文件名稱包含"帽子"
                        if (document.id.contains("帽子")) {
                            val imageUrl = document.getString("圖片網址")
                            if (!imageUrl.isNullOrEmpty()) {
                                imageUrls.add(imageUrl)
                            } else {
                                Log.d(
                                    "Firestore",
                                    "Empty image URL found in document: ${document.id}"
                                )
                            }
                        }
                    }

                    // Create and set the adapter
                    imageAdapter = ImageAdapter(this, imageUrls)
                    recyclerView.adapter = imageAdapter
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error loading images: ${exception.message}")
                }
        }
    }
}
