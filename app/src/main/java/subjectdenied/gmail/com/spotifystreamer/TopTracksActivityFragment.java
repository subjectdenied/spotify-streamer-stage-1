package subjectdenied.gmail.com.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopTracksActivityFragment extends Fragment {

    private String artistID;
    private String artistName;

    private ListView listViewTopTracksResults;

    private SpotifyApi api = new SpotifyApi();
    private SpotifyService spotify = api.getService();

    private List<Track> tracks;
    private TopTracksAdapter topTracksAdapter;

    private AsyncTopTrackFetcher asyncTopTrackFetcher;

    public TopTracksActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_toptracks, container, false);

        listViewTopTracksResults = (ListView) rootView.findViewById(R.id.listTopTracks);

        // get intent data
        Intent intent = getActivity().getIntent();
        artistID = intent.getStringExtra("artistID");
        artistName = intent.getStringExtra("artistName");

        // set actionbar title
        getActivity().setTitle(artistName);

        // create listadapter
        topTracksAdapter = new TopTracksAdapter(getActivity(), new ArrayList<Track>());
        listViewTopTracksResults.setAdapter(topTracksAdapter);

        getTopTracks(artistID);

        return rootView;
    }

    public void showTextEntryDebug(String entry) {

        Toast.makeText(getActivity(), "Using artistID: " + entry, Toast.LENGTH_SHORT).show();

        //getArtists(entry);


    }

    public void getTopTracks(String artistID) {

        asyncTopTrackFetcher = new AsyncTopTrackFetcher();
        asyncTopTrackFetcher.execute(artistID);

    }

    public class AsyncTopTrackFetcher extends AsyncTask<String, Void, List<Track>> {

        @Override
        protected List<Track> doInBackground(String... params) {

            List<Track> tracks = new ArrayList<>();
            Tracks tracksObject = new Tracks();

            Log.d("debug url", "");

            Map options = new HashMap();
            options.put("country", "AT");

            tracksObject = spotify.getArtistTopTrack(params[0], options);

            // get tracks list
            if (tracksObject.tracks != null) {

                tracks = tracksObject.tracks;

            }

            return tracks;
        }

        @Override
        protected void onPostExecute(List<Track> tracks) {
            topTracksAdapter.setData(tracks);

            if (tracks.size() == 0) {
                Toast.makeText(getActivity(), "no Top-Tracks for artist found!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
