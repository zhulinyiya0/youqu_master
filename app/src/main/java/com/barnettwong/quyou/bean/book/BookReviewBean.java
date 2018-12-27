package com.barnettwong.quyou.bean.book;

/**
 * Created by wang on 2018/12/27 09:56
 */
public class BookReviewBean {
    private String id;
    private String title;
    private String updated;
    private String comments;
    private int votes;
    private int useless;
    private String summary;
    private UserRatingBean rating;
    private UserBean author;

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

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getUseless() {
        return useless;
    }

    public void setUseless(int useless) {
        this.useless = useless;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public UserRatingBean getRating() {
        return rating;
    }

    public void setRating(UserRatingBean rating) {
        this.rating = rating;
    }

    public UserBean getAuthor() {
        return author;
    }

    public void setAuthor(UserBean author) {
        this.author = author;
    }
}
