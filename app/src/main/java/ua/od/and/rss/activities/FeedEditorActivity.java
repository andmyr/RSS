package ua.od.and.rss.activities;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ua.od.and.rss.R;
import ua.od.and.rss.asynctasks.RetrieveFeedTask;
import ua.od.and.rss.classes.RSS;
import ua.od.and.rss.database.MyDBHelper;

public class FeedEditorActivity extends AppCompatActivity implements View.OnClickListener
{

    private EditText etRSSLink;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> allRRSList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsseditor);

        ListView listView = (ListView) findViewById(R.id.listView);
        MyDBHelper myDBHelper = new MyDBHelper(getApplicationContext());
        SQLiteDatabase db = myDBHelper.getReadableDatabase();

        Button btnAddRSS = (Button) findViewById(R.id.btnAddRSS);
        btnAddRSS.setOnClickListener(this);
        Button btnDeleteRSS = (Button) findViewById(R.id.btnDeleteRSS);
        btnDeleteRSS.setOnClickListener(this);
        allRRSList = myDBHelper.getAllRRSLinks(db);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allRRSList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {

            }
        });

        //Для тестирования
        etRSSLink = (EditText) findViewById(R.id.etRSSLink);
        Button btnLoadRSS = (Button) findViewById(R.id.btnLoadRSS);
        btnLoadRSS.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnLoadRSS:
            {
                String strUrl;
                strUrl = etRSSLink.getText().toString();
                RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask(this, strUrl);
                retrieveFeedTask.execute();
                break;
            }
            case R.id.btnAddRSS:
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Добавление RSS");
                alert.setMessage("Введите адрес");

                final EditText input = new EditText(getApplicationContext());
                alert.setView(input);

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        String link = input.getEditableText().toString();
                        if (link.length() < 1)
                        {
                            Toast.makeText(FeedEditorActivity.this, "Введите адрес RSS", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        MyDBHelper myDBHelper = new MyDBHelper(getApplicationContext());
                        SQLiteDatabase db = myDBHelper.getWritableDatabase();
                        myDBHelper.addRSS(db, new RSS(link, link));
                        allRRSList.add(link);
                        Toast.makeText(FeedEditorActivity.this, "Адрес добавлен", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    }
                });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        // Canceled.
                        dialog.cancel();
                    }
                }); //End of alert.setNegativeButton
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                break;
            }
            case R.id.btnDeleteRSS:
            {
                break;
            }

            default:
                break;
        }

    }
}
