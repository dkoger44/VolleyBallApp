package com.example.android.volleyballapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_player_stat_view.*
import org.w3c.dom.Text

class PlayerStatView : AppCompatActivity() {
var selectedID = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_stat_view)

        val previousIntent = getIntent()
        val playerID = previousIntent.getStringExtra("PlayerID")
        val statDB = DBHandler(this)
        val gameIDArrays = statDB.selectAllFromGameTable()
        val gameSelector = findViewById<Spinner>(R.id.gameIDSelector) as Spinner
        val gameAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,gameIDArrays)
        gameSelector.setAdapter(gameAdapter)
        gameSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedID = gameIDArrays.get(position)
                onResume()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        val chosenPlayer = statDB.readSinglePlayerData(playerID).get(0)
        statDB.getPlayerStats(chosenPlayer,selectedID)
        val opponentsName = statDB.getOpponentName(selectedID)
        val playerNameText = findViewById<TextView>(R.id.playerNameForStat) as TextView
        playerNameText.setText(""+chosenPlayer.getFirstName()+" "+chosenPlayer.getLastName())
        val opponentNameTextView = findViewById<TextView>(R.id.game1OppName) as TextView
        opponentNameTextView.setText(""+opponentsName)
        val game1KillsTextView = findViewById<TextView>(R.id.game1Kills) as TextView
        game1KillsTextView.setText(""+chosenPlayer.getKills())
        val game1AttackErrorsTextView = findViewById<TextView>(R.id.game1AttackErrors) as TextView
        game1AttackErrorsTextView.setText(""+chosenPlayer.getAttackErrors())
        val game1TotalAttacksTextView = findViewById<TextView>(R.id.game1TotalAttacks) as TextView
        game1TotalAttacksTextView.setText(""+chosenPlayer.getTotalAttacks())
        //val game1HittingPercentageTextView = findViewById<TextView>(R.id.game1HittingPercent) as TextView
        //game1HittingPercentageTextView.setText(""+chosenPlayer.getHittingPercentage())
        val game1AssistTextView = findViewById<TextView>(R.id.game1Assists) as TextView
        game1AssistTextView.setText(""+chosenPlayer.getAssists())
        val game1BallErrorsText = findViewById<TextView>(R.id.game1BallErrors)as TextView
        game1BallErrorsText.setText(""+chosenPlayer.getBallErrors())
        val game1ServeAceText = findViewById<TextView>(R.id.game1ServeAces) as TextView
        game1ServeAceText.setText(""+chosenPlayer.getAces())
        val game1ServeAttemptText = findViewById<TextView>(R.id.game1ServeAttempts) as TextView
        game1ServeAttemptText.setText(""+chosenPlayer.getServeAttempts())
        //val game1ServingPercentageText = findViewById<TextView>(R.id.game1ServePercent) as TextView
        //game1ServingPercentageText.setText("To DO")
        val game1PassRecErrText = findViewById<TextView>(R.id.game1PassRecERR) as TextView
        game1PassRecErrText.setText(""+chosenPlayer.getReceptionErrors())
        val game1DigText = findViewById<TextView>(R.id.game1Digs) as TextView
        game1DigText.setText(""+chosenPlayer.getDigs())
        val game1BlockText = findViewById<TextView>(R.id.game1Blocks) as TextView
        game1BlockText.setText(""+chosenPlayer.getSoloBlock())
        val game1BlockAssistText = findViewById<TextView>(R.id.game1BlockAssist) as TextView
        game1BlockAssistText.setText(""+chosenPlayer.getBlockAssists())
        val game1BlockErrText = findViewById<TextView>(R.id.game1BlockErr) as TextView
        game1BlockErrText.setText(""+chosenPlayer.getBlockErrors())
    }

    override fun onResume() {
        super.onResume()

        val previousIntent = getIntent()
        val playerID = previousIntent.getStringExtra("PlayerID")
        val statDB = DBHandler(this)
        val gameIDArrays = statDB.selectAllFromGameTable()
        val chosenPlayer = statDB.readSinglePlayerData(playerID).get(0)
        statDB.getPlayerStats(chosenPlayer,selectedID)
        val opponentsName = statDB.getOpponentName(selectedID)

        val opponentNameTextView = findViewById<TextView>(R.id.game1OppName) as TextView
        opponentNameTextView.setText(""+opponentsName)
        val game1KillsTextView = findViewById<TextView>(R.id.game1Kills) as TextView
        game1KillsTextView.setText(""+chosenPlayer.getKills())
        val game1AttackErrorsTextView = findViewById<TextView>(R.id.game1AttackErrors) as TextView
        game1AttackErrorsTextView.setText(""+chosenPlayer.getAttackErrors())
        val game1TotalAttacksTextView = findViewById<TextView>(R.id.game1TotalAttacks) as TextView
        game1TotalAttacksTextView.setText(""+chosenPlayer.getTotalAttacks())
        //val game1HittingPercentageTextView = findViewById<TextView>(R.id.game1HittingPercent) as TextView
        //game1HittingPercentageTextView.setText(""+chosenPlayer.getHittingPercentage())
        val game1AssistTextView = findViewById<TextView>(R.id.game1Assists) as TextView
        game1AssistTextView.setText(""+chosenPlayer.getAssists())
        val game1BallErrorsText = findViewById<TextView>(R.id.game1BallErrors)as TextView
        game1BallErrorsText.setText(""+chosenPlayer.getBallErrors())
        val game1ServeAceText = findViewById<TextView>(R.id.game1ServeAces) as TextView
        game1ServeAceText.setText(""+chosenPlayer.getAces())
        val game1ServeAttemptText = findViewById<TextView>(R.id.game1ServeAttempts) as TextView
        game1ServeAttemptText.setText(""+chosenPlayer.getServeAttempts())
        //val game1ServingPercentageText = findViewById<TextView>(R.id.game1ServePercent) as TextView
        //game1ServingPercentageText.setText("To DO")
        val game1PassRecErrText = findViewById<TextView>(R.id.game1PassRecERR) as TextView
        game1PassRecErrText.setText(""+chosenPlayer.getReceptionErrors())
        val game1DigText = findViewById<TextView>(R.id.game1Digs) as TextView
        game1DigText.setText(""+chosenPlayer.getDigs())
        val game1BlockText = findViewById<TextView>(R.id.game1Blocks) as TextView
        game1BlockText.setText(""+chosenPlayer.getSoloBlock())
        val game1BlockAssistText = findViewById<TextView>(R.id.game1BlockAssist) as TextView
        game1BlockAssistText.setText(""+chosenPlayer.getBlockAssists())
        val game1BlockErrText = findViewById<TextView>(R.id.game1BlockErr) as TextView
        game1BlockErrText.setText(""+chosenPlayer.getBlockErrors())
    }
}