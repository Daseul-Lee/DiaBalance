package com.insulin.diabalance;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Created by risha95 on 2016-07-13.
 */
public class ExerciseActivity extends Activity {
    public static final String EXTRA_ID = "id";
    Button WarmingUpbtn, Walkingbtn, ExerciseOfMusclebtn, FinishingExercisebtn;
    private static String url = "http://diabalance.dothome.co.kr/DiaBalance/";
    int WarmingUpState=0, WalkingState=0, ExerciseOfMuscleState=0, FinishingExerciseState=0;
    //int WarmingUpSecond=600, WalkingSecond=1800, ExerciseOfMuscleSecond=1200, FinishingExerciseSecond=600;
    int WarmingUpSecond=10, WalkingSecond=10, ExerciseOfMuscleSecond=10, FinishingExerciseSecond=10;
    int WalkingSports=1, ExerciseOfMuscleSports=2, FinishingExerciseSports=3, nextSports;
    private int seconds, nextSeconds;
    private Button btn,nextBtn;
    private boolean running;
    private SQLiteDatabase db;

    String id;
    Calendar today;
    String todayString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        today = Calendar.getInstance();
        id = (String)getIntent().getExtras().get(EXTRA_ID);
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH)+1;
        int day = today.get(Calendar.DAY_OF_MONTH);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()

                .detectDiskReads().detectDiskWrites().detectNetwork()

                .penaltyLog().build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()

                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()

                .penaltyLog().penaltyDeath().build());
        seconds=WarmingUpSecond;

        todayString = String.format("%04d%02d%02d", year, month, day);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        WarmingUpbtn=(Button)findViewById(R.id.WarmingUpbtn);
        nextBtn=WarmingUpbtn;
        WarmingUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WarmingUpState == 0&&WarmingUpbtn.getText()!="완료") {
                    seconds = WarmingUpSecond;
                    btn = WarmingUpbtn;
                    running = true;
                    runTimer();
                    WarmingUpState = 1;
                    nextBtn = Walkingbtn;
                    nextSeconds = WalkingSecond;
                    nextSports=WalkingSports;
                } else if (WarmingUpState == 1&&WarmingUpbtn.getText()!="완료") {
                    running = false;
                    WarmingUpState = 2;
                } else if (WarmingUpState == 2&&WarmingUpbtn.getText()!="완료") {
                    running = true;
                    WarmingUpState = 1;
                }
            }
        });


        Walkingbtn=(Button)findViewById(R.id.Walkingbtn);
        Walkingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WalkingState == 0&&Walkingbtn.getText()!="완료") {
                    seconds=WalkingSecond;
                    btn=Walkingbtn;
                    running=true;
                    WalkingState = 1;
                    nextBtn=ExerciseOfMusclebtn;
                    nextSeconds=ExerciseOfMuscleSecond;
                    nextSports=ExerciseOfMuscleSports;
                } else if (WalkingState == 1&&Walkingbtn.getText()!="완료") {
                    running=false;
                    WalkingState=2;
                } else if (WalkingState == 2&&Walkingbtn.getText()!="완료") {
                    running=true;
                    WalkingState=1;
                }
            }
        });

        ExerciseOfMusclebtn=(Button)findViewById(R.id.ExerciseOfMusclebtn);
        ExerciseOfMusclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ExerciseOfMuscleState == 0&&ExerciseOfMusclebtn.getText()!="완료") {
                    seconds=ExerciseOfMuscleSecond;
                    btn=ExerciseOfMusclebtn;
                    running=true;
                    ExerciseOfMuscleState = 1;
                    nextBtn=FinishingExercisebtn;
                    nextSeconds=FinishingExerciseSecond;
                    nextSports=FinishingExerciseSports;

                } else if (ExerciseOfMuscleState == 1&&ExerciseOfMusclebtn.getText()!="완료") {
                    running=false;
                    ExerciseOfMuscleState=2;
                } else if (ExerciseOfMuscleState == 2&&ExerciseOfMusclebtn.getText()!="완료") {
                    running=true;
                    ExerciseOfMuscleState=1;
                }
            }
        });

        FinishingExercisebtn=(Button)findViewById(R.id.FinishingExercisebtn);
        FinishingExercisebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FinishingExerciseState == 0&&FinishingExercisebtn.getText()!="완료") {
                    seconds=FinishingExerciseSecond;
                    btn=FinishingExercisebtn;
                    running=true;
                    FinishingExerciseState = 1;
                    nextBtn=null;
                    nextSports=4;

                } else if (FinishingExerciseState == 1&&FinishingExercisebtn.getText()!="완료") {
                    running=false;
                    FinishingExerciseState=2;
                } else if (FinishingExerciseState == 2&&FinishingExercisebtn.getText()!="완료") {
                    running=true;
                    FinishingExerciseState=1;
                }
            }
        });
        try {
            String qry = "select * from EXERCISE where MEMBERID = '" + id + "' AND EXERCISEDATE = '" + todayString + "'";

            String jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));

            if (!jsonString.substring(0,1).equals("[")) {//인서트
                String memberId= (String)getIntent().getExtras().get(EXTRA_ID);

                URL text=new URL(url + "insertEXERCISE.php?memberid=" + URLEncoder.encode(memberId, "UTF-8")
                        + "&exercisetime=" + WarmingUpSecond
                        + "&exercisedate=" + URLEncoder.encode(todayString, "UTF-8")
                        + "&exercisesuccess=" + 0
                        + "&sports=" + 0);
                text.openStream();
                HttpURLConnection conn = (HttpURLConnection)text.openConnection();
                conn.connect();
                conn.disconnect();
//                JSONArray ja = new JSONArray(jsonString);
//                JSONObject jo = ja.getJSONObject(0);
//                String memoData = jo.getString("MEMODATA");
//                String memoDate = jo.getString("MEMODATE");
//                exerciseValues.put("MEMBERID", id);
//
//                exerciseValues.put("EXERCISETIME", WarmingUpSecond);
//                exerciseValues.put("EXERCISEDATE", todayString);
//                exerciseValues.put("EXERCISESUCCESS", 0);
//                exerciseValues.put("SPORTS", 0);
//                seconds=WarmingUpSecond;
//                db.insert("EXERCISE", null, exerciseValues);
//                // Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();
            }
            else{
                JSONArray ja = new JSONArray(jsonString);
                JSONObject jo = ja.getJSONObject(0);
                seconds=jo.getInt("EXERCISETIME");
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format("%02d:%02d", minutes, secs);
                if(jo.getInt("SPORTS") == 4){
                    WarmingUpbtn.setText("완료");
                    Walkingbtn.setText("완료");
                    ExerciseOfMusclebtn.setText("완료");
                    FinishingExercisebtn.setText("완료");
                    Walkingbtn.setEnabled(true);
                    ExerciseOfMusclebtn.setEnabled(true);
                    FinishingExercisebtn.setEnabled(true);
                }
                else {
                    if (jo.getInt("SPORTS") == 0) {
                        btn = WarmingUpbtn;
                        WarmingUpState=2;
                        nextBtn=Walkingbtn;
                        nextSeconds=WalkingSecond;
                        nextSports=WalkingSports;
                        if(seconds==WarmingUpSecond)
                            time="시작";
                    } else if (jo.getInt("SPORTS") == 1) {
                        WarmingUpbtn.setText("완료");
                        btn = Walkingbtn;
                        WalkingState=2;
                        nextBtn=ExerciseOfMusclebtn;
                        nextSeconds=ExerciseOfMuscleSecond;
                        nextSports=ExerciseOfMuscleSports;
                        if(seconds==WalkingSecond)
                            time="시작";
                    } else if (jo.getInt("SPORTS") == 2) {
                        WarmingUpbtn.setText("완료");
                        Walkingbtn.setText("완료");
                        Walkingbtn.setEnabled(true);
                        btn = ExerciseOfMusclebtn;
                        ExerciseOfMuscleState=2;
                        nextBtn=FinishingExercisebtn;
                        nextSeconds=FinishingExerciseSecond;
                        nextSports=FinishingExerciseSports;
                        if(seconds==ExerciseOfMuscleSecond)
                            time="시작";
                    } else if (jo.getInt("SPORTS") == 3) {
                        WarmingUpbtn.setText("완료");
                        Walkingbtn.setText("완료");
                        ExerciseOfMusclebtn.setText("완료");
                        Walkingbtn.setEnabled(true);
                        ExerciseOfMusclebtn.setEnabled(true);
                        btn = FinishingExercisebtn;
                        FinishingExerciseState=2;
                        nextBtn=null;
                        nextSports=4;
                        if(seconds==FinishingExerciseSecond)
                            time="시작";
                    }

                    btn.setText(time);
                    btn.setEnabled(true);
                    runTimer();
                    running=false;
                }

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        running=false;
        // The activity is no longer visible (it is now "stopped")
        try {
            URL text=new URL(url + "updateEXERCISETIME.php?memberid=" + id
                    + "&exercisetime=" + seconds
                    + "&exercisedate=" + URLEncoder.encode(todayString, "UTF-8"));
            text.openStream();
            HttpURLConnection conn = (HttpURLConnection)text.openConnection();
            conn.connect();
            conn.disconnect();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void runTimer(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            public void run() {
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format("%02d:%02d", minutes, secs);
                if (running) {
                    btn.setText(time);
                    seconds--;
                }
                if(running&&seconds==-2){
                    Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.exercise);
                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())

                            .setSmallIcon(android.R.drawable.ic_menu_gallery)

                            .setContentTitle("운동 완료") // 푸시의 타이틀이다.

                            .setLargeIcon(bitmap)

                            .setContentText("운동이 완료되었습니다!") // 서버에서 받은 텍스트

                            .setAutoCancel(true)

                            .setSound(soundUri); // 푸시가 날아올때 사운드 설정
                    NotificationManager notificationManager =

                            (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                    //노티피케이션을 생성합니다.

                    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
                }
                if (seconds==-2) {
                    running = false;
                    btn.setText("완료");

                    if(nextBtn!=null) {
                        seconds = nextSeconds;
                        btn = nextBtn;
                        btn.setEnabled(true);
                    }
                    else{
                        try {
                            URL text=new URL(url + "updateEXERCISESUCCESS.php?memberid=" + id
                                    + "&exercisedate=" + URLEncoder.encode(todayString, "UTF-8")
                                    + "&exercisesuccess=" + 1);
                            text.openStream();
                            HttpURLConnection conn = (HttpURLConnection)text.openConnection();
                            conn.connect();
                            conn.disconnect();

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                    try {
                        URL text=new URL(url + "updateSPORTS.php?memberid=" + id
                                + "&exercisedate=" + URLEncoder.encode(todayString, "UTF-8")
                                + "&sports=" + nextSports);
                        text.openStream();
                        HttpURLConnection conn = (HttpURLConnection)text.openConnection();
                        conn.connect();
                        conn.disconnect();

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                handler.postDelayed(this, 1000);
            }
        });
    }



}
