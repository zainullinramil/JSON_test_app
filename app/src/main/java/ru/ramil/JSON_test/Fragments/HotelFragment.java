package ru.ramil.JSON_test.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import ru.ramil.JSON_test.DB;
import ru.ramil.JSON_test.Hotel;
import ru.ramil.cleverpumpkin_test.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotelFragment extends Fragment implements View.OnClickListener {
    public static String LOG_TAG = "my_log";
    public static String adressJSONmain = "https://dl.dropboxusercontent.com/u/9270000/";

    long itemID;
    int itemPos;
    double lat, lon;
    OnBtnMapListener mapListener;
    ArrayList<Hotel> arrHotel;
    public DB dbi;
    Button buttonMps;

    public void selPos (long linkID, int linkPos) {
        itemID = linkID;
        itemPos = linkPos;
    }
    public HotelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View v) {
        mapListener.onBtnMapClick(lat, lon);
    }

    public interface OnBtnMapListener {
        public void onBtnMapClick(double lat, double lon);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mapListener = (OnBtnMapListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.itemhotel, container, false);
        setRetainInstance(true);

        dbi = new DB(getActivity(), null);
        dbi.open();
        arrHotel = new ArrayList<Hotel>();

        String idHotel = this.getString(R.string.id_item_text);
        String totheCenter = this.getString(R.string.tothecenter_text);
        String dist = this.getString(R.string.dist_text);
        String freeroom;
        lat = 0;
        lon = 0;



        arrHotel = dbi.getHotelData(itemID);

        Log.d(LOG_TAG, "getView position" + itemPos);

        Hotel hotel = arrHotel.get(0);

        int balance = hotel.getCountAv()%10;

        if (balance == 1) {
            freeroom = this.getString(R.string.free_room_text);
        }else if(hotel.getCountAv() < 5){
            freeroom = this.getString(R.string.free_rooms_text);
        }else {freeroom = this.getString(R.string.free_roomss_text);}

        // заполняем View в пункте списка данными: наименование b т.д.
        ((TextView) view.findViewById(R.id.tviName)).setText(hotel.getName());
        ((TextView) view.findViewById(R.id.tviAddr)).setText(hotel.getAddress());
        ((TextView) view.findViewById(R.id.tviIDHotel)).setText("("+ idHotel + Long.toString(hotel.getIDhotel()) + ")");
        ((TextView) view.findViewById(R.id.tviDist)).setText( totheCenter + Double.toString(hotel.getDistance()) + dist);
        ((TextView) view.findViewById(R.id.tviStar)).setText(Double.toString(hotel.getStars()));
        ((TextView) view.findViewById(R.id.tviSav)).setText(Integer.toString(hotel.getCountAv()) + freeroom + hotel.getSuitesAv());
        ((TextView) view.findViewById(R.id.tviLan)).setText(Double.toString(hotel.getLat()));
        ((TextView) view.findViewById(R.id.tviLon)).setText(Double.toString(hotel.getLon()));

        new DownloadImageTask((ImageView) view.findViewById(R.id.ivHotel)).execute(adressJSONmain + hotel.getImage());

        lat = hotel.getLat();
        lon = hotel.getLon();

        Log.d(LOG_TAG, "Lat: " + lat);
        Log.d(LOG_TAG, "Lon: " + lon);


        buttonMps = (Button) view.findViewById(R.id.btnMap);
        buttonMps.setOnClickListener(this);


        dbi.close();
        return view;
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView  bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap iBitmap = null;
            Log.d(LOG_TAG, "Image link = " + urldisplay);
            try {
                InputStream in = new URL(urldisplay).openStream();
                iBitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.d(LOG_TAG, "Error image transmission " + e.getMessage());
                e.printStackTrace();
            }
            return iBitmap;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null){
                int width = result.getWidth();
                int height = result.getHeight();

                //обрезаем лишний пиксель по периметру
                Bitmap img = Bitmap.createBitmap(result, 1, 1, width - 2, height - 2);

                Log.d(LOG_TAG, "Width image = " + height);
                Log.d(LOG_TAG, "Height image = " + width);
                bmImage.setImageBitmap(img);
            }else{
                bmImage.setImageResource(R.mipmap.ic_not_found);
            }

        }
    }
}