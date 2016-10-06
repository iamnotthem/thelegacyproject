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

import ns804.bigpiph.shitcode.Family_Activity;
import ns804.bigpiph.R;
import ns804.bigpiph.shitcode.models.FAMILY;

/**
 * Created by Luke747 on 7/12/16.
 */
public class GridFamAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    ArrayList<FAMILY> family;
    int w, h;

    // Constructor
    public GridFamAdapter (Context c, ArrayList<FAMILY> family, int w, int h) {
        mContext = c;
        inflater = LayoutInflater.from(mContext);
        this.family = family;
        this.w = w;
        this.h = h;
    }

    public int getCount() {
        return family.size();
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
            v = inflater.inflate(R.layout.fam_grid_item, parent, false);
            holder = new ViewHolder();
            holder.picture = (ImageView)v.findViewById(R.id.imageViewItem);
            holder.title = (TextView)v.findViewById(R.id.textViewItemName);
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
        if (mContext instanceof Family_Activity) {
            FAMILY epi = family.get(position);
            String url = epi.getThumb();
            String tit = epi.getName();

            Picasso.with(mContext).load(url).resize(w , h).into(holder.picture);
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
        TextView title;
        ImageView picture;
    }
}
