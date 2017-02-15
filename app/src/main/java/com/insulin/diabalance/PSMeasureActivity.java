package com.insulin.diabalance;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Toast;


import com.macroyau.thingspeakandroid.ThingSpeakChannel;
import com.macroyau.thingspeakandroid.ThingSpeakService;
import com.macroyau.thingspeakandroid.model.ChannelFeed;
import com.macroyau.thingspeakandroid.model.Feed;

import java.util.Date;



/**
 * Created by risha95 on 2016-07-13.
 */
public class PSMeasureActivity extends Activity {
    private ThingSpeakChannel tsChannel;
    public static final String EXTRA_ID = "ID";
    private SQLiteDatabase db;
    private Cursor cursor;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psmeasure);

//        try {
//            SQLiteOpenHelper Database = new Database(this);
//            db = Database.getReadableDatabase();
//        } catch(SQLiteException e) {
//            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
//            toast.show();
//        }
//
//        int channelID=0;
//        String readAPI="";
//        try {
//            cursor = db.query("MEMBER",
//                    new String[]{"CHANNEL","READAPI"},
//                    "ID = ?", new String[]{(String)getIntent().getExtras().get(EXTRA_ID)}, null, null, null);
//
//            if (cursor.moveToFirst()) {
//                channelID=cursor.getInt(cursor.getColumnIndex("CHANNEL"));
//                readAPI=cursor.getString(cursor.getColumnIndex("READAPI"));
//            }
//            cursor.close();
//        } catch(SQLiteException e) {
//            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
//            toast.show();
//        }
////        Toast toast = Toast.makeText(this, chnnelID+"", Toast.LENGTH_SHORT);
////        toast.show();
//        // Connect to ThinkSpeak Channel 9
//
//        tsChannel = new ThingSpeakChannel(channelID,readAPI);
//
//        // Set listener for Channel feed update events
//        tsChannel.setChannelFeedUpdateListener(new ThingSpeakChannel.ChannelFeedUpdateListener() {
//            @Override
//            public void onChannelFeedUpdated(long channelId, String channelName, ChannelFeed channelFeed) {
//                // Show Channel ID and name on the Action Bar
//                // Notify last update time of the Channel feed through a Toast message
//                Date lastUpdate = channelFeed.getChannel().getUpdatedAt();
//                Toast.makeText(PSMeasureActivity.this, lastUpdate.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//        tsChannel.setChannelFieldFeedUpdateListener(new ThingSpeakChannel.ChannelFieldFeedUpdateListener() {
//            @Override
//            public void onChannelFieldFeedUpdated(long channelId, int fieldId, ChannelFeed channelFeed) {
//                Feed feed = channelFeed.getFeeds().get(0);
//
//                Toast.makeText(PSMeasureActivity.this, feed.getField1().toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//        tsChannel.loadChannelFeed();
//        tsChannel.loadChannelFieldFeed(1);
    }
}
