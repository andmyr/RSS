package ua.od.and.rss.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ua.od.and.rss.R;
import ua.od.and.rss.Utils.NetStatus;
import ua.od.and.rss.asynctasks.RetrieveFeedTask;
import ua.od.and.rss.database.MyDBHelper;
import ua.od.and.rss.fragments.Fragment1;
import ua.od.and.rss.fragments.Fragment2;


public class MainActivity extends AppCompatActivity implements Fragment1.OnSelectedButtonListener
{
    private boolean mIsDynamic;

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
                //Toast.makeText(this, "Заготовка под редактор списка RSS", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, FeedEditorActivity.class);
                startActivityForResult(intent, 1);
                return true;
            case R.id.action_clear_all:
                MyDBHelper myDBHelper = new MyDBHelper(getApplicationContext());
                SQLiteDatabase db = myDBHelper.getWritableDatabase();
                myDBHelper.clearAll(db);
                Toast.makeText(this, "Все удалено", Toast.LENGTH_SHORT).show();
                //TODO Обновление списка новостей
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        Long rssId = data.getLongExtra("rssId", 0);
        MyDBHelper myDBHelper = new MyDBHelper(getApplicationContext());
        SQLiteDatabase db = myDBHelper.getReadableDatabase();
        myDBHelper.getAllNewsFromRss(db, rssId);


    }
}
