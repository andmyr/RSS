package ua.od.and.rss.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ua.od.and.rss.classes.OneNews;
import ua.od.and.rss.classes.RSS;


public class MyDBHelper extends SQLiteOpenHelper
{
    public MyDBHelper(Context context)
    {
        super(context, DBContract.DB_NAME, null, DBContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE '" + DBContract.Tables.RSS_LIST + "' ( `" + DBContract.RSS_list.ID_COLUMN + "` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT " +
                "" + "" + "UNIQUE, `" + DBContract.RSS_list.LINK_COLUMN + "` TEXT NOT NULL, `" + DBContract.RSS_list.NAME_COLUMN + "`" + " TEXT NOT NULL " +
                "UNIQUE )";
        db.execSQL(sql);

        sql = "CREATE TABLE '" + DBContract.Tables.RSS_DATA + "' ( `" + DBContract.RSS_data.ID_COLUMN + "` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" + " "
                + "UNIQUE, `" + DBContract.RSS_data.LIST_ID_COLUMN + "` INTEGER NOT NULL, `" + DBContract.RSS_data.TITLE_COLUMN + "` TEXT, `" + DBContract
                .RSS_data.LINK_COLUMN + "` TEXT," + " `" + DBContract.RSS_data.DESCRIPTION_COLUMN + "` " + "TEXT, `" + DBContract.RSS_data.DATE_COLUMN + "` "
                + "TEXT )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
    }

    public void clearAll(SQLiteDatabase db)
    {
        db.execSQL("DELETE FROM " + DBContract.Tables.RSS_DATA + " WHERE *");
        db.execSQL("DELETE FROM " + DBContract.Tables.RSS_LIST + " WHERE *");
    }

    public void deleteRSS(SQLiteDatabase db, long rssId)
    {
        db.execSQL("DELETE FROM " + DBContract.Tables.RSS_DATA + " WHERE " + DBContract.RSS_data.LIST_ID_COLUMN + " = " + rssId);
        db.execSQL("DELETE FROM " + DBContract.Tables.RSS_LIST + " WHERE " + DBContract.RSS_list.ID_COLUMN + " = " + rssId);
    }

    public void deleteDataFromRSS(SQLiteDatabase db, long rssId)
    {
        db.execSQL("DELETE FROM " + DBContract.Tables.RSS_DATA + " WHERE " + DBContract.RSS_data.LIST_ID_COLUMN + " = " + rssId);
    }

    public long addRSS(SQLiteDatabase db, RSS rss)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.RSS_list.NAME_COLUMN, rss.getName());
        contentValues.put(DBContract.RSS_list.LINK_COLUMN, rss.getLink());
        return db.insert(DBContract.Tables.RSS_LIST, null, contentValues);
    }

    public long addOneNews(SQLiteDatabase db, OneNews oneNews)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.RSS_data.LIST_ID_COLUMN, oneNews.getListId());
        contentValues.put(DBContract.RSS_data.TITLE_COLUMN, oneNews.getTitle());
        contentValues.put(DBContract.RSS_data.LINK_COLUMN, oneNews.getLink());
        contentValues.put(DBContract.RSS_data.DESCRIPTION_COLUMN, oneNews.getDescription());
        contentValues.put(DBContract.RSS_data.DATE_COLUMN, oneNews.getDate());
        return db.insert(DBContract.Tables.RSS_DATA, null, contentValues);
    }
}
