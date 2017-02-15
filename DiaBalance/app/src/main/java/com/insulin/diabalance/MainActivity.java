package com.insulin.diabalance;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
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


public class MainActivity extends Activity {
    ImageView measureImage, menuImage, exerciseImage, statisticsImage, adviceImage, settingImage, uiImage;
    public static final String EXTRA_ID = "id";
    private static String url = "http://diabalance.dothome.co.kr/DiaBalance/";
    private long lastTimeBackPressed;
    Calendar today;
    int year, month, day;
    JSONArray ja;
    TextView latestMday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        measureImage = (ImageView) findViewById(R.id.measureImage);
        measureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MeasureActivity.class);
                intent.putExtra(com.insulin.diabalance.MeasureActivity.EXTRA_ID, (String) getIntent().getExtras().get(EXTRA_ID));
                startActivity(intent);
            }
        });

        menuImage = (ImageView) findViewById(R.id.menuImage);
        menuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                intent.putExtra(com.insulin.diabalance.MenuActivity.EXTRA_ID, (String) getIntent().getExtras().get(EXTRA_ID));
                startActivity(intent);
            }
        });

        exerciseImage = (ImageView) findViewById(R.id.exerciseImage);
        exerciseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExerciseManagementActivity.class);
                intent.putExtra(com.insulin.diabalance.MainActivity.EXTRA_ID, (String) getIntent().getExtras().get(EXTRA_ID));
                startActivity(intent);
            }
        });

        statisticsImage = (ImageView) findViewById(R.id.statisticsImage);
        statisticsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
                intent.putExtra(com.insulin.diabalance.StatisticsActivity.EXTRA_ID, (String) getIntent().getExtras().get(EXTRA_ID));
                startActivity(intent);
            }
        });

        adviceImage = (ImageView) findViewById(R.id.adviceImage);
        adviceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdviceActivity.class);
                intent.putExtra(com.insulin.diabalance.AdviceActivity.EXTRA_ID, (String) getIntent().getExtras().get(EXTRA_ID));
                startActivity(intent);
            }
        });

        settingImage = (ImageView) findViewById(R.id.settingImage);
        settingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                intent.putExtra(com.insulin.diabalance.SettingsActivity.EXTRA_ID, (String) getIntent().getExtras().get(EXTRA_ID));
                startActivity(intent);
            }
        });
        uiImage = (ImageView) findViewById(R.id.uiImage);
        uiImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MadeByActivity.class);

                startActivity(intent);
            }
        });
        Button MBbtn = (Button) findViewById(R.id.MBbtn);
        Button MAbtn = (Button) findViewById(R.id.MAbtn);
        Button ABbtn = (Button) findViewById(R.id.ABbtn);
        Button AAbtn = (Button) findViewById(R.id.AAbtn);
        Button EBbtn = (Button) findViewById(R.id.EBbtn);
        Button EAbtn = (Button) findViewById(R.id.EAbtn);
        Button ZBbtn = (Button) findViewById(R.id.ZBbtn);

        MBbtn.setOnClickListener(ButtonListener);
        MAbtn.setOnClickListener(ButtonListener);
        ABbtn.setOnClickListener(ButtonListener);
        AAbtn.setOnClickListener(ButtonListener);
        EBbtn.setOnClickListener(ButtonListener);
        EAbtn.setOnClickListener(ButtonListener);
        ZBbtn.setOnClickListener(ButtonListener);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()

                .detectDiskReads().detectDiskWrites().detectNetwork()

                .penaltyLog().build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()

                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()

                .penaltyLog().penaltyDeath().build());
        latestMday = (TextView) findViewById(R.id.latestMday);


        latestMday.setText(getBSLastDateTime());
        ja = new JSONArray();


        today = Calendar.getInstance();
        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH) + 1;
        day = today.get(Calendar.DAY_OF_MONTH);
        String todayString = String.format("%04d%02d%02d", year, month, day);
        try {
            String qry = "select * from BS where MEMBERID = '" + (String) getIntent().getExtras().get(EXTRA_ID) + "' and BSDATE= '" + todayString + "' ORDER BY BSTYPE";

            String jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));
            if (jsonString.substring(0, 1).equals("[")) {
                JSONArray ja = new JSONArray(jsonString);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    Button tmp = MBbtn;
                    switch (jo.getInt("BSTYPE")) {
                        case 1:
                            tmp = MAbtn;
                            break;
                        case 2:
                            tmp = ABbtn;
                            break;
                        case 3:
                            tmp = AAbtn;
                            break;
                        case 4:
                            tmp = EBbtn;
                            break;
                        case 5:
                            tmp = EAbtn;
                            break;
                        case 6:
                            tmp = ZBbtn;
                            break;
                    }
                    tmp.setBackground(getResources().getDrawable(R.drawable.o));
                    tmp.setText(jo.getInt("BSLEVELS") + "");
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_settings) {
            Intent SettingActivity = new Intent(this, SettingsActivity.class);
            startActivity(SettingActivity);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onContextItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            moveTaskToBack(true);
            return;

        }
        Toast.makeText(getApplicationContext(), "'뒤로' 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }

    View.OnClickListener ButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final Button tmp = (Button) findViewById(v.getId());
            if (!tmp.getText().equals("")) {
                String typeString = "아침 식전";
                switch (v.getId()) {
                    case R.id.MBbtn:
                        break;
                    case R.id.MAbtn:
                        typeString = "아침 식후";
                        break;
                    case R.id.ABbtn:
                        typeString = "점심 식전";
                        break;
                    case R.id.AAbtn:
                        typeString = "점심 식후";
                        break;
                    case R.id.EBbtn:
                        typeString = "저녁 식전";
                        break;
                    case R.id.EAbtn:
                        typeString = "저녁 식후";
                        break;
                    case R.id.ZBbtn:
                        typeString = "잠자기 전";
                        break;
                }
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                dialog.setMessage("오늘 측정한 " + typeString + " 혈당치를 삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    int type = 0;
                                    switch (v.getId()) {
                                        case R.id.MBbtn:
                                            break;
                                        case R.id.MAbtn:
                                            type = 1;
                                            break;
                                        case R.id.ABbtn:
                                            type = 2;
                                            break;
                                        case R.id.AAbtn:
                                            type = 3;
                                            break;
                                        case R.id.EBbtn:
                                            type = 4;
                                            break;
                                        case R.id.EAbtn:
                                            type = 5;
                                            break;
                                        case R.id.ZBbtn:
                                            type = 6;
                                            break;
                                    }
                                    String todayString = String.format("%04d%02d%02d", year, month, day);

                                    String qry = "delete from BS where MEMBERID = '" + (String) getIntent().getExtras().get(EXTRA_ID) + "'and BSDATE = '" + todayString + "' and BSTYPE =" + type;

                                    String jsonString = JSONParser.getJSONFromUrl(new URL(url + "delete.php?qry=" + URLEncoder.encode(qry, "UTF-8")));

                                    Toast.makeText(MainActivity.this, jsonString.substring(0, jsonString.length() - 1), Toast.LENGTH_SHORT).show();

                                    tmp.setText("");
                                    tmp.setBackground(getResources().getDrawable(R.drawable.x));

                                    latestMday.setText(getBSLastDateTime());
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                }

                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }
        }
    };

    public String getBSLastDateTime() {
        try {
            String qry = "select * from BS where MEMBERID = '" + (String) getIntent().getExtras().get(EXTRA_ID) + "' ORDER BY BSDATE, BSTYPE";

            String jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));
            if (jsonString.substring(0, 1).equals("[")) {
                JSONArray datetimeja = new JSONArray(jsonString);
                JSONObject jo = datetimeja.getJSONObject(datetimeja.length() - 1);
                String tmp = jo.getString("BSDATE");
                String year = tmp.substring(0, 4);
                String month = tmp.substring(4, 6);
                String day = tmp.substring(6, 8);
                return year + "/" + month + "/" + day + " " + jo.getString("BSTIME");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        return "";
    }
}
