package ua.od.and.rss.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.MalformedURLException;
import java.net.URL;

import ua.od.and.rss.R;
import ua.od.and.rss.asynctasks.RetrieveFeedTask;

public class FeedEditorActivity extends AppCompatActivity implements View.OnClickListener
{

    private EditText etRSSLink;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsseditor);
        etRSSLink = (EditText) findViewById(R.id.etRSSLink);
        Button btnLoadRSS = (Button) findViewById(R.id.btnLoadRSS);
        btnLoadRSS.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        URL url = null;
        try
        {
            url = new URL(etRSSLink.getText().toString());
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask(this, url);
        retrieveFeedTask.execute();
    }
}
