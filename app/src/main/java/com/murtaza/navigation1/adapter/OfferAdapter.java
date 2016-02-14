package com.murtaza.navigation1.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.murtaza.navigation1.R;
import com.murtaza.navigation1.network.VolleySingleton;
import com.murtaza.navigation1.pojo.OfferListPojo;

import org.w3c.dom.Text;

import java.util.List;

public class OfferAdapter extends ArrayAdapter<OfferListPojo> {
    List<OfferListPojo> mList;
    private Context mContext;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public OfferAdapter(Context context, int resource, List<OfferListPojo> objects) {
        super(context, resource, objects);
        this.mList = objects;
        this.mContext = context;
        mRequestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.listvieew_layout,parent,false);

        Log.d("HUS","HUS: getView "+position);

        TextView textView = (TextView) view.findViewById(R.id.title);
        Button button = (Button) view.findViewById(R.id.button);
        final ImageView imageView = (ImageView) view.findViewById(R.id.image);
        final OfferListPojo current = mList.get(position);
        TextView titleAmount = (TextView) view.findViewById(R.id.display_amount);

        //Load image
        mImageLoader = VolleySingleton.getInstance().getImageLoader();

        mImageLoader.get(current.getImage(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                imageView.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                imageView.setImageResource(R.drawable.placeholder80);
            }
        });

        textView.setText(current.getTitle());
        //set amount
        titleAmount.append(current.getAmount());

        //when get it button is clicked
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add amount to sharedPrefrences
                SharedPreferences sp = mContext.getSharedPreferences("app", Context.MODE_PRIVATE);
                //get amount from sp
                String amount = sp.getString("amount","0");
                int newAmount = Integer.parseInt(amount) + Integer.parseInt(current.getAmount());
                //store new amount in shared prefrences
                sp.edit().putString("amount",newAmount+"").apply();

                //open browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(current.getLink()));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(browserIntent);

            }
        });
        //button.setText(current.getAmount());

        return view;
    }
}
