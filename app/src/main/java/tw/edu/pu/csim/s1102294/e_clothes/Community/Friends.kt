package tw.edu.pu.csim.s1102294.e_clothes.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import tw.edu.pu.csim.s1102294.e_clothes.Match.Match_home
import tw.edu.pu.csim.s1102294.e_clothes.Match.New_Match
import tw.edu.pu.csim.s1102294.e_clothes.Match.share_Match
import tw.edu.pu.csim.s1102294.e_clothes.R
import tw.edu.pu.csim.s1102294.e_clothes.home

class Friends : AppCompatActivity() {

    lateinit var Home: ImageView
    lateinit var Match: ImageView
    lateinit var New_Clothes: ImageView
    lateinit var Friend: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        val userUid = intent.getStringExtra("userUid")
        Home = findViewById(R.id.Home)
        Home.setOnClickListener {
//            Home.text = ""
            val intent1 = Intent(this, home::class.java)
            if (userUid != null) {
                intent1.putExtra("userUid", userUid)
            }
            startActivity(intent1)
            finish()
        }

        Match = findViewById(R.id.Match)
        Match.setOnClickListener {
//            Home.text = ""
            val intent1 = Intent(this, Match_home::class.java)
            if (userUid != null) {
                intent1.putExtra("userUid", userUid)
            }
            startActivity(intent1)
            finish()
        }

        Friend = findViewById(R.id.Friend)
        Friend.setOnClickListener {
//            Home.text = ""
            val intent1 = Intent(this, Friends::class.java)
            if (userUid != null) {
                intent1.putExtra("userUid", userUid)
            }
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
                        if (userUid != null) {
                            intent2.putExtra("userUid", userUid)
                        }
                        startActivity(intent2)
                        finish()
                        true
                    }
                    R.id.check -> {
                        Toast.makeText(this, "編輯好友", Toast.LENGTH_SHORT).show()
//                        val intent2 = Intent(this, Match_home::class.java)
//                        if (userUid != null) {
//                            intent2.putExtra("userUid", userUid)
//                        }
//                        startActivity(intent2)
//                        finish()
                        true
                    }
                    R.id.share -> {
                        Toast.makeText(this, "查看好友邀請", Toast.LENGTH_SHORT).show()
//                        val intent2 = Intent(this, share_Match::class.java)
//                        if (userUid != null) {
//                            intent2.putExtra("userUid", userUid)
//                        }
//                        startActivity(intent2)
//                        finish()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

    }
}