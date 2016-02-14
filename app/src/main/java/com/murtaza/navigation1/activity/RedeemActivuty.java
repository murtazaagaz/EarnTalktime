package com.murtaza.navigation1.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.murtaza.navigation1.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RedeemActivuty extends AppCompatActivity {
    @Bind(R.id.redeem_toolbar) Toolbar toolbar;
    @Bind(R.id.wallet) Button mWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_activuty);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Redeem");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Create SharedPrefences and store wallet data
        SharedPreferences sp = getSharedPreferences("app", MODE_PRIVATE);
        //get wallet amount
        String walletAmount = sp.getString("amount","0");
        //Toast.makeText(MainActivity.this, ""+walletAmount, Toast.LENGTH_SHORT).show();
        //set amount to WalletButton
        mWallet.setText("\u20B9 " + walletAmount);

    }
}
