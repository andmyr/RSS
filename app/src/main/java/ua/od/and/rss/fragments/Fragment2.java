package ua.od.and.rss.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ua.od.and.rss.R;
import ua.od.and.rss.classes.OneNews;
import ua.od.and.rss.database.MyDBHelper;

public class Fragment2 extends Fragment
{
    // Имя для аргумента
    public static final String BUTTON_INDEX = "button_index";
    // Значение по умолчанию
    private static final int BUTTON_INDEX_DEFAULT = -1;
    private TextView tvTitle;
    private ImageView mImageView;
    private TextView tvDescription;
    private TextView tvLink;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment2, container, false);
        tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
        tvDescription = (TextView) rootView.findViewById(R.id.tvDescription);
        tvLink = (TextView) rootView.findViewById(R.id.tvLink);

        Bundle args = getArguments();
        long buttonIndex = args != null ? args.getLong(BUTTON_INDEX, BUTTON_INDEX_DEFAULT) : BUTTON_INDEX_DEFAULT;
        // Если индекс обнаружен, то используем его
        if (buttonIndex != BUTTON_INDEX_DEFAULT)
        {
            setDescription(buttonIndex);
        }
        return rootView;
    }

    public void setDescription(long newsId)
    {
        MyDBHelper myDBHelper = new MyDBHelper(getContext());
        SQLiteDatabase db = myDBHelper.getReadableDatabase();
        OneNews oneNews = myDBHelper.getNewsById(db, newsId);
        if (oneNews != null)
        {
            tvTitle.setText(oneNews.getTitle());
            tvDescription.setText(oneNews.getDescription());
            tvLink.setText(oneNews.getLink());
        }
    }
}
