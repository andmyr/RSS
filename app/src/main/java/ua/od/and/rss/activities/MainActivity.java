package ua.od.and.rss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ua.od.and.rss.R;
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
        //Toast.makeText(getApplicationContext(), mIsDynamic + "", Toast.LENGTH_SHORT).show();
        if ((mIsDynamic) && (fragmentManager.findFragmentByTag("fragment1") == null))
        {
            // начинаем транзакцию
            FragmentTransaction ft = fragmentManager.beginTransaction();
            // Создаем и добавляем первый фрагмент
            Fragment1 fragment1 = new Fragment1();
            ft.add(R.id.flFragmentContainer1, fragment1, "fragment1");
            // Подтверждаем операцию
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
                Toast.makeText(this, "Заготовка под обновление RSS ленты", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_rss_list:
                //Toast.makeText(this, "Заготовка под редактор списка RSS", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, FeedEditorActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_clear_all:
                Toast.makeText(this, "Заготовка под очистку ленты", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onButtonSelected(int buttonIndex)
    {
        // подключаем FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment2 fragment2;

        // Если фрагмент недоступен
        if (mIsDynamic)
        {
            // Динамическое переключение на другой фрагмент
            FragmentTransaction ft = fragmentManager.beginTransaction();
            fragment2 = new Fragment2();

            // Подготавливаем аргументы
            Bundle args = new Bundle();
            args.putInt(Fragment2.BUTTON_INDEX, buttonIndex);
            fragment2.setArguments(args);

            ft.replace(R.id.flFragmentContainer1, fragment2, "fragment2");
            ft.addToBackStack(null);
            // noinspection ResourceType
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            ft.commit();
        } else
        {
            // Если фрагмент доступен
            fragment2 = (Fragment2) fragmentManager.findFragmentById(R.id.fragment2);
            fragment2.setDescription(buttonIndex);
        }
    }
}
