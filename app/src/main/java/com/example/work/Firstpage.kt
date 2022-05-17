package com.example.work

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Firstpage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.firstpage)
        val myThread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(2000)
                    startActivity(Intent(this@Firstpage, Login::class.java))
                    finish()
                } catch (e: Exception) {
                }
            }
        }

        myThread.start()
    }
}