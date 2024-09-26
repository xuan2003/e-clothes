package tw.edu.pu.csim.s1102294.e_clothes.clothes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import tw.edu.pu.csim.s1102294.e_clothes.Community.Friends
import tw.edu.pu.csim.s1102294.e_clothes.Match.Match_home
import tw.edu.pu.csim.s1102294.e_clothes.R
import tw.edu.pu.csim.s1102294.e_clothes.Setting
import tw.edu.pu.csim.s1102294.e_clothes.home

class add_pants : AppCompatActivity() {

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
        setContentView(R.layout.activity_add_pants)

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

    private fun loadImagesFromFirestore() {
        val imageUrls = mutableListOf<String>()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Retrieve image URLs from Firestore (assuming the collection name is "dresses")
        if (userId != null) {
            firestore.collection(userId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        // Ensure the document contains "dress" (裙子)
                        if (document.id.contains("褲子")) {
                            val imageUrl = document.getString("圖片網址")
                            if (!imageUrl.isNullOrEmpty()) {
                                imageUrls.add(imageUrl)
                            } else {
                                Log.d("Firestore", "Empty image URL found in document: ${document.id}")
                            }
                        }
                    }

                    // Create and set the adapter
                    imageAdapter = ImageAdapter(this, imageUrls)
                    recyclerView.adapter = imageAdapter
                    imageAdapter.notifyDataSetChanged()  // Notify the adapter after setting it
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error loading images: ${exception.message}")
                }
        }
    }
}