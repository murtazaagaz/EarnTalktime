package com.murtaza.navigation1.extra;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.murtaza.navigation1.R;

public class Util  {
    public static void redSnackbar(Context context, View layout, String text) {
        Snackbar snackbar = Snackbar.make(layout, text, Snackbar.LENGTH_LONG);
        View snack = snackbar.getView();
        snack.setBackgroundColor(context.getResources().getColor(R.color.error_color));
        snackbar.show();
    }

    public static void noInternetSnackbar(Context context, View layout) {
        final Snackbar snack = Snackbar.make(layout, R.string.no_internet_connection, Snackbar.LENGTH_INDEFINITE);
        snack.setAction(R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss Snackbar
                snack.dismiss();
            }
        });
        snack.show();
    }

    //Method to check internet is connected or not
    public static boolean isnetworkavailable(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isavailable = false;
        if (networkInfo!= null && networkInfo.isConnected()) {
            isavailable= true;
        }
        return isavailable;
    }
}
