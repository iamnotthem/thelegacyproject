package ns804.bigpiph.shitcode.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ns804.bigpiph.shitcode.Episodes_activity;
import ns804.bigpiph.R;
import ns804.bigpiph.shitcode.models.Episode;

/**
 * Created by Luke747 on 7/12/16.
 */
public class GridEpiAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    ArrayList<Episode> episodes;
    int w, h;

    // Constructor
    public GridEpiAdapter (Context c, ArrayList<Episode> episodes, int w, int h) {
        mContext = c;
        inflater = LayoutInflater.from(mContext);
        this.episodes = episodes;
        this.w = w;
        this.h = h;
    }

    public int getCount() {
        return episodes.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View v = convertView;

        if (convertView == null) {
            v = inflater.inflate(R.layout.grid_item_episode, parent, false);
            holder = new ViewHolder();
            holder.picture = (ImageView)v.findViewById(R.id.imageViewItem);
            holder.title = (TextView)v.findViewById(R.id.textViewItemName);
            holder.number = (TextView)v.findViewById(R.id.episodenum);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
		/*holder.picture = (ImageView)v.getTag(R.id.picture);
		holder.tvAlertCount2 = (TextView)v.getTag(R.id.tvAlertCount2);*/

        Item item = (Item)getItem(position);
//		else
//		{
//			imageView = (ImageView) convertView;
//		}
        if (mContext instanceof Episodes_activity) {
            Episode epi = episodes.get(position);
            String url = epi.getThumbNailImage();
            String num = epi.getOrder();
            String tit = epi.getTitle();

            Picasso.with(mContext).load(url).resize(w , h).into(holder.picture);
            holder.number.setText(epi.getEpisodeNumber());
            holder.title.setText(tit);
        }

        return v;
    }

    private class Item
    {
        final String name;
        final int drawableId;

        Item(String name, int drawableId)
        {
            this.name = name;
            this.drawableId = drawableId;
        }
    }

    class ViewHolder {
        TextView title, number;
        ImageView picture;
    }
}
