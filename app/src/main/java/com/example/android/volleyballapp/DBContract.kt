package com.example.android.volleyballapp
import android.provider.BaseColumns
/*
DELETE THIS FILE
DELETE THIS FILE
DELETE THIS FILE
DELETE THIS FILE
DELETE THIS FILE
DELETE THIS FILE
DELETE THIS FILE
 */

object DBContract {
    object FeedTeam : BaseColumns{
        const val TABLE_NAME = "team"
        const val COLUMN_NAME = "name"
        const val COLUMN_TYPE = "type"
        const val COLUMN_SEASON = "season"
    }
    private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${FeedTeam.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${FeedTeam.COLUMN_NAME} TEXT," +
                    "${FeedTeam.COLUMN_TYPE} TEXT," +
                    "${FeedTeam.COLUMN_SEASON} TEXT)"
    private const val SQL_DELTE_ENTRIES = "DROP TABLE IF EXISTS ${FeedTeam.TABLE_NAME}"
}