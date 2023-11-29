import java.util.*;

public abstract class Production implements Comparable<Production> {
    String title;
    List<String> directors;
    List<String> actors;
    List<Genre> genres;
    List<Rating> ratings;
    String description;
    double averageRating;

    public Production() {}

    public Production(String title, List<String> directors, List<String> actors,
                      List<Genre> genres, List<Rating> ratings,
                      String description, double averageRating) {
        this.title = title;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.description = description;
        this.averageRating = averageRating;
    }

    public void addRating(Rating rating) {
        ratings.add(rating);
        averageRating = calculateAverageRating();
    }

    public void removeRating(Rating rating) {
        ratings.remove(rating);
        averageRating = calculateAverageRating();
    }

    public int compareTo(Production o) {
        return title.compareTo(o.title);
    }

    public boolean equals(Object o) {
        if (!(o instanceof Production)) return false;

        return title.equals((((Production) o).title));
    }

    public double calculateAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }

        int sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getValue();
        }

        return (double) sum / ratings.size();
    }

    public abstract void displayInfo();

}