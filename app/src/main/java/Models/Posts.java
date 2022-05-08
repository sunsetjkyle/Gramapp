package Models;

public class Posts {
    String caption, post_current_date, post_current_time, publisher, post_id, Full_names, post_img_url,  profile_img_url;

    public Posts() {
    }

    public Posts(String caption, String post_current_date, String post_current_time, String publisher, String post_id, String full_names, String post_img_url, String profile_img_url) {
        this.caption = caption;
        this.post_current_date = post_current_date;
        this.post_current_time = post_current_time;
        this.publisher = publisher;
        this.post_id = post_id;
        this.Full_names = full_names;
        this.post_img_url = post_img_url;
        this.profile_img_url = profile_img_url;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPost_current_date() {
        return post_current_date;
    }

    public void setPost_current_date(String post_current_date) {
        this.post_current_date = post_current_date;
    }

    public String getPost_current_time() {
        return post_current_time;
    }

    public void setPost_current_time(String post_current_time) {
        this.post_current_time = post_current_time;
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

    public String getFull_names() {
        return Full_names;
    }

    public void setFull_names(String full_names) {
        this.Full_names = full_names;
    }

    public String getPost_img_url() {
        return post_img_url;
    }

    public void setPost_img_url(String post_img_url) {
        this.post_img_url = post_img_url;
    }

    public String getProfile_img_url() {
        return profile_img_url;
    }

    public void setProfile_img_url(String profile_img_url) {
        this.profile_img_url = profile_img_url;
    }
}
