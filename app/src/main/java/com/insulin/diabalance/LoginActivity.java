package com.insulin.diabalance;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by risha95 on 2016-09-09.
 */
public class LoginActivity extends Activity {
    private static String url = "http://diabalance.dothome.co.kr/DiaBalance/";
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView joinbtn = (TextView) findViewById(R.id.jbtn);
        Button loginbtn = (Button) findViewById(R.id.loginbtn);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()

                .detectDiskReads().detectDiskWrites().detectNetwork()

                .penaltyLog().build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()

                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()

                .penaltyLog().penaltyDeath().build());
        EditText pwEdit = (EditText) findViewById(R.id.loginPWEdit);
        EditText idEdit = (EditText) findViewById(R.id.loginIDEdit);
        try {
            SQLiteOpenHelper Database= new Database(this);
            db = Database.getReadableDatabase();
            Cursor cursor = db.query ("MEMBER",
                            new String[] {"ID, PW, AUTOLOGIN"},
                            null, null, null, null,null);
            if(cursor.getCount()>0) {
                while (cursor.moveToNext()) {
                    if (cursor.getInt(cursor.getColumnIndex("AUTOLOGIN")) == 1) {
                        idEdit.setText(cursor.getString(cursor.getColumnIndex("ID")));
                        pwEdit.setText(cursor.getString(cursor.getColumnIndex("PW")));
                        CheckBox autoLogincheck = (CheckBox) findViewById(R.id.autologincheck);
                        autoLogincheck.setChecked(true);
                        break;
                    }
                }
            }
            cursor.close();
        } catch(SQLiteException e) {
            Toast.makeText(getApplicationContext(), "Database unavailable", Toast.LENGTH_LONG).show();
        }
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText pwEdit = (EditText) findViewById(R.id.loginPWEdit);
                EditText idEdit = (EditText) findViewById(R.id.loginIDEdit);
                String password = pwEdit.getText().toString();
                String id = idEdit.getText().toString();
                int pwcheck = 0, idcheck = 0;
                if (id.equals("")) {
                    Toast.makeText(getApplicationContext(), "ID를 입력하지 않았습니다.", Toast.LENGTH_LONG).show();
                } else if (password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Password를 입력하지 않았습니다.", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        String qry = "select PW from MEMBER where MEMBERID = '" + id + "'";

                        String jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));
                        if (jsonString.substring(0, 1).equals("[")) {
                            JSONArray ja = new JSONArray(jsonString);
                            JSONObject jo = ja.getJSONObject(0);
                            if (password.equals(jo.getString("PW")))
                                pwcheck = 1;
                        } else
                            idcheck = 1;
                        //결과 출력

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }

                    if (pwcheck == 1 && idcheck == 0) {
                        Toast.makeText(getApplicationContext(), "성공적으로 로그인 했습니다!", Toast.LENGTH_LONG).show();
                        CheckBox autoLogincheck = (CheckBox) findViewById(R.id.autologincheck);
                        if (autoLogincheck.isChecked()) {
                            try {
                                Cursor cursor = db.query ("MEMBER",
                                    new String[] {"AUTOLOGIN"},
                                    "ID = ?", new String[] {id}, null, null,null);
                                if(cursor.getCount()>0) {
                                    cursor.moveToNext();
                                    if (cursor.getInt(cursor.getColumnIndex("AUTOLOGIN")) == 0) {
                                        db.execSQL("UPDATE MEMBER SET AUTOLOGIN=1 WHERE ID='"+id+"';");
                                    }
                                }
                                else{
                                    ContentValues memberValues = new ContentValues();
                                    memberValues.put("ID", id);
                                    memberValues.put("PW", password);
                                    memberValues.put("AUTOLOGIN", 1);
                                    db.insert("MEMBER", null, memberValues);
                                }
                                db.execSQL("UPDATE MEMBER SET AUTOLOGIN=0 WHERE ID!='"+id+"';");
                            } catch (SQLiteException e) {
                                Toast.makeText(getApplicationContext(), "Database unavailable", Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            try {
                                Cursor cursor = db.query ("MEMBER",
                                        new String[] {"AUTOLOGIN"},
                                        "ID = ?", new String[] {id}, null, null,null);
                                if(cursor.getCount()>0) {
                                    cursor.moveToNext();
                                    if (cursor.getInt(cursor.getColumnIndex("AUTOLOGIN")) == 1)
                                    {
                                        db.execSQL("UPDATE MEMBER SET AUTOLOGIN=0 WHERE ID='"+id+"';");
                                    }
                                }
                            } catch (SQLiteException e) {
                                Toast.makeText(getApplicationContext(), "Database unavailable", Toast.LENGTH_LONG).show();
                            }
                        }
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra(com.insulin.diabalance.MainActivity.EXTRA_ID, id);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "존재하지 않는 ID거나 비밀번호가 \n틀렸습니다. 다시 로그인 해주세요.", Toast.LENGTH_LONG).show();
                        pwEdit.setText("");
                    }
                }
            }
        });

            joinbtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), join1.class);
                    startActivity(intent);
                }
            });

    }
    public void onBackPressed() {

        moveTaskToBack(true);
    }
}
