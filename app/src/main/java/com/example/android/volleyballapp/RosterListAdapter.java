package com.example.android.volleyballapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Dave on 3/23/2018.
 */

/*
This is the adapter view for the roster Team name list view.
 */


public class RosterListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;


    public RosterListAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.team_roster_list_item, null);
        }


        //this should check to see if the view has been selected and then change its background color
        //however, the onItemClick listener in the activity is not able to recognize when a row has
        //been clicked. Thus, it currently does nothing
        if(view.isSelected()){
            view.setBackgroundColor(Color.BLUE);
        }


        //Handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.teamNameRosterList);
        listItemText.setText(list.get(position));


        //Handle buttons and add onClickListeners

        /* TEST FOR CROWDED LAYOUT
        Button viewEditTeamBtn = (Button) view.findViewById(R.id.viewEditTeamButton);
        Button scheduleBtn = (Button) view.findViewById(R.id.scheduleButton);
        Button statsBtn = (Button) view.findViewById(R.id.statsButton);
        */


        //These are the buttonClick listeners that will be able to do things when the View/Edit..etc
        //buttons are clicked.
        listItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Toast.makeText(context,"HEY List Item Text",Toast.LENGTH_SHORT).show();

            }
        });
        /*
        viewEditTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                //or some other task
                Toast.makeText(context,"HEY View edit team btn",Toast.LENGTH_SHORT).show();

            }
        });
        scheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something

            }
        });

        statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
            }
        });
        */
        return view;
    }
}
