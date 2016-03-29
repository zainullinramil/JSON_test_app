/*
 * Copyright (c) 2016.
 * Ramil Zainullin
 * zainullinramil@gmail.com
 * +7 963 306-16-01
 *
 */

package ru.ramil.JSON_test;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import ru.ramil.JSON_test.Fragments.HotelListFragment;
import ru.ramil.JSON_test.Fragments.HotelFragment;
import ru.ramil.cleverpumpkin_test.R;

public class MainActivity extends AppCompatActivity implements HotelListFragment.OnLinkItemSelectedListener, HotelFragment.OnBtnMapListener {

    public static String LOG_TAG = "my_log";

    HotelListFragment listHotel;
    Spinner spSort;
    Button buttonUpd;
    int sortType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);

        Log.d(LOG_TAG, "onCreate");

        buttonUpd = (Button) findViewById(R.id.btnUpd);
        spSort = (Spinner) findViewById(R.id.spSort);


        //загрузка списка при открытии приложения
        //проверяем подключение
        if (isOnline()) {
                FragmentManager fm = getSupportFragmentManager();

                // выводим список отелей в ListFragment
                if (fm.findFragmentById(R.id.conteiner) == null) {
                    listHotel = new HotelListFragment();
                    fm.beginTransaction().add(R.id.conteiner, listHotel).commit();
                }

        }else {Toast.makeText(this, R.string.no_connect_text,  Toast.LENGTH_LONG).show();
        }
    }

    //нажатие кнопки обновления списка
    public void updateListButton(View view){

        sortType = spSort.getSelectedItemPosition();

        //проверяем подключение
        if (isOnline()) {

            FragmentManager fm = getSupportFragmentManager();

            HotelListFragment listHotel = new HotelListFragment();
            fm.beginTransaction().replace(R.id.conteiner, listHotel).commit();
            listHotel.sortType(sortType);

            //Toast.makeText(this, "Вы выбрали сортировку " + sortType, Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this, R.string.no_connect_text, Toast.LENGTH_LONG).show();
        }
    }

    //выбор пункта списка
    @Override
    public void onItemSelected(int linkPos, ArrayList<Long> linkId) {

        Long id = linkId.get(linkPos);

        //проверяем подключение
        if (isOnline()) {

            FragmentManager fm = getSupportFragmentManager();

            HotelFragment itemHotel = new HotelFragment();
            fm.beginTransaction().replace(R.id.conteiner, itemHotel).addToBackStack(null).commit();
            itemHotel.selPos(id, linkPos);

            //Toast.makeText(this, "Вы выбрали пункт " + id, Toast.LENGTH_SHORT).show();

            buttonUpd.setVisibility(View.INVISIBLE);
            spSort.setVisibility(View.INVISIBLE);

        }else {
            Toast.makeText(this, R.string.no_connect_text, Toast.LENGTH_LONG).show();
        }
    }

    //Нажали кнопку назад <
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        buttonUpd.setVisibility(View.VISIBLE);
        spSort.setVisibility(View.VISIBLE);
    }

    //Проверка соединения
    protected boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBtnMapClick(double lat, double lon) {
        String uri = String.format("geo:%s,%s?z=16", Double.toString(lat), Double.toString(lon));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }
}


