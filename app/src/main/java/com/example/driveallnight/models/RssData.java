package com.example.driveallnight.models;

import android.app.Application;

public class RssData extends Application
{
    public String day, title, description, duration, audio;

    public RssData(String day, String title, String audio) {
        this.day = day;
        this.title = title;
        this.audio = audio;
    }

    public RssData(String day, String title, String description, String duration, String audio) {
        this.day = day;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.audio = audio;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}
