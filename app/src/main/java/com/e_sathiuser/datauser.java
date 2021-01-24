package com.e_sathiuser;

public class datauser {
    String username,phone_no,email,pimage;

    public datauser(String username, String phone_no, String email, String pimage) {
        this.username = username;
        this.phone_no = phone_no;
        this.email = email;
        this.pimage = pimage;
    }

    public datauser() {
    }

    public String getUsername() {
        return username;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
