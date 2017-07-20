package ua.od.and.rss.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ua.od.and.rss.R;

public class Fragment2 extends Fragment
{
    // Имя для аргумента
    public static final String BUTTON_INDEX = "button_index";
    // Значение по умолчанию
    private static final int BUTTON_INDEX_DEFAULT = -1;
    private TextView mInfoTextView;
    private ImageView mImageView;
    private String[] mDescriptionArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment2, container, false);

        mInfoTextView = (TextView) rootView.findViewById(R.id.textView1);

        // загружаем массив из ресурсов
        mDescriptionArray = getResources().getStringArray(R.array.data);

        // Получим индекс, если имеется
        Bundle args = getArguments();
        int buttonIndex = args != null ? args.getInt(BUTTON_INDEX, BUTTON_INDEX_DEFAULT) : BUTTON_INDEX_DEFAULT;
        // Если индекс обнаружен, то используем его
        if (buttonIndex != BUTTON_INDEX_DEFAULT)
        {
            setDescription(buttonIndex);
        }

        return rootView;
    }

    public void setDescription(int buttonIndex)
    {
        String catDescription = mDescriptionArray[buttonIndex];
        mInfoTextView.setText(catDescription);
    }
}
