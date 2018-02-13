package shop.tv.rsys.com.tvapplication.network;

import android.util.Log;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shop.tv.rsys.com.tvapplication.model.BtaResponseModel;
import shop.tv.rsys.com.tvapplication.model.MovieDetailsModel;
import shop.tv.rsys.com.tvapplication.moviesearch.SearchResponsecallback;

/**
 * Created by akash.sharma on 12/15/2017.
 */

public class SearchRetrofitCallbackResponse {

    public static void getNetworkResponse(final SearchResponsecallback responseCallback, Map<String,String> queryMap)
    {
        RetrofirApiInterface apiService = RetrofitApiClient.getApiClient().create(RetrofirApiInterface.class);
        Call<MovieDetailsModel> call = apiService.getMovieDetailsByTitle(queryMap);
        call.enqueue(new Callback<MovieDetailsModel>() {
            @Override
            public void onResponse(Call<MovieDetailsModel> call, Response<MovieDetailsModel> response) {
                try {
                    List<MovieDetailsModel.Movie> btaMovies = response.body().getMovie();
                    responseCallback.getResponse(btaMovies);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<MovieDetailsModel> call, Throwable t) {
                Log.d("RetrofitFail",t.toString());
            }
        });
    }
}
