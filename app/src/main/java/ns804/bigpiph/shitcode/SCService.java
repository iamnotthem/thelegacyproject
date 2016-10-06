package ns804.bigpiph.shitcode;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Luke747 on 5/20/16.
 */
public interface SCService {

    @GET("/tracks?client_id=" + ns804.bigpiph.shitcode.Config.CLIENT_ID)
    public void getRecentTracks(@Query("created_at[from]") String date, Callback<List<Track>> cb);
}
