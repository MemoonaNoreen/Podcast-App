package com.example.driveallnight.adapters;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.driveallnight.R;
import com.example.driveallnight.models.RssData;

import java.io.IOException;
import java.util.ArrayList;

public class EpisodesAdapter extends ArrayAdapter<RssData>
{
    private ArrayList<RssData> rssDataArrayList;
    private Activity context;
    MediaPlayer mediaPlayer = new MediaPlayer();
    boolean wasPlaying = false;
    LinearLayout laySeekbar;
    private Handler mHandler = new Handler();
    SeekBar seekBar;
    ImageView imgPlayPause;

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
        imgPlayPause = (ImageView) rowView.findViewById(R.id.imgPlayPause);
        laySeekbar = (LinearLayout) rowView.findViewById(R.id.lay_seekbar);
        seekBar = (SeekBar) rowView.findViewById(R.id.seekbar);
        TextView txtRemaingTime = (TextView) rowView.findViewById(R.id.txtRemainTime);

        String day = rssDataArrayList.get(position).getDay();
        String title = rssDataArrayList.get(position).getTitle();
        String description = rssDataArrayList.get(position).getDescription();
        String duration = rssDataArrayList.get(position).getDuration();
        String url = rssDataArrayList.get(position).getAudio();
        String totalDuration = durationFormat(duration);

        txtDay.setText(day);
        txtTitle.setText(title);
        txtDescription.setText(description);
        txtDuration.setText(totalDuration);



        imgPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAudio(url);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

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

    public void playAudio(String audioUrl)
    {
        try
        {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                clearMediaPlayer();
                seekBar.setProgress(0);
                wasPlaying = true;
                imgPlayPause.setImageResource(R.drawable.ic_play);
            }

            if (!wasPlaying)
            {
                if (mediaPlayer == null)
                {
                    mediaPlayer = new MediaPlayer();
                }

                imgPlayPause.setImageResource(R.drawable.ic_pause);

                mediaPlayer.setAudioAttributes( new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());
                mediaPlayer.setDataSource(context, Uri.parse(audioUrl));
                mediaPlayer.prepare();
                seekBar.setMax(mediaPlayer.getDuration());

                mediaPlayer.start();
                updateProgressBar();
            }

            wasPlaying = false;

        }

        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateProgressBar()
    {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable()
    {
        public void run()
        {
            int currentPosition = mediaPlayer.getCurrentPosition();
            int total = mediaPlayer.getDuration();


            while (mediaPlayer != null && mediaPlayer.isPlaying() && currentPosition < total) {
                try {
                    Thread.sleep(100);
                    currentPosition = mediaPlayer.getCurrentPosition();
                } catch (InterruptedException e) {
                    return;
                } catch (Exception e) {
                    return;
                }

                seekBar.setProgress(currentPosition);
            }
        }
    };


    private void clearMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;

    }

}
