package practica.model;

import java.io.Serializable;
import java.util.Objects;

public class NewsItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String content;
    private String source;
    private String author;
    private String url;
    private String publishedAt;

    public NewsItem() {
    }

    public NewsItem(String id, String title, String content, String source, String author, String url,
            String publishedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.source = source;
        this.author = author;
        this.url = url;
        this.publishedAt = publishedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTextForAnalysis() {
        String safeTitle = title == null ? "" : title;
        String safeContent = content == null ? "" : content;
        return (safeTitle + "\n" + safeContent).trim();
    }

    public boolean hasContent() {
        return getTextForAnalysis().length() > 0;
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, source, url);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NewsItem)) {
            return false;
        }
        NewsItem other = (NewsItem) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(title, other.title)
                && Objects.equals(source, other.source)
                && Objects.equals(url, other.url);
    }
}
