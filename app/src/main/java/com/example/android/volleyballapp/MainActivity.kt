package com.example.android.volleyballapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //declaring buttons
        val rost_btn = findViewById<Button>(R.id.rosterButton) as Button
        val help_btn = findViewById<Button>(R.id.helpButton) as Button
        val newGame_btn = findViewById<Button>(R.id.newGameButton) as Button
        val setTest = findViewById<Button>(R.id.settingsButton) as Button

        //setting button onClick Listeners
        rost_btn.setOnClickListener({
            val intent = Intent(this, viewTeamActivity::class.java)
            startActivity(intent)
        })


        help_btn.setOnClickListener({
            val intent = Intent(this, HelpActivity::class.java )
            startActivity(intent)
        })

        newGame_btn.setOnClickListener({
            val intent = Intent(this,selectTeamActivity::class.java)
            startActivity(intent)
        })

        setTest.setOnClickListener({
            val intent = Intent(this,MainGameScreenActivity::class.java)
            startActivity(intent)
        })
    }
}
