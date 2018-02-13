package shop.tv.rsys.com.tvapplication.network;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by akash.sharma on 12/15/2017.
 */

public class RetrofitApiClient {
    private static Retrofit retrofit;
    //private static String base_url = "http://10.67.194.30:8085/";
    private static String base_url = "http://10.67.194.40/";
    public static Retrofit getApiClient()
    {
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .client(new OkHttpClient())
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
