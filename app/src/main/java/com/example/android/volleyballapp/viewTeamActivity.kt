package com.example.android.volleyballapp

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast

class viewTeamActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_team)

        //attaching buttons
        val homeBtn = findViewById<Button>(R.id.rosterHomeButton) as Button
        val helpBtn = findViewById<Button>(R.id.rosterHelpButton) as Button
        val newTeamBtn = findViewById<Button>(R.id.newTeamButton) as Button
        val deleteTeamBtn = findViewById<Button>(R.id.deleteTeamButton) as Button
        //setting Button onClick listeners
        homeBtn.setOnClickListener({
            finish()
        })
        helpBtn.setOnClickListener({
            val intent = Intent(this, HelpActivity::class.java )
            startActivity(intent)
        })

        newTeamBtn.setOnClickListener({
            //var context = applicationContext
            //Toast.makeText(context,"newTeam Button Hit",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AddTeamActivity::class.java)
            startActivity(intent)
        })
        deleteTeamBtn.setOnClickListener({
            var context = applicationContext
            Toast.makeText(context,"Delete Button Hit",Toast.LENGTH_SHORT).show()
            val teamDB = DBHandler(this)
            //need to pass team name to be deleted
            //possibly will need to send ID of the team being deleted
            teamDB.deleteEntry("default team")

        })



    }

    override fun onResume() {
        super.onResume()

        //need to make connection to database to get team names
        //put names into array
        val teamDB = DBHandler(this)
        //val defaultTeam = Team("default team","default type", "2018")
        var teams = teamDB.readData()
        var list = ArrayList<String>()
        //list.add("defualt team")
        for(i in 0..teams.size-1){
            list.add(teams.get(i).getName())
        }

        //list.add("Madison Southern High School")
        //list.add("Farristown Middle School")

        //attaching listView to arrayAdapter
        val adapter = RosterListAdapter(list,this)

        val lView = findViewById<ListView>(R.id.teamList)

        lView.adapter = adapter;
        //lView.setChoiceMode(ListView.CHOICE_MODE_SINGLE)
        if(lView.isSelected) {
            var selectedName = lView.selectedItem.toString()
            Toast.makeText(this, selectedName+" was selected",Toast.LENGTH_SHORT).show()
        }
        //lView.onItemClick()
        //this onItemClickListener should be activated when any listview item is clicked. It is not
        //may look  into adding a delete button or checkbox in the listview item since it is easier
        //to know when it is clicked.

        //NOTE: This issue is aparently cause by the layout for each line item being too crowded.
        //      IF I take buttons off of the team_rooster_list_item.xml file then the lView Clicklistener works
        lView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position) as String


            Toast.makeText(this, "HEY list item clicked", Toast.LENGTH_SHORT).show()
        }
        //lView.onItemClickListener.onItemClick(AdapterView<?> RosterListAdapter,)
    }
}
