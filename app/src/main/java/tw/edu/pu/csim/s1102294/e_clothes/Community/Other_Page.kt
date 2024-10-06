package tw.edu.pu.csim.s1102294.e_clothes.Community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import tw.edu.pu.csim.s1102294.e_clothes.Match.Match_home
import tw.edu.pu.csim.s1102294.e_clothes.R
import tw.edu.pu.csim.s1102294.e_clothes.clothes.choose_add
import tw.edu.pu.csim.s1102294.e_clothes.home

class Other_Page : AppCompatActivity() {

    lateinit var add_friend: Button
    lateinit var block_account: Button
    lateinit var Match: ImageView
    lateinit var Clothes: ImageView
    lateinit var Home: ImageView
    lateinit var Friend: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_page)

//        add_friend = findViewById(R.id.add_friend)
//        add_friend.setOnClickListener {
//            Toast.makeText(this, "已寄出好友邀請", Toast.LENGTH_SHORT).show()
//        }
//
//        block_account = findViewById(R.id.block_account)
//        block_account.setOnClickListener {
//            Toast.makeText(this, "已成功封鎖", Toast.LENGTH_SHORT).show()
//        }

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
            val intent3 = Intent(this, choose_add::class.java)
            startActivity(intent3)
            finish()
        }

        Friend = findViewById(R.id.Friend)
        Friend.setOnClickListener {
            val intent1 = Intent(this, Friends::class.java)
            startActivity(intent1)
            finish()
        }
    }
}