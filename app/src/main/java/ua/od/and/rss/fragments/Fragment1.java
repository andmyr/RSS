package ua.od.and.rss.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ua.od.and.rss.R;
import ua.od.and.rss.adapters.MyAdapter;
import ua.od.and.rss.classes.OneNews;
import ua.od.and.rss.classes.RSS;
import ua.od.and.rss.database.MyDBHelper;


public class Fragment1 extends ListFragment
{
    private ArrayList<OneNews> newsList;
    private MyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.listfragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        MyDBHelper myDBHelper = new MyDBHelper(getContext());
        SQLiteDatabase db = myDBHelper.getReadableDatabase();
        ArrayList<RSS> rssList = myDBHelper.getAllRRS(db);
        if (rssList.size() < 1)
        {
            Toast.makeText(getContext(), "Добавьте хотя бы один RSS-лист", Toast.LENGTH_SHORT).show();
            newsList = myDBHelper.getAllNewsFromRss(db, 0);
        } else
        {
            newsList = myDBHelper.getAllNewsFromRss(db, rssList.get(0).getId());
        }

        adapter = new MyAdapter(newsList, getContext());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        OnSelectedButtonListener listener = (OnSelectedButtonListener) getActivity();
        long id1 = newsList.get(position).getId();
        listener.onButtonSelected(id1);
    }

    public void refreshList(long rssId)
    {
        MyDBHelper myDBHelper = new MyDBHelper(getContext());
        SQLiteDatabase db = myDBHelper.getReadableDatabase();
        newsList = myDBHelper.getAllNewsFromRss(db, rssId);
        adapter = new MyAdapter(newsList, getContext());
        getListView().setAdapter(adapter);
        /*adapter.notifyDataSetChanged();
        getListView().invalidateViews();*/
    }

    public interface OnSelectedButtonListener
    {
        void onButtonSelected(long buttonIndex);
    }
}
