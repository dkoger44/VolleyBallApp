package com.example.android.volleyballapp

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager


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
        val addPlayer = findViewById<Button>(R.id.addPlayerButton) as Button

        //button onClick listeners
        addPlayer.setOnClickListener({
            //verify that the user has input in valid data
            if(playerFName.text.toString().length > 0 && playerLName.text.toString().length > 0 && playerJerNum.text.toString().length > 0){
                val player = Player(playerFName.text.toString(),playerLName.text.toString(),playerJerNum.text.toString().toInt(),gradeLevels[selectedGrade])
                var db = DBHandler(context)
                db.insertPlayerData(selTeam,player)
                finish()
            }
            else{
                Toast.makeText(context,"Please enter Name and Player Number!",Toast.LENGTH_SHORT).show()
            }
        })
        val background = findViewById<LinearLayout>(R.id.background) as LinearLayout
        background.setOnClickListener({
            val inputManager:InputMethodManager =getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)

        })
    }
}
