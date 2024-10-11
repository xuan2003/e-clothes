package tw.edu.pu.csim.s1102294.e_clothes.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import tw.edu.pu.csim.s1102294.e_clothes.Match.Match_home
import tw.edu.pu.csim.s1102294.e_clothes.R
import tw.edu.pu.csim.s1102294.e_clothes.Setting
import tw.edu.pu.csim.s1102294.e_clothes.home

class add_Friends : AppCompatActivity() {

    lateinit var Home: ImageView
    lateinit var Match: ImageView
    lateinit var Clothes: ImageView
    lateinit var Friend: ImageView
    lateinit var set: ImageView

    private lateinit var searchFriendEditText: EditText
    private lateinit var searchFriendButton: Button
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var friendsAdapter: FriendsAdapter
    private lateinit var auth: FirebaseAuth

    lateinit var textView19: TextView

    class FriendsAdapter(private val friendList: List<User>) : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
            return FriendViewHolder(view)
        }

        override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
            val user = friendList[position]
            holder.bind(user)

            // 設置點擊事件
            holder.itemView.setOnClickListener {
                // 點擊某個好友後跳轉到好友個人頁面
                val intent = Intent(holder.itemView.context, Other_Page::class.java).apply {
                    putExtra("username", user.username)
                    putExtra("email", user.email)
                    putExtra("profileImage", user.profileImage)
                }
                holder.itemView.context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return friendList.size
        }

        class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val friendImage: ImageView = itemView.findViewById(R.id.friend_image)
            private val friendName: TextView = itemView.findViewById(R.id.friend_name)
            private val friendEmail: TextView = itemView.findViewById(R.id.friend_email)

            fun bind(user: User) {
                friendName.text = user.username
                friendEmail.text = user.email

                // 使用 Picasso 來載入圖片
                loadImageFromUrl(friendImage, user.profileImage)
            }

            private fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
                if (imageUrl.isNotEmpty()) {
                    Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_launcher_foreground) // 載入中的預設圖片
                        .error(R.drawable.user) // 錯誤時的圖片
                        .into(imageView)
                } else {
                    imageView.setImageResource(R.drawable.ic_launcher_foreground) // 沒有圖片時顯示的預設圖
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)

        Home = findViewById(R.id.Home)
        Match = findViewById(R.id.Match)
        Friend = findViewById(R.id.Friend)
        set = findViewById(R.id.set)

        searchFriendEditText = findViewById(R.id.searchFriendEditText)
        searchFriendButton = findViewById(R.id.searchFriendButton)
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView)

        searchResultsRecyclerView.layoutManager = LinearLayoutManager(this)
        friendsAdapter = FriendsAdapter(listOf())
        searchResultsRecyclerView.adapter = friendsAdapter

        auth = FirebaseAuth.getInstance()

        // 底部導航按鈕
        Home.setOnClickListener {
            startActivity(Intent(this, home::class.java))
            finish()
        }

        Match.setOnClickListener {
            startActivity(Intent(this, Match_home::class.java))
            finish()
        }

        Friend.setOnClickListener {
            startActivity(Intent(this, Friends::class.java))
            finish()
        }

        set.setOnClickListener {
            startActivity(Intent(this, Setting::class.java))
            finish()
        }

        // 設置搜尋按鈕的點擊事件
        searchFriendButton.setOnClickListener {
            val email = searchFriendEditText.text.toString().trim()
            if (email.isNotEmpty()) {
                searchFriendByEmail(email)
            } else {
                Toast.makeText(this, "請輸入Email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchFriendByEmail(email: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .whereEqualTo("email", email)  // 基於 'email' 字段來搜尋
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val userList = querySnapshot.documents.mapNotNull { documentSnapshot ->
                        // 正確地抓取中文命名的字段
                        val username = documentSnapshot.getString("使用者名稱") ?: ""
                        val profileImage = documentSnapshot.getString("頭貼圖片") ?: ""
                        val email = documentSnapshot.getString("email") ?: ""

                        // 創建 User 物件
                        User(email, username, profileImage)  // 使用正確的 profileImage 名稱
                    }

                    friendsAdapter = FriendsAdapter(userList)
                    searchResultsRecyclerView.adapter = friendsAdapter
                } else {
                    Toast.makeText(this, "找不到該使用者", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "搜尋失敗", Toast.LENGTH_SHORT).show()
            }
    }

}