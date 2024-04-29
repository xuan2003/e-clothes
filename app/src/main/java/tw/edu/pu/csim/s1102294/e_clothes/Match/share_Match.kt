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

class share_Match : AppCompatActivity() {

    lateinit var Home: TextView
    lateinit var Match: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_match)


        val userUid = intent.getStringExtra("userUid")
        val menu_share = findViewById<ImageView>(R.id.menu_share)
        menu_share.setOnClickListener {
            val popup = PopupMenu(this, menu_share)
            popup.menuInflater.inflate(R.menu.menu_share, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.add -> {
                        Toast.makeText(this, "編輯個人資料", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(this, edit_Profile::class.java)
                        if (userUid != null) {
                            intent2.putExtra("userUid", userUid)
                        }
                        startActivity(intent2)
                        finish()
                        true
                    }
//                    R.id.check -> {
//                        Toast.makeText(this, "編輯精選穿搭", Toast.LENGTH_SHORT).show()
//                        val intent2 = Intent(this, edit_Chosen_Match::class.java)
//                        if (userUid != null) {
//                            intent2.putExtra("userUid", userUid)
//                        }
//                        startActivity(intent2)
//                        finish()
//                        true
//                    }
//                    R.id.share -> {
//                        Toast.makeText(this, "分享搭配", Toast.LENGTH_SHORT).show()
//                        true
//                    }
//                    R.id.like -> {
//                        Toast.makeText(this, "喜歡的貼文", Toast.LENGTH_SHORT).show()
//                        val intent2 = Intent(this, Liked_Post::class.java)
//                        if (userUid != null) {
//                            intent2.putExtra("userUid", userUid)
//                        }
//                        startActivity(intent2)
//                        finish()
//                        true
//                    }
//                    R.id.settings -> {
//                        Toast.makeText(this, "設定", Toast.LENGTH_SHORT).show()
//                        val intent2 = Intent(this, Setting::class.java)
//                        if (userUid != null) {
//                            intent2.putExtra("userUid", userUid)
//                        }
//                        startActivity(intent2)
//                        finish()
//                        true
//                    }
                    else -> false
                }
            }
            popup.show()
        }
    }
}