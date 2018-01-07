package io.ky.mb.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A BlogEntry.
 */
@Document(collection = "blog_entry")
public class BlogEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("blog")
    private String blog;

    @NotNull
    @Field("title")
    private String title;

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
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getBlog() {
        return this.blog;
    }

    public BlogEntry blog(final String blog) {
        this.blog = blog;
        return this;
    }

    public void setBlog(final String blog) {
        this.blog = blog;
    }

    public String getTitle() {
        return this.title;
    }

    public BlogEntry title(final String title) {
        this.title = title;
        return this;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getUser() {
        return this.user;
    }

    public BlogEntry user(final String user) {
        this.user = user;
        return this;
    }

    public void setUser(final String user) {
        this.user = user;
    }

    public String getContent() {
        return this.content;
    }

    public BlogEntry content(final String content) {
        this.content = content;
        return this;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public BlogEntry date(final ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(final ZonedDateTime date) {
        this.date = date;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BlogEntry blogEntry = (BlogEntry) o;
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
