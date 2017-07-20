package ua.od.and.rss.asynctasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import ua.od.and.rss.classes.OneNews;
import ua.od.and.rss.database.MyDBHelper;

/**
 *
 */

public class RetrieveFeedTask extends AsyncTask
{
    private final Context context;
    private final URL url;

    public RetrieveFeedTask(Context context, URL url)
    {
        this.context = context;
        this.url = url;
    }

    @Override
    protected Object doInBackground(Object[] objects)
    {
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();

            // We will get the XML from an input stream
            xpp.setInput(getInputStream(url), "UTF_8");

        /* We will parse the XML content looking for the "<title>" tag which appears inside the "<item>" tag.
         * However, we should take in consideration that the rss feed name also is enclosed in a "<title>" tag.
         * As we know, every feed begins with these lines: "<channel><title>Feed_Name</title>...."
         * so we should skip the "<title>" tag which is a child of "<channel>" tag,
         * and take in consideration only "<title>" tag which is a child of "<item>"
         *
         * In order to achieve this, we will make use of a boolean variable.
         */
            boolean insideItem = false;

            // Returns the type of current event: START_TAG, END_TAG, etc..
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
                            headline = xpp.nextText(); //extract the headline
                        }
                    } else if (xpp.getName().equalsIgnoreCase("link"))
                    {
                        if (insideItem)
                        {
                            link = xpp.nextText(); //extract the link of article
                        }
                    } else if (xpp.getName().equalsIgnoreCase("description"))
                    {
                        if (insideItem)
                        {
                            description = xpp.nextText(); //extract the link of article
                        }
                    } else if (xpp.getName().equalsIgnoreCase("pubDate"))
                    {
                        if (insideItem)
                        {
                            pubDate = xpp.nextText(); //extract the link of article
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
                    myDBHelper.addOneNews(db, new OneNews(0, headline, link, description, pubDate));
                    headline = "";
                    link = "";
                    description = "";
                    pubDate = "";
                }
                eventType = xpp.next(); //move to next element
            }

            Toast.makeText(context, "Загрузка завершена", Toast.LENGTH_LONG).show();
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

        return null;
    }

    public InputStream getInputStream(URL url)
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
