package ns804.bigpiph.shitcode.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ns804.bigpiph.R;
import ns804.bigpiph.shitcode.Track;

/**
 * Created by Luke747 on 5/30/16.
 */
public class SCTrackAdapter extends BaseAdapter {

    private Context mContext;
    private List<Track> mTracks;

    public SCTrackAdapter(Context context, List<Track> tracks) {
        mContext = context;
        mTracks = tracks;
    }

    @Override
    public int getCount() {
        return mTracks.size();
    }

    @Override
    public Track getItem(int position) {
        return mTracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Track track = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.music_item, parent, false);
            holder = new ViewHolder();
            Log.d("position_track","position: "+position);
            Log.d("position_track",": "+mTracks.size());
            if(position + 1 == mTracks.size()){


                holder.titleTextView = (TextView) convertView.findViewById(R.id.track);
                holder.titleTextView.setText(track.getTitle());
                convertView.setTag(holder);
                convertView.setMinimumHeight(100);

            }else {
                holder.titleTextView = (TextView) convertView.findViewById(R.id.track);
                holder.titleTextView.setText(track.getTitle());
                convertView.setTag(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.titleTextView.setText(track.getTitle());
        }



        // Trigger the download of the URL asynchronously into the image view.


        return convertView;
    }

    static class ViewHolder {
        ImageView trackImageView;
        TextView titleTextView;
    }


}
