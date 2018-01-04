package shop.tv.rsys.com.tvapplication.network;

import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shop.tv.rsys.com.tvapplication.model.BtaResponseModel;

/**
 * Created by akash.sharma on 12/15/2017.
 */

public class RetrofitCallbackResponse {

    public static void getNetworkResponse(final ResponseCallback responseCallback,Map<String,String> queryMap)
    {
        RetrofirApiInterface apiService = RetrofitApiClient.getApiClient().create(RetrofirApiInterface.class);
        Call<BtaResponseModel> call = apiService.getBtaResponse(queryMap);
        call.enqueue(new Callback<BtaResponseModel>() {
            @Override
            public void onResponse(Call<BtaResponseModel> call, Response<BtaResponseModel> response) {
                try {
                    List<BtaResponseModel.Movie> btaMovies = response.body().getMovie();
                    responseCallback.getResponse(btaMovies);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<BtaResponseModel> call, Throwable t) {
                Log.d("RetrofitFail",t.toString());
            }
        });
    }
}
