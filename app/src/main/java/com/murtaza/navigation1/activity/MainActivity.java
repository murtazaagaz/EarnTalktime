package com.murtaza.navigation1.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.murtaza.navigation1.R;
import com.murtaza.navigation1.network.ApiUrl;
import com.murtaza.navigation1.network.VolleySingleton;
import com.murtaza.navigation1.parser.JsonParser;
import com.murtaza.navigation1.pojo.OfferListPojo;

import org.json.JSONArray;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar_layout)
    Toolbar mToolbar;
    @Bind(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    //Volley
    private RequestQueue mRequestQueue;
    private List<OfferListPojo> mOfferList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("");

        //Hamburger icon
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        //instanciate volley
        mRequestQueue = VolleySingleton.getInstance().getRequestQueue();


        getOfferListInBackground();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    /*
    * Method to fetch Offers List
    *
    * */
    private void getOfferListInBackground() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, ApiUrl.OFFER_LIST, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                mOfferList = JsonParser.offerListParse(response);
                //check list is not null
                if(mOfferList != null){
                    Toast.makeText(MainActivity.this,""+mOfferList.size(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("HUS", "HUS: " + error.getMessage());
            }
        });

        mRequestQueue.add(request);
    }
}
