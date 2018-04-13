package com.example.android.volleyballapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.ListView

class ViewTeamRosterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_team_roster)

        //val previousIntent = getIntent()
        //val teamName = previousIntent.getSerializableExtra("TeamObject") as Team

        //attaching buttons
        val homeBtn = findViewById<Button>(R.id.rosterHomeButton) as Button
        val helpBtn = findViewById<Button>(R.id.rosterHelpButton) as Button
        val newPlayerBtn = findViewById<Button>(R.id.newPlayerButton) as Button
        val deletePlayerBtn = findViewById<Button>(R.id.deletePlayerButton) as Button

        //setting button onClick listeners

        homeBtn.setOnClickListener({
            finish()
        })
        helpBtn.setOnClickListener({
            val intent = Intent(this, HelpActivity::class.java )
            startActivity(intent)
        })

        /*newPlayerBtn.setOnClickListener({

            val intent = Intent(this, AddPlayerActivity::class.java)
            intent.putExtra("TeamObject", teamName)
            startActivity(intent)

        })*/
    }
    override fun onResume() {
        super.onResume()

        val newPlayerBtn = findViewById<Button>(R.id.newPlayerButton) as Button
        val previousIntent = getIntent()
        val teamName = previousIntent.getSerializableExtra("TeamObject") as Team


        newPlayerBtn.setOnClickListener({

            val intent = Intent(this, AddPlayerActivity::class.java)
            intent.putExtra("TeamObject", teamName)
            startActivity(intent)

        })



        //need to make connection to database to get player names
        //put names into array
        val playerDB = DBHandler(this)
        //val defaultTeam = Team("default team","default type", "2018")
        var players = playerDB.readPlayerData(teamName)
        var list = ArrayList<String>()
        //list.add("defualt team")
        for(i in 0..players.size-1){
            var playerFullName = players.get(i).getFirstName() + " " + players.get(i).getLastName()
            list.add(playerFullName)
        }

        //attaching listView to arrayAdapter
        val adapter = SelectPlayerRosterListAdapter(list,this)

        val lView = findViewById<ListView>(R.id.playerList)

        lView.adapter = adapter;
    }
}