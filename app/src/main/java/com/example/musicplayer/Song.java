package com.example.musicplayer;

import android.net.Uri;

public class Song {
    String songTitle;
    String songArtist;
    String songAlbum;
    //String songYear;
    Uri uri;
    Uri songArtwork;
    int size;
    int duration;

    public Song(String songTitle, String songArtist, String songAlbum, Uri uri, Uri songArtwork, int duration) {
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.songAlbum = songAlbum;
        this.uri = uri;
        this.songArtwork = songArtwork;
        //this.size = size;
        this.duration = duration;
    }
    //getter and setters

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    public String getSongAlbum() {
        return songAlbum;
    }

    public void setSongAlbum(String songAlbum) {
        this.songAlbum = songAlbum;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Uri getSongArtwork() {
        return songArtwork;
    }

    public void setSongArtwork(Uri songArtwork) {
        this.songArtwork = songArtwork;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
