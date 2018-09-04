package nl.idexx.animana.assignment.model;

public class Artwork {

    //The response
    //elements will only contain title, authors(/artists) and information whether it’s a book or an album.
    private final String author;
    private final String title;
    private final String type;

    public Artwork(String author, String title, String type) {
        this.author = author;
        this.title = title;
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }
}
