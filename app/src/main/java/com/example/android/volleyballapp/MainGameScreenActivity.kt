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
    var playingTeam = Team()
    var opponentName = ""
    var startingPlayersList : MutableList<Player> = ArrayList()
    var playersOnCourtList : MutableList<Player> = ArrayList()
    var intialOnCourtList : MutableList<Player> = ArrayList()
    var playersOnBenchList : MutableList<Player> = ArrayList()
    var intialOnBenchList : MutableList<Player> = ArrayList()
    var playersOnTeamList : MutableList<Player> = ArrayList()
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
    var serveAttempt = false;
    var server = Player()
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

        playingTeam = previousIntent.getSerializableExtra("Team Object") as Team
        opponentName = previousIntent.getStringExtra("Opponent Name")
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
        playersOnTeamList = DB.readPlayerData(playingTeam)
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
        val myTOUsedIndicatorView = findViewById<TextView>(R.id.leftTeamTimeOuts) as TextView
        myTOUsedIndicatorView.setText(""+myTO)
        val otherTeamTOUsedIndicatorView = findViewById<TextView>(R.id.rightTeamTimeOuts) as TextView
        otherTeamTOUsedIndicatorView.setText(""+otherTeamTO)

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
        //initializing libero button
        val liberoSwapBtn = findViewById<Button>(R.id.libBtn) as Button
        liberoSwapBtn.setOnClickListener({
            if(libPlayer.equals(intialLibPlayer)) {
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
                var currentPlayer = libPlayer
                var positionInt = 0
                val benchNameList = ArrayList<String>()
                for (i in 0..playersOnCourtList.size - 1) {
                    if(i==0||i==4||i==5) {
                        benchNameList.add(playersOnCourtList.get(i).getFirstName() + " " + playersOnCourtList.get(i).getLastName())
                    }
                }
                val subPlayerAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, benchNameList)
                mSpinner.setAdapter(subPlayerAdapter)
                mSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        if(position ==1){
                            positionInt=4
                        }
                        else if(position == 2){
                            positionInt=5
                        }
                        chosenPlayer = playersOnCourtList.get(positionInt)
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
                    //mySubsUsed++
                    playersOnCourtList.set(positionInt, currentPlayer)
                    libPlayer = chosenPlayer
                    alertDialog.dismiss()
                    val playersOnCourtCopy = ArrayList(playersOnCourtList)
                    val playersOnBenchCopy = ArrayList(playersOnBenchList)
                    val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                            otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation, myTO, otherTeamTO, "libSwap", currentPlayer, chosenPlayer,null,false)
                    actions.push(thisAction)
                    onResume()
                })
                alertDialog.show()
                alertDialog.setCanceledOnTouchOutside(false)
            }
            else{
                var currentPlayer = libPlayer
                for(i in 0..playersOnCourtList.size-1){
                    if(playersOnCourtList[i].equals(intialLibPlayer)){
                        libPlayer = intialLibPlayer
                        playersOnCourtList.set(i,currentPlayer)
                        break;
                    }
                }
                val playersOnCourtCopy = ArrayList(playersOnCourtList)
                val playersOnBenchCopy = ArrayList(playersOnBenchList)
                val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                        otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation, myTO, otherTeamTO, "reverseLibSwap", currentPlayer, intialLibPlayer,null,false)
                actions.push(thisAction)
                onResume()
            }
        })
        //initializing gameBoard buttons
        val useMyTOBtn = findViewById<Button>(R.id.leftTeamTOButton) as Button
        val otherTeamUseTOBtn = findViewById<Button>(R.id.rightTeamTOButton) as Button
        val endGameBtn = findViewById<Button>(R.id.endGameButton) as Button
        val endMatchBtn = findViewById<Button>(R.id.endMatchButton) as Button
        val oppTeamErrBtn = findViewById<Button>(R.id.opTeamErrorButton) as Button
        val undoButton = findViewById<Button>(R.id.gameUndoButton) as Button
        useMyTOBtn.setOnClickListener({
            if(myTO>0) {
                myTO--
                val playersOnCourtCopy = ArrayList(playersOnCourtList)
                val playersOnBenchCopy = ArrayList(playersOnBenchList)
                val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                        otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation, myTO, otherTeamTO, "ourTO", null, null,null,false)
                actions.push(thisAction)
                onResume()
            }
            else{
                Toast.makeText(this,"You have no TimeOuts Left!",Toast.LENGTH_SHORT).show()
            }
        })
        otherTeamUseTOBtn.setOnClickListener({
            if(otherTeamTO>0) {
                otherTeamTO--
                val playersOnCourtCopy = ArrayList(playersOnCourtList)
                val playersOnBenchCopy = ArrayList(playersOnBenchList)
                val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                        otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation, myTO, otherTeamTO, "otherTeamUseTO", null, null,null,false)
                actions.push(thisAction)
                onResume()
            }
            else{
                Toast.makeText(this,"The Other Team has no more TimeOuts left!",Toast.LENGTH_LONG).show()
            }
        })
        /*
        TODO this may need to put the stack into the database and clear it from memory if there becomes
        TODO may need to add games into database seperately so the user can see the games stats.
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
                        otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"gameEnd", null, null,null,false)
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
                        otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"gameEnd", null, null,null,false)
                actions.push(thisAction)
                onResume()
            }
            else{
                //May Print a toast about no change
            }

        })
        //TODO THIS MAY REQUIRE SPECIAL HANDLEING OF THE STACK
        endMatchBtn.setOnClickListener({
            val li = LayoutInflater.from(this)
            val promptsView = li.inflate(R.layout.end_match_prompt, null)
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setView(promptsView)
            // set dialog message
            alertDialogBuilder.setTitle("NOTICE!!!")
            //alertDialogBuilder.setIcon(R.drawable)
            // create alert dialog
            val alertDialog = alertDialogBuilder.create()
            val message = promptsView.findViewById<TextView>(R.id.matchEndMessage)
            if(myTeamScore>otherTeamScore){
                val myTeamGamesPossible = myTeamGames+1
                message.setText("This will end the match "+myTeamGamesPossible + " to "+otherTeamGames)
            }
            else if(otherTeamScore>myTeamScore){
                val otherTeamPossible = otherTeamGames+1
                message.setText("This will end the match "+myTeamGames + " to "+otherTeamPossible)
            }
            else {
                message.setText("This will end the match " + myTeamGames + " to " + otherTeamGames+" with a tie on the final game!")
            }
            val aButton = promptsView.findViewById<Button>(R.id.matchEndAccept) as Button
            val cButton = promptsView.findViewById<Button>(R.id.matchEndCancel) as Button
            cButton.setOnClickListener({
                alertDialog.dismiss()
            })
            aButton.setOnClickListener({
                val gameDB = DBHandler(this)
                val scheduleID = gameDB.getTeamSchedule(playingTeam)
                gameDB.insertGame(scheduleID.get(0),opponentName)
                val gameID = gameDB.getGameID(scheduleID.get(0),opponentName)
                //make method call to pop through the stack
                recordStats(actions)
                //either make this call here or in the recordStats method
                for(i in 0..playersOnTeamList.size-1){
                    gameDB.insertPlayerGameTable(playersOnTeamList.get(i),gameID)
                }
                alertDialog.dismiss()
                finish()

            })
            alertDialog.show()
            alertDialog.setCanceledOnTouchOutside(false)
        })
        oppTeamErrBtn.setOnClickListener({
            myTeamScore++
            if(!serveIndicator) {
                serveIndicator = true
                rotatePlayers()
                serveAttempt=false
            }
            else{
                serveAttempt=true
            }
            val playersOnCourtCopy = ArrayList(playersOnCourtList)
            val playersOnBenchCopy = ArrayList(playersOnBenchList)
            val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                    otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"otherTeamError", null, null,playersOnCourtList[0],serveAttempt)
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
                myTeamScoreInc()
                createAction(0,"ace")
            }
            else{
                Toast.makeText(this,"Your team doesn't have the serve",Toast.LENGTH_SHORT).show()
            }
        })
        player1MissBtn.setOnClickListener({
            if(serveIndicator){
                otherTeamScoreInc()
                createAction(0,"missServe")
            }
            else {
                Toast.makeText(this, "Your team doesn't have the serve", Toast.LENGTH_SHORT).show()
            }
        })
        player1SerRecErrBtn.setOnClickListener({
            if(serveIndicator){
                myTeamScore++
                createAction(0,"serRecErr")
            }
            else {
                Toast.makeText(this, "Your team doesn't have the serve", Toast.LENGTH_SHORT).show()
            }
        })
        player1KillBtn.setOnClickListener({
            myTeamScoreInc()
            createAction(0,"kill")
        })
        player1ErrorBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(0,"attackErr")
        })
        player1HitInPlayBtn.setOnClickListener({
            createAction(0,"hitInPlay")
        })
        player1PassRecErrBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(0,"passRecErr")
        })
        player1DigBtn.setOnClickListener({
            createAction(0,"dig")
        })
        player1Pass1Btn.setOnClickListener({
            createAction(0,"pass1")
        })
        player1Pass2Btn.setOnClickListener({
            createAction(0,"pass2")
        })
        player1Pass3Btn.setOnClickListener({
            createAction(0,"pass3")
        })
        player1AssistBtn.setOnClickListener({
            createAction(0,"assist")
        })
        player1BallErrBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(0,"ballErr")
        })
        player1SubBtn.setOnClickListener({
           subButtonHelper(0)
        })

        //declare player 2 buttons
        val player2BlockBtn = findViewById<Button>(R.id.pos2BlockButton) as Button
        val player2BlockAssistBtn = findViewById<Button>(R.id.pos2BlockAssistButton) as Button
        val player2BlockErrBtn = findViewById<Button>(R.id.pos2BlockErrorButton) as Button
        val player2KillBtn = findViewById<Button>(R.id.pos2KillButton) as Button
        val player2ErrorBtn = findViewById<Button>(R.id.pos2AttackErrButton) as Button
        val player2HitInPlayBtn = findViewById<Button>(R.id.pos2AttackHNPButton) as Button
        val player2PassRecErrBtn = findViewById<Button>(R.id.pos2RecErrButton) as Button
        val player2DigBtn = findViewById<Button>(R.id.pos2DigButton) as Button
        val player2Pass1Btn = findViewById<Button>(R.id.pos2Pass1Button) as Button
        val player2Pass2Btn = findViewById<Button>(R.id.pos2Pass2Button) as Button
        val player2Pass3Btn = findViewById<Button>(R.id.pos2Pass3Button) as Button
        val player2AssistBtn = findViewById<Button>(R.id.pos2AssistButton) as Button
        val player2BallErrBtn = findViewById<Button>(R.id.pos2BallErrButton) as Button
        val player2SubBtn = findViewById<Button>(R.id.pos2SubButton) as Button
        player2BlockBtn.setOnClickListener({
            myTeamScoreInc()
            createAction(1,"block")
        })
        player2BlockAssistBtn.setOnClickListener({
            createAction(1,"blockAssist")
        })
        player2BlockErrBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(1,"blockErr")
        })
        player2KillBtn.setOnClickListener({
            myTeamScoreInc()
            createAction(1,"kill")
        })
        player2ErrorBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(1,"attackErr")
        })
        player2HitInPlayBtn.setOnClickListener({
            createAction(1,"hitInPlay")
        })
        player2PassRecErrBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(1,"passRecErr")
        })
        player2DigBtn.setOnClickListener({
            createAction(1,"dig")
        })
        player2Pass1Btn.setOnClickListener({
            createAction(1,"pass1")

        })
        player2Pass2Btn.setOnClickListener({
            createAction(1,"pass2")
        })
        player2Pass3Btn.setOnClickListener({
            createAction(1,"pass3")
        })
        player2AssistBtn.setOnClickListener({
            createAction(1,"assist")
        })
        player2BallErrBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(1,"ballErr")
        })
        player2SubBtn.setOnClickListener({
           subButtonHelper(1)
        })

        //declare player 3 buttons
        val player3BlockBtn = findViewById<Button>(R.id.pos3BlockButton) as Button
        val player3BlockAssistBtn = findViewById<Button>(R.id.pos3BlockAssistButton) as Button
        val player3BlockErrBtn = findViewById<Button>(R.id.pos3BlockErrorButton) as Button
        val player3KillBtn = findViewById<Button>(R.id.pos3KillButton) as Button
        val player3ErrorBtn = findViewById<Button>(R.id.pos3AttackErrButton) as Button
        val player3HitInPlayBtn = findViewById<Button>(R.id.pos3AttackHNPButton) as Button
        val player3PassRecErrBtn = findViewById<Button>(R.id.pos3RecErrButton) as Button
        val player3DigBtn = findViewById<Button>(R.id.pos3DigButton) as Button
        val player3Pass1Btn = findViewById<Button>(R.id.pos3Pass1Button) as Button
        val player3Pass2Btn = findViewById<Button>(R.id.pos3Pass2Button) as Button
        val player3Pass3Btn = findViewById<Button>(R.id.pos3Pass3Button) as Button
        val player3AssistBtn = findViewById<Button>(R.id.pos3AssistButton) as Button
        val player3BallErrBtn = findViewById<Button>(R.id.pos3BallErrButton) as Button
        val player3SubBtn = findViewById<Button>(R.id.pos3SubButton) as Button
        player3BlockBtn.setOnClickListener({
            myTeamScoreInc()
            createAction(2,"block")
        })
        player3BlockAssistBtn.setOnClickListener({
            createAction(2,"blockAssist")
        })
        player3BlockErrBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(2,"blockErr")
        })
        player3KillBtn.setOnClickListener({
            myTeamScoreInc()
            createAction(2,"kill")
        })
        player3ErrorBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(2,"attackErr")
        })
        player3HitInPlayBtn.setOnClickListener({
            createAction(2,"hitInPlay")
        })
        player3PassRecErrBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(2,"passRecErr")
        })
        player3DigBtn.setOnClickListener({
            createAction(2,"dig")
        })
        player3Pass1Btn.setOnClickListener({
            createAction(2,"pass1")

        })
        player3Pass2Btn.setOnClickListener({
            createAction(2,"pass2")
        })
        player3Pass3Btn.setOnClickListener({
            createAction(2,"pass3")
        })
        player3AssistBtn.setOnClickListener({
            createAction(2,"assist")
        })
        player3BallErrBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(2,"ballErr")
        })
        player3SubBtn.setOnClickListener({
            subButtonHelper(2)
        })

        //declare player 4 buttons
        val player4BlockBtn = findViewById<Button>(R.id.pos4BlockButton) as Button
        val player4BlockAssistBtn = findViewById<Button>(R.id.pos4BlockAssistButton) as Button
        val player4BlockErrBtn = findViewById<Button>(R.id.pos4BlockErrorButton) as Button
        val player4KillBtn = findViewById<Button>(R.id.pos4KillButton) as Button
        val player4ErrorBtn = findViewById<Button>(R.id.pos4AttackErrButton) as Button
        val player4HitInPlayBtn = findViewById<Button>(R.id.pos4AttackHNPButton) as Button
        val player4PassRecErrBtn = findViewById<Button>(R.id.pos4RecErrButton) as Button
        val player4DigBtn = findViewById<Button>(R.id.pos4DigButton) as Button
        val player4Pass1Btn = findViewById<Button>(R.id.pos4Pass1Button) as Button
        val player4Pass2Btn = findViewById<Button>(R.id.pos4Pass2Button) as Button
        val player4Pass3Btn = findViewById<Button>(R.id.pos4Pass3Button) as Button
        val player4AssistBtn = findViewById<Button>(R.id.pos4AssistButton) as Button
        val player4BallErrBtn = findViewById<Button>(R.id.pos4BallErrButton) as Button
        val player4SubBtn = findViewById<Button>(R.id.pos4SubButton) as Button
        player4BlockBtn.setOnClickListener({
            myTeamScoreInc()
            createAction(3,"block")
        })
        player4BlockAssistBtn.setOnClickListener({
            createAction(3,"blockAssist")
        })
        player4BlockErrBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(3,"blockErr")
        })
        player4KillBtn.setOnClickListener({
            myTeamScoreInc()
            createAction(3,"kill")
        })
        player4ErrorBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(3,"attackErr")
        })
        player4HitInPlayBtn.setOnClickListener({
            createAction(3,"hitInPlay")
        })
        player4PassRecErrBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(3,"passRecErr")
        })
        player4DigBtn.setOnClickListener({
            createAction(3,"dig")
        })
        player4Pass1Btn.setOnClickListener({
            createAction(3,"pass1")

        })
        player4Pass2Btn.setOnClickListener({
            createAction(3,"pass2")
        })
        player4Pass3Btn.setOnClickListener({
            createAction(3,"pass3")
        })
        player4AssistBtn.setOnClickListener({
            createAction(3,"assist")
        })
        player4BallErrBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(3,"ballErr")
        })
        player4SubBtn.setOnClickListener({
            subButtonHelper(3)
        })

        //declare player 5 buttons
        val player5KillBtn = findViewById<Button>(R.id.pos5KillButton) as Button
        val player5ErrorBtn = findViewById<Button>(R.id.pos5AttackErrButton) as Button
        val player5HitInPlayBtn = findViewById<Button>(R.id.pos5AttackHNPButton) as Button
        val player5PassRecErrBtn = findViewById<Button>(R.id.pos5RecErrButton) as Button
        val player5DigBtn = findViewById<Button>(R.id.pos5DigButton) as Button
        val player5Pass1Btn = findViewById<Button>(R.id.pos5Pass1Button) as Button
        val player5Pass2Btn = findViewById<Button>(R.id.pos5Pass2Button) as Button
        val player5Pass3Btn = findViewById<Button>(R.id.pos5Pass3Button) as Button
        val player5AssistBtn = findViewById<Button>(R.id.pos5AssistButton) as Button
        val player5BallErrBtn = findViewById<Button>(R.id.pos5BallErrButton) as Button
        val player5SubBtn = findViewById<Button>(R.id.pos5SubButton) as Button
        player5KillBtn.setOnClickListener({
            myTeamScoreInc()
            createAction(4,"kill")
        })
        player5ErrorBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(4,"attackErr")
        })
        player5HitInPlayBtn.setOnClickListener({
            createAction(4,"hitInPlay")
        })
        player5PassRecErrBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(4,"passRecErr")
        })
        player5DigBtn.setOnClickListener({
            createAction(4,"dig")
        })
        player5Pass1Btn.setOnClickListener({
            createAction(4,"pass1")
        })
        player5Pass2Btn.setOnClickListener({
            createAction(4,"pass2")
        })
        player5Pass3Btn.setOnClickListener({
            createAction(4,"pass3")
        })
        player5AssistBtn.setOnClickListener({
            createAction(4,"assist")
        })
        player5BallErrBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(4,"ballErr")
        })
        player5SubBtn.setOnClickListener({
            subButtonHelper(4)
        })

        //declare player 6 buttons
        val player6KillBtn = findViewById<Button>(R.id.pos6KillButton) as Button
        val player6ErrorBtn = findViewById<Button>(R.id.pos6AttackErrButton) as Button
        val player6HitInPlayBtn = findViewById<Button>(R.id.pos6AttackHNPButton) as Button
        val player6PassRecErrBtn = findViewById<Button>(R.id.pos6RecErrButton) as Button
        val player6DigBtn = findViewById<Button>(R.id.pos6DigButton) as Button
        val player6Pass1Btn = findViewById<Button>(R.id.pos6Pass1Button) as Button
        val player6Pass2Btn = findViewById<Button>(R.id.pos6Pass2Button) as Button
        val player6Pass3Btn = findViewById<Button>(R.id.pos6Pass3Button) as Button
        val player6AssistBtn = findViewById<Button>(R.id.pos6AssistButton) as Button
        val player6BallErrBtn = findViewById<Button>(R.id.pos6BallErrButton) as Button
        val player6SubBtn = findViewById<Button>(R.id.pos6SubButton) as Button
        player6KillBtn.setOnClickListener({
            myTeamScoreInc()
            createAction(5,"kill")
        })
        player6ErrorBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(5,"attackErr")
        })
        player6HitInPlayBtn.setOnClickListener({
            createAction(5,"hitInPlay")
        })
        player5PassRecErrBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(4,"passRecErr")
        })
        player5DigBtn.setOnClickListener({
            createAction(4,"dig")
        })
        player5Pass1Btn.setOnClickListener({
            createAction(4,"pass1")
        })
        player5Pass2Btn.setOnClickListener({
            createAction(4,"pass2")
        })
        player5Pass3Btn.setOnClickListener({
            createAction(4,"pass3")
        })
        player5AssistBtn.setOnClickListener({
            createAction(4,"assist")
        })
        player5BallErrBtn.setOnClickListener({
            otherTeamScoreInc()
            createAction(4,"ballErr")
        })
        player5SubBtn.setOnClickListener({
            subButtonHelper(4)
        })
    }
    fun myTeamScoreInc(){
        myTeamScore++
        if(!serveIndicator){
            serveIndicator=true
            serveAttempt=false
            rotatePlayers()
        }
        else{
            serveAttempt=true
            server=playersOnCourtList[0]
        }
    }
    fun otherTeamScoreInc(){
        otherTeamScore++
        if(serveIndicator){
            serveIndicator=false
            serveAttempt=true
            server=playersOnCourtList[0]
        }
        else{
            serveAttempt=false
        }
    }

    //this function will create the alert box for chosing the player to be subbed in. This prevents
    //the code from having to be dublicated 6 times.
    fun subButtonHelper(p: Int){
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
        var currentPlayer = playersOnCourtList.get(p)
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
            playersOnCourtList.set(p,chosenPlayer)
            alertDialog.dismiss()
            val playersOnCourtCopy = ArrayList(playersOnCourtList)
            val playersOnBenchCopy = ArrayList(playersOnBenchList)
            val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                    otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,"sub", currentPlayer, chosenPlayer,null,false)
            actions.push(thisAction)
            onResume()
        })
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)
    }
    //this function will create the action nodes
    fun createAction(p:Int,a:String){

        val playersOnCourtCopy = ArrayList(playersOnCourtList)
        val playersOnBenchCopy = ArrayList(playersOnBenchList)
        val thisAction = ActionNode(playersOnCourtCopy, playersOnBenchCopy, libPlayer, myTeamScore,
                otherTeamScore, myTeamGames, otherTeamGames, serveIndicator, mySubsUsed, rotation,myTO,otherTeamTO,a, playersOnCourtList[p], null,playersOnCourtList[0],serveAttempt)
        actions.push(thisAction)
        onResume()
    }
    //this function is for rotating the players on the court
    fun rotatePlayers(){
        if(!libPlayer.equals(intialLibPlayer)){
            val li = LayoutInflater.from(this)
            val promptsView = li.inflate(R.layout.lib_rotate_out, null)
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setView(promptsView)
            // set dialog message
            alertDialogBuilder.setTitle("NOTICE!!!")
            //alertDialogBuilder.setIcon(R.drawable)
            // create alert dialog
            val alertDialog = alertDialogBuilder.create()
            val mButton = promptsView.findViewById<Button>(R.id.libRotateOkBtn) as Button
            mButton.setOnClickListener({
                alertDialog.dismiss()
            })
            alertDialog.show()
            alertDialog.setCanceledOnTouchOutside(false)
            var currentPlayer = libPlayer
            for(i in 0..playersOnCourtList.size-1){
                if(playersOnCourtList[i].equals(intialLibPlayer)){
                    libPlayer = intialLibPlayer
                    playersOnCourtList.set(i,currentPlayer)
                    break;
                }
            }
        }
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

    //this function will go through the stack and add each stat to the player
    //MAY DO THIS AT THE END OF EACH GAME RATHER THAN EACH MATCH
    fun recordStats(stack:ActionStack){

        while(stack.peek()!=null){
            val currentAction = stack.pop()
            if(currentAction.activePlayer!=null){
                val currentActivePlayer = currentAction.activePlayer
                val currentStat = currentAction.action
                for(i in 0..playersOnTeamList.size-1) {
                    if (playersOnTeamList.get(i).getID() == currentActivePlayer.getID()) {
                        when (currentStat) {
                            "ace" -> playersOnTeamList.get(i).incAces()
                            "missServe" -> playersOnTeamList.get(i).incMissedServes()
                            "serRecErr" -> playersOnTeamList.get(i).incSerRecErr()
                            "kill" -> playersOnTeamList.get(i).incKills()
                            "attackErr" -> playersOnTeamList.get(i).incAttackErrors()
                            "passRecErr" -> playersOnTeamList.get(i).incPassRecErr()
                            "dig" -> playersOnTeamList.get(i).incDigs()
                            "assist" -> playersOnTeamList.get(i).incAssists()
                            "ballErr" -> playersOnTeamList.get(i).incBallErrors()
                            "block" -> playersOnTeamList.get(i).incSoloBlock()
                            "blockAssist" -> playersOnTeamList.get(i).incBlockAssists()
                            "blockErr" -> playersOnTeamList.get(i).incBlockErrors()
                            "hitInPlay"-> playersOnTeamList.get(i).incHitsInPlay()
                        }
                        if(serveAttempt){
                            val currentServer = currentAction.getServer()
                            for(j in 0..playersOnTeamList.size-1){
                                if(playersOnTeamList.get(j).getID()==currentServer.getID()) {
                                    playersOnTeamList.get(j).incServeAttempts()
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        for(i in 0..playersOnTeamList.size-1) {
            playersOnTeamList.get(i).setTotalAttacks(playersOnTeamList.get(i).getKills()+playersOnTeamList.get(i).getAttackErrors()+playersOnTeamList.get(i).getHitsInPlay())
            val totalPassErr = playersOnTeamList.get(i).getReceptionErrors().toDouble()
            val totalPass1 = playersOnTeamList.get(i).getPass1().toDouble()
            val totalPass2 = playersOnTeamList.get(i).getPass2().toDouble()
            val totalPass3 = playersOnTeamList.get(i).getPass3().toDouble()
            val total = totalPass1+totalPass2+totalPass3+totalPassErr
            val avg=0.0
            if(total>0) {
                val avg = ((totalPass3 * 3) + (totalPass2 * 2) + (totalPass1)) / total
            }
            playersOnTeamList.get(i).setPassPercentage(avg)
            val totalKills = playersOnTeamList.get(i).getKills()
            val totalHitsInPlay = playersOnTeamList.get(i).getHitsInPlay()
            val totalAttackErrors = playersOnTeamList.get(i).getAttackErrors()
            playersOnTeamList.get(i).setTotalAttacks(totalKills+totalHitsInPlay+totalAttackErrors)
            val totalAttacks = playersOnTeamList.get(i).getTotalAttacks().toDouble()
            val killsMinusErrors = (totalKills-totalAttackErrors).toDouble()
            playersOnTeamList.get(i).setHittingPercentage(killsMinusErrors/totalAttacks)

            //playersOnTeamList.get(i).setServeAttempts(playersOnTeamList.get(i).getAces()+playersOnTeamList.get(i).getMissedServes())
        }
        //RETURN to endMatch Method so that the players stats can each be inserted to the DB

    }
}