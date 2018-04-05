package com.example.android.volleyballapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AddTeamActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_team)

        val context = this
        val addTeamBtn = findViewById<Button>(R.id.addTeamButton) as Button
        val teamNameView = findViewById<EditText>(R.id.teamNameTextBox) as EditText
        val teamTypeView = findViewById<EditText>(R.id.teamTypeTextBox) as EditText
        addTeamBtn.setOnClickListener({
            if(teamNameView.text.toString().length > 0 &&
                    teamTypeView.text.toString().length > 0){
                val team = Team(teamNameView.text.toString(),teamTypeView.text.toString(),"2018")
                var db = DBHandler(context)
                db.insertData(team)
                finish()
            }else{
                Toast.makeText(context,"Please input team information",Toast.LENGTH_SHORT)
            }

        })
    }
}
