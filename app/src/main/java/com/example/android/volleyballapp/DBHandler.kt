package com.example.android.volleyballapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.security.AccessControlContext

val DATABASE_NAME = "VolleyBall"
val TABLE_NAME = "teams"
val COL_NAME = "name"
val COL_TYPE = "type"
val COL_SEASON = "season"
val COL_ID = "id"

class DBHandler (var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null,1){
    override fun onCreate(db: SQLiteDatabase?){
        val createTable = "CREATE TABLE "+ TABLE_NAME + " (" +
                COL_NAME +" VARCHAR(50) PRIMARY KEY," +
                COL_TYPE + " VARCHAR(50)," +
                COL_SEASON + " VARCHAR(4))"
//COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
        db?.execSQL(createTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insertData(team: Team){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_NAME,team.getName())
        cv.put(COL_TYPE,team.getType())
        cv.put(COL_SEASON,team.getSeason())
        var result = db.insert(TABLE_NAME,null, cv)
        if(result == -1.toLong())
            Toast.makeText(context,"Team Name Already Exists",Toast.LENGTH_LONG).show()
        else
            Toast.makeText(context,"Success", Toast.LENGTH_SHORT).show()
    }
    fun readData() :MutableList<Team>{
        var list : MutableList<Team> = ArrayList()

        val db = this.readableDatabase

        //val query = "Select * from " + TABLE_NAME
        //val result = db.rawQuery(query,null)
        //projection will be the array of columns that will be queried
        val projection = arrayOf(COL_NAME, COL_TYPE, COL_SEASON)

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
    fun deleteEntry (n: String){
        val selection = COL_NAME+" LIKE "+n
        val db = this.writableDatabase
        var sA = ArrayList<String>()
        sA.add(n)
        Toast.makeText(context,""+n,Toast.LENGTH_SHORT).show()
        db.delete(TABLE_NAME, COL_NAME+"=?", arrayOf(n.toString()))

    }
}