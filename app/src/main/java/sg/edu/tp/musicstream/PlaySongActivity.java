package sg.edu.tp.musicstream;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import sg.edu.tp.musicstream.util.AppUtil;

public class PlaySongActivity extends AppCompatActivity {

    enum LoopState
    {
        LOOP_ALL,
        LOOP_ONCE,
        LOOP_NONE
    }
    enum ShuffleState
    {
        SHUFFLE_OFF,
        SHUFFLE_ON
    }

    private static final String BASE_URL = "https://p.scdn.co/mp3-preview/";
    private MediaPlayer player = null;
    private int musicPosition = 0;
    private Button btnPlayPause = null;
    private TextView txtSongTime = null;
    private SeekBar seekBar = null;

    private ImageButton loopBtn;
    private ImageButton shuffleBtn;

    private String songId = "";
    private String title = "";
    private String artiste = "";
    private String fileLink = "";
    private String coverArt = "";
    private String url = "";

    private LoopState loopState = LoopState.LOOP_NONE;
    private ShuffleState shuffleState = ShuffleState.SHUFFLE_OFF;

    //Timer and handler to handle background updates to UI
    Handler timerHandler;
    Runnable timerRunnable;

    private SongCollection songCollection = SongCollection.getSongCollection();

    protected void preparePlayer() {
        //Create MediaPlayer instance
        try {
            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);       //Set to streaming
            player.setDataSource(url);                                  //Load song from url
            player.prepare();                                           //Prepare player for playback
            seekBar.setMax(player.getDuration());
            seekBar.setVisibility(View.VISIBLE);
            txtSongTime.setVisibility(View.VISIBLE);

        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        btnPlayPause = findViewById(R.id.btnPlayPause);
        loopBtn = findViewById(R.id.loopBtn);
        shuffleBtn = findViewById(R.id.shuffleBtn);
        shuffleBtn.setAlpha(0.5f);
        loopBtn.setAlpha(0.5f);
        //Due to bug in Android, we need to set icon at runtime
        Drawable draw = getResources().getDrawable(android.R.drawable.ic_media_play);
        btnPlayPause.setCompoundDrawablesWithIntrinsicBounds(draw, null, null, null);

        txtSongTime = findViewById(R.id.textSongTime);
        retrieveData();
        displaySong();

        //Setup seekbar
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if(fromUser) {
                    // AppUtil.popMessage(seekBar.getContext(), "Jumped to " + progress);
                    if (player == null) {//Load player and song if not loaded already
                        playOrPauseMusic(btnPlayPause);
                    }
                    player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Setup timer and handler for background updates
        timerHandler = new Handler();
        timerRunnable = new Runnable() {

            @Override
            public void run() {
                update();
                timerHandler.postDelayed(this, 500);       //Only run twice a second
            }
        };
        timerHandler.postDelayed(timerRunnable, 0);

    }

    private void retrieveData()
    {
        Bundle songData = this.getIntent().getExtras(); //Retrieve passed data
        songId = songData.getString("id");
        title = songData.getString("title");
        artiste = songData.getString("artiste");
        fileLink = songData.getString("fileLink");
        coverArt = songData.getString("coverArt");
        url = BASE_URL + fileLink;
    }

    private void displaySong()
    {
        TextView txtTitle = findViewById(R.id.txtSongTitle);
        txtTitle.setText(title);
        TextView txtArtiste = findViewById(R.id.txtArtist);
        txtArtiste.setText(artiste);

        int imageId = AppUtil.getImageIdFromDrawable(this, coverArt);
        ImageView iCoverArt = findViewById(R.id.imgCoverArt);
        iCoverArt.setImageResource(imageId);

    }

    public void playOrPauseMusic(View view)
    {
        if(player == null) {
            //Prepare player
            preparePlayer();
        }

        if(!player.isPlaying()) {
            if(musicPosition > 0)
            {
                player.seekTo(musicPosition);
            }
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                   if(loopState == LoopState.LOOP_ALL)
                   {
                           playNext(view);
                   }
                   else if(loopState == LoopState.LOOP_ONCE)
                   {
                       player.seekTo(0);
                       player.start();
                   }
                   else
                   {
                       endMusic();
                   }
                }
            });
            //Start playback
            player.start();
            //Change button text

            Drawable draw = getResources().getDrawable(android.R.drawable.ic_media_pause);
            btnPlayPause.setCompoundDrawablesWithIntrinsicBounds(draw, null, null, null);
            btnPlayPause.setText("PAUSE");

            //Update title
            setTitle("Now playing : " + title + " by " + artiste);
        }
        else
        {
            pauseMusic();
        }
    }


    public void playPrevious(View view)
    {
        Song nextSong = songCollection.getPrevSong(songId);
        if(nextSong != null)
        {
            songId = nextSong.getId();
            title = nextSong.getTitle();
            artiste = nextSong.getArtist();
            fileLink = nextSong.getFileLink();
            coverArt = nextSong.getCoverArt();

            url = BASE_URL + fileLink;

            displaySong();
            endMusic();
            playOrPauseMusic(view);
        }
    }

    public void playNext(View view)
    {
        Song nextSong;
        if(shuffleState == ShuffleState.SHUFFLE_ON)
        {
            nextSong = songCollection.getNextShuffleSong(songId);
        }
        else {
            nextSong = songCollection.getNextSong(songId);
        }
        if(nextSong != null)
        {
            songId = nextSong.getId();
            title = nextSong.getTitle();
            artiste = nextSong.getArtist();
            fileLink = nextSong.getFileLink();
            coverArt = nextSong.getCoverArt();

            url = BASE_URL + fileLink;

            displaySong();
            endMusic();
            playOrPauseMusic(view);
        }
    }

    private void pauseMusic()
    {
        player.pause();     //Pause song
        musicPosition = player.getCurrentPosition();
        Drawable draw = getResources().getDrawable(android.R.drawable.ic_media_play);
        btnPlayPause.setCompoundDrawablesWithIntrinsicBounds(draw, null, null, null);
        btnPlayPause.setText("PLAY");
    }

    private void endMusic()
    {
        Drawable draw = getResources().getDrawable(android.R.drawable.ic_media_play);
        btnPlayPause.setCompoundDrawablesWithIntrinsicBounds(draw, null, null, null);
        btnPlayPause.setText("PLAY");
        musicPosition = 0;
        setTitle("");
        txtSongTime.setVisibility(View.INVISIBLE);
        //Stop and release player
        if(player != null) {
            player.stop();
            player.release();
            player = null;      //Free memory
        }
    }
    @Override
    public void onBackPressed()
    {
        endMusic();
        super.onBackPressed();  // optional depending on your needs
    }

    public void toggleShuffle(View view)
    {
        if(shuffleState == ShuffleState.SHUFFLE_OFF)
        {
            shuffleState = ShuffleState.SHUFFLE_ON;
            shuffleBtn.setAlpha(1.0f);
        }
        else
        {
            shuffleState = ShuffleState.SHUFFLE_OFF;
            shuffleBtn.setAlpha(0.5f);
        }

    }

    public void toggleLoop(View view)
    {
        if(loopState == LoopState.LOOP_ALL)
        {
            loopState = LoopState.LOOP_ONCE;
            loopBtn.setImageDrawable( getResources().getDrawable(R.drawable.baseline_repeat_one_black_18dp) );
        }
        else if(loopState == LoopState.LOOP_ONCE)
        {
            loopState = LoopState.LOOP_NONE;
            loopBtn.setImageDrawable( getResources().getDrawable(R.drawable.baseline_repeat_black_18dp) );
            loopBtn.setAlpha(0.5f);
        }
        else
        {
            loopState = LoopState.LOOP_ALL;
            loopBtn.setAlpha(1.0f);
        }

    }

    //For background thread to update musicPosition
    public void update()
    {
        if(player != null)
        {
            try {
                musicPosition = player.getCurrentPosition();
                System.out.println("Update "+musicPosition);
                seekBar.setProgress(musicPosition);
                //Calculate time in seconds
                int musicS = musicPosition/1000;
                int durS = player.getDuration()/1000;
                String displayString = "["+musicS +"s/"+durS+"s]";
                txtSongTime.setText(displayString);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}