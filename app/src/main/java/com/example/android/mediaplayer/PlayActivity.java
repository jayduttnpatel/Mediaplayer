package com.example.android.mediaplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    private TextView duration_textview;
    private int progress = 0;
    private Thread thread=null;
    private Runnable runnable;
    private int min=0;
    private int sec=0;
    private AudioManager mAudiomanager;
    private SeekBar seekbar;
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChange;
    private int songReference;
    private String name="Song Name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);


        Intent intent=getIntent();
        songReference=intent.getIntExtra("SongReference",R.raw.pele);
        name=intent.getStringExtra("SongName");
        ((TextView)findViewById(R.id.song_name)).setText(name);

        mAudiomanager=(AudioManager)getSystemService(AUDIO_SERVICE);
        mAudioFocusChange=new AudioManager.OnAudioFocusChangeListener()
        {
            @Override
            public void onAudioFocusChange(int state) {
                if(state==AudioManager.AUDIOFOCUS_GAIN)
                {
                    startSong();
                }
                else if(state==AudioManager.AUDIOFOCUS_LOSS)
                {
                    mMediaPlayer.pause();
                }
                else if(state==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)
                {
                    mMediaPlayer.pause();
                }
            }
        };

        duration_textview=(TextView)findViewById(R.id.show_duratiom_textview);
        seekbar=(SeekBar)findViewById(R.id.show_duration_seekwbar);
        final android.os.Handler handler=new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {
                duration_textview.setText(min+":"+sec);
                seekbar.setProgress(progress);
            }
        };

        runnable=new Runnable(){
            @Override
            public void run() {
                while(mMediaPlayer!=null && mMediaPlayer.isPlaying()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Toast.makeText(PlayActivity.this,"Some error occured",Toast.LENGTH_SHORT).show();
                    };
                    if(mMediaPlayer!=null && mMediaPlayer.isPlaying()) {
                        int temp;
                        synchronized (this) {
                            progress = (int) (mMediaPlayer.getCurrentPosition() * 100.0 / mMediaPlayer.getDuration() * 1.0);
                            temp = mMediaPlayer.getCurrentPosition();
                        }
                        temp /= 1000;
                        min = temp / 60;
                        sec = temp % 60;
                        handler.sendEmptyMessage(0);
                    }
                }
            }
        };
        //add onclick listener to the play button
        ((ImageView)findViewById(R.id.play)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(thread==null) {
                    startSong();
                }
            }
        });

        ((ImageView)findViewById(R.id.pause)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaPlayer.pause();
                thread=null;
            }
        });

        ((ImageView)findViewById(R.id.relay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMediaPlayer!=null && mMediaPlayer.isPlaying()) {
                    int temp = mMediaPlayer.getCurrentPosition();
                    temp -= 5000;
                    if (temp < 0)
                        temp = 0;
                    mMediaPlayer.seekTo(temp);
                }
                else
                {
                    Toast.makeText(PlayActivity.this,"App is not playing any song",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ((ImageView)findViewById(R.id.forward)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMediaPlayer.isPlaying()) {
                    int temp = mMediaPlayer.getCurrentPosition();
                    temp += 5000;
                    if (temp > mMediaPlayer.getDuration())
                        temp = mMediaPlayer.getDuration();
                    mMediaPlayer.seekTo(temp);
                }
                else
                {
                    Toast.makeText(PlayActivity.this,"App is not playing any song",Toast.LENGTH_SHORT).show();
                }
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.seekTo((int)(mMediaPlayer.getDuration()*seekBar.getProgress()/100.0));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMemory();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.pause();
    }

    @Override
    protected void onStart() {
        int state=mAudiomanager.requestAudioFocus(mAudioFocusChange,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);
        if(state==AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            super.onStart();
            startSong();
        }
        else
        {
            Toast.makeText(PlayActivity.this,"Can not gain audio focus",Toast.LENGTH_SHORT).show();
        }
    }

    private void startSong()
    {
        if(mMediaPlayer==null) {
            mMediaPlayer=MediaPlayer.create(this,songReference);
        }
        mMediaPlayer.start();
        thread=new Thread(runnable);
        thread.start();
    }

    synchronized private void releaseMemory()
    {
        mMediaPlayer.pause();
        mMediaPlayer.release(); //problem is occuring
        mMediaPlayer=null;
        mAudiomanager.abandonAudioFocus(mAudioFocusChange);
    }
}
