package Models;

public class User { String user, User_email, Phone_numbers, profile_img_url, Full_names;

    public User() {
    }

    public User(String user, String user_email, String phone_numbers, String profile_img_url, String full_names) {
        this.user = user;
        this.User_email = user_email;
       this.Phone_numbers = phone_numbers;
        this.profile_img_url = profile_img_url;
       this.Full_names = full_names;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser_email() {
        return User_email;
    }

    public void setUser_email(String user_email) {
       this.User_email = user_email;
    }

    public String getPhone_numbers() {
        return Phone_numbers;
    }

    public void setPhone_numbers(String phone_numbers) {
        this.Phone_numbers = phone_numbers;
    }

    public String getProfile_img_url() {
        return profile_img_url;
    }

    public void setProfile_img_url(String profile_img_url) {
        this.profile_img_url = profile_img_url;
    }

    public String getFull_names() {
        return Full_names;
    }

    public void setFull_names(String full_names) {
        this.Full_names = full_names;
    }
}
