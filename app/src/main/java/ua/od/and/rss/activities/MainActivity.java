package ua.od.and.rss.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import ua.od.and.rss.R;
import ua.od.and.rss.Utils.NetStatus;
import ua.od.and.rss.asynctasks.RetrieveFeedTask;
import ua.od.and.rss.classes.RSS;
import ua.od.and.rss.database.MyDBHelper;
import ua.od.and.rss.fragments.Fragment1;
import ua.od.and.rss.fragments.Fragment2;

import static ua.od.and.rss.Utils.cons.FEED_EDITOR_ACTIVITY;


public class MainActivity extends AppCompatActivity implements Fragment1.OnSelectedButtonListener
{
    SharedPreferences prefs = null;
    private boolean mIsDynamic;
    private MyDBHelper myDBHelper;
    private FragmentManager fragmentManager;
    private long currentRssId = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wide);

        prefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment2 fragment2 = (Fragment2) fragmentManager.findFragmentById(R.id.fragment2);
        mIsDynamic = (fragment2 == null) || (!fragment2.isInLayout());
        if ((mIsDynamic) && (fragmentManager.findFragmentByTag("fragment1") == null))
        {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            Fragment1 fragment1 = new Fragment1();
            ft.add(R.id.flFragmentContainer1, fragment1, "fragment1");
            ft.commit();
        }

        MyDBHelper myDBHelper = new MyDBHelper(getApplicationContext());
        SQLiteDatabase db = myDBHelper.getReadableDatabase();
        ArrayList<RSS> rssList = myDBHelper.getAllRRS(db);
        if (rssList.size() > 0)
        {
            currentRssId = rssList.get(0).getId();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_refresh:
                if (!NetStatus.getInstance(this).isOnline())
                {
                    Toast.makeText(this, "Отсутствует подключение к сети.", Toast.LENGTH_SHORT).show();
                    return super.onOptionsItemSelected(item);
                }
                RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask(this, ""); //Пустая строка для того чтобы были обновлены все RSS
                retrieveFeedTask.execute();

                return true;
            case R.id.action_rss_list:
                Intent intent = new Intent(MainActivity.this, FeedEditorActivity.class);
                startActivityForResult(intent, FEED_EDITOR_ACTIVITY);

                return true;
            case R.id.action_clear_all:
                myDBHelper = new MyDBHelper(getApplicationContext());
                SQLiteDatabase db = myDBHelper.getWritableDatabase();
                myDBHelper.clearAll(db);
                callRefresh(0l);
                Toast.makeText(this, "Все удалено", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.action_refresh_list:
                callRefresh(currentRssId);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void callRefresh(Long rssId)
    {
        fragmentManager = getSupportFragmentManager();
        Fragment1 fragment1 = (Fragment1) fragmentManager.findFragmentByTag("fragment1");
        fragment1.refreshList(rssId);
    }

    @Override
    public void onButtonSelected(long buttonIndex)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment2 fragment2;
        if (mIsDynamic)
        {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            fragment2 = new Fragment2();

            Bundle args = new Bundle();
            args.putLong(Fragment2.BUTTON_INDEX, buttonIndex);
            fragment2.setArguments(args);

            ft.replace(R.id.flFragmentContainer1, fragment2, "fragment2");
            ft.addToBackStack(null);
            // noinspection ResourceType
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            ft.commit();
        } else
        {
            fragment2 = (Fragment2) fragmentManager.findFragmentById(R.id.fragment2);
            fragment2.setDescription(buttonIndex);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (data == null)
        {
            return;
        }
        currentRssId = data.getLongExtra("rssId", 0);
        callRefresh(currentRssId);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //Проверка на первый запуск. Заполняем источниками RSS
        if (prefs.getBoolean("firstrun", true))
        {
            MyDBHelper myDBHelper = new MyDBHelper(getApplicationContext());
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            myDBHelper.addRSS(db, new RSS("http://nv.ua/xml/rss.html", "http://nv.ua/xml/rss.html"));
            myDBHelper.addRSS(db, new RSS("http://itc.ua/feed/", "http://itc.ua/feed/"));
            myDBHelper.addRSS(db, new RSS("https://3dnews.ru/news/rss/", "https://3dnews.ru/news/rss/"));

            prefs.edit().putBoolean("firstrun", false).commit();

            callRefresh(currentRssId);
        }
    }

}
