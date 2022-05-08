package Models;

public class Comments {
    String publisher, post_id, img_url, Full_names, user, comment, comment_id, profile_img_url;

    public Comments() {
    }

    public Comments(String publisher, String post_id, String img_url, String full_names, String user, String comment, String comment_id, String profile_img_url) {
        this.publisher = publisher;
        this.post_id = post_id;
        this.img_url = img_url;
        this.Full_names = full_names;
        this.user = user;
        this.comment = comment;
        this.comment_id = comment_id;
        this.profile_img_url = profile_img_url;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getFull_names() {
        return Full_names;
    }

    public void setFull_names(String full_names) {
        this.Full_names = full_names;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getProfile_img_url() {
        return profile_img_url;
    }

    public void setProfile_img_url(String profile_img_url) {
        this.profile_img_url = profile_img_url;
    }
}
