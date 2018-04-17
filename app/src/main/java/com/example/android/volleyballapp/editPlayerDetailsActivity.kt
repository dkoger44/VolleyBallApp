package com.example.android.volleyballapp

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*

class editPlayerDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_player_details)

        val context = this
        //query database for player information
        val previousIntent = getIntent()
        val playerID = previousIntent.getSerializableExtra("PlayerID") as String
        val playerDB = DBHandler(this)
        val player = playerDB.readSinglePlayerData(playerID)

        //set all editText fields to player data
        val playerFirstNameField = findViewById<EditText>(R.id.playerFNameEditText) as EditText
        val playerLastNameField = findViewById<EditText>(R.id.playerLNameEditText) as EditText
        val playerJerNum = findViewById<EditText>(R.id.playerJerseyNumber) as EditText

        val gradeLevels = arrayOf("7th", "8th","Freshman","Sophomore","Junior","Senior")
        val gradeLevelSpinner = findViewById<Spinner>(R.id.gradeLevelSpinner) as Spinner
        val gradeSpinnerAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, gradeLevels)
        gradeLevelSpinner.setAdapter(gradeSpinnerAdapter)

        var selectedGrade = 0

        gradeLevelSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedGrade = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        playerFirstNameField.setText(player.get(0).getFirstName())
        playerLastNameField.setText(player.get(0).getLastName())
        playerJerNum.setText(player.get(0).getNumber().toString())
        val playerGrade = player.get(0).getGradeLevel()
        when(playerGrade){
            "7th" -> gradeLevelSpinner.setSelection(0)
            "8th" -> gradeLevelSpinner.setSelection(1)
            "Freshman" -> gradeLevelSpinner.setSelection(2)
            "Sophomore" -> gradeLevelSpinner.setSelection(3)
            "Junior" -> gradeLevelSpinner.setSelection(4)
            "Senior" -> gradeLevelSpinner.setSelection(5)
        }
        //update PlayerTable where playerID = id
        val doneBtn = findViewById<Button>(R.id.finishEditButton) as Button
        doneBtn.setOnClickListener({
            if(playerFirstNameField.text.toString().length > 0 && playerLastNameField.text.toString().length > 0 && playerJerNum.text.toString().length > 0){
                val player = Player(playerID.toInt(),playerFirstNameField.text.toString(),playerLastNameField.text.toString(),playerJerNum.text.toString().toInt(),gradeLevels[selectedGrade])
                var db = DBHandler(context)
                db.updatePlayerData(player)

                finish()
            }
        })
        val background = findViewById<LinearLayout>(R.id.background) as LinearLayout
        background.setOnClickListener({
            val inputManager: InputMethodManager =getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)

        })
    }
}