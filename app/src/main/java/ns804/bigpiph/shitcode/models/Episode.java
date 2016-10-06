package ns804.bigpiph.shitcode.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Luke747 on 5/23/16.
 */
public class Episode implements Parcelable, Comparable {

    private int mData;
    public String title = "";
    public String description = "";
    public String episodeNumber = "";
    public String videoURL = "";
    public String thumbNailImage = "";
    public String fullImage = "";
    public String order ="";
    public String featured ="";
    public String EpisodeExplores = "";
    public String category;



    // We can also include child Parcelable objects. Assume MySubParcel is such a Parcelable:
    private Episode mInfo;

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){return title;}

    public void setEpisodeNumber(String episodeNumber){
        this.episodeNumber = episodeNumber;
    }
    public String getEpisodeNumber(){return episodeNumber;}

    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){return description;}

    public void setVideoURL(String videoURL){
        this.videoURL = videoURL;
    }
    public String getVideoURL(){return videoURL;}

    public void setThumbNailImage(String thumbNailImage){
        this.thumbNailImage = thumbNailImage;
    }
    public String getThumbNailImage(){
        return thumbNailImage;
    }
    public void setFullImage(String fullImage){
        this.fullImage = fullImage;
    }
    public String getFullImage(){return fullImage;}
    public void setOrder(String order){
        this.order = order;
    }
    public String getOrder(){return order;}
    public void setFeatured(String featured){
        this.featured = featured;
    }
    public String getFeatured(){return featured;}

    public void setEpisodeExplores(String EpisodesExplores){
        this.EpisodeExplores = EpisodesExplores;
    }
    public String getEpisodeExplores(){return EpisodeExplores;}

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {
        //out.writeInt(mData);

        //
        out.writeString(title);
        out.writeString(description);
        out.writeString(videoURL);
        out.writeString(thumbNailImage);
        out.writeString(fullImage);
        out.writeString(episodeNumber);
        out.writeString(order);
        out.writeString(featured);
        out.writeString(EpisodeExplores);
        //out.writeParcelable(mInfo, flags);
        Log.d("PARCEL_VALUES","title: "+title);
        Log.d("PARCEL_VALUES","Description: "+description);
        Log.d("PARCEL_VALUES","video: "+videoURL);
        Log.d("PARCEL_VALUES","thumb: "+thumbNailImage);
        Log.d("PARCEL_VALUES","image: "+fullImage);
        Log.d("PARCEL_VALUES","episode: "+episodeNumber);
        Log.d("PARCEL_VALUES","order: "+order);
        Log.d("PARCEL_VALUES","featured: "+featured);
        Log.d("PARCEL_VALUES","episode_detail: "+EpisodeExplores);

    }




    public Episode() {
        Log.d("PARCEL_VALUES_e",": "+title);
        Log.d("PARCEL_VALUES_e",": "+description);
        Log.d("PARCEL_VALUES_e",": "+videoURL);
        Log.d("PARCEL_VALUES_e",": "+thumbNailImage);
        Log.d("PARCEL_VALUES_e",": "+fullImage);
        Log.d("PARCEL_VALUES_e",": "+episodeNumber);
        Log.d("PARCEL_VALUES_e",": "+order);
        Log.d("PARCEL_VALUES_e",": "+featured);
        Log.d("PARCEL_VALUES_e",": "+EpisodeExplores);

        // Normal actions performed by class, since this is still a normal object!
    }

    public static final Parcelable.Creator<Episode> CREATOR
            = new Parcelable.Creator<Episode>(){

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };

    private Episode(Parcel in){
        //mData = in.readInt();
        title = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbNailImage = in.readString();
        fullImage = in.readString();
        episodeNumber = in.readString();
        order = in.readString();
        featured = in.readString();
        EpisodeExplores = in.readString();

        Log.d("PARCEL_VALUES_r","title: "+title);
        Log.d("PARCEL_VALUES_r","description: "+description);
        Log.d("PARCEL_VALUES_r","video: "+videoURL);
        Log.d("PARCEL_VALUES_r","thumb: "+thumbNailImage);
        Log.d("PARCEL_VALUES_r","image: "+fullImage);
        Log.d("PARCEL_VALUES_r","episode: "+episodeNumber);
        Log.d("PARCEL_VALUES_r","order: "+order);
        Log.d("PARCEL_VALUES_r","featured: "+featured);
        Log.d("PARCEL_VALUES_r","Episode_deatils: "+EpisodeExplores);






    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
