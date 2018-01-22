package com.example.android.mediaplayer;

/**
 * Created by jaydutt on 14/01/2018.
 */

public class Song
{
    private String name;
    private int songReference;
    private String Details="No Detail Available";
    public Song(String s,int reference)
    {
        name=s;
        songReference=reference;
    }
    public Song(String s,String detail,int reference)
    {
        name=s;
        Details=detail;
        songReference=reference;
    }
    String getName()
    {
        return name;
    }
    int getSongReference()
    {
        return songReference;
    }
    String getDetails()
    {
        return Details;
    }
}
