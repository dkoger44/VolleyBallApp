package com.example.android.volleyballapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class selectTeamActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_team)


        //attaching buttons
        val homeBtn = findViewById<Button>(R.id.selectTeamHomeButton) as Button
        val helpBtn = findViewById<Button>(R.id.selectTeamHelpButton) as Button


        //setting Button onClick listeners
        homeBtn.setOnClickListener({
            finish()
        })
        helpBtn.setOnClickListener({
            val intent = Intent(this, HelpActivity::class.java )
            startActivity(intent)
        })


    }

    override fun onResume() {
        super.onResume()

        //need to make connection to database to get team names
        //put names into array
        val teamDB = DBHandler(this)
        //val defaultTeam = Team("default team","default type", "2018")
        var teams = teamDB.readTeamData()
        var list = ArrayList<String>()
        //list.add("defualt team")
        for (i in 0..teams.size - 1) {
            list.add(teams.get(i).getName())
        }

        //attaching listView to arrayAdapter
        val adapter = SelectTeamListAdapter(list, this)

        val lView = findViewById<ListView>(R.id.teamList)

        lView.adapter = adapter;
        //lView.setChoiceMode(ListView.CHOICE_MODE_SINGLE)
        if (lView.isSelected) {
            var selectedName = lView.selectedItem.toString()
            Toast.makeText(this, selectedName + " was selected", Toast.LENGTH_SHORT).show()
        }
        //lView.onItemClick()
        //this onItemClickListener should be activated when any listview item is clicked. It is not
        //may look  into adding a delete button or checkbox in the listview item since it is easier
        //to know when it is clicked.

        lView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position) as String
            //val teamTextView = findViewById<TextView>(R.id.teamNameSelectTeam)



            Toast.makeText(this, "HEY list item clicked and the name is " + item, Toast.LENGTH_SHORT).show()
        }
        //lView.onItemClickListener.onItemClick(AdapterView<?> RosterListAdapter,)

    }
}
