package se.amir;

public record Book(String isbn, String title, String author) {

    @Override
    public String toString() {
        return "\"" + title + "\" av " + author + " (ISBN: " + isbn + ")";
    }
}
