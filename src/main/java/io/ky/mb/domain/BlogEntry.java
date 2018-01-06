package io.ky.mb.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A BlogEntry.
 */
@Document(collection = "blog_entry")
public class BlogEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("blog")
    private String blog;

    @NotNull
    @Field("title")
    private String title;

    @NotNull
    @Field("user")
    private String user;

    @NotNull
    @Field("content")
    private String content;

    @NotNull
    @Field("date")
    private ZonedDateTime date;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBlog() {
        return blog;
    }

    public BlogEntry blog(String blog) {
        this.blog = blog;
        return this;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getTitle() {
        return title;
    }

    public BlogEntry title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public BlogEntry user(String user) {
        this.user = user;
        return this;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public BlogEntry content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public BlogEntry date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BlogEntry blogEntry = (BlogEntry) o;
        if (blogEntry.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), blogEntry.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BlogEntry{" +
            "id=" + getId() +
            ", blog='" + getBlog() + "'" +
            ", title='" + getTitle() + "'" +
            ", user='" + getUser() + "'" +
            ", content='" + getContent() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
