package modules;

import java.util.List;
import java.util.Set;

import retrofit.Call;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;


public interface RetrofitAPI {
    @GET("bus_stops")
    Call<List<BusStop>> getBusStops(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("radius") int radius);

    @GET("neighborhoods.json")
    Call<List<Neighborhood>> getNeighborhoodsJSON();

    @GET("routes.json")
    Call<List<Route>> getRoutesJSON();

    @GET("neighborhoods/{id}/alerts")
    Call<List<NeighborhoodAlert>> getNeighborhoodAlertsJSON(@Path("id") int NeighborhoodID);

    @GET("routes/{id}/alerts")
    Call<List<RouteAlert>> getRouteAlertsJSON(@Path("id") String routeID);

    @GET("route_alerts/{id}/comments")
    Call<List<Comment>> getRouteAlertComments(@Path("id") int alertID);

    @GET("neighborhood_alerts/{id}/comments")
    Call<List<Comment>> getNeighborhoodAlertComments(@Path("id") int alertID);

    @GET("routes/{id}/bus_locations")
    Call<List<Bus>> getBuses(@Path("id") String routeID);

    @FormUrlEncoded
    @POST("neighborhoods/{id}/alerts")
    Call<NeighborhoodAlert> postNeighborhoodAlert(@Path("id") int neighborhoodID,
                                                  @Field("issues") String alertType,
                                                  @Field("description") String description,
                                                  @Field("user_id") String userID,
                                                  @Field("affected_routes[]") List<String> affectedRouteIds);

    @FormUrlEncoded
    @POST("routes/{id}/alerts")
    Call<RouteAlert> postRouteAlert(@Path("id") String routeID,
                                    @Field("issues") String alertType,
                                    @Field("description") String description,
                                    @Field("user_id") String userID);

    @FormUrlEncoded
    @POST("route_alerts/{id}/comments")
    Call<Comment> postRouteAlertComment(@Path("id") int routeAlertID,
                                        @Field("message") String data,
                                        @Field("user_id") String userID);

    @FormUrlEncoded
    @POST("neighborhood_alerts/{id}/comments")
    Call<Comment> postNeighborhoodAlertComment(@Path("id") int neighborhoodAlertID,
                                               @Field("message") String data,
                                               @Field("user_id") String userID);

    @FormUrlEncoded
    @POST("{db_type}/{id}/{upORdown}")
    Call<VoteConfirmation> postVote(@Path("db_type") String db_type,
                                           @Path("id") int id,
                                           @Path("upORdown") String updown,
                                           @Field("user_id") String userID);

    @DELETE("{db_type}/{id}/unvote")
    Call<VoteConfirmation> unvote(@Path("db_type") String db_type,
                                  @Path("id") int id,
                                  @Query("user_id") String userID);
}
