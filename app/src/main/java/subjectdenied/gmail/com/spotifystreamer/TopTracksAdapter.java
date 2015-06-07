package subjectdenied.gmail.com.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by chris on 07.06.15.
 */
public class TopTracksAdapter extends BaseAdapter {

    private Context context;
    private List<Track> tracks;

    private LayoutInflater layoutInflater;

    public TopTracksAdapter(Context context, List<Track> tracks) {

        this.context = context;
        this.tracks = tracks;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<Track> tracks) {

        this.tracks = tracks;
        this.notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return tracks.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.toptracks_item, parent, false);
        }

        // get Track data
        Track track = tracks.get(position);

        String trackName = track.name;
        String albumName = track.album.name;
        String previewUrl = track.preview_url;
        String imgUrlLarge = null;
        String imgUrlSmall = null;

        if (track.album.images.size() > 0) {

            // check if high-quality artwork is avail
            imgUrlLarge = track.album.images.get(0).url;

            if (track.album.images.get(1) != null) {
                imgUrlSmall = track.album.images.get(1).url;
            } else {
                imgUrlSmall = imgUrlLarge;
            }

        }

        // get ui elements
        ImageView albumArt = (ImageView) convertView.findViewById(R.id.imgTrackPreview);
        TextView lblTrackName = (TextView) convertView.findViewById(R.id.lblTrackName);
        TextView lblAlbumName = (TextView) convertView.findViewById(R.id.lblAlbumName);

        // set ui elements
        lblTrackName.setText(trackName);
        lblAlbumName.setText(albumName);
        Picasso.with(context).load(imgUrlSmall).into(albumArt);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return tracks.get(position);
    }


}
