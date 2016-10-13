package sbin.com.webserviceapp;

import java.util.List;

import retrofit2.Callback;
import retrofit2.http.GET;
import sbin.com.webserviceapp.model.Flower;

/**
 * Created by sbin on 10/13/2016.
 */

//This is needed for retrofit
// refer http://square.github.io/retrofit/
public interface FlowersAPI {
    //Only include after base URL..
    @GET("/feeds/flowers.json")
    public void getFeed(Callback<List<Flower>> response);
}
