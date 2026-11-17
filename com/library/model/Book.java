
package com.library.model;

public class Book {
    
    private static int totalBooksCreated = 0;

    private int id;
    private String title;
    private String author;
    private String category;
    private String status;
    private String issuedTo;
    private String url;

    public Book(int id, String title, String author, String category, String status, String issuedTo, String url) {
        totalBooksCreated++;
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.status = status;
        this.issuedTo = issuedTo;
        this.url = url;
    }

    public static int getTotalBooksCreated() { return totalBooksCreated; }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getIssuedTo() { return issuedTo; }
    public void setIssuedTo(String issuedTo) { this.issuedTo = issuedTo; }
    public String getUrl() { return url; }
}