package ua.od.and.rss.classes;

/**
 * Класс одной новости
 */

public class OneNews
{
    private long id;
    private long listId;
    private String title;
    private String link;
    private String description;
    private String date;

    public OneNews(long listId, String title, String link, String description, String date)
    {
        this.listId = listId;
        this.title = title;
        this.link = link;
        this.description = description;
        this.date = date;
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
