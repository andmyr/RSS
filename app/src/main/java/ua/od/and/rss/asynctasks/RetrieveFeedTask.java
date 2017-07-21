package ua.od.and.rss.asynctasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ua.od.and.rss.classes.OneNews;
import ua.od.and.rss.classes.RSS;
import ua.od.and.rss.database.MyDBHelper;

/**
 *
 */

public class RetrieveFeedTask extends AsyncTask
{
    private final Context context;
    private final String strUrl;

    /**
     * Загрузка новостей в базу в AsyncTask. Перед загрузкой удаляем старые новости.
     *
     * @param context
     * @param strUrl     Адрес RSS,
     *                   если длина строки 0 - получаем из базы все ссылки на RSS и по всем загружаем в базу новости
     *                   иначе загружаем одну ленту из url
     */

    public RetrieveFeedTask(Context context, String strUrl)
    {
        this.context = context;
        this.strUrl = strUrl;
    }

    @Override
    protected Object doInBackground(Object[] objects)
    {
        if (strUrl.length() > 0)
        { //кусок кода для тестирования
            try
            {
                RssParse(new URL(strUrl), 1);
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
        } else
        {
            MyDBHelper myDBHelper = new MyDBHelper(context);
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            ArrayList<RSS> rssList = myDBHelper.getAllRRS(db);

            for (RSS item : rssList)
            {
                try
                {
                    myDBHelper.deleteDataFromRSS(db, item.getId());
                    RssParse(new URL(item.getLink()), item.getId());
                } catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private void RssParse(URL url, long rssId)
    {
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(getInputStream(url), "UTF_8");

            boolean insideItem = false;

            int eventType = xpp.getEventType();
            String headline = "";
            String link = "";
            String description = "";
            String pubDate = "";
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                if (eventType == XmlPullParser.START_TAG)
                {
                    if (xpp.getName().equalsIgnoreCase("item"))
                    {
                        insideItem = true;
                    } else if (xpp.getName().equalsIgnoreCase("title"))
                    {
                        if (insideItem)
                        {
                            headline = xpp.nextText();
                        }
                    } else if (xpp.getName().equalsIgnoreCase("link"))
                    {
                        if (insideItem)
                        {
                            link = xpp.nextText();
                        }
                    } else if (xpp.getName().equalsIgnoreCase("description"))
                    {
                        if (insideItem)
                        {
                            description = xpp.nextText();
                        }
                    } else if (xpp.getName().equalsIgnoreCase("pubDate"))
                    {
                        if (insideItem)
                        {
                            pubDate = xpp.nextText();
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item"))
                {
                    insideItem = false;
                }
                if ((headline.length() > 0) && (description.length() > 0))
                {//Запись новости в базу
                    MyDBHelper myDBHelper = new MyDBHelper(context);
                    SQLiteDatabase db = myDBHelper.getWritableDatabase();
                    myDBHelper.addOneNews(db, new OneNews(rssId, headline, link, description, pubDate));
                    headline = "";
                    link = "";
                    description = "";
                    pubDate = "";
                }
                eventType = xpp.next();
            }

        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (XmlPullParserException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private InputStream getInputStream(URL url)
    {
        try
        {
            return url.openConnection().getInputStream();
        } catch (IOException e)
        {
            return null;
        }
    }
}
