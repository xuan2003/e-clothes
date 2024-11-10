package tw.edu.pu.csim.s1102294.e_clothes.Match

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import tw.edu.pu.csim.s1102294.e_clothes.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import android.widget.TextView
import android.widget.ImageView
import com.bumptech.glide.Glide // Or your preferred image loading library

class Matching_details : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    // UI elements
    private lateinit var weatherTextView: TextView
    private lateinit var tagsTextView: TextView
    private lateinit var hatImageView: ImageView
    private lateinit var clothesImageView: ImageView
    private lateinit var pantsImageView: ImageView
    private lateinit var shoesImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matching_details)  // Assuming layout name is correct

        // Initialize Firestore
        db = FirebaseFirestore.getInstance()

        // Initialize UI elements
        weatherTextView = findViewById(R.id.textView22)  // Assuming the ID for weather text view
        tagsTextView = findViewById(R.id.textView20)  // Assuming the ID for tags text view
        hatImageView = findViewById(R.id.imghat)  // Assuming the ID for hat image view
        clothesImageView = findViewById(R.id.imgclothes)  // Assuming the ID for clothes image view
        pantsImageView = findViewById(R.id.imgpants)  // Assuming the ID for pants image view
        shoesImageView = findViewById(R.id.imgshoes)  // Assuming the ID for shoes image view

        // Get data from Firestore
        val matchId = intent.getStringExtra("matchId") // Assuming you passed the matchId in the intent
        if (matchId != null) {
            fetchMatchDetails(matchId)
        }
    }

    // Function to retrieve match data from Firestore
    private fun fetchMatchDetails(matchId: String) {
        db.collection("matches").document(matchId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    // Extract the data
                    val weather = document.getString("weather") ?: "Unknown"
                    val tags = document.getString("tags") ?: "No tags"
                    val hatImageUrl = document.getString("hatImage")
                    val clothesImageUrl = document.getString("clothesImage")
                    val pantsImageUrl = document.getString("pantsImage")
                    val shoesImageUrl = document.getString("shoesImage")

                    // Update the UI with the data
                    weatherTextView.text = "天氣：$weather"
                    tagsTextView.text = "標籤：$tags"

                    // Load images using Glide (or another image loading library)
                    Glide.with(this).load(hatImageUrl).into(hatImageView)
                    Glide.with(this).load(clothesImageUrl).into(clothesImageView)
                    Glide.with(this).load(pantsImageUrl).into(pantsImageView)
                    Glide.with(this).load(shoesImageUrl).into(shoesImageView)
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure
                Log.e("Firestore", "Error getting document", exception)
            }
    }
}
