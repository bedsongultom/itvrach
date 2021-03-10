package com.itvrach.model;

import java.io.Serializable;

/**
 * Created by engineer on 5/17/2018.
 */

public class User implements Serializable {
    private int user_id;
    private String username;
    private String password;
    private String reset_password_key;
    private String activation_key;
    private String type;
    private String firstname;
    private String lastname;
    private String fullname;

    private String place;
    private String dateofbirth;
    private int age;

    private double salary;

    private String gender;
    private String religions;
    private String marital_status;
    private String email;
    private String file;
    private String hp;
    private String address;
    private String session_id;

    public User(int user_id, String username, String password, String reset_password_key, String activation_key, String type, String firstname, String lastname, String fullname, String place, String dateofbirth, int age, double salary, String gender, String religions, String marital_status, String email, String file, String hp, String address, String session_id) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.reset_password_key = reset_password_key;
        this.activation_key = activation_key;
        this.type = type;
        this.firstname = firstname;
        this.lastname = lastname;
        this.fullname = fullname;
        this.place = place;
        this.dateofbirth = dateofbirth;
        this.age = age;
        this.salary = salary;
        this.gender = gender;
        this.religions = religions;
        this.marital_status = marital_status;
        this.email = email;
        this.file = file;
        this.hp = hp;
        this.address = address;
        this.session_id = session_id;
    }

    public User() {

    }




    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getReset_password_key() {
        return reset_password_key;
    }

    public void setReset_password_key(String reset_password_key) {
        this.reset_password_key = reset_password_key;
    }


    public String getActivation_key() {
        return activation_key;
    }

    public void setActivation_key(String activation_key) {
        this.activation_key = activation_key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getReligions() {
        return religions;
    }

    public void setReligions(String religions) {
        this.religions = religions;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}
