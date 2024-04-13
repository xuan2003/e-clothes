package tw.edu.pu.csim.s1102294.e_clothes.Match

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import tw.edu.pu.csim.s1102294.e_clothes.R
import tw.edu.pu.csim.s1102294.e_clothes.home

class Match_home : AppCompatActivity() {
    lateinit var Home: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_home)

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

        val menu_catch = findViewById<ImageView>(R.id.menu_catch)
        menu_catch.setOnClickListener {
            val popup = PopupMenu(this, menu_catch)
            popup.menuInflater.inflate(R.menu.menu_pop, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.add -> {
                        Toast.makeText(this, "新增搭配", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(this, New_Match::class.java)
                        if (userUid != null) {
                            intent2.putExtra("userUid", userUid)
                        }
                        startActivity(intent2)
                        finish()
                        true
                    }
                    R.id.check -> {
                        Toast.makeText(this, "查看搭配", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.share -> {
                        Toast.makeText(this, "分享搭配", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

    }
}