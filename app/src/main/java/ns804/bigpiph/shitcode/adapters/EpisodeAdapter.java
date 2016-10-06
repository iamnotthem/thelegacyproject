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

import ns804.bigpiph.R;
import ns804.bigpiph.shitcode.models.epiDetail;

public class EpisodeAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    Context context;
    ArrayList<epiDetail> episodeExplores;
    public String title = "";
    public String description = "";
    public String episodeNumber = "";
    public String videoURL = "";
    public String thumbNailImage = "";
    public String fullImage = "";
    public String order ="";
    public String featured ="";
    public String EpisodeExplores = "";


    public EpisodeAdapter(Context context, ArrayList<epiDetail> episodeExplores){
        this.context = context;
        this.episodeExplores = episodeExplores;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return episodeExplores.size();
    }

    @Override
    public Object getItem(int position) {
        return episodeExplores.get(position);
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
            view = inflater.inflate(R.layout.episode_item ,parent, false);

            holder = new ViewHolder();
            holder.title = (TextView)view.findViewById(R.id.episodeName);
            holder.dataPic = (ImageView)view.findViewById(R.id.dataPic);
            holder.descript = (TextView)view.findViewById(R.id.price);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
            title = episodeExplores.get(position).getTitle();
            description = episodeExplores.get(position).getDescription();
            thumbNailImage = episodeExplores.get(position).getThumb();

            holder.title.setText(title);
            holder.descript.setText(description);
            Picasso.with(context).load(thumbNailImage).resize(100, 100).into(holder.dataPic);


        return view;
    }

    class ViewHolder {
        TextView title;
        ImageView dataPic;
        TextView descript;
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
