package com.example.android.volleyballapp

import android.app.PendingIntent.getActivity
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import kotlinx.android.synthetic.main.activity_main_game_screen.*
import org.w3c.dom.Text
import com.example.android.volleyballapp.R.mipmap.ic_launcher
//import javax.swing.text.StyleConstants.setIcon
import android.view.LayoutInflater
import android.widget.*


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
    var rotation = 1
    var mySubsUsed = 0
    var myTO = 2
    var otherTeamTO = 2
    val actions = ActionStack();
    val actionStrings = arrayOf("ace","missedServe","serveRecErr","kill","attackErr","hitInPlay","passRecErr"
    ,"dig","pass1","pass2","pass3","assist","ballErr","sub")
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
        intialOnCourtList = ArrayList(playersOnCourtList)
        intialOnBenchList = ArrayList(playersOnBenchList)
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
        val rotationIndicatorView = findViewById<TextView>(R.id.rotationIndicator) as TextView
        rotationIndicatorView.setText(""+rotation)
        val subsUsedIndicatorView = findViewById<TextView>(R.id.subsUsedIndicator) as TextView
        subsUsedIndicatorView.setText(""+mySubsUsed)

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
        //initializing gameBoard buttons
        val useMyTOBtn = findViewById<Button>(R.id.leftTeamTOButton) as Button
        val otherTeamUseTOBtn = findViewById<Button>(R.id.rightTeamTOButton) as Button
        val endGameBtn = findViewById<Button>(R.id.endGameButton) as Button
        val endMatchBtn = findViewById<Button>(R.id.endMatchButton) as Button
        val oppTeamErrBtn = findViewById<Button>(R.id.opTeamErrorButton) as Button
        val undoButton = findViewById<Button>(R.id.gameUndoButton) as Button
        useMyTOBtn.setOnClickListener({
            myTO--
            val playersOnCourtCopy = ArrayList(playersOnCourtList)
            val playersOnBenchCopy = ArrayList(playersOnBenchList)
            val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                    otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"ourTO", null, null)
            actions.push(thisAction)
            onResume()
        })
        otherTeamUseTOBtn.setOnClickListener({
            otherTeamTO--
            val playersOnCourtCopy = ArrayList(playersOnCourtList)
            val playersOnBenchCopy = ArrayList(playersOnBenchList)
            val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                    otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"otherTeamUseTO", null, null)
            actions.push(thisAction)
            onResume()
        })
        /*
        TODO this may need to put the stack into the database and clear it from memory if there becomes
        a memory issue
        */
        endGameBtn.setOnClickListener({
            if(myTeamScore>otherTeamScore){
                myTeamGames++
                myTeamScore = 0
                otherTeamScore = 0
                val playersOnCourtCopy = ArrayList(playersOnCourtList)
                val playersOnBenchCopy = ArrayList(playersOnBenchList)
                val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                        otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"gameEnd", null, null)
                actions.push(thisAction)
                onResume()
            }
            else if(otherTeamScore>myTeamScore){
                otherTeamGames++
                myTeamScore = 0
                otherTeamScore = 0
                val playersOnCourtCopy = ArrayList(playersOnCourtList)
                val playersOnBenchCopy = ArrayList(playersOnBenchList)
                val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                        otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"gameEnd", null, null)
                actions.push(thisAction)
                onResume()
            }
            else{
                //May Print a toast about no change
            }

        })
        //TODO THIS MAY REQUIRE SPECIAL HANDLEING OF THE STACK
        endMatchBtn.setOnClickListener({

        })
        oppTeamErrBtn.setOnClickListener({
            myTeamScore++
            if(!serveIndicator) {
                serveIndicator = true

                //TODO METHOD CALL FOR ROTATING PLAYERS ON COURT
                rotatePlayers()
            }
            val playersOnCourtCopy = ArrayList(playersOnCourtList)
            val playersOnBenchCopy = ArrayList(playersOnBenchList)
            val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                    otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"otherTeamError", null, null)
            actions.push(thisAction)
            onResume()
        })
        undoButton.setOnClickListener({
            val nullCheck = actions.peek()
            if(nullCheck!=null) {
                val prevPlay = actions.pop()
                if(prevPlay.getActivePlayer()!=null) {
                    Toast.makeText(this, "Undo " + prevPlay.getActivePlayer().getFirstName() + " " + prevPlay.getAction(), Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Undo " + prevPlay.getAction(), Toast.LENGTH_SHORT).show()
                }
                val gameState = actions.peek()
                if (gameState != null) {
                    playersOnCourtList = ArrayList(gameState.getPlayersOnCourtList())
                    playersOnBenchList = ArrayList(gameState.getPlayersOnBenchList())
                    libPlayer = gameState.getLibPlayer()
                    myTeamScore = gameState.getMyTeamScore()
                    otherTeamScore = gameState.getOtherTeamScore()
                    myTeamGames = gameState.getMyTeamGames()
                    otherTeamGames = gameState.getOtherTeamGames()
                    serveIndicator = gameState.getServeIndicator()
                    mySubsUsed = gameState.getMySubsUsed()
                    rotation = gameState.getRotation()
                    myTO = gameState.getMyTO()
                    otherTeamTO = gameState.getOtherTeamTO()
                } else {
                    playersOnCourtList = ArrayList(intialOnCourtList)
                    playersOnBenchList = ArrayList(intialOnBenchList)
                    libPlayer = intialLibPlayer
                    myTeamScore = 0
                    otherTeamScore = 0
                    myTeamGames = 0
                    otherTeamGames = 0
                    serveIndicator = intialServeIndicator
                    mySubsUsed = 0
                    rotation = 1
                    myTO = 2
                    otherTeamTO = 2
                }
                onResume()
            }
        })
        //initializing player 1 buttons
        val player1AceBtn = findViewById<Button>(R.id.pos1AceButton) as Button
        val player1MissBtn = findViewById<Button>(R.id.pos1MissButton) as Button
        val player1SerRecErrBtn = findViewById<Button>(R.id.pos1SerRecErrButton) as Button
        val player1KillBtn = findViewById<Button>(R.id.pos1KillButton) as Button
        val player1ErrorBtn = findViewById<Button>(R.id.pos1AttackErrButton) as Button
        val player1HitInPlayBtn = findViewById<Button>(R.id.pos1AttackHNPButton) as Button
        val player1PassRecErrBtn = findViewById<Button>(R.id.pos1RecErrButton) as Button
        val player1DigBtn = findViewById<Button>(R.id.pos1DigButton) as Button
        val player1Pass1Btn = findViewById<Button>(R.id.pos1Pass1Button) as Button
        val player1Pass2Btn = findViewById<Button>(R.id.pos1Pass2Button) as Button
        val player1Pass3Btn = findViewById<Button>(R.id.pos1Pass3Button) as Button
        val player1AssistBtn = findViewById<Button>(R.id.pos1AssistButton) as Button
        val player1BallErrBtn = findViewById<Button>(R.id.pos1BallErrButton) as Button
        val player1SubBtn = findViewById<Button>(R.id.pos1SubButton) as Button
        player1AceBtn.setOnClickListener({
            if(serveIndicator) {
                myTeamScore=myTeamScore+1
                //removing this codesegment because games may end at different scores
                /*
                if(myTeamScore==25&&myTeamScore>otherTeamScore+2){
                    myTeamGames=myTeamGames+1
                    myTeamScore=0
                    otherTeamScore=0
                }
                */
                val playersOnCourtCopy = ArrayList(playersOnCourtList)
                val playersOnBenchCopy = ArrayList(playersOnBenchList)
                val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                        otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"ace", playersOnCourtList.get(0), null)
                actions.push(thisAction)
                onResume()
            }
            else{
                Toast.makeText(this,"Your team doesn't have the serve",Toast.LENGTH_SHORT).show()
            }
        })
        player1MissBtn.setOnClickListener({
            if(serveIndicator){
                otherTeamScore++
                serveIndicator=false
                val playersOnCourtCopy = ArrayList(playersOnCourtList)
                val playersOnBenchCopy = ArrayList(playersOnBenchList)
                val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                        otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"missServe", playersOnCourtList.get(0), null)
                actions.push(thisAction)
                onResume()
            }
            else {
                Toast.makeText(this, "Your team doesn't have the serve", Toast.LENGTH_SHORT).show()
            }
        })
        //TODO MAKE SURE THAT SERVE RECIEVE ERROR IS CORRECT
        player1SerRecErrBtn.setOnClickListener({
            if(serveIndicator){
                myTeamScore++
                val playersOnCourtCopy = ArrayList(playersOnCourtList)
                val playersOnBenchCopy = ArrayList(playersOnBenchList)
                val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                        otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"serRecErr", playersOnCourtList.get(0), null)
                actions.push(thisAction)
                onResume()
            }
            else {
                Toast.makeText(this, "Your team doesn't have the serve", Toast.LENGTH_SHORT).show()
            }
        })
        player1KillBtn.setOnClickListener({
            myTeamScore++
            if(!serveIndicator){
                serveIndicator=true
                rotatePlayers()
            }
            val playersOnCourtCopy = ArrayList(playersOnCourtList)
            val playersOnBenchCopy = ArrayList(playersOnBenchList)
            val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                    otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"kill", playersOnCourtList.get(0), null)
            actions.push(thisAction)
            onResume()
        })
        player1ErrorBtn.setOnClickListener({
            otherTeamScore++
            if(serveIndicator){
                serveIndicator=false
            }
            val playersOnCourtCopy = ArrayList(playersOnCourtList)
            val playersOnBenchCopy = ArrayList(playersOnBenchList)
            val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                    otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"attackErr", playersOnCourtList.get(0), null)
            actions.push(thisAction)
            onResume()
        })
        player1HitInPlayBtn.setOnClickListener({
            val playersOnCourtCopy = ArrayList(playersOnCourtList)
            val playersOnBenchCopy = ArrayList(playersOnBenchList)
            val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                    otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"hitInPlay", playersOnCourtList.get(0), null)
            actions.push(thisAction)
            onResume()
        })
        player1PassRecErrBtn.setOnClickListener({
            otherTeamScore++
            if(serveIndicator){
                serveIndicator=false
            }
            val playersOnCourtCopy = ArrayList(playersOnCourtList)
            val playersOnBenchCopy = ArrayList(playersOnBenchList)
            val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                    otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"passRecErr", playersOnCourtList.get(0), null)
            actions.push(thisAction)
            onResume()
        })
        player1DigBtn.setOnClickListener({
            val playersOnCourtCopy = ArrayList(playersOnCourtList)
            val playersOnBenchCopy = ArrayList(playersOnBenchList)
            val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                    otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"dig", playersOnCourtList.get(0), null)
            actions.push(thisAction)
            onResume()
        })
        player1Pass1Btn.setOnClickListener({
            val playersOnCourtCopy = ArrayList(playersOnCourtList)
            val playersOnBenchCopy = ArrayList(playersOnBenchList)
            val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                    otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"pass1", playersOnCourtList.get(0), null)
            actions.push(thisAction)
            onResume()
        })
        player1Pass2Btn.setOnClickListener({
            val playersOnCourtCopy = ArrayList(playersOnCourtList)
            val playersOnBenchCopy = ArrayList(playersOnBenchList)
            val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                    otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"pass2", playersOnCourtList.get(0), null)
            actions.push(thisAction)
            onResume()
        })
        player1Pass3Btn.setOnClickListener({
            val playersOnCourtCopy = ArrayList(playersOnCourtList)
            val playersOnBenchCopy = ArrayList(playersOnBenchList)
            val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                    otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"pass3", playersOnCourtList.get(0), null)
            actions.push(thisAction)
            onResume()
        })
        player1AssistBtn.setOnClickListener({
            val playersOnCourtCopy = ArrayList(playersOnCourtList)
            val playersOnBenchCopy = ArrayList(playersOnBenchList)
            val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                    otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"assist", playersOnCourtList.get(0), null)
            actions.push(thisAction)
            onResume()
        })
        player1BallErrBtn.setOnClickListener({
            otherTeamScore++
            if(serveIndicator){
                serveIndicator=false
            }
            val playersOnCourtCopy = ArrayList(playersOnCourtList)
            val playersOnBenchCopy = ArrayList(playersOnBenchList)
            val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                    otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"ballErr", playersOnCourtList.get(0), null)
            actions.push(thisAction)
            onResume()
        })
        player1SubBtn.setOnClickListener({
            val li = LayoutInflater.from(this)

            val promptsView = li.inflate(R.layout.sub_player_alert, null)

            val alertDialogBuilder = AlertDialog.Builder(this)

            alertDialogBuilder.setView(promptsView)

            // set dialog message

            alertDialogBuilder.setTitle("Please Select Player")
            //alertDialogBuilder.setIcon(R.drawable)
            // create alert dialog
            val alertDialog = alertDialogBuilder.create()
            val mSpinner = promptsView.findViewById<Spinner>(R.id.subSpinner) as Spinner
            val mButton = promptsView.findViewById<Button>(R.id.subButton) as Button
            val cancel = promptsView.findViewById<Button>(R.id.subCancelButton) as Button
            var chosenPlayer = Player()
            var currentPlayer = playersOnCourtList.get(0)
            var positionInt = 0
            val benchNameList = ArrayList<String>()
            for(i in 0..playersOnBenchList.size-1){
                benchNameList.add(playersOnBenchList.get(i).getFirstName()+" "+playersOnBenchList.get(i).getLastName())
            }
            val subPlayerAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,benchNameList)
            mSpinner.setAdapter(subPlayerAdapter)
            mSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    chosenPlayer = playersOnBenchList.get(position)
                    positionInt = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }
            cancel.setOnClickListener({
                alertDialog.dismiss()
            })
            mButton.setOnClickListener({
                //playersOnBenchList.remove(chosenPlayer)
                mySubsUsed++
                playersOnBenchList.set(positionInt,currentPlayer)
                playersOnCourtList.set(0,chosenPlayer)
                alertDialog.dismiss()
                val playersOnCourtCopy = ArrayList(playersOnCourtList)
                val playersOnBenchCopy = ArrayList(playersOnBenchList)
                val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                        otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"sub", currentPlayer, chosenPlayer)
                actions.push(thisAction)
                onResume()
            })
            alertDialog.show()
            alertDialog.setCanceledOnTouchOutside(false)
        })
    }

    //this function is for rotating the players on the court
    fun rotatePlayers(){
        if(rotation<6) {
            rotation++
        }
        else{
            rotation = 1
        }
        val tempPlayer = playersOnCourtList.get(0)
        for(i in 0..4){
            playersOnCourtList.set(i,playersOnCourtList.get(i+1))
        }
        playersOnCourtList.set(5,tempPlayer)
    }
}