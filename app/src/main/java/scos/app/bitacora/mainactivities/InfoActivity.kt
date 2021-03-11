package scos.app.bitacora.mainactivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import scos.app.bitacora.R

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener{
            finish()
        }
    }
}