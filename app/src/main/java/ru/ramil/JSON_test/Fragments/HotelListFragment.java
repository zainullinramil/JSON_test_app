package ru.ramil.JSON_test.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import ru.ramil.JSON_test.DB;
import ru.ramil.JSON_test.DataListLoader;
import ru.ramil.JSON_test.Hotel;
import ru.ramil.JSON_test.ItemListAdapter;
import ru.ramil.cleverpumpkin_test.R;

/**
 * Created by user on 24.03.2016.
 */
public class HotelListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<Hotel>>{

    public static String LOG_TAG = "my_log";

    ItemListAdapter itemListAdapter;
    ArrayList<Long> linkId;
    int sortType;
    public DB db;

    OnLinkItemSelectedListener mListener;

    public interface OnLinkItemSelectedListener {
        public void onItemSelected(int linkPos, ArrayList<Long> linkId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mListener = (OnLinkItemSelectedListener) context;
    }

    public void sortType (int sortArr) {
        sortType = sortArr;
    }
    public void backButtonWasPressed() {
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "HotelListFragment onActivityCreated");

        setEmptyText(this.getString(R.string.nodata));

        itemListAdapter = new ItemListAdapter(getActivity());
        setListAdapter(itemListAdapter);

        setListShown(false);

        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        //v.findViewById(R.id.tvIDHotel);

        mListener.onItemSelected(position, linkId);

    }

    @Override
    public Loader<ArrayList<Hotel>> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "DataFragment.onCreateLoaderr");
        return new DataListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Hotel>> loader, ArrayList<Hotel> data) {

        db = new DB(getActivity(), data);
        db.open();

        linkId = new ArrayList<Long>();

        ArrayList<Hotel> arrHotellSort = db.getItemData(sortType);

//        порядок id после сортировки
        for (int i = 0; i < arrHotellSort.size(); i++) {
            Hotel iHotel = arrHotellSort.get(i);

            linkId.add(iHotel.getIDhotel());
            Log.d(LOG_TAG, "linkId: " + i + " - " + iHotel.getIDhotel());
        }


        itemListAdapter.setData(arrHotellSort);
        Log.d(LOG_TAG, "DataListFragment.onLoadFinished");
        // The list should now be shown.
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
        db.close();

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Hotel>> loader) {
        //itemListAdapter = new ItemListAdapter(getActivity());

//            itemListAdapter.setData(null);
    }



}