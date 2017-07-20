package ua.od.and.rss.database;

/**
 *
 */

public class DBContract
{
    public static final String DB_NAME = "RSSDataBase";
    public static final int DATABASE_VERSION = 1;

    public static final class Tables
    {
        public static final String RSS_LIST = "RSS_list";
        public static final String RSS_DATA = "RSS_data";
    }

    public static final class RSS_list
    {
        public static final String ID_COLUMN = "_id";
        public static final String LINK_COLUMN = "link";
        public static final String NAME_COLUMN = "name";
    }

    public static final class RSS_data
    {
        public static final String ID_COLUMN = "_id";
        public static final String LIST_ID_COLUMN = "list_id";
        public static final String TITLE_COLUMN = "title";
        public static final String LINK_COLUMN = "link";
        public static final String DESCRIPTION_COLUMN = "description";
        public static final String DATE_COLUMN = "date";
    }
}
