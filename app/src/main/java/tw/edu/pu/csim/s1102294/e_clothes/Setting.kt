package tw.edu.pu.csim.s1102294.e_clothes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import tw.edu.pu.csim.s1102294.e_clothes.Community.Liked_Post
//import tw.edu.pu.csim.s1102294.e_clothes.Community.Liked_Post
import tw.edu.pu.csim.s1102294.e_clothes.Match.edit_Chosen_Match
import tw.edu.pu.csim.s1102294.e_clothes.Match.edit_Profile
import tw.edu.pu.csim.s1102294.e_clothes.Match.share_Match

class Setting : AppCompatActivity() {

    lateinit var btn_Delete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)


        btn_Delete = findViewById(R.id.btn_Delete)
        val userUid = intent.getStringExtra("userUid")
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
                        val intent3 = Intent(this, Liked_Post::class.java)
                        if (userUid != null) {
                            intent3.putExtra("userUid", userUid)
                        }
                        startActivity(intent3)
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
        btn_Delete.setOnClickListener {
            AlertDialog.Builder(this@Setting)
                .setTitle("title")
                .setMessage("確定刪掉本帳號嗎?")
                .setPositiveButton("否") { dialog, _ ->
                    // 可以在这里处理确定按钮的点击事件
                    dialog.dismiss()
                }
                .setNegativeButton("是") { dialog, _ ->
                    // 可以在这里处理取消按钮的点击事件
                    AlertDialog.Builder(this)
                        .setTitle("title")
                        .setMessage("刪除後將無法復原!!")
                        .setPositiveButton("否") { dialog2, _ ->

                            dialog2.dismiss()
                        }
                        .setNegativeButton("是") { dialog2, _ ->
                            dialog2.dismiss()
                            val intent = Intent(this, MainActivity::class.java) // 更換為您的目標Activity
                            startActivity(intent)
                        }
                        .show()
                    dialog.dismiss()
                }
                .show()
        }
    }
}