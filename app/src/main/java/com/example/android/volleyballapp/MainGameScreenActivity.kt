package com.example.android.volleyballapp

import android.app.PendingIntent.getActivity
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class MainGameScreenActivity : AppCompatActivity() {
    var startingPlayersList : MutableList<Player> = ArrayList()
    var playersOnCourtList : MutableList<Player> = ArrayList()
    var intialOnCourtList : MutableList<Player> = ArrayList()
    var playersOnBenchList : MutableList<Player> = ArrayList()
    var intialOnBenchList : MutableList<Player> = ArrayList()
    var libPlayer = Player()
    var intialLibPlayer = Player()
    var libCheck = false
    //limits the forloop for adding player names depending on if there is a libero in the game or not.
    var textViewArrayLimit = 5
    var myTeamScore = 0
    var otherTeamScore = 0
    //true is my team false is other team
    var serveIndicator = true
    var intialServeIndicator = true
    var myTeamGames = 0
    var otherTeamGames = 0
    var mySubsUsed = 0
    val actions = ActionStack();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_game_screen)

        //grab previous intent: Starting players, libero boolean, opponent team, team object
        val previousIntent = getIntent()
        val player1 = previousIntent.getSerializableExtra("player1") as Player
        val player2 = previousIntent.getSerializableExtra("player2") as Player
        val player3 = previousIntent.getSerializableExtra("player3") as Player
        val player4 = previousIntent.getSerializableExtra("player4") as Player
        val player5 = previousIntent.getSerializableExtra("player5") as Player
        val player6 = previousIntent.getSerializableExtra("player6") as Player
        libCheck = previousIntent.getBooleanExtra("Libero Check",false)

        val playingTeam = previousIntent.getSerializableExtra("Team Object") as Team
        val opponentName = previousIntent.getStringExtra("Opponent Name")
        serveIndicator = previousIntent.getBooleanExtra("startingServe",true)

        //put starting players in list and on the court
        startingPlayersList.add(player1)
        startingPlayersList.add(player2)
        startingPlayersList.add(player3)
        startingPlayersList.add(player4)
        startingPlayersList.add(player5)
        startingPlayersList.add(player6)
        if(libCheck){
            libPlayer = previousIntent.getSerializableExtra("libero") as Player
            startingPlayersList.add(libPlayer)
            textViewArrayLimit = 6
        }
        //putting the players on the court
        for (i in 0..5){
            playersOnCourtList.add(startingPlayersList[i])
        }

        //adding players to the bench
        //creating db handler to access database
        val DB = DBHandler(this)
        //grabbing the entire team roster
        val playersOnTeamList = DB.readPlayerData(playingTeam)
        //for each player on the roster we should check to see if they are a starting player
        for(i in 0..playersOnTeamList.size-1){
            var addToBenchCheck = false
            //moving through the starting player list
            for(j in 0..startingPlayersList.size-1){
                //if the player is on the staring list then we should stop looking and not add them to the bench
                if(playersOnTeamList.get(i).getID()==startingPlayersList.get(j).getID()){
                    addToBenchCheck=false
                    break;
                }
                //if they are not found then we should add them to the bench
                else{
                    addToBenchCheck=true
                }
            }
            //if the player was never found on the starting player list then we will add them to the bench.
            if(addToBenchCheck) {
                playersOnBenchList.add(playersOnTeamList.get(i))
            }
        }
        intialOnCourtList = playersOnCourtList
        intialOnBenchList = playersOnBenchList
        intialLibPlayer = libPlayer
        intialServeIndicator = serveIndicator

    }

    override fun onResume() {
        super.onResume()
        //setServeArrowImageViews
        val myTeamServeArrow = findViewById<ImageView>(R.id.serveArrowLeft) as ImageView
        val otherTeamServeArrow = findViewById<ImageView>(R.id.serveArrowRight) as ImageView
        if(serveIndicator){
            otherTeamServeArrow.visibility = View.INVISIBLE
            myTeamServeArrow.visibility = View.VISIBLE
        }
        else{
            otherTeamServeArrow.visibility = View.VISIBLE
            myTeamServeArrow.visibility = View.INVISIBLE
        }
        //setScoreBoardTextViews
        val myTeamScoreViewTens = findViewById<TextView>(R.id.leftTeamScoreTensDigit) as TextView
        val myTeamSCoreViewOnes = findViewById<TextView>(R.id.leftTeamScoreOnesDigit) as TextView
        val otherTeamScoreViewTens = findViewById<TextView>(R.id.rightTeamScoreTensDigit) as TextView
        val otherTeamScoreViewOnes = findViewById<TextView>(R.id.rightTeamScoreOnesDigit) as TextView
        myTeamScoreViewTens.setText(""+myTeamScore/10)
        myTeamSCoreViewOnes.setText(""+myTeamScore%10)
        otherTeamScoreViewTens.setText(""+otherTeamScore/10)
        otherTeamScoreViewOnes.setText(""+otherTeamScore%10)
        //setGameBoardTextViews
        val myTeamGameView = findViewById<TextView>(R.id.leftTeamGamesWon) as TextView
        val otherTeamGameView = findViewById<TextView>(R.id.rightTeamGamesWon) as TextView
        myTeamGameView.setText(""+myTeamGames)
        otherTeamGameView.setText(""+otherTeamGames)

        var nameTextViews : MutableList<TextView> = ArrayList()
        var numberTextViews : MutableList<TextView> = ArrayList()
        //declaring Player text views NOTE:: MAY NEED PLAYER ID TEXT VIEWS
        //adding player textViews to arrays
        val pos1Name = findViewById<TextView>(R.id.pos1PlayerName) as TextView
        nameTextViews.add(pos1Name)
        val pos1Num = findViewById<TextView>(R.id.pos1PlayerNumber) as TextView
        numberTextViews.add(pos1Num)
        val pos2Name = findViewById<TextView>(R.id.pos2PlayerName) as TextView
        nameTextViews.add(pos2Name)
        val pos2Num = findViewById<TextView>(R.id.pos2PlayerNumber) as TextView
        numberTextViews.add(pos2Num)
        val pos3Name = findViewById<TextView>(R.id.pos3PlayerName) as TextView
        nameTextViews.add(pos3Name)
        val pos3Num = findViewById<TextView>(R.id.pos3PlayerNumber) as TextView
        numberTextViews.add(pos3Num)
        val pos4Name = findViewById<TextView>(R.id.pos4PlayerName) as TextView
        nameTextViews.add(pos4Name)
        val pos4Num = findViewById<TextView>(R.id.pos4PlayerNumber) as TextView
        numberTextViews.add(pos4Num)
        val pos5Name = findViewById<TextView>(R.id.pos5PlayerName) as TextView
        nameTextViews.add(pos5Name)
        val pos5Num = findViewById<TextView>(R.id.pos5PlayerNumber) as TextView
        numberTextViews.add(pos5Num)
        val pos6Name = findViewById<TextView>(R.id.pos6PlayerName) as TextView
        nameTextViews.add(pos6Name)
        val pos6Num = findViewById<TextView>(R.id.pos6PlayerNumber) as TextView
        numberTextViews.add(pos6Num)
        if(libCheck){
            val liberoPosName = findViewById<TextView>(R.id.libName) as TextView
            nameTextViews.add(liberoPosName)
            val liberoPosNum = findViewById<TextView>(R.id.libNumber) as TextView
            numberTextViews.add(liberoPosNum)
        }

        //updating playerNames and numbers
        for (i in 0..textViewArrayLimit){
            if(i<6) {
                nameTextViews[i].setText(playersOnCourtList.get(i).getFirstName() + " " + playersOnCourtList.get(i).getLastName())
                numberTextViews[i].setText(playersOnCourtList.get(i).getNumber().toString())
            }
            else{
                nameTextViews[i].setText(libPlayer.getFirstName()+" "+libPlayer.getLastName())
                numberTextViews[i].setText(libPlayer.getNumber().toString())
            }
        }
        val undoButton = findViewById<Button>(R.id.gameUndoButton) as Button
        undoButton.setOnClickListener({
            val nullCheck = actions.peek()
            if(nullCheck!=null) {
                actions.pop()
                val gameState = actions.peek()
                if (gameState != null) {
                    playersOnCourtList = gameState.getPlayersOnCourtList()
                    playersOnBenchList = gameState.getPlayersOnBenchList()
                    libPlayer = gameState.getLibPlayer()
                    myTeamScore = gameState.getMyTeamScore()
                    otherTeamScore = gameState.getOtherTeamScore()
                    myTeamGames = gameState.getMyTeamGames()
                    otherTeamGames = gameState.getOtherTeamGames()
                    serveIndicator = gameState.getServeIndicator()
                    mySubsUsed = gameState.getMySubsUsed()
                } else {
                    playersOnCourtList = intialOnCourtList
                    playersOnBenchList = intialOnBenchList
                    libPlayer = intialLibPlayer
                    myTeamScore = 0
                    otherTeamScore = 0
                    myTeamGames = 0
                    otherTeamGames = 0
                    serveIndicator = intialServeIndicator
                    mySubsUsed = 0
                }
                onResume()
            }
        })
        //initializing buttons
        val player1AceBtn = findViewById<Button>(R.id.pos1AceButton) as Button
        player1AceBtn.setOnClickListener({
            if(serveIndicator) {
                myTeamScore=myTeamScore+1
                if(myTeamScore==25&&myTeamScore>otherTeamScore+2){
                    myTeamGames=myTeamGames+1
                    myTeamScore=0
                    otherTeamScore=0
                }
                val thisAction = ActionNode(playersOnCourtList, playersOnBenchList, libPlayer, myTeamScore,
                        otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, "ace", playersOnCourtList.get(1), null)
                actions.push(thisAction)
                onResume()
            }
            else{
                Toast.makeText(this,"Your team doesn't have the serve",Toast.LENGTH_SHORT).show()
            }

        })
    }
}