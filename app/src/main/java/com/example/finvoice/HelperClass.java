package com.example.finvoice;

public class HelperClass {
    private String name;
    private String email;
    private String username;
    private String password;
    private String contactNo;
    // Store encrypted passwords in production

    // Default no-argument constructor for Firebase
    public HelperClass() {
    }



    // Parameterized constructor
    public HelperClass(String name, String email, String username, String password, String contactNo) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.contactNo = contactNo;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
}
