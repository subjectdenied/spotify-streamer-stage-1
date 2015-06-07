package subjectdenied.gmail.com.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private EditText txtSearchArtist;
    private ListView listViewSearchResults;

    private SpotifyApi api = new SpotifyApi();
    private SpotifyService spotify = api.getService();

    private List<Artist> artists;
    private ArtistsAdapter artistsAdapter;

    private AsyncArtistsSearcher asyncArtistsSearcher;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        txtSearchArtist = (EditText) rootView.findViewById(R.id.txtSearchArtist);
        listViewSearchResults = (ListView) rootView.findViewById(R.id.listSearchResults);

        // create listAdapter
        artistsAdapter = new ArtistsAdapter(getActivity(), new ArrayList<Artist>());
        listViewSearchResults.setAdapter(artistsAdapter);

        listViewSearchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // get item data
                Artist artist = (Artist) artistsAdapter.getItem(position);
                String artistID = artist.id;
                String artistName = artist.name;

                // create intent
                Intent intent = new Intent(getActivity(), TopTracksActivity.class);
                intent.putExtra("artistID", artistID);
                intent.putExtra("artistName", artistName);
                startActivity(intent);

            }
        });

        txtSearchArtist.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                // if keydown and "enter" is pressed
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    String entry = txtSearchArtist.getText().toString();

                    // display a floating message
                    getArtists(entry);
                    return true;

                }

                return false;

            }
        });

        return rootView;
    }

    public void getArtists(String entry) {

        asyncArtistsSearcher = new AsyncArtistsSearcher();
        asyncArtistsSearcher.execute(entry);

    }

    public class AsyncArtistsSearcher extends AsyncTask<String, Void, List<Artist>> {

        @Override
        protected List<Artist> doInBackground(String... params) {

            List<Artist> artists = new ArrayList<Artist>();

            ArtistsPager artistsPager = spotify.searchArtists(params[0]);
            artists = artistsPager.artists.items;

            return artists;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            artistsAdapter.setData(artists);

            if (artists.size() == 0) {
                Toast.makeText(getActivity(), "no results found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showTextEntryDebug(String entry) {
        Toast.makeText(getActivity(), "searching for: " + entry, Toast.LENGTH_SHORT).show();

        getArtists(entry);
    }
}
