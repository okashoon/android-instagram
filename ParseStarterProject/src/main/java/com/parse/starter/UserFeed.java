package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UserFeed extends AppCompatActivity {

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        linearLayout = (LinearLayout)findViewById(R.id.linear_layout);

        Intent i = getIntent();
        String name = i.getStringExtra("name");
        setTitle(name +"'s" + " feed");


        ParseQuery<ParseObject> object = new ParseQuery<ParseObject>("Images");
        object.whereEqualTo("username", name);
        object.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size()>0) {
                        for(ParseObject object : objects){
                            ParseFile file = object.getParseFile("image");
                            final String objectName = object.getString("username");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e == null){

                                        Bitmap image = BitmapFactory.decodeByteArray(data,0,data.length);

                                        ImageView imageView = new ImageView(getApplicationContext());
                                        imageView.setImageBitmap(image);

                                        linearLayout.addView(imageView);
                                        Log.i("aaa",objectName + "'s image loaded");


                                    } else {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } else {
                        Log.i("aaa","nothing returned from querying images");
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });

    }
}
