package com.example.driveallnight.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.driveallnight.R;
import com.example.driveallnight.adapters.EpisodesAdapter;
import com.example.driveallnight.databinding.ActivityEpisodesBinding;
import com.example.driveallnight.models.RssData;
import com.prof.rssparser.Article;
import com.prof.rssparser.Channel;
import com.prof.rssparser.Image;
import com.prof.rssparser.ItunesArticleData;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class EpisodesActivity extends AppCompatActivity
{

    private static final String TAG = "EpisodesActivity";
    private Context context = EpisodesActivity.this;
    ArrayList<RssData> rssDataArrayList = new ArrayList<>();
    private ProgressDialog progressDialog;
    RssData rssData;
    EpisodesAdapter episodesAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("URL");

        progressDialog = new ProgressDialog(context);
        listView = (ListView) findViewById(R.id.list_episodes);

        fetchRSSFeeds(url);



    }

    private void fetchRSSFeeds(String URL)
    {
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        Parser parser = new Parser.Builder()
                .charset(Charset.forName("ISO-8859-7"))
                .build();

        parser.onFinish(new OnTaskCompleted() {

            @Override
            public void onTaskCompleted(@NonNull Channel channel)
            {
                progressDialog.dismiss();

                Log.d(TAG, "onTaskCompleted: " + channel.getTitle());
                Log.d(TAG, "onTaskCompleted: " + channel.getDescription());

                Image image = channel.getImage();

                Log.d(TAG, "onTaskCompleted: " + image.getUrl());

                List<Article> articles = channel.getArticles();

                Log.d(TAG, "onTaskCompleted: Size: " + articles.size());

                for (Article article : articles)
                {
                    ItunesArticleData itunesArticleData = article.getItunesArticleData();

                    rssData = new RssData(article.getPubDate(), article.getTitle(), itunesArticleData.getSummary(),
                            itunesArticleData.getDuration(), article.getAudio());
                    rssDataArrayList.add(rssData);

                    Log.d(TAG, "onTaskCompleted: Duration: " + itunesArticleData.getDuration());
                    Log.d(TAG, "onTaskCompleted: Summary: " + itunesArticleData.getSummary());
                }

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run() {
                        episodesAdapter = new EpisodesAdapter(EpisodesActivity.this, rssDataArrayList);
                        listView.setAdapter(episodesAdapter);
                        Log.d(TAG, "DataList: " + rssDataArrayList.size());
                    }
                });
            }

            public void onError(Exception e)
            {
                progressDialog.dismiss();
                Log.d(TAG, "onError: " + e.getMessage());
            }
        });
        parser.execute(URL);
    }
}
