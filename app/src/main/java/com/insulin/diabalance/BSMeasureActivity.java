package com.insulin.diabalance;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by risha95 on 2016-07-13.
 */
public class BSMeasureActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener{
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_TYPE = "type";
    int type=0;
    private static String url = "http://diabalance.dothome.co.kr/DiaBalance/";
    private static String tsurl = "http://api.thingspeak.com/";
    Calendar today;
    int year, month, day;
    String todayString;
    int channelID=0;
    String readAPI="";
    JSONArray ja;
    int index=-1;
    String [] explanation={"일회용 테스트 스트립을\n미리 혈당측정기에 꽂아 놓는다.",
                            "일회용 체혈침을 이용해\n손가락에 피를 낸다.",
                            "뽑은 피를 일회용 테스트 스트립에 묻혀\n결과가 나오기를 기다린다."};
    int [] explanationImage={R.drawable.bsmeasure,R.drawable.bsmeasure1,R.drawable.bsmeasure2};

    TextView explanationtxt;
    Button backbtn, nextbtn;
    ImageView explanationImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bsmeasure);

        backbtn=(Button)findViewById(R.id.backbtn);
        backbtn.setOnClickListener(ButtonListener);
        nextbtn=(Button)findViewById(R.id.nextbtn);
        nextbtn.setOnClickListener(ButtonListener);

        explanationtxt=(TextView)findViewById(R.id.explanationtxt);
        explanationImageView=(ImageView)findViewById(R.id.explanationImage);

        nextbtn.callOnClick();





        final Button bsmbtn=(Button)findViewById(R.id.bsmbtn);

        final Spinner spinner = (Spinner)findViewById(R.id.bssp);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.BSspinnerItems, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        today = Calendar.getInstance();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()

                .detectDiskReads().detectDiskWrites().detectNetwork()

                .penaltyLog().build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()

                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()

                .penaltyLog().penaltyDeath().build());
        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH)+1;
        day = today.get(Calendar.DAY_OF_MONTH);
        todayString = String.format("%04d%02d%02d", year, month, day);


        ja= new JSONArray();
        final Button bssavebtn=(Button)findViewById(R.id.bssavebtn);


        try {
            String qry = "select * from MEMBER where MEMBERID = '" + (String) getIntent().getExtras().get(EXTRA_ID) + "'";

            String jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));
            if((jsonString.substring(0, 1).equals("["))){
                JSONArray memberja = new JSONArray(jsonString);
                JSONObject jo = memberja.getJSONObject(0);
                channelID=jo.getInt("CHANNEL");
                readAPI=jo.getString("READAPI");
            }
            qry = "select * from BS where MEMBERID = '" + (String) getIntent().getExtras().get(EXTRA_ID) + "' and BSDATE= '"+todayString+"' ORDER BY BSTYPE";

            jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));
            if (jsonString.substring(0, 1).equals("[")) {
                ja = new JSONArray(jsonString);
                JSONObject jo = ja.getJSONObject(ja.length() - 1);
                if (jo.getInt("BSTYPE") != 6) {
                    spinner.setSelection(jo.getInt("BSTYPE") + 1);
                    type = jo.getInt("BSTYPE") + 1;
                    onItemSelected((AdapterView) spinner, spinner.getSelectedView(), spinner.getSelectedItemPosition(), spinner.getId());
                }
            else{
                    TextView endtxt=(TextView)findViewById(R.id.endtxt);
                    EditText bsEdit = (EditText) findViewById(R.id.bsEdit);
                    endtxt.setText("오늘(" + year + "년" + month + "월" + day + "일) 혈당을 모두 측정하셨습니다.");
                    spinner.setEnabled(false);
                    bssavebtn.setEnabled(false);
                    bsEdit.setEnabled(false);
                    bsEdit.setFocusable(false);
                }
            }
            else
                spinner.setSelection(0);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }  catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        if(getIntent().hasExtra(EXTRA_TYPE)){
            bsmbtn.setText("");
            bsmbtn.setEnabled(false);
            bsmbtn.setBackground(getResources().getDrawable(R.drawable.alpha));
        }
        else{
            final EditText bsEdit = (EditText) findViewById(R.id.bsEdit);
            bsEdit.setEnabled(false);
            bsEdit.setFocusable(false);
            bsmbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        HttpClient httpclient;

                        httpclient = new DefaultHttpClient();
                        HttpGet httpGet = new HttpGet("http://api.thingspeak.com/channels/" + channelID + "/feed.json?results=1");
                        httpGet.setHeader("Content-type", "application/json; charset=utf-8");
                        httpGet.setHeader("Host", "api.thingspeak.com");


                        HttpResponse response = httpclient.execute(httpGet);
                        String jsonString = "";
                        if (response != null) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                            StringBuilder sb = new StringBuilder();
                            String line = null;
                            try {
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line + "\n");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                jsonString = sb.toString();
                            }
                        }
                        ;
                        //Toast.makeText(getApplicationContext(), jsonString, Toast.LENGTH_LONG).show();
                        if (jsonString.substring(0, 1).equals("{")) {
                            JSONObject jo= new JSONObject(jsonString);
                            JSONArray ja= jo.getJSONArray("feeds");
                            jo = ja.getJSONObject(0);
                            bsEdit.setText(jo.getInt("field1") + "");
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

        spinner.setOnItemSelectedListener(this);

        bssavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EditText bsEdit = (EditText) findViewById(R.id.bsEdit);
                    Double bslevels=0.0;
                    int check=0;
                    if(bsEdit.getText().toString().length()>0) {
                        bslevels = Double.parseDouble(bsEdit.getText().toString());
                    }
                    else{
                        check=1;
                        if(getIntent().hasExtra(EXTRA_TYPE)) {
                            Toast.makeText(getApplicationContext(), "혈당치를 입력해주세요!", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "먼저 혈당치를 가져와주세요!", Toast.LENGTH_LONG).show();
                        }
                    }
                    Calendar now =Calendar.getInstance();
                    Date date = now.getTime();
                    String time = (new SimpleDateFormat("HH:mm").format(date));
                    //Toast.makeText(getApplicationContext(), time, Toast.LENGTH_LONG).show();
                    int i=0;
                    for(; i<ja.length(); i++){
                        JSONObject jo = ja.getJSONObject(i);
                        if(jo.getInt("BSTYPE")==type)
                            break;
                    }
                    if(i==ja.length()&&check==0) {
                        URL text = new URL(url + "insertBS.php?memberid=" + URLEncoder.encode((String) getIntent().getExtras().get(EXTRA_ID), "UTF-8")
                                + "&bslevels=" + bslevels
                                + "&bsdate=" + URLEncoder.encode(todayString, "UTF-8")
                                + "&bstime=" + URLEncoder.encode(time, "UTF-8")
                                + "&bstype=" + type);
                        if (type == 6) {
                            TextView endtxt = (TextView) findViewById(R.id.endtxt);
                            endtxt.setText("오늘(" + year + "년" + month + "월" + day + "일) 혈당을 모두 측정하셨습니다.");
                            spinner.setEnabled(false);
                            bssavebtn.setEnabled(false);
                            bsEdit.setEnabled(false);
                            bsEdit.setFocusable(false);
                        }
                        spinner.setSelection((7 + type + 1) % 7);
                        type = (7 + type + 1) % 7;
                        onItemSelected((AdapterView) spinner, spinner.getSelectedView(), spinner.getSelectedItemPosition(), spinner.getId());
                        text.openStream();
                        HttpURLConnection conn = (HttpURLConnection) text.openConnection();
                        conn.connect();
                        conn.disconnect();
                        bsEdit.setText("");
                        Toast.makeText(getApplicationContext(), "혈당치를 성공적으로 저장했습니다.", Toast.LENGTH_LONG).show();
                    } else if (i!=ja.length())
                        Toast.makeText(getApplicationContext(), "오늘 이미 "+spinner.getSelectedItem().toString()+"의 혈당치는 저장했습니다.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type=position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    View.OnClickListener ButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button tmp =(Button)findViewById(v.getId());
            int x = -1;

            if (v.getId() == R.id.nextbtn)
                x = 1;

            index=index+x;
            if(index==0) {
                backbtn.setEnabled(false);
                backbtn.setBackground(getResources().getDrawable(R.drawable.alpha));
            }
            else if(index==explanation.length-1){
                nextbtn.setEnabled(false);
                nextbtn.setBackground(getResources().getDrawable(R.drawable.alpha));
            }
            else{
                nextbtn.setBackground(getResources().getDrawable(R.drawable.next));
                backbtn.setBackground(getResources().getDrawable(R.drawable.back));
                nextbtn.setEnabled(true);
                backbtn.setEnabled(true);
            }
            explanationtxt.setText(explanation[index]);
           explanationImageView.setBackground(getResources().getDrawable(explanationImage[index]));
        }
    };
}
