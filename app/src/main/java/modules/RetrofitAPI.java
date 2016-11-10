package modules;

import java.util.List;
import java.util.Set;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by gunter on 11/5/16.
 */

public interface RetrofitAPI {
    @GET("neighborhoods.json")
    Call<List<Neighborhood>> getNeighborhoodsJSON();

    @GET("routes.json")
    Call<Set<Route>> getRoutesJSON();
}
