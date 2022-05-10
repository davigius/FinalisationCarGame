package com.example.oo_project_2022

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.TextView

class GameActivity() : AppCompatActivity() {

    lateinit var roadView: RoadView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        roadView = findViewById<RoadView>(R.id.vMain)

    }

    override fun onPause(){
        super.onPause()
        roadView.pause()
    }

    override fun onResume(){
        super.onResume()
        roadView.resume()
    }




}