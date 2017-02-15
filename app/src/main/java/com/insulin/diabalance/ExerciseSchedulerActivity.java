package com.insulin.diabalance;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;


/**
 * Created by risha95 on 2016-07-13.
 */
public class ExerciseSchedulerActivity extends Activity {
    public static final String EXTRA_ID = "id";
    private static String url = "http://diabalance.dothome.co.kr/DiaBalance/";
    int year,month,day;
    Button [] DayBtn=new Button[42];
    Button LMbtn, NMbtn;
    TextView YMtxt;
    int[] month_day = {31,28,31,30,31,30,
            31,31,30,31,30,31};
    Calendar today,date;

    String id;
    String dateString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercisescheduler);
        today = Calendar.getInstance();
        date = Calendar.getInstance();

        id = (String)getIntent().getStringExtra(EXTRA_ID);

        DayBtn[0]=(Button)findViewById(R.id.day1);
        DayBtn[1]=(Button)findViewById(R.id.day2);
        DayBtn[2]=(Button)findViewById(R.id.day3);
        DayBtn[3]=(Button)findViewById(R.id.day4);
        DayBtn[4]=(Button)findViewById(R.id.day5);
        DayBtn[5]=(Button)findViewById(R.id.day6);
        DayBtn[6]=(Button)findViewById(R.id.day7);
        DayBtn[7]=(Button)findViewById(R.id.day8);
        DayBtn[8]=(Button)findViewById(R.id.day9);
        DayBtn[9]=(Button)findViewById(R.id.day10);
        DayBtn[10]=(Button)findViewById(R.id.day11);
        DayBtn[11]=(Button)findViewById(R.id.day12);
        DayBtn[12]=(Button)findViewById(R.id.day13);
        DayBtn[13]=(Button)findViewById(R.id.day14);
        DayBtn[14]=(Button)findViewById(R.id.day15);
        DayBtn[15]=(Button)findViewById(R.id.day16);
        DayBtn[16]=(Button)findViewById(R.id.day17);
        DayBtn[17]=(Button)findViewById(R.id.day18);
        DayBtn[18]=(Button)findViewById(R.id.day19);
        DayBtn[19]=(Button)findViewById(R.id.day20);
        DayBtn[20]=(Button)findViewById(R.id.day21);
        DayBtn[21]=(Button)findViewById(R.id.day22);
        DayBtn[22]=(Button)findViewById(R.id.day23);
        DayBtn[23]=(Button)findViewById(R.id.day24);
        DayBtn[24]=(Button)findViewById(R.id.day25);
        DayBtn[25]=(Button)findViewById(R.id.day26);
        DayBtn[26]=(Button)findViewById(R.id.day27);
        DayBtn[27]=(Button)findViewById(R.id.day28);
        DayBtn[28]=(Button)findViewById(R.id.day29);
        DayBtn[29]=(Button)findViewById(R.id.day30);
        DayBtn[30]=(Button)findViewById(R.id.day31);
        DayBtn[31]=(Button)findViewById(R.id.day32);
        DayBtn[32]=(Button)findViewById(R.id.day33);
        DayBtn[33]=(Button)findViewById(R.id.day34);
        DayBtn[34]=(Button)findViewById(R.id.day35);
        DayBtn[35]=(Button)findViewById(R.id.day36);
        DayBtn[36]=(Button)findViewById(R.id.day37);
        DayBtn[37]=(Button)findViewById(R.id.day38);
        DayBtn[38]=(Button)findViewById(R.id.day39);
        DayBtn[39]=(Button)findViewById(R.id.day40);
        DayBtn[40]=(Button)findViewById(R.id.day41);
        DayBtn[41]=(Button)findViewById(R.id.day42);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()

                .detectDiskReads().detectDiskWrites().detectNetwork()

                .penaltyLog().build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()

                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()

                .penaltyLog().penaltyDeath().build());

        YMtxt=(TextView)findViewById(R.id.YMtxt);

        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH);
        day = today.get(Calendar.DAY_OF_MONTH);

        YMtxt.setText(year + "-" + month);

        LMbtn=(Button)findViewById(R.id.LMbtn);
        LMbtn.setOnClickListener(ButtonListener);

        NMbtn=(Button)findViewById(R.id.NMbtn);
        NMbtn.setOnClickListener(ButtonListener);

        NMbtn.callOnClick();
        TextView bstxt=(TextView)findViewById(R.id.bstxt);
        TextView bstxt2=(TextView)findViewById(R.id.bstxt2);

        String todayString = String.format("%04d%02d%02d", year, month, day);
        try {
            String qry = "select * from BS where MEMBERID = '" + (String) getIntent().getExtras().get(EXTRA_ID) + "' and BSDATE= '"+todayString+"' ORDER BY BSTYPE";

            String jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));
            if (jsonString.substring(0, 1).equals("[")) {
                JSONArray ja = new JSONArray(jsonString);
                JSONObject jo = ja.getJSONObject(ja.length() - 1);
                bstxt.setText(" 현재 측정 혈당이 "+jo.getInt("BSLEVELS")+"mg/dl이므로");
                if(60>=jo.getInt("BSLEVELS")||jo.getInt("BSLEVELS")>=290) {
                    bstxt2.setText("운동을 중지해주시기 바랍니다.");
                    bstxt2.setTextColor(Color.RED);
                } else
                    bstxt2.setText("운동을 하셔도 좋습니다.");
            }
            else
            {
                bstxt.setText("오늘 측정된 혈당치가 없습니다.\n혈당을 측정해주세요!");
                bstxt2.setText("");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }  catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }
    View.OnClickListener ButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int x=-1;

            if(v.getId()==R.id.NMbtn)
                x=1;

            year=year-1+(12+month+x-1)/12;
            month=(12+month+x-1)%12+1;

            YMtxt.setText(year + "-" + month);

            if ((year % 4 == 0 && year % 100 == 0) || year % 400 == 0) {
                month_day[1] = 29;
            } else
                month_day[1] = 28;

            int i = 0;
            date.set(year, month - 1, 1);
            int Yoil = date.get(Calendar.DAY_OF_WEEK) - 1;

            for (; i < Yoil; i++) {
                DayBtn[i].setBackground(getResources().getDrawable(R.drawable.alpha));
                DayBtn[i].setText("");
            }
            try {
                dateString = String.format("%04d%02d%02d", year, month, 1);
                String qry = "select * from EXERCISE where MEMBERID = '" + id + "' AND EXERCISEDATE LIKE '" + dateString.substring(0, 6) + "%' ORDER BY EXERCISEDATE";
                String jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));
                int k = 0;
                for (int j = 1; j <= month_day[month - 1]; j++) {
                    date.set(year, month - 1, j);
                    int t = today.get(Calendar.YEAR) * 365 + today.get(Calendar.DAY_OF_YEAR);
                    int d = date.get(Calendar.YEAR) * 365 + date.get(Calendar.DAY_OF_YEAR)-1;

                    dateString = String.format("%04d%02d%02d", year, month, j);
                    if (t <= d) {
                        DayBtn[i].setBackground(getResources().getDrawable(R.drawable.alpha));
                    } else if (jsonString.substring(0, 1).equals("[")) {
                        JSONArray ja = new JSONArray(jsonString);
                        if (k >= ja.length()) {
                            DayBtn[i].setBackground(getResources().getDrawable(R.drawable.x));
                        } else {
                            JSONObject jo = ja.getJSONObject(k);
                            if (jo.getString("EXERCISEDATE").equals(dateString)) {
                                k++;
                                if (jo.getInt("EXERCISESUCCESS") == 1) {
                                    DayBtn[i].setBackground(getResources().getDrawable(R.drawable.o));
                                } else {
                                    DayBtn[i].setBackground(getResources().getDrawable(R.drawable.x));
                                }
                            } else {
                                DayBtn[i].setBackground(getResources().getDrawable(R.drawable.x));
                            }
                        }
                    } else {
                        DayBtn[i].setBackground(getResources().getDrawable(R.drawable.x));
                    }
                    DayBtn[i++].setText(j + "");
                }
            }catch(JSONException e){
                // TODO Auto-generated catch block
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }

            for (; i < 42; i++) {
                DayBtn[i].setBackground(getResources().getDrawable(R.drawable.alpha));
                DayBtn[i].setText("");
            }
        }
    };
}

