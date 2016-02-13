package com.murtaza.navigation1.parser;

import android.util.Log;

import com.murtaza.navigation1.pojo.OfferListPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    private static final String TAG = JsonParser.class.getSimpleName();

    public static List<OfferListPojo> offerListParse(JSONArray jsonArray){
        //check size of jsonArray
        if(jsonArray.length() >= 1){
            //loop through
            List<OfferListPojo> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    OfferListPojo pojo = new OfferListPojo();
                    pojo.setId(jo.getString("id"));
                    pojo.setTitle(jo.getString("title"));
                    pojo.setActive(jo.getString("active"));
                    pojo.setAmount(jo.getString("amount"));
                    pojo.setImage(jo.getString("icon"));
                    pojo.setLink(jo.getString("link"));
                    pojo.setTime(jo.getString("time"));

                    list.add(pojo);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG,"HUS: offerListParse "+e.getMessage());
                    return null;
                }
            }

            return list;
        }else{
            //Return null
            return null;
        }
    }
}
