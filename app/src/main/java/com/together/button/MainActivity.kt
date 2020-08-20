package com.together.button

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        supportActionBar!!.hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val togetherModal: LinearLayout = findViewById(R.id.togethermodal)
        val numberDays: ImageView = findViewById(R.id.numberDays)
        numberDays.setOnClickListener {
            togetherModal.visibility = View.INVISIBLE
        }

        val sloganDown: TextView = findViewById(R.id.slogan_down)
        val text: String = getString(R.string.aux1)+"\n"+ getString(R.string.aux2)+"\n"+
                getString(R.string.aux3)
        sloganDown.text = text
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}