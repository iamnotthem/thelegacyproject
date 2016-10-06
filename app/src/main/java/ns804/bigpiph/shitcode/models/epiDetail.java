package ns804.bigpiph.shitcode.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Luke747 on 5/31/16.
 */
public class epiDetail implements Parcelable {

    private int id = 0;
    public int order = 0;
    public String title = "";
    public String url = "";
    public String description = "";
    public String releaseDate = "";
    public int epiId = 0;
    public String thumb = "";



    // We can also include child Parcelable objects. Assume MySubParcel is such a Parcelable:
    private Episode mInfo;

    public void setId(int id){this.id = id;}

    public int getId(){return id;}

    public void setOrder(int order){this.order = order;}

    public int getOrder(){return order;}

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){return title;}

    public void setUrl(String url){this.url = url;}

    public String getUrl(){return url;}

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){return description;}

    public void setReleaseDate(String releaseDate){this.releaseDate = releaseDate;}

    public String getReleaseDate(){return releaseDate;}

    public void setEpiId(int epiId){this.epiId = epiId;}

    public int getEpiId(){return epiId;}





    public void setThumb(String thumb){
        this.thumb = thumb;
    }
    public String getThumb(){
        return thumb;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {
        //out.writeInt(mData);


        //
        out.writeInt(id);
        out.writeInt(order);
        out.writeString(title);
        out.writeString(url);
        out.writeString(description);
        out.writeString(releaseDate);
        out.writeInt(epiId);
        out.writeString(thumb);

        //out.writeParcelable(mInfo, flags);
        Log.d("PARCEL_VALUES","title: "+title);
        Log.d("PARCEL_VALUES","Description: "+description);


    }




    public epiDetail() {
        Log.d("PARCEL_VALUES_e",": "+title);
        Log.d("PARCEL_VALUES_e",": "+description);


        // Normal actions performed by class, since this is still a normal object!
    }

    public static final Parcelable.Creator<epiDetail> CREATOR
            = new Parcelable.Creator<epiDetail>(){

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public epiDetail createFromParcel(Parcel in) {
            return new epiDetail(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public epiDetail[] newArray(int size) {
            return new epiDetail[size];
        }
    };

    private epiDetail(Parcel in){
        //mData = in.readInt();


        id = in.readInt();
        order = in.readInt();
        title = in.readString();
        url = in.readString();
        description = in.readString();
        releaseDate = in.readString();
        epiId = in.readInt();
        thumb = in.readString();



        Log.d("PARCEL_VALUES_r","title: "+title);
        Log.d("PARCEL_VALUES_r","description: "+description);






    }
}
