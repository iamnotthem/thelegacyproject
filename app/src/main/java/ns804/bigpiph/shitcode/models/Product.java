package ns804.bigpiph.shitcode.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Luke747 on 5/26/16.
 */
public class Product implements Parcelable {

    public int id = 0;
    public String title = "";
    public Double price = 0.00;
    public String image = "";
    public String url = "";
    public String feature = "";
    public String thumb = "";
    public int order = 0;


    public void setId(int id){this.id = id;}

    public int getId(){return id;}

    public void setTitle(String title){this.title = title;}

    public String getTitle(){return title;}

    public void setPrice(Double price){this.price = price;}

    public Double getPrice(){return price;}

    public void setImage(String image){this.image = image;}

    public String getImage(){return image;}

    public void setUrl(String url){this.url = url;}

    public String getUrl(){return url;}

    public void setFeature(String feature){this.feature = feature;}

    public String getFeature(){return feature;}

    public void setThumb(String thumb){this.thumb = thumb;}

    public String getThumb(){return thumb;}

    public void setOrder(int order){this.order = order;}

    public int getOrder(){return order;}


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {
        //out.writeInt(mData);


        out.writeInt(id);
        out.writeString(title);
        out.writeDouble(price);
        out.writeString(image);
        out.writeString(url);
        out.writeString(feature);
        out.writeString(thumb);
        out.writeInt(order);

        //out.writeParcelable(mInfo, flags);
        Log.d("PARCEL_VALUES_o","title: "+title);
        Log.d("PARCEL_VALUES_o","id: "+id);
        Log.d("PARCEL_VALUES_o","price: "+price);
        Log.d("PARCEL_VALUES_o","image: "+image);
        Log.d("PARCEL_VALUES_o","url: "+url);
        Log.d("PARCEL_VALUES_o","feature: "+feature);
        Log.d("PARCEL_VALUES_o","order: "+order);
        Log.d("PARCEL_VALUES_o","thumb: "+thumb);


    }




    public Product() {


        // Normal actions performed by class, since this is still a normal object!
    }

    public static final Parcelable.Creator<Product> CREATOR
            = new Parcelable.Creator<Product>(){

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    private Product(Parcel in){
        //mData = in.readInt();
           /*
        public int id = 0;
        public String title = "";
        public String price = "";
        public String image = "";
        public String url = "";
        public Boolean feature = false;
        public String thumb = "";
        public int order = 0;
        */
        id = in.readInt();
        title = in.readString();
        price = in.readDouble();
        image = in.readString();
        url = in.readString();
        feature = in.readString();
        thumb = in.readString();
        order = in.readInt();




        Log.d("PARCEL_VALUES_r","title: "+title);
        Log.d("PARCEL_VALUES_r","id: "+id);
        Log.d("PARCEL_VALUES_r","price: "+price);
        Log.d("PARCEL_VALUES_r","image: "+image);
        Log.d("PARCEL_VALUES_r","url: "+url);
        Log.d("PARCEL_VALUES_r","feature: "+feature);
        Log.d("PARCEL_VALUES_r","order: "+order);
        Log.d("PARCEL_VALUES_r","thumb: "+thumb);








    }




}
