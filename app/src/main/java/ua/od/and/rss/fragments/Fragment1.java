package ua.od.and.rss.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ua.od.and.rss.R;


public class Fragment1 extends ListFragment// Fragment implements
        // View.OnClickListener
{
    final String[] vegetables = new String[]{"Картошка", "Капуста", "Морковка", "Помидор", "Буряк", "Редиска", "Укроп", "Еще укроп", "Кабачек", "Перец",
            "Баклажан", "Хрень непонятная"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.listfragment, null);

        /*
         * Button button1 = (Button) rootView.findViewById(R.id.button1); Button
         * button2 = (Button) rootView.findViewById(R.id.button2); Button
         * button3 = (Button) rootView.findViewById(R.id.button3);
         * 
         * button1.setOnClickListener(this); button2.setOnClickListener(this);
         * button3.setOnClickListener(this);
         */
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, vegetables);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        OnSelectedButtonListener listener = (OnSelectedButtonListener) getActivity();
        listener.onButtonSelected(position);
    }

    // @Override
    public void onClick(View v)
    {
        int buttonIndex = translateIdToIndex(v.getId());

        OnSelectedButtonListener listener = (OnSelectedButtonListener) getActivity();
        listener.onButtonSelected(buttonIndex);

        // Toast.makeText(getActivity(), String.valueOf(buttonIndex),
        // Toast.LENGTH_SHORT).show();
    }

    int translateIdToIndex(int id)
    {
        /*
         * int index = -1; switch (id) { case R.id.button1: index = 1; break;
         * case R.id.button2: index = 2; break; case R.id.button3: index = 3;
         * break; }
         */
        return id;
    }

    public interface OnSelectedButtonListener
    {
        void onButtonSelected(int buttonIndex);
    }
}
