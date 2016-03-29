/*
 * Copyright (c) 2016.
 * Ramil Zainullin
 * zainullinramil@gmail.com
 * +7 963 306-16-01
 *
 */

package ru.ramil.JSON_test;

/**
 * Created by user on 23.03.2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class DB {

    public static String LOG_TAG = "my_log";
    private static final String DB_NAME = "dbHotel";
    private static final int DB_VERSION = 1;

    private static final String ITEM_TABLE = "hotel";
    public static final String ITEM_COLUMN_ID = "_id";
    public static final String ITEM_COLUMN_NAME = "name";
    public static final String ITEM_COLUMN_IDHOTEL = "idhotel";
    public static final String ITEM_COLUMN_IMAGE = "image";
    public static final String ITEM_COLUMN_ADDRESS= "address";
    public static final String ITEM_COLUMN_STARS = "stars";
    public static final String ITEM_COLUMN_DISTANCE = "distance";
    public static final String ITEM_COLUMN_AVAILAB = "suites_availability";
    public static final String ITEM_COLUMN_AVCOUNT = "availab_count";
    public static final String ITEM_COLUMN_LAT = "lat";
    public static final String ITEM_COLUMN_LON = "lon";

    public static final int NUM_COLUMN_ID = 0;
    public static final int NUM_COLUMN_NAME = 1;
    public static final int NUM_COLUMN_IDHOTEL = 2;
    public static final int NUM_COLUMN_IMAGE = 3;
    public static final int NUM_COLUMN_ADDRESS= 4;
    public static final int NUM_COLUMN_STARS = 5;
    public static final int NUM_COLUMN_DISTANCE = 6;
    public static final int NUM_COLUMN_AVAILAB = 7;
    public static final int NUM_COLUMN_AVCOUNT = 10;
    public static final int NUM_COLUMN_LAT = 8;
    public static final int NUM_COLUMN_LON = 9;

    private static final String ITEM_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + ITEM_TABLE + "(" + ITEM_COLUMN_ID + " integer primary key autoincrement, " +
            ITEM_COLUMN_NAME + " text, " +
            ITEM_COLUMN_IDHOTEL + " integer," +
            ITEM_COLUMN_IMAGE + " text," +
            ITEM_COLUMN_ADDRESS + " text, " +
            ITEM_COLUMN_STARS + " integer," +
            ITEM_COLUMN_DISTANCE + " real," +
            ITEM_COLUMN_AVAILAB + " text," +
            ITEM_COLUMN_LAT + " real," +
            ITEM_COLUMN_LON + " real," +
            ITEM_COLUMN_AVCOUNT + " integer" +");";

    private final Context mCtx;
    ArrayList<Hotel> addHotel;

    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx, ArrayList<Hotel> arrLoader) {
        mCtx = ctx;
        addHotel = arrLoader;
    }

    // открываем подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        Log.d(LOG_TAG, "getWritableDatabase");
    }

    // закрываем подключение
    public void close() {
        if (mDBHelper != null)
            mDBHelper.close();
    }

    // данные по позициям конкретной группы
    public ArrayList<Hotel> getItemData(int sortType) {

        String orderby = null;

        switch (sortType){
            case 1: orderby = ITEM_COLUMN_DISTANCE;
                break;
            case 2: orderby = ITEM_COLUMN_AVCOUNT;
                break;
            default: orderby = null;
                break;
        }

        Log.d(LOG_TAG, "getItemData orderby:" + orderby);

        Cursor mCursor = mDB.query(ITEM_TABLE, null, null, null, null, null, orderby);

        ArrayList<Hotel> arr = new ArrayList<Hotel>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                int id = mCursor.getInt(NUM_COLUMN_ID);
                long idhotel = mCursor.getInt(NUM_COLUMN_IDHOTEL);
                String image = mCursor.getString(NUM_COLUMN_IMAGE);
                String name = mCursor.getString(NUM_COLUMN_NAME);
                String address = mCursor.getString(NUM_COLUMN_ADDRESS);
                String availab = mCursor.getString(NUM_COLUMN_AVAILAB);
                int stars = mCursor.getInt(NUM_COLUMN_STARS);
                double distance = mCursor.getDouble(NUM_COLUMN_DISTANCE);
                double lat = mCursor.getDouble(NUM_COLUMN_LAT);
                double lon = mCursor.getDouble(NUM_COLUMN_LON);
                int availab_count = mCursor.getInt(NUM_COLUMN_AVCOUNT);
                arr.add(new Hotel(id, name, idhotel, image, address, stars, distance, availab, availab_count, lat, lon));
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        return arr;
    }

    // данные позиции выбранной в списке
    public ArrayList<Hotel> getHotelData(long hotelID) {

        String orderby = null;

        Log.d(LOG_TAG, "getHotelData :" + hotelID);

        Cursor mCursor = mDB.query(ITEM_TABLE, null, ITEM_COLUMN_IDHOTEL +" = ?", new String[]{Long.toString(hotelID)}, null, null, orderby);

        ArrayList<Hotel> arr = new ArrayList<Hotel>();
        Log.d(LOG_TAG, "moveToFirst");

        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {

            do {
                int id = mCursor.getInt(NUM_COLUMN_ID);
                long idhotel = mCursor.getInt(NUM_COLUMN_IDHOTEL);
                String image = mCursor.getString(NUM_COLUMN_IMAGE);
                String name = mCursor.getString(NUM_COLUMN_NAME);
                String address = mCursor.getString(NUM_COLUMN_ADDRESS);
                String availab = mCursor.getString(NUM_COLUMN_AVAILAB);
                int stars = mCursor.getInt(NUM_COLUMN_STARS);
                double distance = mCursor.getDouble(NUM_COLUMN_DISTANCE);
                double lat = mCursor.getDouble(NUM_COLUMN_LAT);
                double lon = mCursor.getDouble(NUM_COLUMN_LON);
                int availab_count = mCursor.getInt(NUM_COLUMN_AVCOUNT);
                arr.add(new Hotel(id, name, idhotel, image, address, stars, distance, availab, availab_count, lat, lon));
            } while (mCursor.moveToNext());

        }
        mCursor.close();
        return arr;
    }



    private class DBHelper extends SQLiteOpenHelper {


        public DBHelper(Context context, String name, CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            ContentValues cv = new ContentValues();

            Log.d(LOG_TAG, "onCreate SQLiteDatabase");
            if (addHotel != null) {


                for (int i = 0; i < addHotel.size(); i++) {
                    Hotel iHotel = addHotel.get(i);

                    //Количество свободных номеров
                    String sa = iHotel.getSuitesAv();
                    StringTokenizer saTok = new StringTokenizer(sa, ":");

                    // создаем и заполняем таблицу позиций
                    db.execSQL(ITEM_TABLE_CREATE);
                    cv.clear();
                    cv.put(ITEM_COLUMN_NAME, iHotel.getName());
                    cv.put(ITEM_COLUMN_IDHOTEL, iHotel.getIDhotel());
                    cv.put(ITEM_COLUMN_IMAGE, iHotel.getImage());
                    cv.put(ITEM_COLUMN_ADDRESS, iHotel.getAddress());
                    cv.put(ITEM_COLUMN_STARS, iHotel.getStars());
                    cv.put(ITEM_COLUMN_DISTANCE, iHotel.getDistance());
                    cv.put(ITEM_COLUMN_AVAILAB, iHotel.getSuitesAv());
                    cv.put(ITEM_COLUMN_AVCOUNT, saTok.countTokens());
                    cv.put(ITEM_COLUMN_LAT, iHotel.getLat());
                    cv.put(ITEM_COLUMN_LON, iHotel.getLon());

                    db.insert(ITEM_TABLE, null, cv);
                }
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}