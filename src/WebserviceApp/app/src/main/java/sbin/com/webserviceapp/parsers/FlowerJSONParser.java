package sbin.com.webserviceapp.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sbin.com.webserviceapp.model.Flower;

/**
 * Created by sbin on 10/12/2016.
 */

//This is parsing JSON object.
public class FlowerJSONParser {
    public static List<Flower> parseFeed(String content){

        try {
            JSONArray jsonArray = new JSONArray(content);
            List<Flower> flowerList = new ArrayList<>();

            for (int i =0; i< jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Flower flower = new Flower();

                //productId has to match json object from http://services.hanselandpetal.com/feeds/flowers.json
                flower.setCategory(jsonObject.getString("category"));
                flower.setPrice(jsonObject.getDouble("price"));
                flower.setInstructions(jsonObject.getString("instructions"));
                flower.setPhoto(jsonObject.getString("photo"));
                flower.setName(jsonObject.getString("name"));
                flower.setProductId(jsonObject.getInt("productId"));

                flowerList.add(flower);
            }

            return flowerList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
