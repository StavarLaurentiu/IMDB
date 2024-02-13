import java.io.File;
import java.util.*;

public abstract class Production implements Comparable<Production> {
    String title;
    String imagePath = "/Users/laur/Desktop/Tema Poo/assets/noImageFound.png";
    List<String> directors;
    List<String> actors;
    List<Genre> genres;
    List<Rating> ratings;
    String description;
    double averageRating;
    String contributor;

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
        String possiblePhoto = "/Users/laur/Desktop/Tema Poo/assets/productions/" + title + ".jpg";
        File file = new File(possiblePhoto);
        if (file.exists()) {
            this.imagePath = possiblePhoto;
        }

        contributor = "STAFF";
    }

    public void addRating(Rating rating) {
        for (User<?> user : IMDB.usersList) {
            if (user.username.equals(contributor)) {
                rating.addObserver(user);
                break;
            }
        }

        for (Rating r : ratings) {
            for (User<?> user : IMDB.usersList) {
                if (user.username.equals(r.getUsername())) {
                    rating.addObserver(user);
                    break;
                }
            }
        }

        ratings.add(rating);
        averageRating = calculateAverageRating();
        rating.notifyObserver(this);
    }

    public void removeRating(Rating rating) {
        ratings.remove(rating);
        averageRating = calculateAverageRating();
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


    public int compareTo(Production o) {
        return title.compareTo(o.title);
    }
    public double getAverageRating() {
        return averageRating;
    }

    public void setContributor(User<?> contributor) {
        this.contributor = contributor.username;

        for (Rating rating : ratings) {
            rating.addObserver(contributor);
        }
    }

    public boolean equals(Object o) {
        if (!(o instanceof Production)) return false;

        return title.equals((((Production) o).title));
    }
    public abstract void displayInfo();

}