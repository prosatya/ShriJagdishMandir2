package com.matictechnology.shrijagdishmandir.Utility;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.matictechnology.shrijagdishmandir.Activity.ActivityNotification;
import com.matictechnology.shrijagdishmandir.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maticd1 on 19/3/16.
 */
public class MainCardFragment extends Fragment
{
    //fragment class to show fragments as per user selection
    private static final String ARG_POSITION = "position";

    private int position;   //position flag to get the count of current fragment selection

    public static MainCardFragment newInstance(int position)
    {
        MainCardFragment f = new MainCardFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //getting current fragment count
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //getting layout inflater service
        LayoutInflater inflater1 = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View itemView1=null;
        if(position==0)
        {
            itemView1=inflater1.inflate(R.layout.pager_main1, null);

            //inflating main fragment of the activity
            AdView adView = (AdView)itemView1.findViewById(R.id.adView);
            // Request for Ads
            AdRequest adRequest = new AdRequest.Builder().build();
            // Load ads into Banner Ads
            adView.loadAd(adRequest);
        }
        else if(position==1)
        {
            itemView1=inflater1.inflate(R.layout.pager_main2, null);

            //inflating second fragment of the activity
            AdView adView = (AdView)itemView1.findViewById(R.id.adView);
            // Request for Ads
            AdRequest adRequest = new AdRequest.Builder().build();
            // Load ads into Banner Ads
            adView.loadAd(adRequest);
        }
        else if(position==2)
        {
            itemView1=inflater1.inflate(R.layout.pager_main3, null);

            //inflating third fragment of the activity
            AdView adView = (AdView)itemView1.findViewById(R.id.adView);
            // Request for Ads
            AdRequest adRequest = new AdRequest.Builder().build();
            // Load ads into Banner Ads
            adView.loadAd(adRequest);
        }
        else if(position==3)
        {
            itemView1=inflater1.inflate(R.layout.pager_main4, null);

            //inflating fourth fragment of the activity
            AdView adView = (AdView)itemView1.findViewById(R.id.adView);
            // Request for Ads
            AdRequest adRequest = new AdRequest.Builder().build();
            // Load ads into Banner Ads
            adView.loadAd(adRequest);

            ImageView kfb,kli,jfb,jfbg;
            kfb=(ImageView)itemView1.findViewById(R.id.kfb);
            kli=(ImageView)itemView1.findViewById(R.id.kli);
            jfb=(ImageView)itemView1.findViewById(R.id.jfb);
            jfbg=(ImageView)itemView1.findViewById(R.id.jfbg);

            //facebook page button listener
            kfb.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    String url = "https://www.facebook.com/khatisamaj";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

            //twitter page button listener
            kli.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    String url = "https://twitter.com/khatisamaj";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

            //facebook page button listener
            jfb.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    String url = "https://www.facebook.com/jagdishmandirujjain";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

            //facebook group button listener
            jfbg.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    String url = "https://www.facebook.com/groups/khateesamaj";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });


        }
        return itemView1;
    }
}