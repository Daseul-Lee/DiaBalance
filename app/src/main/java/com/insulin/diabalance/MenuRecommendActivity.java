package com.insulin.diabalance;

import android.app.Activity;
import android.content.Intent;
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
import org.w3c.dom.Text;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by risha95 on 2016-07-13.
 */
public class MenuRecommendActivity extends Activity{
    public static final String EXTRA_ID = "id";
    private static String url = "http://diabalance.dothome.co.kr/DiaBalance/";
    String [][] SicDan={{"현미 닭죽","꽁치 된장 구이","날치알 얹은 가지 구이", "부추무침"},
            {"표고버섯밥","감자 부추잡채","고추구이","비트나물"},
            {"아스파라거스 스프","양송이 오븐구이","비타민 오렌지 샐러드","포도 19알"},
            {"컬러플라워 죽","감자 오믈렛","더덕나물","배추김치"},
            {"현미밥 1/2공기","고등어 양념구이","된장찌개","깻잎나물"},
            {"해초 비빔밥","대추 하수오 유정란 조림", "양파 무침","열무 물김치"},
            {"현미밥 1/2공기","꽃게와 배추탕","고추구이","감자찜"},
            {"현미 잡곡밥 1/2공기","된장찌개","다시마와 북어볶음","마구이"},
            {"현미밥 1/2공기","고기구이와 마늘 소스", "가지찜", "꽈리고추 무침"},
            {"현미밥 1/2공기","콩나물국","대하 청경채 볶음","표고나물"},
            {"현미 잡곡밥 1/2공기","녹차 비지찌개","생더덕무침","가지구이와 된장 소스"},
            {"현미밥 1/3공기","북어국","소고기구이","땅콩조림"},
            {"현미 잡곡밥 1/2공기","생선냉이전","전복구이","두릅무침"},
            {"흑미 생선죽","오이와 죽순 된장 소스 무침","시금치 무침","양배추 겉절이"},
            {"현미밥 1/2공기","고등어 피클액조림","참나물무침","무숙채"},
            {"현미밥", "닭찜","버섯볶음","실파무침"}};
    Calendar today;
    int year, month, day;
    String todayString;
    int m=-1,a=-1,e=-1;
    String id;
    JSONArray ja;
    Button saveMenubtn;
    TextView [] msdtxt= new TextView[4];
    TextView [] asdtxt= new TextView[4];
    TextView [] esdtxt= new TextView[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menurecommend);
        Button MLbtn = (Button) findViewById(R.id.MLbtn);
        Button MRbtn = (Button) findViewById(R.id.MRbtn);
        Button ALbtn = (Button) findViewById(R.id.ALbtn);
        Button ARbtn = (Button) findViewById(R.id.ARbtn);
        Button ELbtn = (Button) findViewById(R.id.ELbtn);
        Button ERbtn = (Button) findViewById(R.id.ERbtn);
        saveMenubtn = (Button) findViewById(R.id.saveMenubtn);
        TextView sicdantitle =(TextView)findViewById(R.id.sicdantitle);
        id = (String) getIntent().getExtras().get(EXTRA_ID);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()

                .detectDiskReads().detectDiskWrites().detectNetwork()

                .penaltyLog().build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()

                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()

                .penaltyLog().penaltyDeath().build());
        try {
            String qry = "select MEMBERNAME from MEMBER where MEMBERID = '" + id + "'";

            String jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));
            if (jsonString.substring(0, 1).equals("[")) {
                JSONArray ja = new JSONArray(jsonString);
                JSONObject jo = ja.getJSONObject(0);
                sicdantitle.setText(jo.getString("MEMBERNAME")+"님의 오늘 추천 식단");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        ja = new JSONArray();
        today = Calendar.getInstance();


        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH) + 1;
        day = today.get(Calendar.DAY_OF_MONTH);
        todayString = String.format("%04d%02d%02d", year, month, day);

        MLbtn.setOnClickListener(ButtonListener);
        MRbtn.setOnClickListener(ButtonListener);

        ALbtn.setOnClickListener(ButtonListener);
        ARbtn.setOnClickListener(ButtonListener);

        ELbtn.setOnClickListener(ButtonListener);
        ERbtn.setOnClickListener(ButtonListener);

        msdtxt[0] = (TextView) findViewById(R.id.msdtxt);
        msdtxt[1] = (TextView) findViewById(R.id.msdtxt1);
        msdtxt[2] = (TextView) findViewById(R.id.msdtxt2);
        msdtxt[3] = (TextView) findViewById(R.id.msdtxt3);

        asdtxt[0]= (TextView) findViewById(R.id.asdtxt);
        asdtxt[1]= (TextView) findViewById(R.id.asdtxt1);
        asdtxt[2]= (TextView) findViewById(R.id.asdtxt2);
        asdtxt[3]= (TextView) findViewById(R.id.asdtxt3);

        esdtxt[0] = (TextView) findViewById(R.id.esdtxt);
        esdtxt[1] = (TextView) findViewById(R.id.esdtxt1);
        esdtxt[2] = (TextView) findViewById(R.id.esdtxt2);
        esdtxt[3] = (TextView) findViewById(R.id.esdtxt3);

        for(int i=0; i<4; i++) {
            msdtxt[i].setOnClickListener(TextListener);
            asdtxt[i].setOnClickListener(TextListener);
            esdtxt[i].setOnClickListener(TextListener);
        }

        try {
            String qry = "select * from MENURECOMMEND where MEMBERID = '" + id + "' AND RECOMMENDDATE = '" + todayString + "'";

            String jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));
            if (jsonString.substring(0, 1).equals("[")) {
                ja = new JSONArray(jsonString);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    if (jo.getInt("CHOICE") == 1) {
                        if (jo.getInt("MENURECOMMENDTYPE") == 0)
                            m = (ja.length() + i - 1) % ja.length();
                        else if (jo.getInt("MENURECOMMENDTYPE") == 1)
                            a = (ja.length() + i - 1) % ja.length();
                        else if (jo.getInt("MENURECOMMENDTYPE") == 2)
                            e = (ja.length() + i - 1) % ja.length();
                        saveMenubtn.setEnabled(false);
                    }
                }
            } else {
                int[] check = new int[SicDan.length];
                int i;
                for (i = 0; i < SicDan.length; i++)
                    check[i] = 0;
                i = 0;
                for (i = 0; i < 3; i++) {
                    int j = 0;
                    while (j < 3) {
                        int r = (int) (Math.random() * SicDan.length);

                        if (check[r] == 0) {
                            URL text = new URL(url + "insertMENURECOMMEND.php?memberid=" + URLEncoder.encode(id, "UTF-8")
                                    + "&menunumber=" + r
                                    + "&recommenddate=" + URLEncoder.encode(todayString, "UTF-8")
                                    + "&menurecommendtype=" + i);
                            text.openStream();
                            HttpURLConnection conn = (HttpURLConnection) text.openConnection();
                            conn.connect();
                            conn.disconnect();
                            check[r] = 1;
                            j++;
                        }
                    }
                }
                qry = "select * from MENURECOMMEND where MEMBERID = '" + id + "' AND RECOMMENDDATE = '" + todayString + "'";

                jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));
                if (jsonString.substring(0, 1).equals("[")) {
                    ja = new JSONArray(jsonString);
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        MRbtn.callOnClick();
        ARbtn.callOnClick();
        ERbtn.callOnClick();
        saveMenubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jo = ja.getJSONObject(a);

                    URL text = new URL(url + "updateMENURECOMMEND.php?memberid=" + URLEncoder.encode(id, "UTF-8")
                            + "&menunumber=" + jo.getInt("MENUNUMBER")
                            + "&recommenddate=" + URLEncoder.encode(todayString, "UTF-8")
                            + "&menurecommendtype=" + 1);
                    text.openStream();
                    HttpURLConnection conn = (HttpURLConnection) text.openConnection();
                    conn.connect();
                    conn.disconnect();

                    jo = ja.getJSONObject(e);
                    text = new URL(url + "updateMENURECOMMEND.php?memberid=" + URLEncoder.encode(id, "UTF-8")
                            + "&menunumber=" + jo.getInt("MENUNUMBER")
                            + "&recommenddate=" + URLEncoder.encode(todayString, "UTF-8")
                            + "&menurecommendtype=" + 2);
                    text.openStream();
                    conn = (HttpURLConnection) text.openConnection();
                    conn.connect();
                    conn.disconnect();

                    jo = ja.getJSONObject(m);
                    text = new URL(url + "updateMENURECOMMEND.php?memberid=" + URLEncoder.encode(id, "UTF-8")
                            + "&menunumber=" + jo.getInt("MENUNUMBER")
                            + "&recommenddate=" + URLEncoder.encode(todayString, "UTF-8")
                            + "&menurecommendtype=" + 0);
                    text.openStream();
                    conn = (HttpURLConnection) text.openConnection();
                    conn.connect();
                    conn.disconnect();

                    for(int i=0; i<4; i++) {
                        msdtxt[i].setTextColor(Color.RED);
                        asdtxt[i].setTextColor(Color.RED);
                        esdtxt[i].setTextColor(Color.RED);
                    }

                    Toast.makeText(getApplicationContext(), "저장이 완료되었습니다.", Toast.LENGTH_LONG).show();
                    saveMenubtn.setEnabled(false);

                    String qry = "select * from MENURECOMMEND where MEMBERID = '" + id + "' AND RECOMMENDDATE = '" + todayString + "'";

                    String jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));
                    if (jsonString.substring(0, 1).equals("[")) {
                        ja = new JSONArray(jsonString);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    View.OnClickListener ButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView [] sdtxt =msdtxt;
            int x=m, y=-1, type=0;
            switch (v.getId()) {
                case R.id.MRbtn:
                    y=1;
                    
                    break;
                case R.id.ARbtn:
                    y=1;
                case R.id.ALbtn:
                    sdtxt = asdtxt;
                    x=a;
                    type=1;
                    break;
                case R.id.ERbtn:
                    y=1;
                case R.id.ELbtn:
                    sdtxt =esdtxt;
                    x=e;
                    type=2;
                    break;
            }
            JSONObject jo;

            try {
                int color;
                while (true) {
                    x = (ja.length() + x + y) % ja.length();

                    jo = ja.getJSONObject(x);
                    if (jo.getInt("MENURECOMMENDTYPE") == type) {

                        if (jo.getInt("CHOICE") == 1)
                            color=Color.RED;
                        else
                            color=Color.rgb(111,111,111);
                        break;
                    }
                }
                for (int j = 0; j < 4; j++) {
                    sdtxt[j].setTextColor(color);
                    sdtxt[j].setText(SicDan[jo.getInt("MENUNUMBER")][j]);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
            saveMenubtn.setEnabled(true);
            switch (v.getId()) {
                case R.id.MLbtn:
                case R.id.MRbtn:
                    m=x;
                    break;
                case R.id.ALbtn:
                case R.id.ARbtn:
                    a=x;
                    break;
                case R.id.ELbtn:
                case R.id.ERbtn:
                    e=x;
                    break;
            }
        }
    };

    View.OnClickListener TextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView tmptxt = (TextView) findViewById(v.getId());
            Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
            intent.putExtra(com.insulin.diabalance.RecipeActivity.EXTRA_ID, (String) getIntent().getExtras().get(EXTRA_ID));
            intent.putExtra(com.insulin.diabalance.RecipeActivity.EXTRA_TEXT, tmptxt.getText());
            startActivity(intent);

        }
    };
}
