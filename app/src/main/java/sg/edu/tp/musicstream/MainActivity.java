package sg.edu.tp.musicstream;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import sg.edu.tp.musicstream.util.AppUtil;
//MainActivity is started upon launching the app
//Implements SearchView.OnQueryTextListener to receive/handle search queries
public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener
{
    private SongCollection songCollection = SongCollection.getSongCollection();

    //Recyclerview displays all the songs in songCollection
    private RecyclerView songListView;
    private LinearLayoutManager layoutManager;
    //RecyclerView Adapter to populate each cell/row for display per song
    private SongItemRecyclerViewAdapter songListAdapter;

    //Search box to search for songs
    private SearchView searchView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        songListView = (RecyclerView) findViewById(R.id.songListView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        songListView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        songListView.setLayoutManager(layoutManager);

        // specify an adapter to display songCollection
        songListAdapter = new SongItemRecyclerViewAdapter(songCollection);
        songListView.setAdapter(songListAdapter);

        //Setup searchbar
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.searchView);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(this);

    }

    public void sendDataToActivity(Song song)
    {
        //Start PlaySongActivity and tell it to play the following song
        Intent intent = new Intent(this, PlaySongActivity.class);

        intent.putExtra("id", song.getId());
        intent.putExtra("title", song.getTitle());
        intent.putExtra("artiste", song.getArtist());
        intent.putExtra("fileLink", song.getFileLink());
        intent.putExtra("coverArt", song.getCoverArt());

        startActivity(intent);
    }

    public void handleSelection(View view)
    {
        // 1. Get the ID of the selected song
        String resourceId = (String) view.getTag();
        //2. Search for the selected song based on the ID so that all information/data of
        // the song can be retrieved from a song list.
        Song selectedSong = songCollection.searchById(resourceId);

        // 3. Popup a message on the screen to show the title of the song.
        AppUtil.popMessage(this, "Streaming song: " + selectedSong.getTitle());

        //4. Send the song data to the player screen to be played.
        sendDataToActivity(selectedSong);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        songListAdapter.getFilter().filter(newText);
        return true;
    }
    @Override
    public boolean onQueryTextSubmit(String newText) {
        return true;
    }
}