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
import ns804.bigpiph.shitcode.models.Product;

/**
 * Created by Luke747 on 5/26/16.
 */
public class StoreAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    Context context;
    ArrayList<Product> product;
    public int id = 0;
    public String title = "";
    public Double price = 0.00;
    public String image = "";
    public String url = "";
    public String feature = "";
    public String thumb = "";
    public int order = 0;

    public StoreAdapter(Context context, ArrayList<Product> product){
        this.context = context;
        this.product = product;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return product.size();
    }

    @Override
    public Object getItem(int position) {
        return product.get(position);
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
            view = inflater.inflate(R.layout.store_item ,parent, false);

            holder = new ViewHolder();
            holder.name = (TextView)view.findViewById(R.id.productName);
            holder.imageProduct = (ImageView)view.findViewById(R.id.storepic);
            holder.Price = (TextView)view.findViewById(R.id.price);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        title = product.get(position).getTitle();
        price = product.get(position).getPrice();


        holder.Price.setText(String.format( "$%.2f", price ));
        holder.name.setText(title);
        if(product.get(position).getImage().length()> 6)
        Picasso.with(context).load(product.get(position).getThumb()).resize(100,100).into(holder.imageProduct);

        return view;
    }

    class ViewHolder {
        TextView name;
        ImageView imageProduct;
        TextView Price;
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
