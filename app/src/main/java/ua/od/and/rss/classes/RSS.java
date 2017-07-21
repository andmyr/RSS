package ua.od.and.rss.classes;

/**
 * RSS лента
 */

public class RSS
{
    private long id;
    private final String name;
    private final String link;

    public RSS(String name, String link)
    {
        this.name = name;
        this.link = link;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public String getLink()
    {
        return link;
    }
}
