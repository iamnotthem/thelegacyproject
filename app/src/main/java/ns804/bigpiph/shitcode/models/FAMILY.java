package ns804.bigpiph.shitcode.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Luke747 on 5/24/16.
 */
public class FAMILY implements Parcelable {

    public int id = 0;
    public String name = "";
    public String story = "";
    public String workex = "";
    public String workdes = "";
    public String url = "";
    public String urldes = "";
    public String thumb ="";
    public String image = "";
    public int order = 0;
    public String feature = "";




    // We can also include child Parcelable objects. Assume MySubParcel is such a Parcelable:
    private FAMILY mInfo;

    public void setId(int id){this.id = id;}

    public int getId(){return id;}

    public void setName(String title){
        this.name = title;
    }

    public String getName(){return name;}

    public void setStory(String story){this.story = story;}

    public String getStory(){return story;}

    public void setWorkex(String workex){this.workex = workex;}

    public String getWorkex(){return workex;}

    public void setWorkdes(String workdes){this.workdes = workdes;}

    public String getWorkdes(){return workdes;}

    public void setUrl(String url){this.url = url;}

    public String getUrl(){return url;}

    public void setUrldes(String urldes){this.urldes = urldes;}

    public String getUrldes(){return urldes;}

    public void setThumb(String thumb){this.thumb = thumb;}

    public String getThumb(){return thumb;}

    public void setOrder(int order){this.order = order;}

    public int getOrder(){return order;}

    public void setFeature(String feature){this.feature = feature;}

    public String getFeature(){return feature;}

    public void setImage(String image){this.image = image;}

    public String getImage(){return image;}


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeInt(id);
        out.writeString(name);
        out.writeString(story);
        out.writeString(workex);
        out.writeString(workdes);
        out.writeString(url);
        out.writeString(urldes);
        out.writeString(thumb);
        out.writeString(image);
        out.writeInt(order);
        out.writeString(feature);
        //out.writeParcelable(mInfo, flags);
        Log.d("PARCEL_VALUES","name: "+name);
        Log.d("PARCEL_VALUES","story: "+story);
        Log.d("PARCEL_VALUES","workex: "+workex);
        Log.d("PARCEL_VALUES","workdes: "+workdes);
        Log.d("PARCEL_VALUES","url: "+url);
        Log.d("PARCEL_VALUES","urldes: "+urldes);
        Log.d("PARCEL_VALUES","thumb: "+thumb);
        Log.d("PARCEL_VALUES","image: "+image);
        Log.d("PARCEL_VALUES","feature: "+feature);


    }




    public FAMILY() {


        // Normal actions performed by class, since this is still a normal object!
    }

    public static final Parcelable.Creator<FAMILY> CREATOR
            = new Parcelable.Creator<FAMILY>(){

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public FAMILY createFromParcel(Parcel in) {
            return new FAMILY(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public FAMILY[] newArray(int size) {
            return new FAMILY[size];
        }
    };

    private FAMILY(Parcel in){

        id = in.readInt();
        name = in.readString();
        story = in.readString();
        workex = in.readString();
        workdes = in.readString();
        url = in.readString();
        urldes = in.readString();
        thumb = in.readString();
        image = in.readString();
        order = in.readInt();
        feature = in.readString();



        Log.d("PARCEL_VALUES_o","name: "+name);
        Log.d("PARCEL_VALUES_o","story: "+story);
        Log.d("PARCEL_VALUES_o","workex: "+workex);
        Log.d("PARCEL_VALUES_o","workdes: "+workdes);
        Log.d("PARCEL_VALUES_o","url: "+url);
        Log.d("PARCEL_VALUES_o","urldes: "+urldes);
        Log.d("PARCEL_VALUES_o","thumb: "+thumb);
        Log.d("PARCEL_VALUES_o","image: "+image);
        Log.d("PARCEL_VALUES_o","feature: "+feature);


    }

}
