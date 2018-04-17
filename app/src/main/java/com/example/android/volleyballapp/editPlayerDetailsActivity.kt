package com.example.android.volleyballapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner

class editPlayerDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_player_details)

        //query database for player information
        val previousIntent = getIntent()
        val playerID = previousIntent.getSerializableExtra("PlayerID") as String
        val playerDB = DBHandler(this)
        var player = playerDB.readSinglePlayerData(playerID)

        //set all editText fields to player data
        val playerFirstNameField = findViewById<EditText>(R.id.playerFNameEditText) as EditText
        val playerLastNameField = findViewById<EditText>(R.id.playerLNameEditText) as EditText
        val playerJerNum = findViewById<EditText>(R.id.playerJerseyNumber) as EditText

        val gradeLevels = arrayOf("7th", "8th","Freshman","Sophomore","Junior","Senior")
        val gradeLevelSpinner = findViewById<Spinner>(R.id.gradeLevelSpinner) as Spinner
        val gradeSpinnerAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, gradeLevels)
        gradeLevelSpinner.setAdapter(gradeSpinnerAdapter)


        playerFirstNameField.setText(player.get(0).getFirstName())
        playerLastNameField.setText(player.get(0).getLastName())
        playerJerNum.setText(player.get(0).getNumber())
        gradeLevelSpinner


        //update PlayerTable where playerID = id
    }
}
