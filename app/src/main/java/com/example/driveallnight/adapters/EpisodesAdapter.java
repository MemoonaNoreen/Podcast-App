package com.example.driveallnight.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.driveallnight.R;
import com.example.driveallnight.models.RssData;

import java.util.ArrayList;

public class EpisodesAdapter extends ArrayAdapter<RssData>
{
    private ArrayList<RssData> rssDataArrayList;
    private Activity context;

    public EpisodesAdapter(Activity context, ArrayList<RssData> rssDataArrayList)
    {
        super(context, R.layout.list_item_episodes, rssDataArrayList);

        this.rssDataArrayList = rssDataArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        Log.d("TEST", "Adapter: " + rssDataArrayList.size());
        return rssDataArrayList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item_episodes, null, true);

        TextView txtDay = (TextView) rowView.findViewById(R.id.txtDay);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) rowView.findViewById(R.id.txtDescription);

        String day = rssDataArrayList.get(position).getDay();
        String title = rssDataArrayList.get(position).getTitle();
        String description = rssDataArrayList.get(position).getDescription();

        Log.d("TEST", "Text:" + day + title + description);

        txtDay.setText(day);
        txtTitle.setText(title);
        txtDescription.setText(description);

        return rowView;
    }
}
