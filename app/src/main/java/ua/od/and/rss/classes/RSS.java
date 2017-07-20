package ua.od.and.rss.classes;

/**
 * RSS лента
 */

public class RSS
{
    private long id;
    private String name;
    private String link;

    public RSS(String name, String link)
    {
        this.name = name;
        this.link = link;
    }

    public long getId()
    {
        return id;
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
