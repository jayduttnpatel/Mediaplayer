package com.example.android.mediaplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Song> songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        songList=new ArrayList<Song>();
        songList.add(new Song("Ain't No Montain High enough","Guardian of the Galaxy",R.raw.aint_no_mountain_high_enough));
        songList.add(new Song("Wind of my Soul","Gifted",R.raw.gifted));
        songList.add(new Song("Getting stronger day by day","Diamond no ace",R.raw.diamond));
        songList.add(new Song("Faded",R.raw.faded));
        songList.add(new Song("You'r in the Army now",R.raw.in_the_army_now));
        songList.add(new Song("Keep up",R.raw.keep_up));
        songList.add(new Song("Lean On",R.raw.lean_on));
        songList.add(new Song("Pele","PELE Birth of the legend",R.raw.pele));
        songList.add(new Song("War",R.raw.war));
        songList.add(new Song("See you again","Fast and Furious",R.raw.see_you_again_feat));
        songList.add(new Song("You Belong with me",R.raw.you_belong_with_me));

        SongAdapter adapter = new SongAdapter(this, songList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getBaseContext(), PlayActivity.class);
                intent.putExtra("SongReference", (songList.get(i)).getSongReference());
                intent.putExtra("SongName", (songList.get(i)).getName());
                startActivity(intent);
            }
        });
    }
}

