package subjectdenied.gmail.com.spotifystreamer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by chris on 06.06.15.
 */
public class ArtistsAdapter extends BaseAdapter {

    private Context context;
    private List<Artist> artists;

    private LayoutInflater inflater;

    public ArtistsAdapter(final Context context, List<Artist> artists) {
        this.context = context;
        this.artists = artists;

        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<Artist> artists) {

        this.artists = artists;
        this.notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return this.artists.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.searchresult_item, parent, false);

        }

        // get data
        Artist artist = this.artists.get(position);

        // get name
        String name = artist.name;

        // get id
        String spotifyID = artist.id;

        // get image-url
        String imgUrl = null;

        if (artist.images.size() > 0) {

            if (artist.images.get(0).url != null) {

                imgUrl = artist.images.get(0).url;

            }

        } else {

            Log.d("artist image preview fetch", "artist image preview not found");

        }

        ImageView imgArtistPreview = (ImageView) convertView.findViewById(R.id.imgArtistPreview);
        TextView lblArtist = (TextView) convertView.findViewById(R.id.lblArtistName);

        // load artists preview image
        Picasso.with(context).load(imgUrl).into(imgArtistPreview);

        // set name
        lblArtist.setText(name);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return this.artists.get(position);
    }
}
