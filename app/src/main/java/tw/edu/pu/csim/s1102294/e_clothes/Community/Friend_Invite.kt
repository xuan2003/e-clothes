package tw.edu.pu.csim.s1102294.e_clothes.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import tw.edu.pu.csim.s1102294.e_clothes.Match.Match_home
import tw.edu.pu.csim.s1102294.e_clothes.R
import tw.edu.pu.csim.s1102294.e_clothes.home

class Friend_Invite : AppCompatActivity() {

    lateinit var Home: ImageView
    lateinit var Match: ImageView
    lateinit var New_Clothes: ImageView
    lateinit var Friend: ImageView
    lateinit var Agree: ImageView
    lateinit var Reject: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_invite)

        Home = findViewById(R.id.Home)
        Home.setOnClickListener {
//            Home.text = ""
            val intent1 = Intent(this, home::class.java)
            startActivity(intent1)
            finish()
        }

        Match = findViewById(R.id.Match)
        Match.setOnClickListener {
//            Home.text = ""
            val intent1 = Intent(this, Match_home::class.java)
            startActivity(intent1)
            finish()
        }

        Friend = findViewById(R.id.Friend)
        Friend.setOnClickListener {
//            Home.text = ""
            val intent1 = Intent(this, Friends::class.java)
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

        Agree = findViewById(R.id.Agree)
        Agree.setOnClickListener {
            Toast.makeText(this, "以同意好友邀請\n\n太好了!", Toast.LENGTH_SHORT).show()
        }
        Reject = findViewById(R.id.Reject)
        Reject.setOnClickListener {
            Toast.makeText(this, "以拒絕好友邀請\n\n掰掰!", Toast.LENGTH_SHORT).show()
        }
    }
}