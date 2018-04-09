package com.example.android.volleyballapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class AddPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_player)

        val context = this
        //getting intent from previous activity
        val addPlayerToTeam = getIntent()

        //grabbing team object from intent
        val selTeam = addPlayerToTeam.getSerializableExtra("TeamObject") as Team

        //declareing text fields, spinners, and buttons
        val playerFName = findViewById<EditText>(R.id.playerFNameEditText) as EditText
        val playerLName = findViewById<EditText>(R.id.playerLNameEditText) as EditText
        //get player number
        //get player gradeLevel
        val addPlayer = findViewById<Button>(R.id.addPlayerButton) as Button

        //button onClick listeners
        addPlayer.setOnClickListener({
            //verify that the user has input in valid data
            if(playerFName.text.toString().length > 0 && playerLName.text.toString().length > 0){
                val player = Player(playerFName.text.toString(),playerLName.text.toString(),44,"Senior")
                var db = DBHandler(context)
                db.insertPlayerData(selTeam,player)
                finish()
            }
        })


    }
}
