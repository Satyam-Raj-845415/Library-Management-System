package com.library.model;


public class User extends Person {
    private String email;
    private String password;
    private String studentId;
    private String course;

    public User(String name, String email, String password, String studentId, String course) {
        super(name);
        this.email = email;
        this.password = password;
        this.studentId = studentId;
        this.course = course;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    
    @Override
    public String getRole() {
        return "Student";
    }
}
