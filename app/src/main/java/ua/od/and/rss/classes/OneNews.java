package ua.od.and.rss.classes;

/**
 * Класс одной новости
 */

public class OneNews
{
    private long id;
    private final long listId;
    private final String title;
    private final String link;
    private final String description;
    private String date;

    public OneNews(long listId, String title, String link, String description, String date)
    {
        this.listId = listId;
        this.title = title;
        this.link = link;
        this.description = description;
        this.date = date;
    }

    public OneNews(long id, long listId, String title, String link, String description)
    {
        this.id = id;
        this.listId = listId;
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getListId()
    {
        return listId;
    }

    public String getTitle()
    {
        return title;
    }

    public String getLink()
    {
        return link;
    }

    public String getDescription()
    {
        return description;
    }


    public String getDate()
    {
        return date;
    }
}
