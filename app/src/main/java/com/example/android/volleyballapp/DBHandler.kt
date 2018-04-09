package com.example.android.volleyballapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DATABASE_NAME = "VolleyBall"
val TEAM_TABLE_NAME = "teams"
val PLAYER_TABLE_NAME = "player"
val COL_TEAM_NAME = "name"
val COL_TEAM_TYPE = "type"
val COL_TEAM_SEASON = "season"
val COL_PLAYER_ID = "id"
val COL_PLAYER_FIRST_NAME = "fName"
val COL_PLAYER_LAST_NAME = "lName"
val COL_PLAYER_JERSEY = "jerseyNum"
val COL_PLAYER_GRADE = "grade"
val COL_PLAYER_TEAM_NAME = "teamName"
val COL_ID = "id"

class DBHandler (var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null,1){
    override fun onCreate(db: SQLiteDatabase?){
        val createTeamTable = "CREATE TABLE "+ TEAM_TABLE_NAME + " (" +
                COL_TEAM_NAME +" VARCHAR(50) PRIMARY KEY," +
                COL_TEAM_TYPE + " VARCHAR(50)," +
                COL_TEAM_SEASON + " VARCHAR(4))"
//COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
        db?.execSQL(createTeamTable)

        val createPlayerTable = "CREATE TABLE "+ PLAYER_TABLE_NAME + " (" +
                COL_PLAYER_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_PLAYER_FIRST_NAME +" VARCHAR(50) NOT NULL," +
                COL_PLAYER_LAST_NAME +" VARCHAR(50) NOT NULL," +
                COL_PLAYER_JERSEY +" VARCHAR(2)," +
                COL_PLAYER_GRADE +" VARCHAR(10)," +
                COL_PLAYER_TEAM_NAME +" VARCHAR(50), " +
                "FOREIGN KEY(" + COL_PLAYER_TEAM_NAME + ") REFERENCES "+
                TEAM_TABLE_NAME +"(" + COL_TEAM_NAME + "))"

        db?.execSQL(createPlayerTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        //make drop table statments
        // recreate table names
        val dropTEAMTable = "DROP TABLE "+ TEAM_TABLE_NAME
        val dropPlayerTable = "DROP TABLE " + PLAYER_TABLE_NAME
        db?.execSQL(dropTEAMTable)
        db?.execSQL(dropPlayerTable)

        val createTeamTable = "CREATE TABLE "+ TEAM_TABLE_NAME + " (" +
                COL_TEAM_NAME +" VARCHAR(50) PRIMARY KEY," +
                COL_TEAM_TYPE + " VARCHAR(50)," +
                COL_TEAM_SEASON + " VARCHAR(4))"

        db?.execSQL(createTeamTable)

        val createPlayerTable = "CREATE TABLE "+ PLAYER_TABLE_NAME + " (" +
                COL_PLAYER_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_PLAYER_FIRST_NAME +" VARCHAR(50) NOT NULL," +
                COL_PLAYER_LAST_NAME +" VARCHAR(50) NOT NULL," +
                COL_PLAYER_JERSEY +" VARCHAR(2)," +
                COL_PLAYER_GRADE +" VARCHAR(10)," +
                COL_PLAYER_TEAM_NAME +" VARCHAR(50), " +
                "FOREIGN KEY(" + COL_PLAYER_TEAM_NAME + ") REFERENCES "+
                TEAM_TABLE_NAME +"(" + COL_TEAM_NAME + "))"

        db?.execSQL(createPlayerTable)

    }

    //inserting value into Team table
    fun insertTeamData(team: Team){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_TEAM_NAME,team.getName())
        cv.put(COL_TEAM_TYPE,team.getType())
        cv.put(COL_TEAM_SEASON,team.getSeason())
        var result = db.insert(TEAM_TABLE_NAME,null, cv)
        if(result == -1.toLong())
            Toast.makeText(context,"Team Name Already Exists",Toast.LENGTH_LONG).show()
        else
            Toast.makeText(context,"Success", Toast.LENGTH_SHORT).show()
    }

    //inserting value into Player Table
    fun insertPlayerData(team: Team,player: Player){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_PLAYER_FIRST_NAME, player.getFirstName())
        cv.put(COL_PLAYER_LAST_NAME, player.getLastName())
        cv.put(COL_PLAYER_JERSEY, player.getNumber())
        cv.put(COL_PLAYER_GRADE, player.getGradeLevel())
        cv.put(COL_PLAYER_TEAM_NAME, team.getName())

        var result = db.insert(PLAYER_TABLE_NAME, null, cv)
        if(result == -1.toLong())
            Toast.makeText(context,"ERROR ADDING PLAYER", Toast.LENGTH_LONG).show()
        else
            Toast.makeText(context,"Player added to team", Toast.LENGTH_SHORT).show()
    }

    //getting all entries in Team table
    fun readTeamData() :MutableList<Team>{
        var list : MutableList<Team> = ArrayList()

        val db = this.readableDatabase

        //val query = "Select * from " + TABLE_NAME
        //val result = db.rawQuery(query,null)
        //projection will be the array of columns that will be queried
        val projection = arrayOf(COL_TEAM_NAME, COL_TEAM_TYPE, COL_TEAM_SEASON)

        val cursor = db.query("teams",projection,null,null,null,null,null)
        if(cursor.moveToFirst()){
            do{
                //var id = cursor.getString(0).toString()
                var n = cursor.getString(0).toString()
                var t = cursor.getString(1).toString()
                var s = cursor.getString(2).toString()
                var team = Team(n,t,s)
                list.add(team)
            }while(cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    //getting all players of a team from the Player Table
    fun readPlayerData(team: Team) :MutableList<Player>{
        var list : MutableList<Player> = ArrayList()
        val db = this.readableDatabase
        var teamName = arrayOf(team.getName())
        val projection = arrayOf(COL_PLAYER_ID, COL_PLAYER_FIRST_NAME, COL_PLAYER_LAST_NAME, COL_PLAYER_JERSEY, COL_PLAYER_GRADE)

        val cursor = db.query(PLAYER_TABLE_NAME, projection, COL_PLAYER_TEAM_NAME, teamName, null, null, null)
        if(cursor.moveToFirst()){
            do{
                var id = cursor.getString(0).toString()
                var fn = cursor.getString(1).toString()
                var ln = cursor.getString(2).toString()
                var jn = cursor.getString(3).toString()
                var pg = cursor.getString(4).toString()
                var player = Player(id.toInt(),fn,ln,jn.toInt(),pg)
                list.add(player)
            }while(cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }


    fun deleteEntry (n: String){
        val selection = COL_TEAM_NAME+" LIKE "+n
        val db = this.writableDatabase
        var sA = ArrayList<String>()
        sA.add(n)
        Toast.makeText(context,""+n,Toast.LENGTH_SHORT).show()
        db.delete(TEAM_TABLE_NAME, COL_TEAM_NAME+"=?", arrayOf(n.toString()))

    }
}