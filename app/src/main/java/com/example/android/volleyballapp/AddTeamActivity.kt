package com.example.android.volleyballapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class AddTeamActivity : AppCompatActivity() {
    var teamTypes = arrayOf("Varsity", "Junior Varsity", "Freshmen", "Club")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_team)

        var teamTypes = arrayOf("Varsity", "Junior Varsity", "Freshmen", "Club")

        val context = this
        val addTeamBtn = findViewById<Button>(R.id.addTeamButton) as Button
        val teamNameView = findViewById<EditText>(R.id.teamNameTextBox) as EditText
        val teamTypeSpinner = findViewById<Spinner>(R.id.teamTypeSpinner) as Spinner
        val teamSpinnerAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, teamTypes)

        addTeamBtn.setOnClickListener({
            if(teamNameView.text.toString().length > 0 &&
                    teamTypeView.text.toString().length > 0){
                val team = Team(teamNameView.text.toString(),teamTypeView.text.toString(),"2018")
                var db = DBHandler(context)
                db.insertTeamData(team)
                finish()
            }else{
                Toast.makeText(context,"Please input team information",Toast.LENGTH_SHORT)
            }

        })
    }
}
