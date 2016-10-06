package ns804.bigpiph.shitcode.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import ns804.bigpiph.R;
import ns804.bigpiph.shitcode.models.Tracks;

/**
 * Created by Luke747 on 5/25/16.
 */
public class MusicAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    Context context;
    ArrayList<Tracks> tracks;
    String title;

    public MusicAdapter(Context context, ArrayList<Tracks> tracks){
        this.context = context;
        this.tracks = tracks;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return tracks.size();
    }

    @Override
    public Object getItem(int position) {
        return tracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.music_item ,parent, false);

            holder = new ViewHolder();
            holder.name = (TextView)view.findViewById(R.id.track);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        title = tracks.get(position).getTitle();
        holder.name.setText(title);

        return view;
    }

    class ViewHolder {
        TextView name;
        ImageView imageViewProfilePic;
        RatingBar ratingBar;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
}
