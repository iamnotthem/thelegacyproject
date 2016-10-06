package ns804.bigpiph.shitcode.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Luke747 on 5/23/16.
 */
public class Tracks implements Parcelable {


    public int id = 0;
    public String title = "";
    public String soundcloud = "";
    public String soundstream = "";
    public String itunes = "";
    public String googleplay = "";
    public String band = "";
    public String spotify ="";
    public String art = "";




    // We can also include child Parcelable objects. Assume MySubParcel is such a Parcelable:
    private Tracks mInfo;

    public void setId(int id){this.id = id;}

    public int getId(){return id;}

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){return title;}

    public void setArt(String art){this.art = art;}

    public String getArt(){return art;}

    public void setSoundcloud(String soundcloud){
        this.soundcloud = soundcloud;
    }
    public String getSoundcloud(){return soundcloud;}

    public void setSoundstream(String soundstream){
        this.soundstream = soundstream;
    }
    public String getSoundstream(){return soundstream;}

    public void setItunes(String itunes){
        this.itunes = itunes;
    }
    public String getItunes(){return itunes;}

    public void setGoogleplay (String googleplay){
        this.googleplay = googleplay;
    }
    public String getGoogleplay(){
        return googleplay;
    }
    public void setBand(String band){
        this.band = band;
    }
    public String getBand(){return band;}
    public void setSpotify(String spotify){
        this.spotify = spotify;
    }
    public String getSpotify(){return spotify;}



    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {
        //out.writeInt(mData);

        //
        out.writeInt(id);
        out.writeString(title);
        out.writeString(soundcloud);
        out.writeString(soundstream);
        out.writeString(itunes);
        out.writeString(googleplay);
        out.writeString(band);
        out.writeString(spotify);
        out.writeString(art);
        //out.writeParcelable(mInfo, flags);
        Log.d("PARCEL_VALUES","title: "+title);
        Log.d("PARCEL_VALUES","Description: "+soundcloud);
        Log.d("PARCEL_VALUES","video: "+soundstream);
        Log.d("PARCEL_VALUES","thumb: "+itunes);
        Log.d("PARCEL_VALUES","image: "+googleplay);
        Log.d("PARCEL_VALUES","episode: "+band);
        Log.d("PARCEL_VALUES","order: "+spotify);


    }




    public Tracks() {


        // Normal actions performed by class, since this is still a normal object!
    }

    public static final Parcelable.Creator<Tracks> CREATOR
            = new Parcelable.Creator<Tracks>(){

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Tracks createFromParcel(Parcel in) {
            return new Tracks(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Tracks[] newArray(int size) {
            return new Tracks[size];
        }
    };

    private Tracks(Parcel in){
        //mData = in.readInt();
        id = in.readInt();
        title = in.readString();
        soundcloud = in.readString();
        soundstream = in.readString();
        itunes = in.readString();
        googleplay = in.readString();
        band = in.readString();
        spotify = in.readString();
        art = in.readString();



        Log.d("PARCEL_VALUES_r","title: "+title);
        Log.d("PARCEL_VALUES_r","id: "+id);
        Log.d("PARCEL_VALUES_r","soundstream: "+soundstream);
        Log.d("PARCEL_VALUES_r","thumb: "+soundcloud);
        Log.d("PARCEL_VALUES_r","image: "+itunes);
        Log.d("PARCEL_VALUES_r","episode: "+googleplay);
        Log.d("PARCEL_VALUES_r","order: "+band);
        Log.d("PARCEL_VALUES_r","spotify: "+spotify);
        Log.d("PARCEL_VALUES_r","art: "+art);







    }
}
