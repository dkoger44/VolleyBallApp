package com.example.android.volleyballapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_player_stat_view.*
import org.w3c.dom.Text

class PlayerStatView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_stat_view)

        val previousIntent = getIntent()
        val playerID = previousIntent.getStringExtra("PlayerID")
        val statDB = DBHandler(this)
        statDB.selectAllFromGameTable()
        val chosenPlayer = statDB.readSinglePlayerData(playerID).get(0)
        statDB.getPlayerStats(chosenPlayer,2)
        val opponentsName = statDB.getOpponentName(2)

        val opponentNameTextView = findViewById<TextView>(R.id.game1OppName) as TextView
        opponentNameTextView.setText(""+opponentsName)
        val game1KillsTextView = findViewById<TextView>(R.id.game1Kills) as TextView
        game1KillsTextView.setText(""+chosenPlayer.getKills())
        val game1AttackErrorsTextView = findViewById<TextView>(R.id.game1AttackErrors) as TextView
        game1AttackErrorsTextView.setText(""+chosenPlayer.getAttackErrors())
        val game1TotalAttacksTextView = findViewById<TextView>(R.id.game1TotalAttacks) as TextView
        game1TotalAttacksTextView.setText(""+chosenPlayer.getTotalAttacks())
        val game1HittingPercentageTextView = findViewById<TextView>(R.id.game1HittingPercent) as TextView
        game1HittingPercentageTextView.setText(""+chosenPlayer.getHittingPercentage())
        val game1AssistTextView = findViewById<TextView>(R.id.game1Assists) as TextView
        game1AssistTextView.setText(""+chosenPlayer.getAssists())
        val game1BallErrorsText = findViewById<TextView>(R.id.game1BallErrors)as TextView
        game1BallErrorsText.setText(""+chosenPlayer.getBallErrors())
        val game1ServeAceText = findViewById<TextView>(R.id.game1ServeAces) as TextView
        game1ServeAceText.setText(""+chosenPlayer.getAces())
        val game1ServeAttemptText = findViewById<TextView>(R.id.game1ServeAttempts) as TextView
        game1ServeAttemptText.setText(""+chosenPlayer.getServeAttempts())
        val game1ServingPercentageText = findViewById<TextView>(R.id.game1ServePercent) as TextView
        game1ServingPercentageText.setText("To DO")
    }
}
