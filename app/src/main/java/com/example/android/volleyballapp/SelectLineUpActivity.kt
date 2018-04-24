package com.example.android.volleyballapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_add_team.*

class SelectLineUpActivity : AppCompatActivity() {
    var gameStartedCheck = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_line_up)

        //boolean to check if the game has been started. If it has you want to skip this screen
        //when coming back through the backStack
        gameStartedCheck = false
    }
    override fun onResume() {
        super.onResume()
        //will check to see if the game has already been started and if so then the lineup page will
        //be skipped so that the user can go back to the main new game page
       // if(gameStartedCheck){
        //    finish()
        //}
        //declating database handler
        val playerDB = DBHandler(this)

        //declaring editText field
        val opName = findViewById<EditText>(R.id.opponentEditText) as EditText

        //declaring buttons
        val homeBtn = findViewById<Button>(R.id.rosterHomeButton) as Button
        val doneBtn = findViewById<Button>(R.id.doneLineUpButton) as Button

        //setting button onClick listeners
        homeBtn.setOnClickListener({
            finish()
        })
        //get team object from previous activity
        val previousIntent = getIntent()
        val teamName = previousIntent.getStringExtra("TeamObject")
        val selectedTeam = Team(teamName)
        //get the player names from database
        val playersOnTeam = playerDB.readPlayerData(selectedTeam)
        //create array of player names for the spinners
        var lineUpOptions = arrayListOf<String>()
        lineUpOptions.add(" ")
        for (i in 0..playersOnTeam.size-1){
            lineUpOptions.add(playersOnTeam.get(i).getFirstName().toString()+" "+playersOnTeam.get(i).getLastName().toString())
        }
        //setting spinners
        val p1Spin = findViewById<Spinner>(R.id.player1SelectorSpinner) as Spinner
        val p2Spin = findViewById<Spinner>(R.id.player2SelectorSpinner) as Spinner
        val p3Spin = findViewById<Spinner>(R.id.player3SelectorSpinner) as Spinner
        val p4Spin = findViewById<Spinner>(R.id.player4SelectorSpinner) as Spinner
        val p5Spin = findViewById<Spinner>(R.id.player5SelectorSpinner) as Spinner
        val p6Spin = findViewById<Spinner>(R.id.player6SelectorSpinner) as Spinner
        val libSpin = findViewById<Spinner>(R.id.libaroSelectorSpinner) as Spinner

        //attaching adapters to spinners
        val playerSpinAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, lineUpOptions)
        p1Spin.setAdapter(playerSpinAdapter)
        p2Spin.setAdapter(playerSpinAdapter)
        p3Spin.setAdapter(playerSpinAdapter)
        p4Spin.setAdapter(playerSpinAdapter)
        p5Spin.setAdapter(playerSpinAdapter)
        p6Spin.setAdapter(playerSpinAdapter)
        libSpin.setAdapter(playerSpinAdapter)
        /* testing if muliple adapters are needed
        val p1SPinAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, lineUpOptions)
        p1Spin.setAdapter(p1SPinAdapter)
        val p2SPinAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, lineUpOptions)
        p2Spin.setAdapter(p2SPinAdapter)
        val p3SPinAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, lineUpOptions)
        p3Spin.setAdapter(p3SPinAdapter)
        val p4SPinAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, lineUpOptions)
        p4Spin.setAdapter(p4SPinAdapter)
        val p5SPinAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, lineUpOptions)
        p5Spin.setAdapter(p5SPinAdapter)
        val p6SPinAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, lineUpOptions)
        p6Spin.setAdapter(p6SPinAdapter)
        val libSPinAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, lineUpOptions)
        libSpin.setAdapter(libSPinAdapter)
        */

        //val lineUp = arrayOfNulls<String>(7)
        val lineUp = arrayOf(" "," "," "," "," "," "," ")
        p1Spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                lineUp[0] = lineUpOptions[position].toString()
                //if(position>0){
                //    p1Spin.setBackgroundColor(Color.WHITE)
                // }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        p2Spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                lineUp[1] = lineUpOptions[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        p3Spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                lineUp[2] = lineUpOptions[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        p4Spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                lineUp[3] = lineUpOptions[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        p5Spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                lineUp[4] = lineUpOptions[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        p6Spin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                lineUp[5] = lineUpOptions[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        libSpin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                lineUp[6] = lineUpOptions[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        doneBtn.setOnClickListener({
            //check to see if the lineup has been filled out
            var lineUpCheck = true
            val noDuplicatesLineUp = lineUp.distinct()

            if(noDuplicatesLineUp.size<7){
                lineUpCheck = false
            }
            else{
                for(i in 0..noDuplicatesLineUp.size-2){
                    if (noDuplicatesLineUp[i] == " ")
                        lineUpCheck = false
                }
            }
            if(lineUpCheck) {
                //create intent
                Toast.makeText(this,"Creating Intent",Toast.LENGTH_LONG).show()
                //need to send players, team object, and opponent team name through intent.
            }
            else{
                Toast.makeText(this,"Please enter different players for positions",Toast.LENGTH_LONG).show()
            }
        })
        val background = findViewById<LinearLayout>(R.id.background) as LinearLayout
        background.setOnClickListener({
            val inputManager: InputMethodManager =getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)

        })
    }
}