package sg.edu.tp.musicstream;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.tp.musicstream.dummy.DummyContent.DummyItem;
import sg.edu.tp.musicstream.util.AppUtil;

/**
 * {@link RecyclerView.Adapter} that can display a {@link song}.
 * Used by mainActivity to display SongCollection on UI
 */
public class SongItemRecyclerViewAdapter extends RecyclerView.Adapter<SongItemRecyclerViewAdapter.ViewHolder> implements Filterable {

    private final SongCollection songList;
    private ArrayList<Song> filteredSongs = null;

    public SongItemRecyclerViewAdapter(SongCollection songs) {
        songList = songs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Song s;
        if(filteredSongs == null) {
            s = songList.getSong(position);
        }
        else
        {
            s = filteredSongs.get(position);
        }
        //Populate rows in Recyclerview with song information/art
        int imageId = AppUtil.getImageIdFromDrawable(holder.mView.getContext(), s.getCoverArt());
        holder.songArt.setImageResource(imageId);
        holder.titleLabel.setText(s.getTitle());
        holder.artistLabel.setText(s.getArtist());
        holder.songArt.setTag(s.getId());
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();
                if(charString.equals(""))
                {
                    filteredSongs = null;
                    return null;
                }
                FilterResults filterResults = new FilterResults();
                filteredSongs = new ArrayList<Song>();
                //Loop through songs and seach Artist/Title for string
                for(int i = 0; i < songList.getNumSongs(); ++i)
                {
                    Song s = songList.getSong(i);
                    if( s.getArtist().toLowerCase().contains(charString) || s.getTitle().toLowerCase().contains(charString) )
                    {
                        filteredSongs.add(s);
                    }
                }
                filterResults.values = filteredSongs;
                filterResults.count = filteredSongs.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        //Return number of songs, take into account if filtered search results or normal
        if(filteredSongs == null) {
            return songList.getNumSongs();
        }
        else
        {
            return filteredSongs.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titleLabel;
        public final TextView artistLabel;
        public final ImageView songArt;

        //Link elements with UI
        public ViewHolder(View view) {
            super(view);
            mView = view;
            songArt = (ImageView) view.findViewById(R.id.songArt);
            titleLabel = (TextView) view.findViewById(R.id.songTitle);
            artistLabel = (TextView) view.findViewById(R.id.songArtist);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + artistLabel.getText() + "'";
        }
    }
}