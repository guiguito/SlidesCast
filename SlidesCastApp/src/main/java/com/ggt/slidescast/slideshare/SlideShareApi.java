package com.ggt.slidescast.slideshare;

import com.ggt.slidescast.slideshare.model.SlideShareObject;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Slideshare API definition with Retrofit.
 *
 * @author guiguito
 */
public interface SlideShareApi {

    @GET("/get_slideshow")
    void getSlideShow(@Query("api_key") String apiKey, @Query("hash") String hash, @Query("ts") long timeStamp, @Query("slideshow_id") String slideshowId,
                      Callback<SlideShareObject> cb);

    @GET("/get_slideshow")
    SlideShareObject getSlideShow(@Query("api_key") String apiKey, @Query("hash") String hash, @Query("ts") long timeStamp, @Query("slideshow_id") String slideshowId);


    @GET("/search_slideshows")
    void searchSlideShows(@Query("api_key") String apiKey, @Query("hash") String hash, @Query("ts") long timeStamp, @Query("q") String query,
                          @Query("lang") String lang, @Query("page") long page, @Query("items_per_page") long itemsPerPage, Callback<SlideShareObject> cb);

    @GET("/get_slideshows_by_user")
    void getSlideShowsByUser(@Query("api_key") String apiKey, @Query("hash") String hash, @Query("ts") long timeStamp,
                             @Query("username_for") String usernameFor, @Query("username") String username, @Query("password") String password, @Query("limit") long limit,
                             Callback<SlideShareObject> cb);

    @GET("/get_slideshows_by_user")
    SlideShareObject getSlideShowsByUser(@Query("api_key") String apiKey, @Query("hash") String hash, @Query("ts") long timeStamp,
                                         @Query("username_for") String usernameFor, @Query("username") String username, @Query("password") String password, @Query("limit") long limit);

    @GET("/get_user_favorites")
    void getUserFavorites(@Query("api_key") String apiKey, @Query("hash") String hash, @Query("ts") long timeStamp, @Query("username_for") String usernameFor,
                          Callback<SlideShareObject> cb);

    @GET("/get_user_favorites")
    SlideShareObject getUserFavorites(@Query("api_key") String apiKey, @Query("hash") String hash, @Query("ts") long timeStamp, @Query("username_for") String usernameFor);

    @GET("/delete_slideshow")
    void deleteSlideShow(@Query("api_key") String apiKey, @Query("hash") String hash, @Query("ts") long timeStamp, @Query("username") String username,
                         @Query("password") String password, @Query("slideshow_id") String slideshowId, Callback<SlideShareObject> cb);

    @GET("/add_favorite")
    void addFavoriteSlideShow(@Query("api_key") String apiKey, @Query("hash") String hash, @Query("ts") long timeStamp, @Query("username") String username,
                              @Query("password") String password, @Query("slideshow_id") String slideshowId, Callback<SlideShareObject> cb);

    @Multipart
    @POST("/upload_slideshow")
    void uploadSlides(@Query("api_key") String apiKey, @Query("hash") String hash, @Query("ts") long timeStamp, @Query("username") String username,
                      @Query("password") String password, @Query("slideshow_title") String title, @Part("slideshow_srcfile") TypedFile file,
                      @Query("slideshow_description") String description, @Query("slideshow_tags") String tags, @Query("make_src_public") String makeDownloadable,
                      @Query("make_slideshow_private") String makeSlideShowPrivate, Callback<SlideShareObject> cb);
}
