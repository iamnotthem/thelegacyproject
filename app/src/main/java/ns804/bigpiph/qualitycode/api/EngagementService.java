package ns804.bigpiph.qualitycode.api;

import ns804.bigpiph.qualitycode.api.response.EngagementResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Anatol on 9/10/2016.
 */
public interface EngagementService {

    String EP1_HOST_NAME = "host_name";
    String EP1_USER_NAME = "user_name";
    String EP2_MEME_IMAGE = "meme_image";
    String EP2_SONG_ONE = "song_one";
    String EP3_IG_IMAGE_ONE = "instagram_image_one";
    String EP3_SONG_TWO = "song_two";
    String EP4_TOO_LATE_SELECTION = "too_late_selection";
    String EP4_SONG_THREE = "song_three";
    String EP5_SONG_FOUR = "song_four";
    String EP6_IG_IMAGE_TWO = "instagram_image_two";
    String EP6_SONG_FIVE = "song_five";
    String EP7_YOU_GOTTA_KNOW_SELECTION = "you_gotta_know_selection";
    String EP7_SONG_SIX = "song_six";

    @FormUrlEncoded
    @POST("EngagmentResponses")
    Call<EngagementResponse> postEP1_hostName(@Field("key") String key, @Field("response") String response);

    @FormUrlEncoded
    @POST("EngagmentResponses")
    Call<EngagementResponse> postEP1_userName(@Field("key") String key, @Field("response") String response);

    @Multipart
    @POST("EngagmentResponses")
    Call<EngagementResponse> postEP2_memeImage(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("EngagmentResponses")
    Call<EngagementResponse> postEP2_songOne(@Field("key") String key, @Field("response") String response);

    @FormUrlEncoded
    @POST("EngagmentResponses")
    Call<EngagementResponse> postEP3_igImageOne(@Field("key") String key, @Field("response") String response);

    @FormUrlEncoded
    @POST("EngagmentResponses")
    Call<EngagementResponse> postEP3_songTwo(@Field("key") String key, @Field("response") String response);

    @FormUrlEncoded
    @POST("EngagmentResponses")
    Call<EngagementResponse> postEP4_tooLateSelection(@Field("key") String key, @Field("response") String response);

    @FormUrlEncoded
    @POST("EngagmentResponses")
    Call<EngagementResponse> postEP4_songThree(@Field("key") String key, @Field("response") String response);

    @FormUrlEncoded
    @POST("EngagmentResponses")
    Call<EngagementResponse> postEP5_songFour(@Field("key") String key, @Field("response") String response);

    @FormUrlEncoded
    @POST("EngagmentResponses")
    Call<EngagementResponse> postEP6_igImageTwo(@Field("key") String key, @Field("response") String response);

    @FormUrlEncoded
    @POST("EngagmentResponses")
    Call<EngagementResponse> postEP6_songFive(@Field("key") String key, @Field("response") String response);

    @FormUrlEncoded
    @POST("EngagmentResponses")
    Call<EngagementResponse> postEP7_gottaKnowSelection(@Field("key") String key, @Field("response") String response);

    @FormUrlEncoded
    @POST("EngagmentResponses")
    Call<EngagementResponse> postEP7_songSix(@Field("key") String key, @Field("response") String response);
}
