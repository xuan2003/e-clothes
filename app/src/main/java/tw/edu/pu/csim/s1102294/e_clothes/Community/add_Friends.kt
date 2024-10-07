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
        }

        override fun getItemCount(): Int {
            return friendList.size
        }

        class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
            private val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)

            fun bind(user: User) {
                usernameTextView.text = user.username
                emailTextView.text = user.email
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)

        textView19 = findViewById(R.id.textView19)

        Home = findViewById(R.id.Home)
        Home.setOnClickListener {
            val intent1 = Intent(this, home::class.java)
            startActivity(intent1)
            finish()
        }

        Match = findViewById(R.id.Match)
        Match.setOnClickListener {
            val intent1 = Intent(this, Match_home::class.java)
            startActivity(intent1)
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

        val menu_catch = findViewById<ImageView>(R.id.menu_friend)
        menu_catch.setOnClickListener {
            val popup = PopupMenu(this, menu_catch)
            popup.menuInflater.inflate(R.menu.menu_friends, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.add -> {
                        Toast.makeText(this, "新增好友", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(this, add_Friends::class.java)
                        startActivity(intent2)
                        finish()
                        true
                    }
                    R.id.edit -> {
                        Toast.makeText(this, "編輯好友", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(this, edit_Friends::class.java)
                        startActivity(intent2)
                        finish()
                        true
                    }
                    R.id.invit -> {
                        Toast.makeText(this, "查看好友邀請", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(this, Friend_Invite::class.java)
                        startActivity(intent2)
                        finish()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        searchFriendEditText = findViewById(R.id.searchFriendEditText)
        searchFriendButton = findViewById(R.id.searchFriendButton)
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView)

        searchResultsRecyclerView.layoutManager = LinearLayoutManager(this)
        friendsAdapter = FriendsAdapter(listOf())
        searchResultsRecyclerView.adapter = friendsAdapter

        auth = FirebaseAuth.getInstance()

        // 設置搜尋按鈕的點擊事件
        searchFriendButton.setOnClickListener {
            val email = searchFriendEditText.text.toString().trim()
            Log.d("SearchFriend", "Search button clicked, email: $email")
            if (email.isNotEmpty()) {
                searchFriendByEmail(email)
            } else {
                Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchFriendByEmail(email: String) {
        val db = FirebaseFirestore.getInstance()

        // Log the email being searched
        Log.d("SearchFriend", "Searching for user with email: $email")

        db.collection("users")
            .whereEqualTo("email", email)  // Searching based on the 'email' field
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val userList = querySnapshot.documents.mapNotNull { it.toObject(User::class.java) }
                    friendsAdapter = FriendsAdapter(userList)
                    searchResultsRecyclerView.adapter = friendsAdapter
                    Log.d("SearchFriend", "User(s) found: $userList")
                } else {
                    Log.d("SearchFriend", "No user found with the email")
                    Toast.makeText(this, "No user found with this email", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("SearchFriend", "Error searching for friend by email", e)
                Toast.makeText(this, "Failed to search", Toast.LENGTH_SHORT).show()
            }
    }

}
