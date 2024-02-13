import java.util.*;

public class Movie extends Production {
    int duration;
    int releaseYear;

    public Movie() {}

    public Movie(String title, List<String> directors, List<String> actors,
                 List<Genre> genres, List<Rating> ratings, String description,
                 double averageRating, int duration, int releaseYear) {
        super(title, directors, actors, genres, ratings, description, averageRating);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    @Override
    public void displayInfo() {
        if (title != null) System.out.println("Movie Title: " + title);
        if (directors != null) System.out.println("Directors: " + directors);
        if (actors != null) System.out.println("Actors: " + actors);
        if (genres != null) System.out.println("Genres: " + genres);

        System.out.println("Ratings: ");
        for (Rating rating : ratings) {
            rating.displayInfo();
        }

        if (description != null) System.out.println("Description: " + description);
        if (duration > 0) System.out.println("Duration: " + duration + " minutes");
        if (releaseYear > 0 ) System.out.println("Release Year: " + releaseYear);
        if (averageRating > 0) System.out.println("Average Rating: " + averageRating);
        if (contributor != null) System.out.println("Contributor: " + contributor);
    }

}
