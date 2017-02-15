package com.insulin.diabalance;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by risha95 on 2016-07-25.
 */
public class MemoActivity  extends Activity {
    public static final String EXTRA_NO = "NO";
    public static final String EXTRA_ID = "ID";
    private static String url = "http://diabalance.dothome.co.kr/DiaBalance/";
    Button savebtn, backbtn;
    EditText dataTxt;

    Calendar today;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()

                .detectDiskReads().detectDiskWrites().detectNetwork()

                .penaltyLog().build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()

                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()

                .penaltyLog().penaltyDeath().build());

        final DatePicker memodatepicker = (DatePicker) findViewById(R.id.memodatepicker);
        dataTxt = (EditText) findViewById(R.id.dataTxt);
        if(!(getIntent().getExtras().get(EXTRA_NO).equals(-1))) {
            int memoNo = (Integer) getIntent().getExtras().get(EXTRA_NO);
            try {
                String qry = "select * from MEMO where MEMOID = '" + memoNo + "'";

                String jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));

                if (jsonString.substring(0,1).equals("[")) {
                    JSONArray ja = new JSONArray(jsonString);
                    JSONObject jo = ja.getJSONObject(0);
                    String memoData = jo.getString("MEMODATA");
                    String memoDate = jo.getString("MEMODATE");

                    int year =  Integer.parseInt(memoDate.substring(0, 4));
                    int month = Integer.parseInt(memoDate.substring(4, 6))-1;
                    int day = Integer.parseInt(memoDate.substring(6, 8));
                    memodatepicker.updateDate(year, month, day);

                    dataTxt.setText(memoData);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }

        savebtn=(Button)findViewById(R.id.savebtn);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String memodata= dataTxt.getText().toString();
                    int year=memodatepicker.getYear();
                    int month=memodatepicker.getMonth()+1;
                    int day=memodatepicker.getDayOfMonth();
                    String memodate =String.format("%04d%02d%02d", year, month, day);
                    if(!(getIntent().getExtras().get(EXTRA_NO).equals(-1))) {
                        int memoNo = (Integer) getIntent().getExtras().get(EXTRA_NO);
                        URL text=new URL(url + "updateMEMO.php?memoid=" + memoNo
                                + "&memodata=" + URLEncoder.encode(memodata, "UTF-8")
                                + "&memodate=" + URLEncoder.encode(memodate, "UTF-8"));
                        text.openStream();
                        HttpURLConnection conn = (HttpURLConnection)text.openConnection();
                        conn.connect();
                        Toast.makeText(getApplicationContext(), "수정된 상담 기록의 저장이 완료되었습니다.", Toast.LENGTH_LONG).show();
                        conn.disconnect();
                    }
                    else {
                        String memberId= (String)getIntent().getExtras().get(EXTRA_ID);

                        URL text=new URL(url + "insertMEMO.php?memberid=" + URLEncoder.encode(memberId, "UTF-8")
                                + "&memodata=" + URLEncoder.encode(memodata, "UTF-8")
                                + "&memodate=" + URLEncoder.encode(memodate, "UTF-8"));
                        text.openStream();
                        HttpURLConnection conn = (HttpURLConnection)text.openConnection();
                        conn.connect();

                        Toast.makeText(getApplicationContext(), "추가된 상담 기록의 저장이 완료되었습니다.", Toast.LENGTH_LONG).show();
                        conn.disconnect();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(getApplicationContext(), AdviceActivity.class);
                intent.putExtra(com.insulin.diabalance.AdviceActivity.EXTRA_ID, (String)getIntent().getExtras().get(EXTRA_ID));
                startActivity(intent);
            }
        });

        backbtn=(Button)findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdviceActivity.class);
                intent.putExtra(com.insulin.diabalance.AdviceActivity.EXTRA_ID, (String)getIntent().getExtras().get(EXTRA_ID));
                startActivity(intent);
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
