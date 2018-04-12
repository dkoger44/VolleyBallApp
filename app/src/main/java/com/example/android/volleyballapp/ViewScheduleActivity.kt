package com.example.android.volleyballapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView

class ViewScheduleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_schedule)

        val schedule = findViewById<CalendarView>(R.id.calendarView) as CalendarView
        schedule.minDate = 1514782800000

        //would like to change the color of a specific date.
        //could use array of dates to change all of the days to a specific color
        //if a day was in the array (i.e. a game was scheduled) then the color
        //of the day would be different then the standard days.
        schedule?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var game = schedule.getDate()

        }
    }
}
