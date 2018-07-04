package me.stevelle.logging.testSupport;

import me.stevelle.logging.Logger;
import me.stevelle.logging.Loggable;
import me.stevelle.logging.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Just a testing object which implements Loggable with an overridden builder.
 */
public class Book implements Loggable {

    private static final Logger LOG = LoggerFactory.getLogger(Book.class);

    public final String title;
    public final String[][] authors;
    public final int publishedYear;

    public Book(String title, String[][] authors, int published) {
        this.title = title;
        this.authors = authors;
        this.publishedYear = published;
    }

    public String getAuthors() {
        return String.join("; ", listAuthors(true, true));
    }

    private String[] listAuthors(boolean byLastName, boolean commaSeparate) {
        // This method should never get called
        LOG.warn("Calculating Authors List");
        return Arrays.stream(this.authors)
                .map(author -> formatedName(author, byLastName, commaSeparate))
                .collect(Collectors.toList()).toArray(new String[0]);
    }

    private String formatedName(String[] name, boolean byLastName, boolean commaSeparate) {
        if (byLastName && name.length > 1) {
            String lastName = commaSeparate? last(name) + ", " : last(name);
            StringBuilder parts = new StringBuilder(lastName);
            Arrays.stream(name, 0, name.length - 1)
                    .forEach(parts::append);
            return parts.toString();
        }
        return String.join(" ", Arrays.asList(name));
    }

    private String last(String[] strings) {
        return strings[strings.length - 1];
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", authors=" + Arrays.toString(authors) +
                ", publishedYear=" + publishedYear +
                '}';
    }

    @Override
    public Builder logFormat() {
        return new Loggable.Builder()
                .with("title", this.title)
                .with("year", this.publishedYear);
    }
}
