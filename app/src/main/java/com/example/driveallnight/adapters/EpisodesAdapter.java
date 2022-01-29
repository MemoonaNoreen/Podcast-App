package com.example.driveallnight.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        TextView txtDuration = (TextView) rowView.findViewById(R.id.txtDuration);
        ImageView btnPlay = (ImageView) rowView.findViewById(R.id.btn_play);
        ImageView btnPause = (ImageView) rowView.findViewById(R.id.btn_pause);
        LinearLayout laySeekbar = (LinearLayout) rowView.findViewById(R.id.lay_seekbar);

        String day = rssDataArrayList.get(position).getDay();
        String title = rssDataArrayList.get(position).getTitle();
        String description = rssDataArrayList.get(position).getDescription();
        String duration = rssDataArrayList.get(position).getDuration();
        String totalDuration = durationFormat(duration);

        txtDay.setText(day);
        txtTitle.setText(title);
        txtDescription.setText(description);
        txtDuration.setText(totalDuration);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                btnPause.setVisibility(View.VISIBLE);
                btnPlay.setVisibility(View.GONE);
                laySeekbar.setVisibility(View.VISIBLE);
                txtDuration.setVisibility(View.GONE);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                btnPause.setVisibility(View.GONE);
                btnPlay.setVisibility(View.VISIBLE);
                laySeekbar.setVisibility(View.GONE);
                txtDuration.setVisibility(View.VISIBLE);
            }
        });

        return rowView;
    }

    private String durationFormat(String duration)
    {
        String[] time = duration.split(":");

        String hour = time[0];
        String min = time[1];
        String sec = time[2];
        String result = "00";

        if (hour.contains("00"))
            result = min + " min " + sec + " sec";

        else if (sec.contains("00"))
        {
            if (hour.contains("00"))
                result = min + " min";
            else
                result = hour + " hour " + min + " min";
        }

        else
            result = hour + " hour " + min + " min " + sec + " sec";

        return result;
    }
}
