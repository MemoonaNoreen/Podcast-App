package com.example.driveallnight.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driveallnight.R;
import com.example.driveallnight.models.RssData;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.driveallnight.adapters.EpisodesRecyclerAdapter.context;
import static com.example.driveallnight.adapters.EpisodesRecyclerAdapter.itemsViewHolder;
import static com.example.driveallnight.adapters.EpisodesRecyclerAdapter.mediaPlayer;
import static com.example.driveallnight.adapters.EpisodesRecyclerAdapter.rssDataArrayList;

public class EpisodesRecyclerAdapter extends RecyclerView.Adapter<EpisodesitemsViewHolder> implements Handler.Callback
{
    public static ArrayList<RssData> rssDataArrayList;
    private static final int MSG_UPDATE_SEEK_BAR = 1845;
    public static MediaPlayer mediaPlayer;
    private static Handler uiUpdateHandler;
    public static int playingPosition;
    public static EpisodesitemsViewHolder itemsViewHolder;
    public static Activity context;

    public EpisodesRecyclerAdapter(Activity context, ArrayList<RssData> rssDataArrayList)
    {
        this.rssDataArrayList = rssDataArrayList;
        this.context = context;
        this.playingPosition = -1;
        uiUpdateHandler = new Handler(this);
    }

    @NonNull
    @Override
    public EpisodesitemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new EpisodesitemsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.episodes_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodesitemsViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        String day = rssDataArrayList.get(position).getDay();
        String title = rssDataArrayList.get(position).getTitle();
        String description = rssDataArrayList.get(position).getDescription();
        String duration = rssDataArrayList.get(position).getDuration();
        String url = rssDataArrayList.get(position).getAudio();
        String totalDuration = durationFormat(duration);

        holder.txtDay.setText(day);
        holder.txtTitle.setText(title);
        holder.txtDescription.setText(description);
        holder.txtDuration.setText(totalDuration);

        if (position == playingPosition)
        {
            itemsViewHolder = holder;
            // this view holder corresponds to the currently playing audio cell
            // update its view to show playing progress
            updatePlayingView();
        }
        else
            {
            // and this one corresponds to non playing
            updateNonPlayingView(holder);
        }

        holder.imgPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (position == EpisodesRecyclerAdapter.playingPosition)
                {
                    // toggle between play/pause of audio
                    if (mediaPlayer.isPlaying())
                    {
                        mediaPlayer.pause();
                    } else {
                        mediaPlayer.start();
                    }
                }
                else
                {
                    // start another audio playback
                    EpisodesRecyclerAdapter.playingPosition = position;
                    if (mediaPlayer != null)
                    {
                        if (null != itemsViewHolder)
                        {
                            EpisodesRecyclerAdapter.updateNonPlayingView(itemsViewHolder);
                        }
                        mediaPlayer.release();
                    }

                    itemsViewHolder.startMediaPlayer(url);
                }
                EpisodesRecyclerAdapter.updatePlayingView();
            }
        });
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

    @Override
    public int getItemCount()
    {
        return rssDataArrayList.size();
    }

    @Override
    public boolean handleMessage(Message message)
    {
        switch (message.what)
        {
            case MSG_UPDATE_SEEK_BAR:
                {
                itemsViewHolder.seekBar.setProgress(mediaPlayer.getCurrentPosition());
                uiUpdateHandler.sendEmptyMessageDelayed(MSG_UPDATE_SEEK_BAR, 100);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onViewRecycled(EpisodesitemsViewHolder holder)
    {
        super.onViewRecycled(holder);
        if (playingPosition == holder.getAdapterPosition())
        {
            // view holder displaying playing audio cell is being recycled
            // change its state to non-playing
            updateNonPlayingView(itemsViewHolder);
            itemsViewHolder = null;
        }
    }

    public static void updateNonPlayingView(EpisodesitemsViewHolder holder)
    {
        if (holder == itemsViewHolder)
        {
            uiUpdateHandler.removeMessages(MSG_UPDATE_SEEK_BAR);
        }
        holder.seekBar.setEnabled(false);
        holder.seekBar.setProgress(0);
        holder.txtTimeLeft.setVisibility(View.VISIBLE);
        holder.imgPlayPause.setImageResource(R.drawable.ic_play);
    }

    public static void updatePlayingView()
    {
        itemsViewHolder.seekBar.setMax(mediaPlayer.getDuration());
        itemsViewHolder.seekBar.setProgress(mediaPlayer.getCurrentPosition());
        itemsViewHolder.seekBar.setEnabled(true);
        itemsViewHolder.seekBar.setVisibility(View.VISIBLE);
        itemsViewHolder.txtTimeLeft.setVisibility(View.VISIBLE);
        itemsViewHolder.txtDuration.setVisibility(View.GONE);

        if (mediaPlayer.isPlaying()) {
            uiUpdateHandler.sendEmptyMessageDelayed(MSG_UPDATE_SEEK_BAR, 100);
            itemsViewHolder.imgPlayPause.setImageResource(R.drawable.ic_pause);
        } else {
            uiUpdateHandler.removeMessages(MSG_UPDATE_SEEK_BAR);
            itemsViewHolder.imgPlayPause.setImageResource(R.drawable.ic_play);
        }
    }

    public void stopPlayer()
    {
        if (null != mediaPlayer) {
            itemsViewHolder.releaseMediaPlayer();
        }
    }
}

class EpisodesitemsViewHolder extends RecyclerView.ViewHolder implements SeekBar.OnSeekBarChangeListener
{
    SeekBar seekBar;
    ImageView imgPlayPause;
    TextView txtTimeLeft, txtDuration, txtDay, txtTitle, txtDescription;

    public EpisodesitemsViewHolder(@NonNull View itemView)
    {
        super(itemView);

        txtDay = (TextView) itemView.findViewById(R.id.txtDay);
        txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
        txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
        txtDuration = (TextView) itemView.findViewById(R.id.txtDuration);
        imgPlayPause = (ImageView) itemView.findViewById(R.id.imgPlayPause);
        seekBar = (SeekBar) itemView.findViewById(R.id.seekbar);
        txtTimeLeft = (TextView) itemView.findViewById(R.id.txtRemainTime);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b)
    {
        if (b)
        {
            mediaPlayer.seekTo(i);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void startMediaPlayer(String audioUrl)
    {
        mediaPlayer.setAudioAttributes( new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());
        try {
            mediaPlayer.setDataSource(context, Uri.parse(audioUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp) {
                releaseMediaPlayer();
            }
        });
        mediaPlayer.start();
    }

    public void releaseMediaPlayer()
    {
        if (null != itemsViewHolder) {
            EpisodesRecyclerAdapter.updateNonPlayingView(itemsViewHolder);
        }
        mediaPlayer.release();
        mediaPlayer = null;
        EpisodesRecyclerAdapter.playingPosition = -1;
    }
}


