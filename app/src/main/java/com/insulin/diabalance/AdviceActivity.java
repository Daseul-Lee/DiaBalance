package com.insulin.diabalance;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

public class AdviceActivity extends Activity {
    public static final String EXTRA_ID = "id";
    private static String url = "http://diabalance.dothome.co.kr/DiaBalance/";
    ImageView deletebtn, addbtn;
    ArrayList<String> arGeneral;
    ArrayAdapter<String> Adapter;
    Calendar date;
    JSONArray ja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()

                .detectDiskReads().detectDiskWrites().detectNetwork()

                .penaltyLog().build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()

                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()

                .penaltyLog().penaltyDeath().build());

        ListView list = (ListView)findViewById(R.id.memoList);
        date= Calendar.getInstance();
        deletebtn=(ImageView)findViewById(R.id.deletebtn);
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        addbtn=(ImageView)findViewById(R.id.addbtn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdviceActivity.this, com.insulin.diabalance.MemoActivity.class);
                intent.putExtra(com.insulin.diabalance.MemoActivity.EXTRA_ID, (String)getIntent().getExtras().get(EXTRA_ID));
                intent.putExtra(com.insulin.diabalance.MemoActivity.EXTRA_NO, (int) -1);
                startActivity(intent);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AdviceActivity.this, com.insulin.diabalance.MemoActivity.class);
                intent.putExtra(com.insulin.diabalance.MemoActivity.EXTRA_ID, (String)getIntent().getExtras().get(EXTRA_ID));
                try {
                    JSONObject jo = ja.getJSONObject(position);
                    intent.putExtra(com.insulin.diabalance.MemoActivity.EXTRA_NO, jo.getInt("MEMOID"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
                startActivity(intent);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(AdviceActivity.this);

                dialog.setMessage("해당 메모를 삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    JSONObject jo = ja.getJSONObject(position);
                                    String qry = "delete from MEMO where MEMOID = " + jo.getInt("MEMOID");

                                    String jsonString = JSONParser.getJSONFromUrl(new URL(url + "delete.php?qry=" + URLEncoder.encode(qry, "UTF-8")));

                                    Toast.makeText(AdviceActivity.this,jsonString.substring(0, jsonString.length()-1), Toast.LENGTH_SHORT).show();
                                    arGeneral.remove(position);
                                    Adapter.notifyDataSetChanged();
                                    //결과 출력

                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                }

                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();


                return true;//클릭과 롱클릭 화면 모두 뜨는 것 방지
            }
        });
        try {
            arGeneral=new ArrayList<String>();
            String qry="select * from MEMO where MEMBERID = '" +(String)getIntent().getExtras().get(EXTRA_ID)+"' ORDER BY MEMODATE";

            String jsonString = JSONParser.getJSONFromUrl(new URL(url+"select.php?qry="+ URLEncoder.encode(qry, "UTF-8")));

            String res = "";
            if (jsonString.substring(0,1).equals("[")) {
                ja = new JSONArray(jsonString);

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);

                    String tmp = jo.getString("MEMODATE");
                    String year = tmp.substring(0, 4);
                    String month = tmp.substring(4, 6);
                    String day = tmp.substring(6, 8);

                    int y = Integer.valueOf(year);
                    int m = Integer.valueOf(month);
                    int d = Integer.valueOf(day);
                    date.set(y, m - 1, d);
                    int Yoil = date.get(Calendar.DAY_OF_WEEK) - 1;
                    String YoilStr = "";
                    switch (Yoil) {
                        case 0:
                            YoilStr = "일";
                            break;
                        case 1:
                            YoilStr = "월";
                            break;
                        case 2:
                            YoilStr = "화";
                            break;
                        case 3:
                            YoilStr = "수";
                            break;
                        case 4:
                            YoilStr = "목";
                            break;
                        case 5:
                            YoilStr = "금";
                            break;
                        case 6:
                            YoilStr = "토";
                    }

                    if(jo.getString("MEMODATA").length()>80)
                        arGeneral.add(year + "년 " + month + "월 " + day + "일 (" + YoilStr + ")\n" + jo.getString("MEMODATA").substring(0,80) + "···\n");
                    else
                        arGeneral.add(year + "년 " + month + "월 " + day + "일 (" + YoilStr + ")\n" + jo.getString("MEMODATA") + "\n");

                }
            }
            Adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, arGeneral);
            list.setAdapter(Adapter);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(com.insulin.diabalance.MainActivity.EXTRA_ID, (String)getIntent().getExtras().get(EXTRA_ID));
        startActivity(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
