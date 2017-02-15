package com.insulin.diabalance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Created by risha95 on 2016-10-07.
 */
public class RecipeSaveActivity  extends Activity {
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_DATE = "date";
    private static String url = "http://diabalance.dothome.co.kr/DiaBalance/";
    Calendar today;
    int year,month,day;
    TextView sicdantitle;
    ImageView morningImage, lunchImage, eveningImage;
    int type=0;
    TextView [] sdtxt= new TextView[4];
    TextView [] caltxt= new TextView[4];
    Button [] Recipebtn= new Button[4];
    String id;
    String [][] SicDan={{"현미 닭죽","꽁치 된장 구이","날치알 얹은 가지구이", "부추무침"},
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
    JSONArray ja, recipeja;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipesave);
        today = Calendar.getInstance();
        year= today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH)+1;
        day = today.get(Calendar.DAY_OF_MONTH);
        sicdantitle=(TextView)findViewById(R.id.sicdantitle);
        id = (String) getIntent().getExtras().get(EXTRA_ID);
        if(getIntent().hasExtra(EXTRA_DATE)) {
            String date =(String) getIntent().getExtras().get(EXTRA_DATE);
            year =  Integer.parseInt(date.substring(0, 4));
            month =  Integer.parseInt(date.substring(4, 6));
            day =  Integer.parseInt(date.substring(6, 8));
        }
        sicdantitle.setText(year + "년 " + month + "월 " + day + "일 아침 식단");
        final String dateString =String.format("%04d%02d%02d", year, month, day);
        Button dateselectionbtn =(Button)findViewById(R.id.dateselectionbtn);
        dateselectionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DateSelectionActivity.class);
                intent.putExtra(com.insulin.diabalance.DateSelectionActivity.EXTRA_ID, (String)getIntent().getExtras().get(EXTRA_ID));
                intent.putExtra(com.insulin.diabalance.DateSelectionActivity.EXTRA_DATE, dateString);
                startActivity(intent);
            }
        });
        morningImage=(ImageView)findViewById(R.id.morningImage);
        lunchImage =(ImageView)findViewById(R.id.lunchImage);
        eveningImage=(ImageView)findViewById(R.id.eveningImage);


        morningImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=0;
                morningImage.setImageDrawable(getResources().getDrawable(R.drawable.onm));
                lunchImage.setImageDrawable(getResources().getDrawable(R.drawable.offl));
                eveningImage.setImageDrawable(getResources().getDrawable(R.drawable.offe));
                sicdantitle.setText(year + "년 " + month + "월 " + day + "일 아침 식단");
                sicdanChange();
            }
        });

        lunchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                morningImage.setImageDrawable(getResources().getDrawable(R.drawable.offm));
                lunchImage.setImageDrawable(getResources().getDrawable(R.drawable.onl));
                eveningImage.setImageDrawable(getResources().getDrawable(R.drawable.offe));
                sicdantitle.setText(year + "년 " + month + "월 " + day + "일 점심 식단");
                sicdanChange();
            }
        });
        eveningImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=2;
                morningImage.setImageDrawable(getResources().getDrawable(R.drawable.offm));
                lunchImage.setImageDrawable(getResources().getDrawable(R.drawable.offl));
                eveningImage.setImageDrawable(getResources().getDrawable(R.drawable.one));
                sicdantitle.setText(year + "년 " + month + "월 " + day + "일 저녁 식단");
                sicdanChange();
            }
        });

        sdtxt[0]=(TextView)findViewById(R.id.sdtxt);
        sdtxt[1]=(TextView)findViewById(R.id.sdtxt1);
        sdtxt[2]=(TextView)findViewById(R.id.sdtxt2);
        sdtxt[3]=(TextView)findViewById(R.id.sdtxt3);

        caltxt[0]=(TextView)findViewById(R.id.caltxt);
        caltxt[1]=(TextView)findViewById(R.id.caltxt1);
        caltxt[2]=(TextView)findViewById(R.id.caltxt2);
        caltxt[3]=(TextView)findViewById(R.id.caltxt3);

        Recipebtn[0]=(Button)findViewById(R.id.Recipebtn0);
        Recipebtn[1]=(Button)findViewById(R.id.Recipebtn1);
        Recipebtn[2]=(Button)findViewById(R.id.Recipebtn2);
        Recipebtn[3]=(Button)findViewById(R.id.Recipebtn3);

        for(int i=0; i<4; i++) {
            Recipebtn[i].setOnClickListener(ButtonListener);
        }

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()

                .detectDiskReads().detectDiskWrites().detectNetwork()

                .penaltyLog().build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()

                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()

                .penaltyLog().penaltyDeath().build());
        ja = new JSONArray();
        recipeja = new JSONArray();
        try {
            String qry = "select * from MENURECOMMEND where MEMBERID = '" + id + "' and RECOMMENDDATE = '"+dateString+"'";

            String jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));
            if (jsonString.substring(0, 1).equals("[")) {
                ja = new JSONArray(jsonString);
                for(int i=0; i<ja.length();i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    if (jo.getInt("CHOICE") == 1 && jo.getInt("MENURECOMMENDTYPE") == type) {
                        for (int j = 0; j < 4; j++)
                            sdtxt[j].setText(SicDan[jo.getInt("MENUNUMBER")][j]);
                    }
                }
            }

            qry = "select * from RECIPE";

            jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));
            if (jsonString.substring(0, 1).equals("[")) {
                recipeja = new JSONArray(jsonString);
                for(int j=0; j<4; j++) {
                    int i = 0;
                    for (; i < recipeja.length(); i++) {
                        JSONObject jo = recipeja.getJSONObject(i);
                        if (sdtxt[j].getText().equals(jo.getString("RECIPENAME"))) {
                            caltxt[j].setText(jo.getInt("CALORIE") + "");
                            break;
                        }
                    }
                    if(i==recipeja.length()){
                        Recipebtn[j].setEnabled(false);
                    }
                }
            }
            //결과 출력

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        for(int i=0; i<4; i++) {
            if((sdtxt[i].getText()).equals("-")){
                Recipebtn[i].setEnabled(false);

            }
        }
    }
    public void sicdanChange(){
        try {
            for(int i=0; i<ja.length();i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo.getInt("CHOICE") == 1 && jo.getInt("MENURECOMMENDTYPE") == type) {
                    for (int j = 0; j < 4; j++)
                        sdtxt[j].setText(SicDan[jo.getInt("MENUNUMBER")][j]);
                }
            }


            for(int j=0; j<4; j++) {
                int i = 0;
                for ( ;i < recipeja.length(); i++) {
                    JSONObject jo = recipeja.getJSONObject(i);
                    if (sdtxt[j].getText().equals(jo.getString("RECIPENAME"))) {
                        caltxt[j].setText(jo.getInt("CALORIE")+"");
                        Recipebtn[j].setEnabled(true);
                        break;
                    }
                }
                if(i==recipeja.length()){
                    caltxt[j].setText("-");
                    Recipebtn[j].setEnabled(false);
                }
            }

            //결과 출력
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    View.OnClickListener ButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView tmptxt = (TextView) findViewById(R.id.sdtxt);
            switch (v.getId()) {
                case R.id.Recipebtn1:
                    tmptxt = (TextView) findViewById(R.id.sdtxt1);
                    break;
                case R.id.Recipebtn2:
                    tmptxt = (TextView) findViewById(R.id.sdtxt2);
                    break;
                case R.id.Recipebtn3:
                    tmptxt = (TextView) findViewById(R.id.sdtxt3);
                    break;
            }

            Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
            intent.putExtra(com.insulin.diabalance.RecipeActivity.EXTRA_ID, (String) getIntent().getExtras().get(EXTRA_ID));
            intent.putExtra(com.insulin.diabalance.RecipeActivity.EXTRA_TEXT, tmptxt.getText());
            startActivity(intent);
        }
    };
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        intent.putExtra(com.insulin.diabalance.MenuActivity.EXTRA_ID, (String)getIntent().getExtras().get(EXTRA_ID));
        startActivity(intent);
    }
}
