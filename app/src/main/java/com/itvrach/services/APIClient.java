package com.itvrach.services;



import android.text.Html;
import android.text.Spanned;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class APIClient {
    private static Retrofit retrofit = null;
    public static final String BASE_URL = "http://192.168.43.132:8081/web/";//"http://192.168.43.132/web/";
    private static final String URI_API  = BASE_URL+ "api/";
    public static final String URI_SEGMENT_UPLOAD = BASE_URL+ "assets/uploads/";
    static String servers = getColorSpanned("Check Your Inter","#f39c12");
    static String unconnect  = getColorSpanned("net Conncetion !","#ffffff");
    public static final Spanned serverDisconnect = Html.fromHtml(servers+" "+unconnect);








    public static Retrofit getClient() {
        if (retrofit==null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            final OkHttpClient client;// = new OkHttpClient();
          //  OkHttpClient client = new OkHttpClient();
            // init cookie manager
            CookieHandler cookieHandler = new CookieManager();

            client = new OkHttpClient.Builder().addNetworkInterceptor(interceptor)
                    .cookieJar(new JavaNetCookieJar(cookieHandler))
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(URI_API)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    /*static final class CookieInterceptor implements Interceptor {
        private volatile String cookie;

        public void setSessionCookie(String cookie) {
            this.cookie = cookie;
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (this.cookie != null) {
                request = request.newBuilder()
                        .header("Cookie", this.cookie)
                        .build();
            }
            return chain.proceed(request);
        }
    }
*/

   /* public static Retrofit getClient() {
        if(retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(URI_API)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }*/


    /*OkHttpClient client = new OkHttpClient();
    OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new AddCookiesInterceptor(context)); // VERY VERY IMPORTANT
        builder.addInterceptor(new ReceivedCookiesInterceptor(context)); // VERY VERY IMPORTANT
    client = builder.build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("my-base") // REQUIRED
            .client(client) // VERY VERY IMPORTANT
            .addConverterFactory(GsonConverterFactory.create())
            .build(); // REQUIRED
*/

    public static String getColorSpanned(String text, String color){
        String input ="<font color="+color+">"+text+"</font>";
        return input;
    }





}
