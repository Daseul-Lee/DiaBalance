package com.insulin.diabalance;

/**
 * Created by SSOGYO on 2016-09-07.
 */
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Pattern;

public class UserInfoActivity extends Activity {

    private static String url = "http://diabalance.dothome.co.kr/DiaBalance/";
    public static final String EXTRA_ID = "id";
    private SQLiteDatabase db;
    String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        id=(String) getIntent().getExtras().get(EXTRA_ID);
        TextView nameEdit = (TextView) findViewById(R.id.nameView);
        TextView idEdit = (TextView) findViewById(R.id.idView);
        EditText rAPIEdit = (EditText) findViewById(R.id.rAPIEditView);
        EditText pwEdit = (EditText) findViewById(R.id.pwEditView);
        TextView byEdit = (TextView) findViewById(R.id.byView);
        EditText weightEdit = (EditText) findViewById(R.id.weightEditView);
        EditText heightEdit = (EditText) findViewById(R.id.heightEditView);
        EditText cnEdit = (EditText) findViewById(R.id.cnEditView);
        pwEdit.requestFocus();

        rAPIEdit.setFilters(new InputFilter[]{readAPIFilterAlphaNum});
        //Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();

        try {
            SQLiteOpenHelper Database= new Database(this);
            db = Database.getReadableDatabase();
        } catch(SQLiteException e) {
            Toast.makeText(getApplicationContext(), "Database unavailable", Toast.LENGTH_LONG).show();
        }

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()

                .detectDiskReads().detectDiskWrites().detectNetwork()

                .penaltyLog().build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()

                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()

                .penaltyLog().penaltyDeath().build());

        try {
            String qry = "select * from MEMBER where MEMBERID = '" + (String) getIntent().getExtras().get(EXTRA_ID) + "'";

            String jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));
            if(!(jsonString==null)) {
                if (jsonString.substring(0, 1).equals("[")) {
                    JSONArray ja = new JSONArray(jsonString);
                    JSONObject jo = ja.getJSONObject(0);
                    nameEdit.setText(jo.getString("MEMBERNAME"));
                    idEdit.setText(jo.getString("MEMBERID"));
                    byEdit.setText(jo.getInt("BIRTHYEAR") + "");
                    weightEdit.setText(jo.getInt("WEIGHT") + "");
                    heightEdit.setText(jo.getInt("HEIGHT") + "");
                    cnEdit.setText(jo.getInt("CHANNEL") + "");
                    rAPIEdit.setText(jo.getString("READAPI"));
                    //Toast.makeText(getApplicationContext(), jo.getString("MEMBERID"), Toast.LENGTH_LONG).show();

                }
                //Toast.makeText(getApplicationContext(), jsonString, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }


        Button checkbtn = (Button) findViewById(R.id.checkbtn);
        Button cancelBtn = (Button) findViewById(R.id.cancelbtn);
//        try {
//            SQLiteOpenHelper Database= new Database(this);
//            db = Database.getReadableDatabase();
//        } catch(SQLiteException e) {
//            Toast.makeText(getApplicationContext(), "Database unavailable", Toast.LENGTH_LONG).show();
//        }
        checkbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText rAPIEdit = (EditText) findViewById(R.id.rAPIEditView);
                EditText pwEdit = (EditText) findViewById(R.id.pwEditView);
                EditText weightEdit = (EditText) findViewById(R.id.weightEditView);
                EditText heightEdit = (EditText) findViewById(R.id.heightEditView);
                EditText pwchkEdit = (EditText) findViewById(R.id.pwchkEditView);
                EditText cnEdit = (EditText) findViewById(R.id.cnEditView);
                EditText pwupdateEdit = (EditText) findViewById(R.id.pwUpdateEditView);

                String password = pwEdit.getText().toString();
                String passwordUpdate = pwupdateEdit.getText().toString();
                String passwordcheck = pwchkEdit.getText().toString();
                String readAPI = rAPIEdit.getText().toString();

                int check = 0, pwcheck = 0;


                int weight = 0, height = 0, channel = 0;
                if (weightEdit.getText().toString().length() > 0) {
                    weight = Integer.parseInt(weightEdit.getText().toString());
                }
                if (heightEdit.getText().toString().length() > 0) {
                    height = Integer.parseInt(heightEdit.getText().toString());
                }
                if (cnEdit.getText().toString().length() > 0) {
                    channel = Integer.parseInt(cnEdit.getText().toString());
                }
                if (password.equals(""))
                    Toast.makeText(getBaseContext(), "수정하시려면 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                else {
                    try {
                        String qry = "select PW from MEMBER where MEMBERID = '" + id + "'";

                        String jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));
                        if (jsonString.substring(0, 1).equals("[")) {
                            JSONArray ja = new JSONArray(jsonString);
                            JSONObject jo = ja.getJSONObject(0);
                            if (password.equals(jo.getString("PW")))
                                pwcheck = 1;
                        }
                        //결과 출력
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                    if (pwcheck == 0) {
                        Toast.makeText(getBaseContext(), "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                        check = 1;
                    }
                    if (0 < passwordUpdate.length()) {
                        if (passwordUpdate.length() < 4) {
                            Toast.makeText(getBaseContext(), "수정할 비밀번호는 4자리 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                            check = 1;
                        } else if (!(passwordUpdate.equals(passwordcheck)) && check == 0) {
                            Toast.makeText(getBaseContext(), "수정할 비밀번호가 같지 않습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                            check = 1;
                        } else {
                            password = passwordUpdate;
                            pwcheck = 2;
                        }
                    }
                    if (weight == 0 || height == 0 || channel == 0 || readAPI.length() == 0) {
                        Toast.makeText(getBaseContext(), "입력되지 않은 부분이 있습니다.\n 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    } else if (check == 0) {
                        Toast.makeText(getBaseContext(), "회원 정보가 성공적으로 수정되었습니다!", Toast.LENGTH_SHORT).show();
                        try {
                            URL text = new URL(url + "updateMEMBER.php?memberid=" + URLEncoder.encode(id, "UTF-8")
                                    + "&pw=" + URLEncoder.encode(password, "UTF-8")
                                    + "&height=" + height
                                    + "&weight=" + weight
                                    + "&channel=" + channel
                                    + "&readapi=" + URLEncoder.encode(readAPI, "UTF-8"));

                            text.openStream();
                            HttpURLConnection conn = (HttpURLConnection) text.openConnection();
                            conn.connect();
                            conn.disconnect();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                        if (pwcheck == 2) {
                            try {
                                Cursor cursor = db.query("MEMBER",
                                        new String[]{"ID, PW, AUTOLOGIN"},
                                        null, null, null, null, null);
                                if (cursor.getCount() > 0) {
                                    while (cursor.moveToNext()) {
                                        if (id.equals(cursor.getString(cursor.getColumnIndex("ID")))) {
                                            if (cursor.getString(cursor.getColumnIndex("PW")) != password)
                                                db.execSQL("UPDATE MEMBER SET PW='" + password + "' WHERE ID='" + id + "';");
                                            break;
                                        }
                                    }
                                }
                            } catch (SQLiteException e) {
                                Toast.makeText(getApplicationContext(), "Database unavailable", Toast.LENGTH_LONG).show();
                            }
                        }
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        intent.putExtra(com.insulin.diabalance.SettingsActivity.EXTRA_ID, (String) getIntent().getExtras().get(EXTRA_ID));
                        startActivity(intent);
                    }
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(RESULT_OK,resultIntent);

                finish();
            }
        });
    }


    public InputFilter readAPIFilterAlphaNum = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]*$");
            if (!ps.matcher(source).matches()) {
                Toast.makeText(getBaseContext(), "readAPI는 영문자나 숫자로만 입력주세요.", Toast.LENGTH_SHORT).show();
                return "";
            }
            return null;
        }
    };


}