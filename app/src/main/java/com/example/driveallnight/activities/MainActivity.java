package com.example.driveallnight.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.driveallnight.databinding.ActivityMainBinding;

import com.example.driveallnight.models.ItunesArticleData;
import com.example.driveallnight.models.RssData;
import com.prof.rssparser.Article;
import com.prof.rssparser.Channel;
import com.prof.rssparser.Image;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private Context context = MainActivity.this;

    private ActivityMainBinding binding;
    RssData rssData;
    ArrayList<RssData>rssDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.lyDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String url = "https://feeds.soundcloud.com/users/soundcloud:users:561213786/sounds.rss";
                startEpisodesActivity(url);
            }
        });

        binding.lyTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.lyShutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.lyPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String url = "https://www.patreon.com/songsoftoriamos";
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void startEpisodesActivity(String url)
    {
        Intent intent = new Intent(context, EpisodesActivity.class);
        intent.putExtra("URL", url);
        startActivity(intent);
    }

}