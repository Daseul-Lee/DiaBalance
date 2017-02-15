package com.insulin.diabalance;

/**
 * Created by risha95 on 2016-07-20.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Database extends SQLiteOpenHelper {

    private static final String DB_NAME = "DiaBalanceDB";
    private static final int DB_VERSION = 37;
    Database(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * DB를 사용하기 위한 클래스
     * 생성자, 매개변수로 Context를 사용한다.
     * 액티비티인 경우 Phone(this) 또는 Phone(액티비티클래스명.this)
     * 플래그먼트인 경우 Phone(getActivity())
     * 서비스인 경우 Phone(getApplicationContext())
     * @param c 액티비티의 Context
     */

    /**
     * db를 사용하기 위한 준비단계
     * Context를 이용하여 db에 연결하고 테이블이 없다면 테이블 생성하고 값을 초기하고, 테이블 내용이 변경되었다면 이전값을 그대로 유지하며 열만 추가삭제된다.
     * column = 각 열의 이름 배열
     * default_value = 각 열의 초기값 또는 테이블 변경시 값을 유지하기 위한 배열
     * @param c 액티비티의 Context
     */

    public void onCreate(SQLiteDatabase db){
        updateMyDatabase(db, 0, DB_VERSION);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 10) {
            try{
                db.execSQL("CREATE TABLE MEMBER( " +
                        "ID TEXT PRIMARY KEY," +
                        "NAME TEXT," +
                        "BIRTHYEAR INTEGER," +
                        "HEIGHT INTEGER," +
                        "WEIGHT INTEGER);");

                db.execSQL("CREATE TABLE EXERCISE(MEMBERID TEXT NOT NULL," +
                        "EXERCISETIME INTEGER," +
                        "EXERCISEDATE TEXT NOT NULL," +
                        "EXERCISESUCCESS INTEGER," +
                        "SPORTS INTEGER," +
                        "PRIMARY KEY (MEMBERID, EXERCISEDATE)," +
                        "FOREIGN KEY (MEMBERID) REFERENCES MEMBER(ID));");
            }catch(Exception e) {

            }
        }
        if (oldVersion < 20) {
            try {
                db.execSQL("CREATE TABLE MEMO(" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "MEMBERID TEXT NOT NULL," +
                        "MEMODATA TEXT," +
                        "MEMODATE TEXT," +
                        "FOREIGN KEY (MEMBERID) REFERENCES MEMBER(ID));");
            } catch (Exception e) {

            }
        }
        if (oldVersion < 25) {
            try {

            } catch (Exception e) {


            }
        }
        if (oldVersion < 28) {
            try {
                db.execSQL("CREATE TABLE BS(MEMBERID TEXT NOT NULL," +
                        "BSLEVELS INTEGER," +
                        "DATE TEXT NOT NULL," +
                        "TYPE INTEGER NOT NULL," +
                        "MIN INTEGER," +
                        "MAX INTEGER," +
                        "PRIMARY KEY (MEMBERID, DATE, TYPE)," +
                        "FOREIGN KEY (MEMBERID) REFERENCES MEMBER(ID));");
            } catch (Exception e) {

            }

        }
        if (oldVersion < 34) {
            try {

                db.execSQL("UPDATE MEMBER SET PW='12341234' WHERE ID='giho';");
            } catch (Exception e) {

            }

        }
        if (oldVersion < 35) {
            try {
                db.execSQL("ALTER TABLE MEMBER ADD COLUMN CHANNEL NUMBER;");
                db.execSQL("UPDATE MEMBER SET CHANNEL=161454 WHERE ID='giho';");
            } catch (Exception e) {

            }

        }
        if (oldVersion < 36) {
            try {
                db.execSQL("ALTER TABLE MEMBER ADD COLUMN READAPI TEXT;");
                db.execSQL("UPDATE MEMBER SET READAPI='FULTF8IAECAL61LC' WHERE ID='giho';");
            } catch (Exception e) {

            }

        }
        if (oldVersion < 38) {
            try {
                db.execSQL("DROP TABLE MEMBER;");
                db.execSQL("CREATE TABLE MEMBER( " +
                        "ID TEXT PRIMARY KEY," +
                        "PW TEXT," +
                        "AUTOLOGIN INTEGER);");
            } catch (Exception e) {

            }

        }
    }

    private static void insertMember(SQLiteDatabase db, String id,
                                    String name, int birthYear, int height, int weight) {

        ContentValues memberValues = new ContentValues();
        memberValues.put("ID", id);
        memberValues.put("NAME", name);
        memberValues.put("BIRTHYEAR", birthYear);
        memberValues.put("HEIGHT", height);
        memberValues.put("WEIGHT", weight);

        db.insert("MEMBER", null, memberValues);
    }

    private static void insertExercise(SQLiteDatabase db, String id,
                                     int exerciseTime, String exerciseDate, int exerciseSuccess, int sports) {

        ContentValues exerciseValues = new ContentValues();
        exerciseValues.put("MEMBERID", id);
        exerciseValues.put("EXERCISETIME", exerciseTime);
        exerciseValues.put("EXERCISEDATE", exerciseDate);
        exerciseValues.put("EXERCISESUCCESS", exerciseSuccess);
        exerciseValues.put("SPORTS", sports);

        db.insert("EXERCISE", null, exerciseValues);
    }

    private static void insertMemo(SQLiteDatabase db, String memberId,
                                       String memoData, String memoDate) {


        ContentValues memoValues = new ContentValues();
        memoValues.put("MEMBERID", memberId);
        memoValues.put("MEMODATA", memoData);
        memoValues.put("MEMODATE", memoDate);

        db.insert("MEMO", null, memoValues);
    }
    private static void insertBS(SQLiteDatabase db, String memberId,
                                   int BSlevels, String Date,int type, int min, int max) {

        ContentValues BSValues = new ContentValues();
        BSValues.put("MEMBERID", memberId);
        BSValues.put("BSLEVELS", BSlevels);
        BSValues.put("DATE", Date);
        BSValues.put("TYPE",type);
        BSValues.put("MIN", min);
        BSValues.put("MAX", max);

        db.insert("BS", null, BSValues);
    }
    public BSInfo getBS(SQLiteDatabase db, long time){
        Cursor c = db.rawQuery(	"select * from BS where DATE = '"+timeToString(new Date(time))+"'",null);
        BSInfo result = null;
        if(c.moveToNext()){
            result = new BSInfo();
            result.BSlevels = getI(c,"wateram");
            result.min = getI(c,"min");
            result.max = getI(c, "max");
            result.date = getS(c, "date");
        }
        return result;
    }
    public static String timeToString(Date time) {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return transFormat.format(time);
    }
    private String getS(Cursor c,String columnname){
        return c.getString(c.getColumnIndex(columnname));
    }
    private int getI(Cursor c,String columnname){
        return c.getInt(c.getColumnIndex(columnname));
    }
}