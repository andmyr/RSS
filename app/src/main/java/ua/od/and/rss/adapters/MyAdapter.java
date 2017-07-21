package ua.od.and.rss.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ua.od.and.rss.R;
import ua.od.and.rss.classes.OneNews;

/**
 *
 */

public class MyAdapter extends BaseAdapter
{
    private final ArrayList<OneNews> data;
    private final Context context;
    private final LayoutInflater inflater;

    public MyAdapter(ArrayList<OneNews> data, Context context)
    {
        this.data = data;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public Object getItem(int i)
    {
        return data.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent)
    {
        view = inflater.inflate(R.layout.list_item, parent, false);

        TextView name = (TextView) view.findViewById(R.id.tvName);
        name.setText(data.get(i).getTitle());

        return view;
    }
}
