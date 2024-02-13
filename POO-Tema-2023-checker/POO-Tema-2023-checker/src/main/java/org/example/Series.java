import java.util.*;

public class Series extends Production {
    long releaseYear;
    long numSeasons;
    private Map<String, List<Episode>> episodesBySeason;

    public Series() {}

    public Series(String title, List<String> directors, List<String> actors,
                  List<Genre> genres, List<Rating> ratings, String description, double averageRating,
                  long releaseYear, long numSeasons) {
        super(title, directors, actors, genres, ratings, description, averageRating);
        this.releaseYear = releaseYear;
        this.numSeasons = numSeasons;
        this.episodesBySeason = new LinkedHashMap<>();
    }

    public void addEpisode(Episode episode, String season) {
        if (episodesBySeason.containsKey(season)) {
            episodesBySeason.get(season).add(episode);
        } else {
            List<Episode> episodes = new ArrayList<>();
            episodes.add(episode);
            episodesBySeason.put(season, episodes);
        }
    }

    public void removeEpisode(Episode episode, String season) {
        if (episodesBySeason.containsKey(season)) {
            episodesBySeason.get(season).remove(episode);
        }
    }

    public Map<String, List<Episode>> getEpisodesBySeason() {
        return episodesBySeason;
    }

    @Override
    public void displayInfo() {
        System.out.println("Series Title: " + title);
        if (directors != null) System.out.println("Directors: " + directors);
        if (actors != null) System.out.println("Actors: " + actors);
        if (genres != null) System.out.println("Genres: " + genres);

        System.out.println("Ratings: ");
        for (Rating rating : ratings) {
            rating.displayInfo();
        }

        if (description != null) System.out.println("Description: " + description);
        if (averageRating > 0) System.out.println("Average Rating: " + averageRating);
        if (contributor != null) System.out.println("Contributor: " + contributor);
        if (releaseYear > 0) System.out.println("Release Year: " + releaseYear);
        if (numSeasons >= 0) System.out.println("Number of Seasons: " + numSeasons);

        // Display episodes by season if available
        if (episodesBySeason != null) {
            System.out.println("Episodes by Season:");
            for (Map.Entry<String, List<Episode>> entry : episodesBySeason.entrySet()) {
                System.out.println("    " + entry.getKey() + ":");
                for (Episode episode : entry.getValue()) {
                    episode.displayInfo();
                }
            }
        }
    }

}
