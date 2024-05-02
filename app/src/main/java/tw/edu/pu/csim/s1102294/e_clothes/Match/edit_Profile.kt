package tw.edu.pu.csim.s1102294.e_clothes.Match

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import tw.edu.pu.csim.s1102294.e_clothes.Community.Liked_Post
import tw.edu.pu.csim.s1102294.e_clothes.R
import tw.edu.pu.csim.s1102294.e_clothes.Setting
import tw.edu.pu.csim.s1102294.e_clothes.change_password

class edit_Profile : AppCompatActivity() {

    lateinit var ok: Button
    lateinit var txv_change: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val userUid = intent.getStringExtra("userUid")

        ok = findViewById(R.id.ok)
        ok.setOnClickListener {
            Toast.makeText(this, "更改成功!", Toast.LENGTH_SHORT).show()
        }

        txv_change = findViewById(R.id.txv_change)
        txv_change.setOnClickListener {
            val intent2 = Intent(this, change_password::class.java)
            if (userUid != null) {
                intent2.putExtra("userUid", userUid)
            }
            startActivity(intent2)
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
                        val intent2 = Intent(this, edit_Profile::class.java)
                        if (userUid != null) {
                            intent2.putExtra("userUid", userUid)
                        }
                        startActivity(intent2)
                        finish()
                        true
                    }
                    R.id.check -> {
                        Toast.makeText(this, "編輯精選穿搭", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(this, edit_Chosen_Match::class.java)
                        if (userUid != null) {
                            intent2.putExtra("userUid", userUid)
                        }
                        startActivity(intent2)
                        finish()
                        true
                    }
                    R.id.share -> {
                        Toast.makeText(this, "分享搭配", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(this, share_Match::class.java)
                        if (userUid != null) {
                            intent2.putExtra("userUid", userUid)
                        }
                        startActivity(intent2)
                        finish()
                        true
                    }
                    R.id.like -> {
                        Toast.makeText(this, "喜歡的貼文", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(this, Liked_Post::class.java)
                        if (userUid != null) {
                            intent2.putExtra("userUid", userUid)
                        }
                        startActivity(intent2)
                        finish()
                        true
                    }
                    R.id.settings -> {
                        Toast.makeText(this, "設定", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(this, Setting::class.java)
                        if (userUid != null) {
                            intent2.putExtra("userUid", userUid)
                        }
                        startActivity(intent2)
                        finish()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }
}