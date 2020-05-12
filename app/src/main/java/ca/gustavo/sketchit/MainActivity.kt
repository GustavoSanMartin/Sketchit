package ca.gustavo.sketchit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import ca.gustavo.sketchit.view.DrawingListener
import ca.gustavo.sketchit.view.DrawingView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_draw).setOnClickListener {
            val intent = Intent(this, DrawingActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btn_view).setOnClickListener {
            val intent = Intent(this, ViewingActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btn_reset).setOnClickListener {
            Firebase.firestore
                .collection("rounds")
                .document("etqjkzJY0MV16aLYau7b")
                .update("points", null)
        }
    }
}
