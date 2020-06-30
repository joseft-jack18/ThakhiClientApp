package com.example.thakhiclientapp.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.thakhiclientapp.R
import kotlinx.android.synthetic.main.animation_activity.*

class AnimationController : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.animation_activity)

        val intent = Intent(this, CalificacionController::class.java)
        val timer = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(4000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    startActivity(intent)
                    finish()
                }
            }
        }
        timer.start()
    }
}
