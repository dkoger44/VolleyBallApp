package com.example.android.volleyballapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SelectPlayerRosterListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> ids = new ArrayList<String>();
    private Context context;

    public SelectPlayerRosterListAdapter(ArrayList<String> list,ArrayList<String> ids, Context context) {
        this.list = list;
        this.ids = ids;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public Object getItem(int pos) {
        return ids.get(pos);
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
            view = inflater.inflate(R.layout.select_player_list_item, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView) view.findViewById(R.id.playerNameRosterList);
        listItemText.setText(list.get(position));
        final TextView listItemID = (TextView) view.findViewById(R.id.playerID);
        listItemID.setText(ids.get(position));

        Button editPlayerBtn = (Button) view.findViewById(R.id.editPlayerButton);
        Button playerStatsBtn = (Button) view.findViewById(R.id.playerStatsButton);

        //These are the buttonClick listeners that will be able to do things when the View/Edit..etc
        //buttons are clicked.
        listItemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"HEY List Item Text",Toast.LENGTH_SHORT).show();

            }
        });

        editPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating team object based on team name
                //passing team object to AddPlayerActivity to practice serializable classes
                String playerID = listItemID.getText().toString();


                Intent editPlayerDetails = new Intent(v.getContext(), editPlayerDetailsActivity.class);
                editPlayerDetails.putExtra("PlayerID",playerID);
                v.getContext().startActivity(editPlayerDetails);

            }
        });

        playerStatsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
