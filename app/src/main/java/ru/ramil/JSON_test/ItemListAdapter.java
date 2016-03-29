/*
 * Copyright (c) 2016.
 * Ramil Zainullin
 * zainullinramil@gmail.com
 * +7 963 306-16-01
 *
 */

package ru.ramil.JSON_test;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.ramil.cleverpumpkin_test.R;

//адаптер списка позизий
public class ItemListAdapter extends ArrayAdapter<Hotel> {

    public static String LOG_TAG = "my_log";
    Context ctx;
    LayoutInflater lInflater;

    public ItemListAdapter(Context context) {
        super(context, R.layout.itemhotel);
        ctx = context;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d(LOG_TAG, "ItemAdapter create");
    }

    //передаем данные
    public void setData(ArrayList<Hotel> data) {
        clear();
        if (data != null) {
            for (Hotel appEntry : data) {
                add(appEntry);
            }
        }
    }
    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        Log.d(LOG_TAG, "View getView" + position);
        View view = convertView;
        String idHotel = ctx.getString(R.string.id_item_text);
        String totheCenter = ctx.getString(R.string.tothecenter_text);
        String dist = ctx.getString(R.string.dist_text);
        String freeroom;
        if (view == null) {
            view = lInflater.inflate(R.layout.itemlisthotel, parent, false);
        }

        Hotel hotel = getHotel(position);

        int balance = hotel.getCountAv()%10;

        if (balance == 1) {
            freeroom = ctx.getString(R.string.free_room_text);
        }else if(hotel.getCountAv() < 5){
            freeroom = ctx.getString(R.string.free_rooms_text);
        }else {freeroom = ctx.getString(R.string.free_roomss_text);}

        // заполняем View в пункте списка данными: наименование b т.д.
        ((TextView) view.findViewById(R.id.tvName)).setText(hotel.getName());
        ((TextView) view.findViewById(R.id.tvAddr)).setText(hotel.getAddress());
        ((TextView) view.findViewById(R.id.tvIDHotel)).setText("(ID: " + hotel.getIDhotel() + ")");
        ((TextView) view.findViewById(R.id.tvDist)).setText("До центра города: " + hotel.getDistance() + " pc");
        ((TextView) view.findViewById(R.id.tvStar)).setText(""+hotel.getStars());
        ((TextView) view.findViewById(R.id.tvSav)).setText(hotel.getCountAv() + freeroom + hotel.getSuitesAv());

        return view;
    }

    // объект по позиции
    Hotel getHotel(int position) {
        return ((Hotel) getItem(position));
    }
}
