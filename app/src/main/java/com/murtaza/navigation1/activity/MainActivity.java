package com.murtaza.navigation1.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.murtaza.navigation1.R;
import com.murtaza.navigation1.adapter.OfferAdapter;
import com.murtaza.navigation1.extra.Util;
import com.murtaza.navigation1.network.ApiUrl;
import com.murtaza.navigation1.network.VolleySingleton;
import com.murtaza.navigation1.parser.JsonParser;
import com.murtaza.navigation1.pojo.OfferListPojo;

import org.json.JSONArray;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.toolbar_layout) Toolbar mToolbar;
    @Bind(R.id.drawerLayout) DrawerLayout mDrawerLayout;
    @Bind(R.id.linearLayout) LinearLayout mLinearLayout;
    @Bind(R.id.navigation) NavigationView mNavigationView;
    @Bind(R.id.listView) ListView mListView;
    @Bind(R.id.wallet) Button mWallet;

    private ActionBarDrawerToggle mActionBarDrawerToggle;

    //Volley
    private RequestQueue mRequestQueue;
    private List<OfferListPojo> mOfferList;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("");

        //instance progress dialog
        pd = new ProgressDialog(this);
        pd.setMessage("Getting new offers");
        pd.setCancelable(true);

        //Hamburger icon
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);


        //instanciate volley
        mRequestQueue = VolleySingleton.getInstance().getRequestQueue();

        //Create SharedPrefences and store wallet data
        SharedPreferences sp = getSharedPreferences("app", MODE_PRIVATE);
        //get wallet amount
        String walletAmount = sp.getString("amount","0");
        //Toast.makeText(MainActivity.this, ""+walletAmount, Toast.LENGTH_SHORT).show();
        //set amount to WalletButton
        mWallet.setText("\u20B9 "+walletAmount);

        // check network connection
        if (!Util.isnetworkavailable(getApplication())) {
            Util.noInternetSnackbar(getApplication(), mLinearLayout);
            return;
        }


        getOfferListInBackground();

        //When wallet btn is clickecd
        mWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RedeemActivuty.class));
            }
        });

        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(getApplication()).inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshList();
                break;
        }
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    public void refreshList() {
        //check internet
        if (Util.isnetworkavailable(getApplicationContext())) {
            //fetch data
            getOfferListInBackground();
        } else {
            Util.noInternetSnackbar(getApplication(), mLinearLayout);
        }
    }

    /*
    * Method to fetch Offers List
    *
    * */


    private void getOfferListInBackground() {
        pd.show();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, ApiUrl.OFFER_LIST,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pd.dismiss();
                        mOfferList = JsonParser.offerListParse(response);
                        //check list is not null
                        if (mOfferList != null) {
                            setupOffersFromList(mOfferList);
                        } else {
                            Util.redSnackbar(getApplication(), mLinearLayout, "No new offers found");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Log.d("HUS", "HUS: " + error.getMessage());

                String errorString = VolleySingleton.handleVolleyError(error);
                if(errorString != null){
                    Util.redSnackbar(getApplication(),mLinearLayout,errorString);
                }
            }
        });

        mRequestQueue.add(request);
    }

    private void setupOffersFromList(List<OfferListPojo> mOfferList) {
        OfferAdapter adapter = new OfferAdapter(getBaseContext(), R.layout.listvieew_layout, mOfferList);
        mListView.setAdapter(adapter);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.earn_talktime:
                mDrawerLayout.closeDrawers();
                break;
            case R.id.redeem:
                mDrawerLayout.closeDrawers();
                startActivity(new Intent(MainActivity.this,RedeemActivuty.class));
                break;
            case R.id.invite:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
        }
        return true;
    }
}
