package com.insulin.diabalance;

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by risha95 on 2016-07-13.
 */
public class StatisticsActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener {
    public static final String EXTRA_ID = "id";
    private static String url = "http://diabalance.dothome.co.kr/DiaBalance/";
    protected String[] mdays = new String[] {
            "6일전", "5일전", "4일전", "3일전", "2일전", "1일전", "금일"
    };
    protected String[] mWeeks = new String[] {
            "3주전", "2주전", "1주전", "금주"
    };
    protected String[] mMonths = new String[] {
            "11개월전", "10개월전", "9개월전", "8개월전", "7개월전", "6개월전", "5개월전", "4개월전", "3개월전", "2개월전","1개월전","금월"
    };
    int[] month_day = {31,28,31,30,31,30,
            31,31,30,31,30,31};
    LineChart mChart;

    Calendar date;
    int type=0;
    String id;
    ImageView morningImage, lunchImage, eveningImage, zzzImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        date = Calendar.getInstance();
        id=(String)getIntent().getExtras().get(EXTRA_ID);
        final Spinner spinner = (Spinner)findViewById(R.id.chartsp);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spinnerItems, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()

                .detectDiskReads().detectDiskWrites().detectNetwork()

                .penaltyLog().build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()

                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()

                .penaltyLog().penaltyDeath().build());

        morningImage=(ImageView)findViewById(R.id.morningImage);
        lunchImage =(ImageView)findViewById(R.id.lunchImage);
        eveningImage=(ImageView)findViewById(R.id.eveningImage);
        zzzImage=(ImageView)findViewById(R.id.zzzImage);


        morningImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=0;

                morningImage.setImageDrawable(getResources().getDrawable(R.drawable.onm));
                lunchImage.setImageDrawable(getResources().getDrawable(R.drawable.offl));
                eveningImage.setImageDrawable(getResources().getDrawable(R.drawable.offe));
                zzzImage.setImageDrawable(getResources().getDrawable(R.drawable.offz));
                onItemSelected((AdapterView)spinner,spinner.getSelectedView(),spinner.getSelectedItemPosition(),spinner.getId());
            }
        });

        lunchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=2;
                morningImage.setImageDrawable(getResources().getDrawable(R.drawable.offm));
                lunchImage.setImageDrawable(getResources().getDrawable(R.drawable.onl));
                eveningImage.setImageDrawable(getResources().getDrawable(R.drawable.offe));
                zzzImage.setImageDrawable(getResources().getDrawable(R.drawable.offz));
                onItemSelected((AdapterView) spinner, spinner.getSelectedView(), spinner.getSelectedItemPosition(), spinner.getId());
            }
        });
        eveningImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=4;
                morningImage.setImageDrawable(getResources().getDrawable(R.drawable.offm));
                lunchImage.setImageDrawable(getResources().getDrawable(R.drawable.offl));
                eveningImage.setImageDrawable(getResources().getDrawable(R.drawable.one));
                zzzImage.setImageDrawable(getResources().getDrawable(R.drawable.offz));
                onItemSelected((AdapterView) spinner, spinner.getSelectedView(), spinner.getSelectedItemPosition(), spinner.getId());
            }
        });
        zzzImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=6;
                morningImage.setImageDrawable(getResources().getDrawable(R.drawable.offm));
                lunchImage.setImageDrawable(getResources().getDrawable(R.drawable.offl));
                eveningImage.setImageDrawable(getResources().getDrawable(R.drawable.offe));
                zzzImage.setImageDrawable(getResources().getDrawable(R.drawable.onz));
                onItemSelected((AdapterView) spinner, spinner.getSelectedView(), spinner.getSelectedItemPosition(), spinner.getId());
            }
        });


        mChart = (LineChart)findViewById(R.id.chart);

        mChart.setGridBackgroundColor(0);
        mChart.setDescription("");


        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        LineData data = new LineData(mMonths);

        mChart.setData(data);
        mChart.invalidate();
    }


    private  LineDataSet generateLineData(ArrayList<Entry> entries) {

        LineData d = new LineData();
        LineDataSet set;
        if(type!=6)
            set= new LineDataSet(entries, "Before Eating");
        else
            set= new LineDataSet(entries, "Before bed");
        set.setColor(Color.rgb(255, 171, 201));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(255, 171, 201));
        set.setCircleSize(5f);
        set.setFillColor(Color.rgb(255, 171, 201));
        set.setDrawCubic(true);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(255, 171, 201));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        d.addDataSet(set);


        return set;
    }

    private LineDataSet generateBarData(ArrayList<Entry> entries) {
        LineDataSet set = new LineDataSet(entries, "After Eating");
        set.setColor(Color.rgb(60, 220, 78));
        set.setValueTextColor(Color.rgb(60, 220, 78));
        set.setLineWidth(2.5f);
        set.setValueTextSize(10f);
        set.setCircleColor(Color.rgb(60,220,78));
        set.setCircleSize(5f);
        set.setFillColor(Color.rgb(60,220,78));
        set.setDrawCubic(true);
        set.setDrawValues(true);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        return set;
    }

    private void setData(String[] headers, ArrayList<BSInfo> BSs){
        LineData data = new LineData(headers);

        ArrayList<Entry> arr = new ArrayList<>();
        int cnt=0;
        for(int i=0;i<BSs.size();i++) {
            if(BSs.get(i).type==type) {
                arr.add(new Entry(BSs.get(i).BSlevels, cnt++));
            }
        }
        data.addDataSet(generateLineData(arr));
        cnt=0;
        if(type!=6){
            ArrayList<Entry> arr2 = new ArrayList<>();
            for(int i=0;i<BSs.size();i++) {
                if(BSs.get(i).type==type+1){
                    arr2.add(new Entry(BSs.get(i).BSlevels, cnt++));
                }
            }
            data.addDataSet(generateBarData(arr2));
        }
        mChart.setData(data);
        mChart.invalidate();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {//����, �ְ�, �ϰ� ����
        ArrayList<BSInfo> arr = new ArrayList<>();

        GregorianCalendar gc = new GregorianCalendar();
        gc.set(Calendar.HOUR_OF_DAY, 0);
        gc.set(Calendar.MINUTE, 0);

        int year = gc.get(Calendar.YEAR);
        int month = gc.get(Calendar.MONTH);
        int day = gc.get(Calendar.DAY_OF_MONTH);
        int mc,yc,yearmonth,k;
        JSONArray ja=null;
        switch (position){
            case 0:
                mc=((month_day[(12+month-1)%12])+day-7)/(month_day[(12+month-1)%12]);
                yc=(12+month-1+mc)/12;
                yearmonth=((year-1+yc)*100+((12+month+mc-1)%12+1));

                ja = getBS(yearmonth+"");
                k=0;
                for (int i=0; i<7; i++){
                    BSInfo b=null,a=null;
                    int check=(month_day[(12+month-1)%12]+day -6 + i-1)/month_day[(12+month-1)%12];
                    int yearCheck=(12+month-1+check)/12;
                    int time = ((year-1+yearCheck)*10000+((12+month+check-1)%12+1)*100+ (month_day[(12+month-1)%12]+day -6 + i-1)%month_day[(12+month-1)%12]+1);

                    if(yearmonth!=time/100){
                        k=0;
                        yearmonth=time/100;
                        ja = getBS(yearmonth+"");
                    }
                    if(ja!=null) {
                        try{
                            while (k < ja.length()) {
                                JSONObject jo = ja.getJSONObject(k);
                                int jotime= Integer.parseInt(jo.getString("BSDATE"));
                                if (time>jotime){
                                    k++;
                                }
                                else if (time==jotime){
                                    if(type==jo.getInt("BSTYPE")) {
                                        b = new BSInfo();
                                        b.BSlevels = jo.getInt("BSLEVELS");
                                        b.date = jo.getString("BSDATE");
                                        b.type = jo.getInt("BSTYPE");
                                        b.min = jo.getInt("BSMIN");
                                        b.max = jo.getInt("BSMAX");

                                    }
                                    else{
                                        a = new BSInfo();
                                        a.BSlevels = jo.getInt("BSLEVELS");
                                        a.date = jo.getString("BSDATE");
                                        a.type = jo.getInt("BSTYPE");
                                        a.min = jo.getInt("BSMIN");
                                        a.max = jo.getInt("BSMAX");
                                    }
                                    k++;
                                }
                                else {
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }

                    if(b == null){
                        b = new BSInfo();
                        b.BSlevels = 0;
                        b.min = 0;
                        b.max = 0;
                        b.date = DateToString(gc.getTime());
                        b.type=type;
                    }
                    if(a==null){
                        a = new BSInfo();
                        a.BSlevels = 0;
                        a.min = 0;
                        a.max = 0;
                        a.date = DateToString(gc.getTime());
                        a.type=type+1;
                    }
                    arr.add(b);
                    arr.add(a);
                }
                setData(mdays,arr);

                break;
            case 1:
                mc=((month_day[(12+month-1)%12])+day-28)/(month_day[(12+month-1)%12]);
                yc=(12+month-1+mc)/12;
                yearmonth=((year-1+yc)*100+((12+month+mc-1)%12+1));
                ja = getBS(yearmonth+"");
                k=0;
                for(int i=0;i<4;i++){
                    ArrayList<BSInfo> temparr = new ArrayList<>();

                    for(int j=0;j<7;j++){
                        int check=((month_day[(12+month-1)%12])+day- 27+i*7+j-1)/(month_day[(12+month-1)%12]);
                        int yearCheck=(12+month-1+check)/12;
                        int time = ((year-1+yearCheck)*10000+((12+month+check-1)%12+1)*100+ ((month_day[(12+month-1)%12])+day- 27+i*7+j-1)%(month_day[(12+month-1)%12])+1) ;
                        BSInfo b = null;
                        if(yearmonth!=time/100){
                            k=0;
                            yearmonth=time/100;
                            ja = getBS(yearmonth+"");
                        }
                        if(ja!=null) {
                            try {
                                while (k < ja.length()) {
                                    JSONObject jo = ja.getJSONObject(k);
                                    int jotime = Integer.parseInt(jo.getString("BSDATE"));
                                    if (time > jotime) {
                                        k++;
                                    } else if (time == jotime) {
                                        b = new BSInfo();
                                        b.BSlevels = jo.getInt("BSLEVELS");
                                        b.date = jo.getString("BSDATE");
                                        b.type = jo.getInt("BSTYPE");
                                        b.min = jo.getInt("BSMIN");
                                        b.max = jo.getInt("BSMAX");
                                        k++;
                                        temparr.add(b);
                                    } else {
                                        break;
                                    }
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    BSInfo before = new BSInfo();
                    before.type=type;
                    BSInfo after = new BSInfo();
                    after.type=type+1;

                    int bCnt=0, aCnt=0;

                    if(temparr.size() != 0){
                        for(int j=0;j<temparr.size();j++){
                            if(temparr.get(j).type==type) {
                                before.BSlevels += temparr.get(j).BSlevels;
                                before.min += temparr.get(j).min;
                                before.max += temparr.get(j).max;
                                bCnt++;
                            }
                            else if (temparr.get(j).type==type+1) {
                                after.BSlevels += temparr.get(j).BSlevels;
                                after.min += temparr.get(j).min;
                                after.max += temparr.get(j).max;
                                aCnt++;
                            }

                        }
                        if(bCnt!=0) {
                            before.BSlevels /= bCnt;
                            before.min /= bCnt;
                            before.max /= bCnt;
                        }

                        if (aCnt != 0) {
                            after.BSlevels /= aCnt;
                            after.min /= aCnt;
                            after.max /= aCnt;
                        }
                    }
                    arr.add(before);
                    arr.add(after);
                }
                setData(mWeeks,arr);
                break;
            case 2:
                String[] mMonths = new String[12];
                int check=(month+2)/12;
                int yearcheck=year-1+check;
                ja=getBS(yearcheck+"");
                k=0;
                for(int i=0;i<12;i++){
                    ArrayList<BSInfo> temparr = new ArrayList<>();
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.MONTH, i - 11);
                    c.set(Calendar.HOUR_OF_DAY, 0);
                    c.set(Calendar.MINUTE,0);
                    c.set(Calendar.SECOND,0);
                    mMonths[i] = (c.get(Calendar.YEAR)%100)+"/"+(c.get(Calendar.MONTH)+1);
                    BSInfo b = null;
                    check=(month+2+i)/12;
                    int time = ((year-1+check)*100+((month+2+i)%12));
                    if(yearcheck!=time/100){
                        k=0;
                        yearcheck=time/100;
                        ja = getBS(yearcheck+"");
                    }
                    if(ja!=null) {
                        try {
                            while (k < ja.length()) {
                                JSONObject jo = ja.getJSONObject(k);
                                int jotime = Integer.parseInt(jo.getString("BSDATE"))/100;
                                if (time > jotime) {
                                    k++;
                                } else if (time == jotime) {
                                    b = new BSInfo();
                                    b.BSlevels = jo.getInt("BSLEVELS");
                                    b.date = jo.getString("BSDATE");
                                    b.type = jo.getInt("BSTYPE");
                                    b.min = jo.getInt("BSMIN");
                                    b.max = jo.getInt("BSMAX");
                                    temparr.add(b);
                                    k++;
                                } else {
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                    BSInfo before = new BSInfo();
                    before.BSlevels =0;
                    before.type=type;
                    BSInfo after = new BSInfo();
                    after.BSlevels=0;
                    after.type=type+1;
                    int bCnt=0, aCnt=0;

                    if(temparr.size() != 0){
                        for(int j=0;j<temparr.size();j++){
                            if(temparr.get(j).type==type) {
                                before.BSlevels += temparr.get(j).BSlevels;
                                before.min += temparr.get(j).min;
                                before.max += temparr.get(j).max;
                                bCnt++;
                            }
                            else if (temparr.get(j).type==type+1) {
                                after.BSlevels += temparr.get(j).BSlevels;
                                after.min += temparr.get(j).min;
                                after.max += temparr.get(j).max;
                                aCnt++;
                            }

                        }
                        if(bCnt!=0) {
                            before.BSlevels /= bCnt;
                            before.min /= bCnt;
                            before.max /= bCnt;
                        }

                        if (aCnt != 0) {
                            after.BSlevels /= aCnt;
                            after.min /= aCnt;
                            after.max /= aCnt;
                        }
                    }
                    arr.add(before);
                    arr.add(after);
                }
                setData(mMonths, arr);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public JSONArray getBS(String date){
        JSONArray ja=null;
        try {
            int tmp=type+1;
            String qry = "select * from BS where MEMBERID = '" + id + "' and BSDATE LIKE '"+ date+"%' and (BSTYPE = "+type+" OR BSTYPE = "+tmp+") ORDER BY BSDATE";

            String jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));
            if (jsonString.substring(0,1).equals("[")) {
                ja = new JSONArray(jsonString);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        return ja;
    }
    public static String DateToString(Date date) {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
        return transFormat.format(date);
    }
}

