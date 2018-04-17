package com.example.android.volleyballapp

import android.app.ProgressDialog.show
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import java.time.Year
import java.util.*

class AddTeamActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_team)

        var teamTypes = arrayOf("Varsity", "Junior Varsity", "Freshmen", "Club")

        val context = this
        val addTeamBtn = findViewById<Button>(R.id.addTeamButton) as Button
        val teamNameView = findViewById<EditText>(R.id.teamNameTextBox) as EditText
        val teamTypeSpinner = findViewById<Spinner>(R.id.teamTypeSpinner) as Spinner
        val teamSpinnerAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, teamTypes)
        var selectedType = 0;
        teamTypeSpinner.setAdapter(teamSpinnerAdapter)

        teamTypeSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedType = position;
                //Toast.makeText(context, teamTypes[selectedType]+" was selected",Toast.LENGTH_SHORT).show()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        addTeamBtn.setOnClickListener({
            if(teamNameView.text.toString().length>0){
                val year = Calendar.getInstance().get(Calendar.YEAR)
                val team = Team(teamNameView.text.toString(),teamTypes[selectedType],year.toString())
                var db = DBHandler(context)
                db.insertTeamData(team)
                finish()
            }else{
                Toast.makeText(context,"Please input team information",Toast.LENGTH_SHORT)
            }

        })
        val background = findViewById<LinearLayout>(R.id.background) as LinearLayout
        background.setOnClickListener({
            val inputManager: InputMethodManager =getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)

        })
    }

}
