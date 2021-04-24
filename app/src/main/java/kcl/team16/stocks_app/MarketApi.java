package kcl.team16.stocks_app; /**
 * Team 16 Market Analytics Tool 2021
 * Alexander Hoare K20102303
 */

import android.os.StrictMode;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MarketApi {

    private static MarketApi marketApi;

    //API Key to RapidAPI
    private static final String rapidapi_key = "fab638801cmsh476e70c98d6367ap1d5dc0jsn71df8e3a6807";

    // Private constructor - Singleton Pattern
    private MarketApi() {}

    public static MarketApi getInstance()
    {
        if (marketApi == null)
            marketApi = new MarketApi();
        return marketApi;
    }


    /**
     * Fetches market data based on a API query, returning a Response object.
     * @param query_url - The string address of the API call.
     * @throws java.io.IOException, JSONException
     * @return content - the String response. Null if invalid URL.
     */
    public String getMarketData(String query_url) throws IOException {
        //Set threading policies to allow for HTTP Connection.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //Creating the http client
        OkHttpClient client = new OkHttpClient();
        //Creating the request
        Request request = new Request.Builder()
                .url(query_url)
                .get()
                .addHeader("x-rapidapi-key", rapidapi_key)
                .addHeader("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                .build();
        //Fetching response
        Response response = client.newCall(request).execute();
        //Returning response
        try {
            return response.body().string();
        }
        catch (Exception e){ //If bad URL is sent (but still valid), return null
            return null;
        }
    }
}
