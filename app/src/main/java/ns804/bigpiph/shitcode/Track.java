package ns804.bigpiph.shitcode;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Luke747 on 5/20/16.
 */
public class Track implements Parcelable {

    private String mTitle ="";
    private int mID = 0;
    private String mStreamURL = "";
    private String mArtworkURL = "";
    String spotify = "";
    String gooleplay = "";
    String itunes ="";
    String band = "";
    String soundcloudDown = "";

    public String getTitle() {
        return mTitle;
    }

    public int getID() {
        return mID;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public String getArtworkURL() {
        return mArtworkURL;
    }

    public void setmID(int mID){this.mID = mID;}

    public void setmTitle(String mTitle){this.mTitle = mTitle;}

    public void setmStreamURL(String mStreamURL){this.mStreamURL = mStreamURL;}

    public void setmArtworkURL(String mArtworkURL){this.mArtworkURL = mArtworkURL;}

    public void setSpotify(String spotify){this.spotify = spotify;}

    public String getSpotify(){return spotify;}

    public void setGooleplay(String gooleplay){this.gooleplay = gooleplay;}

    public String getGooleplay(){return gooleplay;}

    public void setItunes(String itunes){this.itunes = itunes;}

    public String getItunes(){return itunes;}

    public void setBand(String band){this.band = band;}

    public String getBand(){return band;}

    public void setSoundcloudDown(String soundcloudDown){this.soundcloudDown = soundcloudDown;}

    public String getSoundcloudDown(){return soundcloudDown;}

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {
        //out.writeInt(mData);

        //
        out.writeString(mTitle);
        out.writeInt(mID);
        out.writeString(mStreamURL);
        out.writeString(mArtworkURL);
        out.writeString(spotify);
        out.writeString(itunes);
        out.writeString(gooleplay);
        out.writeString(band);
        out.writeString(soundcloudDown);



    }




    public Track() {


        // Normal actions performed by class, since this is still a normal object!
    }

    public static final Parcelable.Creator<Track> CREATOR
            = new Parcelable.Creator<Track>(){

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    private Track(Parcel in){
        //mData = in.readInt();

        mTitle = in.readString();
        mID = in.readInt();
        mStreamURL = in.readString();
        mArtworkURL = in.readString();
        spotify = in.readString();
        itunes = in.readString();
        gooleplay = in.readString();
        band = in.readString();
        soundcloudDown = in.readString();

    }

}

