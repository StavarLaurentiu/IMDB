import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import javax.swing.*;

public class IMDB {
    private static IMDB instance = null;
    public static List<User<?>> usersList = new ArrayList<>();
    public static List<Actor> actorsList = new ArrayList<>();
    public static List<Request> requestsList = new ArrayList<>();
    public static List<Production> productionsList = new ArrayList<>();
    public boolean jsonLoaded = false;

    private IMDB() {
    }

    public static IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();
        }

        return instance;
    }


    // ---------------------------------------- Methods for options -------------------------------------------
    public void viewProductionsDetails() throws InvalidCommandException {
        System.out.println("\nYou chose to view productions details.\n");
        System.out.println("Please choose an option:");
        System.out.println("  1. View all productions");
        System.out.println("  2. View only a specific Genre");
        System.out.println("  3. View only productions with more than a specific number of ratings");

        Scanner scanner = new Scanner(System.in);
        System.out.print("\n  Option: ");
        int option = scanner.nextInt();

        if (option == 1) {
            printProductions();
        } else if (option == 2) {
            System.out.println("\nPlease choose a genre:");
            for (int i = 0; i < Genre.values().length; i++) {
                System.out.println("  " + (i + 1) + ". " + Genre.values()[i]);
            }

            System.out.print("\n  Option: ");
            int genreOption = scanner.nextInt();
            if (genreOption < 1 || genreOption > Genre.values().length) {
                throw new InvalidCommandException("Invalid command!");
            }

            Genre genre = Genre.values()[genreOption - 1];
            for (Production p : productionsList) {
                if (p.genres.contains(genre)) {
                    p.displayInfo();
                    System.out.println("----------------------------------------");
                }
            }
        } else if (option == 3) {
            System.out.println("\nPlease enter the minimum number of ratings:");
            System.out.print("\n  Minimum number of ratings: ");
            int minimumNumberOfRatings = scanner.nextInt();

            for (Production p : productionsList) {
                if (p.ratings.size() >= minimumNumberOfRatings) {
                    p.displayInfo();
                    System.out.println("----------------------------------------");
                }
            }
        } else {
            throw new InvalidCommandException("Invalid command!");
        }
    }

    public void viewActorsDetails() throws InvalidCommandException {
        System.out.println("\nYou chose to view actors details.\n");
        System.out.println("Please choose an option:");
        System.out.println("  1. View all actors in random order");
        System.out.println("  2. View all actors in alphabetical order");

        Scanner scanner = new Scanner(System.in);
        System.out.print("\n  Option: ");
        int option = scanner.nextInt();

        if (option == 1) {
            printActors();
        } else if (option == 2) {
            List<Actor> sortedActorsList = new ArrayList<>(actorsList);
            Collections.sort(sortedActorsList);

            for (Actor a : sortedActorsList) {
                a.displayInfo();
                System.out.println("----------------------------------------");
            }
        } else {
            throw new InvalidCommandException("Invalid command!");
        }
    }

    public void viewNotifications(User<?> currentUser) {
        System.out.println("\nYou chose to view notifications.\n");
        System.out.println("Notifications: ");

        int count = 1;
        for (String notification : currentUser.notifications) {
            System.out.println(count + ") " + notification);
            count++;
        }
    }

    public void searchMovieSeriesActor() throws InvalidCommandException {
        System.out.println("\nYou chose to search for a actor/movie/series.\n");
        System.out.println("Please choose an option:");
        System.out.println("  1. Search for a actor");
        System.out.println("  2. Search for a movie");
        System.out.println("  3. Search for an series");

        Scanner scanner = new Scanner(System.in);
        System.out.print("\n  Option: ");
        int option = scanner.nextInt();

        String optionString = "";
        if (option == 1) {
            optionString = "actor";
        } else if (option == 2) {
            optionString = "movie";
        } else if (option == 3) {
            optionString = "series";
        } else {
            throw new InvalidCommandException("Invalid command!");
        }

        System.out.println("\nPlease enter the name of the " + optionString + ":");
        System.out.print("\n  Name: ");
        scanner.nextLine();
        String actorName = scanner.nextLine();
        System.out.println();

        if (option == 1) {
            boolean found = false;
            for (Actor a : actorsList) {
                if (a.getName().equals(actorName)) {
                    a.displayInfo();
                    System.out.println("----------------------------------------");
                    found = true;
                    break;
                }
            }

            if (!found)
                System.out.println("Actor not found!");

        } else if(option == 2) {
            boolean found = false;
            for (Production p : productionsList) {
                if (p.title.equals(actorName) && p instanceof Movie) {
                    p.displayInfo();
                    System.out.println("----------------------------------------");
                    found = true;
                    break;
                }
            }

            if (!found)
                System.out.println("Movie not found!");

        } else if (option == 3) {
            boolean found = false;
            for (Production p : productionsList) {
                if (p.title.equals(actorName) && p instanceof Series) {
                    p.displayInfo();
                    System.out.println("----------------------------------------");
                    found = true;
                    break;
                }
            }

            if (!found)
                System.out.println("Series not found!");

        }
    }

    public void addDeleteToFromFavorites(User<?> currentUser) throws InvalidCommandException {
        System.out.println("\nYou chose to add/delete a production/actor to/from favorites.\n");
        System.out.println("Please choose an action:");
        System.out.println("  1. Add");
        System.out.println("  2. Delete");

        Scanner scanner = new Scanner(System.in);
        System.out.print("\n  Option: ");
        int option = scanner.nextInt();

        if (option == 1) {
            System.out.println("\nYou chose to add a production/actor to favorites.\n");
            System.out.println("What you want to add:");
            System.out.println("  1. Production");
            System.out.println("  2. Actor");

            System.out.print("\n  Option: ");
            int secondOption = scanner.nextInt();

            if (secondOption == 1) {
                System.out.println("\nPlease enter the name of the production:");
                System.out.print("\n  Name: ");
                scanner.nextLine();
                String productionName = scanner.nextLine();
                System.out.println();

                boolean found = false;
                for (Production p : productionsList) {
                    if (p.title.equals(productionName)) {
                        currentUser.addPreference(p);
                        found = true;
                        break;
                    }
                }

                if (!found)
                    System.out.println("Production not found!\n");

            } else if (secondOption == 2) {
                System.out.println("\nPlease enter the name of the actor:");
                System.out.print("\n  Name: ");
                scanner.nextLine();
                String actorName = scanner.nextLine();
                System.out.println();

                boolean found = false;
                for (Actor a : actorsList) {
                    if (a.getName().equals(actorName)) {
                        currentUser.addPreference(a);
                        found = true;
                        break;
                    }
                }

                if (!found)
                    System.out.println("Actor not found!\n");

            } else {
                throw new InvalidCommandException("Invalid command!");
            }
        } else if (option == 2) {
            System.out.println("\nYou chose to delete a production/actor to favorites.\n");
            System.out.println("What you want to delete:");
            System.out.println("  1. Production");
            System.out.println("  2. Actor");

            System.out.print("\n  Option: ");
            int secondOption = scanner.nextInt();

            if (secondOption == 1) {
                System.out.println("\nPlease enter the name of the production:");
                System.out.print("\n  Name: ");
                scanner.nextLine();
                String productionName = scanner.nextLine();
                System.out.println();

                boolean found = false;
                for (Production p : productionsList) {
                    if (p.title.equals(productionName)) {
                        currentUser.removePreference(p);
                        found = true;
                        break;
                    }
                }

                if (!found)
                    System.out.println("Production not found!\n");

            } else if (secondOption == 2) {
                System.out.println("\nPlease enter the name of the actor:");
                System.out.print("\n  Name: ");
                scanner.nextLine();
                String actorName = scanner.nextLine();
                System.out.println();

                boolean found = false;
                for (Actor a : actorsList) {
                    if (a.getName().equals(actorName)) {
                        currentUser.removePreference(a);
                        found = true;
                        break;
                    }
                }

                if (!found)
                    System.out.println("Actor not found!\n");

            } else {
                throw new InvalidCommandException("Invalid command!");
            }
        } else {
            throw new InvalidCommandException("Invalid command!");
        }

        System.out.println("New favorites list: ");
        int count = 1;
        for (Object preference : currentUser.preferences) {
            if (preference instanceof Production) {
                Production p = (Production) preference;
                System.out.println(count + ") " + p.title + ", Production");
            } else if (preference instanceof Actor) {
                Actor a = (Actor) preference;
                System.out.println(count + ") " + a.getName() + ", Actor");
            }

            count++;
        }
    }

    public void createDeleteRequest(User<?> currentUser) throws InvalidCommandException {
        System.out.println("\nYou chose to create/delete a request.\n");
        System.out.println("Please choose an action:");
        System.out.println("  1. Create");
        System.out.println("  2. Delete");

        Scanner scanner = new Scanner(System.in);
        System.out.print("\n  Option: ");
        int option = scanner.nextInt();

        if (option == 1) {
            System.out.println("\nYou chose to create a request.\n");
            System.out.println("Please choose a request type:");
            System.out.println("  1. Actor issue");
            System.out.println("  2. Movie issue");
            System.out.println("  3. Delete account");
            System.out.println("  4. Others");

            System.out.print("\n  Option: ");
            int secondOption = scanner.nextInt();

            if (secondOption == 1) {
                System.out.println("\nPlease enter the name of the actor:");
                System.out.print("\n  Name: ");
                scanner.nextLine();
                String actorName = scanner.nextLine();
                System.out.println();

                System.out.println("\nPlease enter the description of the request:");
                System.out.print("\n  Description: ");
                String description = scanner.nextLine();
                System.out.println();

                Request newRequest = new Request(RequestType.ACTOR_ISSUE, actorName, LocalDateTime.now(), description, currentUser.username);
                RequestsHolder.addRequest(newRequest);

            } else if (secondOption == 2) {
                System.out.println("\nPlease enter the name of the movie:");
                System.out.print("\n  Name: ");
                scanner.nextLine();
                String movieName = scanner.nextLine();
                System.out.println();

                System.out.println("\nPlease enter the description of the request:");
                System.out.print("\n  Description: ");
                String description = scanner.nextLine();
                System.out.println();

                Request newRequest = new Request(RequestType.MOVIE_ISSUE, movieName, LocalDateTime.now(), description, currentUser.username);
                RequestsHolder.addRequest(newRequest);

            } else if (secondOption == 3) {
                System.out.println("\nPlease enter the description of the request:");
                System.out.print("\n  Description: ");
                scanner.nextLine();
                String description = scanner.nextLine();
                System.out.println();

                Request newRequest = new Request(RequestType.DELETE_ACCOUNT, null, LocalDateTime.now(), description, currentUser.username);
                RequestsHolder.addRequest(newRequest);

            } else if (secondOption == 4) {
                System.out.println("\nPlease enter the description of the request:");
                System.out.print("\n  Description: ");
                scanner.nextLine();
                String description = scanner.nextLine();
                System.out.println();

                Request newRequest = new Request(RequestType.OTHERS, null, LocalDateTime.now(), description, currentUser.username);
                RequestsHolder.addRequest(newRequest);
            } else {
                throw new InvalidCommandException("Invalid command!");
            }
        } else if (option == 2) {
            System.out.println("\nYou chose to delete a request.\n");
            System.out.println("Here's a list with all the requests you created:");

            List<Request> myRequests = new ArrayList<>();
            for (Request r : IMDB.requestsList) {
                if (r.getRequester().equals(currentUser.username)) {
                    myRequests.add(r);
                }
            }

            int count = 1;
            for (Request r : myRequests) {
                System.out.println(count + ") For: " + r.getTitleOrName() +
                        ", Description: \"" + r.getDescription() + "\", Type: " +
                        r.getType());
                count++;
            }

            if (count == 1) {
                System.out.println("You didn't created any requests!");
                return;
            }

            System.out.println("\nPlease enter the number of the request you want to delete:");
            System.out.print("\n  Number: ");
            int requestNumber = scanner.nextInt();
            System.out.println();

            if (requestNumber < 1 || requestNumber >= count) {
                throw new InvalidCommandException("Invalid command!");
            }

            Request requestToDelete = myRequests.get(requestNumber - 1);
            RequestsHolder.removeRequest(requestToDelete);
        } else {
            throw new InvalidCommandException("Invalid command!");
        }

        System.out.println("New requests list: ");
        int count = 1;
        for (Request request : IMDB.requestsList) {
            System.out.println(count + ") For: " + request.getTitleOrName() +
                    ", Description: \"" + request.getDescription() + "\", Type: " +
                    request.getType());
            count++;
        }
    }

    public void addDeleteReview(User<?> currentUser) throws InvalidCommandException {
        System.out.println("\nYou chose to add/delete a rating.\n");
        System.out.println("Please choose an action:");
        System.out.println("  1. Create");
        System.out.println("  2. Delete");

        Scanner scanner = new Scanner(System.in);
        System.out.print("\n  Option: ");
        int option = scanner.nextInt();

        if (option == 1) {
            System.out.println("\nYou chose to add a rating.\n");
            System.out.println("Here is a list with all the productions in the system:");

            int count = 1;
            for (Production p : productionsList) {
                if (!p.contributor.equals(currentUser.username)) {
                    System.out.println(count + ") " + p.title);
                    count++;
                }
            }

            System.out.println("\nPlease enter the number of the production " +
                    "you want to add the review for.");
            System.out.print("Number: ");
            int productionNumber = scanner.nextInt();
            System.out.println();

            if (productionNumber < 1 || productionNumber >= count) {
                throw new InvalidCommandException("Invalid command!");
            } else {
                Production currentProduction = productionsList.get(productionNumber - 1);

                System.out.println("Please enter the rating value:");
                System.out.print("  Value: ");
                int value = scanner.nextInt();
                System.out.println();

                System.out.println("Please enter the comment:");
                System.out.print("  Comment: ");
                scanner.nextLine();
                String comment = scanner.nextLine();
                System.out.println();

                Rating newRating = new Rating(currentUser.username, value, comment);
                ((Regular) currentUser).addRating(currentProduction, newRating);
            }
        } else if (option == 2) {
            System.out.println("\nYou chose to delete a rating.\n");
            System.out.println("Here is a list with all the productions in the system:");

            int count = 1;
            for (Production p : productionsList) {
                if (!p.contributor.equals(currentUser.username)) {
                    System.out.println(count + ") " + p.title);
                    count++;
                }
            }

            System.out.println("\nPlease enter the number of the production " +
                    "you want to delete the review from.");
            System.out.print("Number: ");
            int productionNumber = scanner.nextInt();
            System.out.println();

            if (productionNumber < 1 || productionNumber >= count) {
                throw new InvalidCommandException("Invalid command!");
            } else {
                Production currentProduction = productionsList.get(productionNumber - 1);

                System.out.println("Here is a list with all the ratings " +
                        "you've added for this production:");

                count = 1;
                for (Rating r : currentProduction.ratings) {
                    if (r.getUsername().equals(currentUser.username)) {
                        System.out.println(count + ") " + r.getValue() + " by " + r.getUsername() + ", Comment: " + r.getComments());
                        count++;
                    }
                }

                if (count == 1) {
                    System.out.println("You didn't added any ratings for this production!");
                    return;
                } else {
                    System.out.println("\nPlease enter the number of the rating you want to delete:");
                    System.out.print("  Number: ");
                    int ratingNumber = scanner.nextInt();
                    System.out.println();

                    if (ratingNumber < 1 || ratingNumber >= count) {
                        throw new InvalidCommandException("Invalid command!");
                    }

                    Rating ratingToDelete = null;
                    int currentCount = 1;
                    for (Rating r : currentProduction.ratings) {
                        if (r.getUsername().equals(currentUser.username)) {
                            if (currentCount == ratingNumber) {
                                ratingToDelete = r;
                                break;
                            }

                            currentCount++;
                        }
                    }

                    currentProduction.removeRating(ratingToDelete);
                }
            }
        } else {
            throw new InvalidCommandException("Invalid Command");
        }
    }

    public void addDeleteProductionActor(User<?> currentUser) throws InvalidCommandException {
        System.out.println("\nYou chose to add/delete a production/actor.\n");
        System.out.println("Please choose an action:");
        System.out.println("  1. Add");
        System.out.println("  2. Delete");

        Scanner scanner = new Scanner(System.in);
        System.out.print("\n  Option: ");
        int option = scanner.nextInt();

        if (option == 1) {
            System.out.println("\nYou chose to add a production/actor.\n");
            System.out.println("What you want to add:");
            System.out.println("  1. Production");
            System.out.println("  2. Actor");

            System.out.print("\n  Option: ");
            int addOption = scanner.nextInt();

            if (addOption == 1) {
                System.out.println("\nYou chose to add a production.\n");
                System.out.println("Please choose a production type:");
                System.out.println("  1. Movie");
                System.out.println("  2. Series");

                System.out.print("\n  Option: ");
                int secondOption = scanner.nextInt();

                if (secondOption == 1) {
                    System.out.println("\nPlease enter the title of the movie:");
                    System.out.print("\n  Title: ");
                    scanner.nextLine();
                    String title = scanner.nextLine();
                    System.out.println();

                    List<String> directors = new ArrayList<>();
                    System.out.println("Please enter the directors of the movie. Enter \"done\" when you are done.");
                    System.out.print("\n  Director: ");
                    String director = scanner.nextLine();
                    System.out.println();

                    while (!director.equals("done")) {
                        directors.add(director);
                        System.out.print("\n  Director: ");
                        director = scanner.nextLine();
                        System.out.println();
                    }

                    List<String> actors = new ArrayList<>();
                    System.out.println("Please enter the actors of the movie. Enter \"done\" when you are done.");
                    System.out.print("\n  Actor: ");
                    String actor = scanner.nextLine();
                    System.out.println();

                    while (!actor.equals("done")) {
                        actors.add(actor);
                        System.out.print("\n  Actor: ");
                        actor = scanner.nextLine();
                        System.out.println();
                    }

                    List<Genre> genres = new ArrayList<>();
                    System.out.println("Please enter the genres of the movie. Enter \"done\" when you are done.");
                    System.out.print("\n  Genre: ");
                    String genre = scanner.nextLine();
                    System.out.println();

                    while (!genre.equals("done")) {
                        try {
                            genres.add(Genre.valueOf(genre.toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid genre!");
                        }

                        System.out.print("\n  Genre: ");
                        genre = scanner.nextLine();
                        System.out.println();
                    }

                    System.out.println("Please enter the description of the movie.");
                    System.out.print("\n  Description: ");
                    String description = scanner.nextLine();
                    System.out.println();

                    System.out.println("Please enter the duration of the movie.");
                    System.out.print("\n  Duration: ");
                    int duration = scanner.nextInt();
                    System.out.println();

                    System.out.println("Please enter the release year of the movie.");
                    System.out.print("\n  Release year: ");
                    int releaseYear = scanner.nextInt();
                    System.out.println();

                    Movie newMovie = new Movie(title, directors, actors, genres, new ArrayList<>(), description, 0.0, duration, releaseYear);
                    ((Staff) currentUser).addProductionSystem(newMovie);

                } else if (secondOption == 2) {
                    System.out.println("\nPlease enter the title of the series:");
                    System.out.print("\n  Title: ");
                    scanner.nextLine();
                    String title = scanner.nextLine();
                    System.out.println();

                    List<String> directors = new ArrayList<>();
                    System.out.println("Please enter the directors of the series. Enter \"done\" when you are done.");
                    System.out.print("\n  Director: ");
                    String director = scanner.nextLine();
                    System.out.println();

                    while (!director.equals("done")) {
                        directors.add(director);
                        System.out.print("\n  Director: ");
                        director = scanner.nextLine();
                        System.out.println();
                    }

                    List<String> actors = new ArrayList<>();
                    System.out.println("Please enter the actors of the series. Enter \"done\" when you are done.");
                    System.out.print("\n  Actor: ");
                    String actor = scanner.nextLine();
                    System.out.println();

                    while (!actor.equals("done")) {
                        actors.add(actor);
                        System.out.print("\n  Actor: ");
                        actor = scanner.nextLine();
                        System.out.println();
                    }

                    List<Genre> genres = new ArrayList<>();
                    System.out.println("Please enter the genres of the series. Enter \"done\" when you are done.");
                    System.out.print("\n  Genre: ");
                    String genre = scanner.nextLine();
                    System.out.println();

                    while (!genre.equals("done")) {
                        try {
                            genres.add(Genre.valueOf(genre.toUpperCase()));
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid genre!");
                        }

                        System.out.print("\n  Genre: ");
                        genre = scanner.nextLine();
                        System.out.println();
                    }

                    System.out.println("Please enter the description of the series.");
                    System.out.print("\n  Description: ");
                    String description = scanner.nextLine();
                    System.out.println();

                    System.out.println("Please enter the release year of the series.");
                    System.out.print("\n  Release year: ");
                    int releaseYear = scanner.nextInt();
                    System.out.println();

                    System.out.println("Please enter the number of seasons of the series.");
                    System.out.print("\n  Number of seasons: ");
                    int numSeasons = scanner.nextInt();

                    Series newSeries = new Series(title, directors, actors, genres, new ArrayList<>(), description, 0.0, releaseYear, numSeasons);

                    for (int currentSeason = 0; currentSeason < numSeasons; currentSeason++) {
                        String season = "Season " + (currentSeason + 1);
                        System.out.println("Please enter the episodes of the " + season + ". Enter \"done\" as the \"Episode name\" " +
                                "and anything as the \"Duration\" when you are done.");
                        System.out.print("\n  Episode name: ");
                        scanner.nextLine();
                        String episodeName = scanner.nextLine();
                        System.out.println();
                        System.out.print("\n  Duration: ");
                        int duration = scanner.nextInt();
                        System.out.println();

                        while (!episodeName.equals("done")) {
                            Episode newEpisode = new Episode(episodeName, duration);
                            newSeries.addEpisode(newEpisode, season);
                            System.out.print("\n  Episode name: ");
                            scanner.nextLine();
                            episodeName = scanner.nextLine();
                            System.out.println();
                            System.out.print("\n  Duration: ");
                            duration = scanner.nextInt();
                            System.out.println();
                        }

                    }

                    ((Staff) currentUser).addProductionSystem(newSeries);
                }
            } else if (addOption == 2) {
                System.out.println("\nYou chose to add an actor.\n");
                System.out.println("Please enter the name of the actor:");
                System.out.print("\n  Name: ");
                scanner.nextLine();
                String name = scanner.nextLine();
                System.out.println();

                System.out.println("Please enter the biography of the actor.");
                System.out.print("\n  Biography: ");
                String biography = scanner.nextLine();
                System.out.println();

                Actor newActor = new Actor(name, biography);

                System.out.println("Please enter the performances of the actor. Enter \"done\" as the \"Title\" and anything as the \"Type\" when you are done.");
                System.out.print("\n  Title: ");
                String title = scanner.nextLine();
                System.out.println();
                System.out.println("Please enter the type of the performance.");
                System.out.print("\n  Type: ");
                String type = scanner.nextLine();
                System.out.println();

                while (!title.equals("done")) {
                    try {
                        newActor.addProduction(title, ProductionType.valueOf(type.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid production type!");
                    }

                    System.out.print("\n  Title: ");
                    title = scanner.nextLine();
                    System.out.println();
                    System.out.println("Please enter the type of the performance.");
                    System.out.print("\n  Type: ");
                    type = scanner.nextLine();
                    System.out.println();
                }


                ((Staff) currentUser).addActorSystem(newActor);

            } else {
                throw new InvalidCommandException("Invalid command!");
            }
        } else if (option == 2) {
            System.out.println("\nYou chose to delete a production.\n");
            System.out.println("Here is a list with all your contributions in the system:");

            int count = 1;
            for (Object o : ((Staff) currentUser).getContributions()) {
                if (o instanceof Production) {
                    Production p = (Production) o;
                    System.out.println(count + ") " + p.title + ", Production");

                } else if (o instanceof Actor) {
                    Actor a = (Actor) o;
                    System.out.println(count + ") " + a.getName() + ", Actor");
                }
                count++;
            }

            if (count == 1) {
                System.out.println("You don't have any contributions!");
                return;
            }

            System.out.println("\nPlease enter the number of the contribution " +
                    "you want to delete.");
            System.out.print("Number: ");
            int contributionNumber = scanner.nextInt();
            System.out.println();

            Object contributionToDelete = ((Staff<?>) currentUser).getContributions().toArray()[contributionNumber - 1];
            if (contributionToDelete instanceof Production) {
                ((Staff<?>) currentUser).removeProductionSystem((Production) contributionToDelete);
            } else if (contributionToDelete instanceof Actor) {
                ((Staff<?>) currentUser).removeActorSystem((Actor) contributionToDelete);
            }

        } else {
            throw new InvalidCommandException("Invalid command!");
        }
    }

    public void viewSolveRequests(User<?> currentUser) throws InvalidCommandException {
        System.out.println("\nYou chose to view and solve requests.\n");
        System.out.println("Here is a list with all the requests you could solve:");
        List<Request> requestsToSolve = RequestsHolder.getRequestsList(currentUser);

        int count = 1;
        for (Request r : requestsToSolve) {
            System.out.println(count + ") For: " + r.getTitleOrName() +
                    ", Description: \"" + r.getDescription() + "\", Type: " +
                    r.getType() + ", Requester: " + r.getRequester() + ", Solver: " + r.getSolver());
            count++;
        }

        if (count == 1) {
            System.out.println("You don't have any requests to solve!\n");
            return;
        }

        System.out.println("\nPlease enter the number of the request you want to solve or 0 in case" +
                "you don't want to solve any request:");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Number: ");
        int requestNumber = scanner.nextInt();

        if (requestNumber < 0 || requestNumber >= count) {
            throw new InvalidCommandException("Invalid command!");
        } else if (requestNumber == 0) {
            return;
        }

        Request requestToSolve = requestsToSolve.get(requestNumber - 1);

        System.out.println("\nPlease enter the action you want to take:");
        System.out.println("  1. Solve");
        System.out.println("  2. Reject");

        System.out.print("\n  Option: ");
        int option = scanner.nextInt();

        if (option != 1 && option != 2) {
            throw new InvalidCommandException("Invalid command!");
        } else if (option == 1){
            RequestsHolder.solveRequest(requestToSolve, "Solved");
        } else {
            RequestsHolder.solveRequest(requestToSolve, "Rejected");
        }
    }

    public void updateProductionsActors() throws InvalidCommandException {
        System.out.println("\nYou chose to update information about production/actor.\n");

        System.out.println("Please choose an option:");
        System.out.println("  1. Update production");
        System.out.println("  2. Update actor");

        Scanner scanner = new Scanner(System.in);
        System.out.print("\n  Option: ");
        int option = scanner.nextInt();

        if (option == 1) {
            System.out.println("\nYou chose to update a production.\n");
            System.out.println("Here is a list with all the productions in the system:");

            int count = 1;
            for (Production p : productionsList) {
                System.out.println(count + ") " + p.title);
                count++;
            }

            System.out.println("\nPlease enter the number of the production " +
                    "you want to update.");
            System.out.print("Number: ");
            int productionNumber = scanner.nextInt();
            System.out.println();

            if (productionNumber < 1 || productionNumber > productionsList.size())
                throw new InvalidCommandException("Invalid command!");

            Production productionToUpdate = productionsList.get(productionNumber - 1);

            System.out.println("Please choose an option:");
            System.out.println("  1. Update title");
            System.out.println("  2. Update directors");
            System.out.println("  3. Update actors");
            System.out.println("  4. Update genres");
            System.out.println("  5. Update description");

            System.out.print("\n  Option: ");
            int updateOption = scanner.nextInt();
            System.out.println();

            if (updateOption == 1) {
                System.out.println("Please enter the new title:");
                System.out.print("\n  Title: ");
                scanner.nextLine();
                String newTitle = scanner.nextLine();
                System.out.println();

                productionToUpdate.title = newTitle;
            } else if (updateOption == 2) {
                System.out.println("Please enter the new directors. Enter \"done\" when you are done.");
                System.out.print("\n  Director: ");
                scanner.nextLine();
                String director = scanner.nextLine();
                System.out.println();

                List<String> newDirectors = new ArrayList<>();
                while (!director.equals("done")) {
                    newDirectors.add(director);
                    System.out.print("\n  Director: ");
                    director = scanner.nextLine();
                    System.out.println();
                }

                productionToUpdate.directors = newDirectors;
            } else if (updateOption == 3) {
                System.out.println("Please enter the new actors. Enter \"done\" when you are done.");
                System.out.print("\n  Actor: ");
                scanner.nextLine();
                String actor = scanner.nextLine();
                System.out.println();

                List<String> newActors = new ArrayList<>();
                while (!actor.equals("done")) {
                    newActors.add(actor);
                    System.out.print("\n  Actor: ");
                    actor = scanner.nextLine();
                    System.out.println();
                }

                productionToUpdate.actors = newActors;
            } else if (updateOption == 4) {
                System.out.println("Please enter the new genres. Enter \"done\" when you are done.");
                System.out.print("\n  Genre: ");
                scanner.nextLine();
                String genre = scanner.nextLine();
                System.out.println();

                List<Genre> newGenres = new ArrayList<>();
                while (!genre.equals("done")) {
                    try {
                        newGenres.add(Genre.valueOf(genre.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid genre!");
                    }

                    System.out.print("\n  Genre: ");
                    genre = scanner.nextLine();
                    System.out.println();
                }

                productionToUpdate.genres = newGenres;
            } else if (updateOption == 5) {
                System.out.println("Please enter the new description:");
                System.out.print("\n  Description: ");
                scanner.nextLine();
                String newDescription = scanner.nextLine();
                System.out.println();

                productionToUpdate.description = newDescription;
            } else {
                throw new InvalidCommandException("Invalid command!");
            }

        } else if (option == 2) {
            System.out.println("\nYou chose to update an actor.\n");
            System.out.println("Here is a list with all the actors in the system:");

            int count = 1;
            for (Actor a : actorsList) {
                System.out.println(count + ") " + a.getName());
                count++;
            }

            System.out.println("\nPlease enter the number of the actor " +
                    "you want to update.");
            System.out.print("Number: ");
            int actorNumber = scanner.nextInt();
            System.out.println();

            if (actorNumber < 1 || actorNumber > actorsList.size())
                throw new InvalidCommandException("Invalid command!");

            Actor actorToUpdate = actorsList.get(actorNumber - 1);

            System.out.println("Please choose an option:");
            System.out.println("  1. Update name");
            System.out.println("  2. Update biography");

            System.out.print("\n  Option: ");
            int updateOption = scanner.nextInt();
            System.out.println();

            if (updateOption == 1) {
                System.out.println("Please enter the new name:");
                System.out.print("\n  Name: ");
                scanner.nextLine();
                String newName = scanner.nextLine();
                System.out.println();

                actorToUpdate.setName(newName);
            } else if (updateOption == 2) {
                System.out.println("Please enter the new biography:");
                System.out.print("\n  Biography: ");
                scanner.nextLine();
                String newBiography = scanner.nextLine();
                System.out.println();

                actorToUpdate.setBiography(newBiography);
            } else {
                throw new InvalidCommandException("Invalid command!");
            }
        } else {
            throw new InvalidCommandException("Invalid command!");
        }
    }

    public void addDeleteUser(User<?> currentUser) throws InvalidCommandException {
        System.out.println("\nYou chose to add/delete a user.\n");
        System.out.println("Please choose an action:");
        System.out.println("  1. Add");
        System.out.println("  2. Delete");

        Scanner scanner = new Scanner(System.in);
        System.out.print("\n  Option: ");
        int option = scanner.nextInt();

        if (option == 1) {
            System.out.println("\nYou chose to add a user.\n");
            System.out.println("Please insert user's details.");

            System.out.println("Account type: ");
            System.out.println("  1. Regular");
            System.out.println("  2. Contributor");
            System.out.println("  3. Admin");

            System.out.print("\n  Option: ");
            int accountType = scanner.nextInt();
            System.out.println();

            if (accountType < 1 || accountType > 3)
                throw new InvalidCommandException("Invalid command!");

            System.out.print("\n  Email: ");
            scanner.nextLine();
            String email = scanner.nextLine();
            System.out.println();

            System.out.print("\n  Name: ");
            String name = scanner.nextLine();
            System.out.println();

            System.out.print("\n  Age: ");
            int age = scanner.nextInt();
            System.out.println();

            System.out.print("\n  Gender (Male/Female): ");
            scanner.nextLine();
            String genderString = scanner.nextLine();
            char gender = genderString.charAt(0);
            System.out.println();

            System.out.print("\n  Country: ");
            String country = scanner.nextLine();
            System.out.println();

            System.out.print("\n  Date of birth (yyyy-MM-dd): ");
            String dateOfBirth = scanner.nextLine();
            System.out.println();

            System.out.print("\n  Experience: ");
            int experience = scanner.nextInt();
            System.out.println();

            UserFactory userFactory = new UserFactory();
            User<?> newUser = userFactory.getInstance(AccountType.values()[accountType - 1]);

            User.InformationBuilder informationBuilder = newUser.new InformationBuilder();
            informationBuilder.setCredentials(new Credentials(email, User.generatePassword()));
            informationBuilder.setName(name);
            informationBuilder.setCountry(country);
            informationBuilder.setAge(age);
            informationBuilder.setGender(gender);
            informationBuilder.setDateOfBirth(LocalDate.parse(dateOfBirth));
            User.Information information = informationBuilder.getInformation();

            newUser.setInfo(information);
            newUser.setUsername(newUser.generateUsername());
            newUser.setExperience(experience);

            ((Admin) currentUser).addUser(newUser);
        } else if (option == 2) {
            System.out.println("\nYou chose to delete a user.\n");
            System.out.println("Here is a list with all the users in the system:");

            int count = 1;
            for (User<?> u : IMDB.usersList) {
                  if (!u.username.equals(currentUser.username)) {
                      System.out.println(count + ") " + u.username);
                      count++;
                  }
            }

            System.out.println("\nPlease enter the number of the user " +
                    "you want to delete.");
            System.out.print("Number: ");
            int userNumber = scanner.nextInt();
            System.out.println();

            if (userNumber < 1 || userNumber > IMDB.usersList.size())
                throw new InvalidCommandException("Invalid command!");

            User<?> userToDelete = IMDB.usersList.get(userNumber - 1);
            ((Admin) currentUser).removeUser(userToDelete);
        } else {
            throw new InvalidCommandException("Invalid command!");
        }

    }

    public void singOut(User<?> currentUser) throws InvalidCommandException {
        currentUser.signOut();

        System.out.println("\n\nYou've been signed out successfully!\n\n");
        System.out.println("Please select an option:");
        System.out.println("  1. Sign in");
        System.out.println("  2. Exit");

        Scanner scanner = new Scanner(System.in);
        System.out.print("\n  Option: ");
        int option = scanner.nextInt();

        if (option == 1) {
            IMDB.getInstance().run();
        } else if (option == 2) {
            System.exit(0);
        } else {
            throw new InvalidCommandException("Invalid command!");
        }
    }

    // ------------------------------------- Methods for app flow in terminal ---------------------------------
    public void run() throws InvalidCommandException {
        // Load data from JSON files
        if (!jsonLoaded) {
            try {
                loadDataFromJSON();
            } catch (InformationIncompleteException e) {
                System.out.println(e.getMessage());
            }

            jsonLoaded = true;
        }

        // Authenticate user
        User<?> currentUser = authenticateUser();

        // Display options
        while(true) {
            int option = displayOptions(currentUser);

            if (currentUser.accountType.equals(AccountType.REGULAR)) {
                if (option == 1) {
                    viewProductionsDetails();
                } else if (option == 2) {
                    viewActorsDetails();
                } else if (option == 3) {
                    viewNotifications(currentUser);
                } else if (option == 4) {
                    searchMovieSeriesActor();
                } else if (option == 5) {
                    addDeleteToFromFavorites(currentUser);
                } else if (option == 6) {
                    createDeleteRequest(currentUser);
                } else if (option == 7) {
                    addDeleteReview(currentUser);
                } else if (option == 8) {
                    singOut(currentUser);
                }
            } else if (currentUser.accountType.equals(AccountType.CONTRIBUTOR)) {
                if (option == 1) {
                    viewProductionsDetails();
                } else if (option == 2) {
                    viewActorsDetails();
                } else if (option == 3) {
                    viewNotifications(currentUser);
                } else if (option == 4) {
                    searchMovieSeriesActor();
                } else if (option == 5) {
                    addDeleteToFromFavorites(currentUser);
                } else if (option == 6) {
                    createDeleteRequest(currentUser);
                } else if (option == 7) {
                    addDeleteProductionActor(currentUser);
                } else if (option == 8) {
                    viewSolveRequests(currentUser);
                } else if (option == 9) {
                    updateProductionsActors();
                } else if (option == 10) {
                    singOut(currentUser);
                }
            } else if (currentUser.accountType.equals(AccountType.ADMIN)) {
                if (option == 1) {
                    viewProductionsDetails();
                } else if (option == 2) {
                    viewActorsDetails();
                } else if (option == 3) {
                    viewNotifications(currentUser);
                } else if (option == 4) {
                    searchMovieSeriesActor();
                } else if (option == 5) {
                    addDeleteToFromFavorites(currentUser);
                } else if (option == 6) {
                    addDeleteProductionActor(currentUser);
                } else if (option == 7) {
                    viewSolveRequests(currentUser);
                } else if (option == 8) {
                    updateProductionsActors();
                } else if (option == 9) {
                    addDeleteUser(currentUser);
                } else if (option == 10) {
                    singOut(currentUser);
                }
            }
        }
    }

    public int displayOptions(User<?> currentUser) throws InvalidCommandException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n---> Current experience: " + currentUser.experience);
        System.out.println("---> Number of notifications: " + currentUser.notifications.size());
        System.out.println("\nChose an action:");

        switch (currentUser.accountType) {
            case REGULAR:
                System.out.println("  1. View productions details");
                System.out.println("  2. View actor details");
                System.out.println("  3. View notifications");
                System.out.println("  4. Search for actor/movie/series");
                System.out.println("  5. Add/Delete actor/movie/series to/from favorites");
                System.out.println("  6. Create/Delete request");
                System.out.println("  7. Add/Delete review");
                System.out.println("  8. Sing out");
                break;
            case CONTRIBUTOR:
                System.out.println("  1. View productions details");
                System.out.println("  2. View actor details");
                System.out.println("  3. View notifications");
                System.out.println("  4. Search for actor/movie/series");
                System.out.println("  5. Add/Delete actor/movie/series to/from favorites");
                System.out.println("  6. Create/Delete request");
                System.out.println("  7. Add/Delete production/actor");
                System.out.println("  8. View and solve requests");
                System.out.println("  9. Modify production/actor");
                System.out.println("  10. Sing out");
                break;
            case ADMIN:
                System.out.println("  1. View productions details");
                System.out.println("  2. View actor details");
                System.out.println("  3. View notifications");
                System.out.println("  4. Search for actor/movie/series");
                System.out.println("  5. Add/Delete actor/movie/series to/from favorites");
                System.out.println("  6. Add/Delete production/actor");
                System.out.println("  7. View and solve requests");
                System.out.println("  8. Modify production/actor");
                System.out.println("  9. Add/Delete user");
                System.out.println("  10. Sing out");
                break;
        }

        System.out.print("\n  Option: ");
        int option = scanner.nextInt();
        if ((currentUser.accountType == AccountType.REGULAR && (option > 8 || option < 1)) ||
                (currentUser.accountType == AccountType.CONTRIBUTOR && (option > 10 || option < 1)) ||
                (currentUser.accountType == AccountType.ADMIN && (option > 10 || option < 1))) {
            throw new InvalidCommandException("Invalid command!");
        }

        return option;
    }

    public User<?> authenticateUser() {
        System.out.println("\nWelcome to IMDB! Please enter your credentials.\n\n");
        Scanner scanner = new Scanner(System.in);

        int validUser = 0;
        User<?> currentUser = null;
        while (validUser == 0) {
            System.out.print("  Email: ");
            String email = scanner.nextLine();
            System.out.print("  Password: ");
            String password = scanner.nextLine();

            for (User<?> user : usersList) {
                if (user.info.getCredentials().getEmail().equals(email)
                        && user.info.getCredentials().getPassword().equals(password)) {
                    System.out.println("\n\nWelcome back, " + user.info.getName() + "!");
                    System.out.println("Username: " + user.username);
                    System.out.println("Account type: " + user.accountType);
                    if (user.accountType == AccountType.ADMIN) {
                        System.out.println("Experience: ∞");
                    } else {
                        System.out.println("Experience: " + user.experience);
                    }
                    validUser = 1;
                    currentUser = user;
                    break;
                }
            }

            if (validUser == 0) {
                System.out.println("\n\nInvalid credentials. Please try again.\n\n");
            }
        }

        return currentUser;
    }

    // ------------------------------------- Methods for loading date from JSON ------------------------------
    public String getJSONFromFile(String filename) {
        StringBuilder jsonText = new StringBuilder();
        try {
            BufferedReader bufferedReader =
                    new BufferedReader(new FileReader(filename));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonText.append(line).append("\n");
            }

            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonText.toString();
    }

    public void loadDataFromJSON() throws InformationIncompleteException {
        JSONParser parser = new JSONParser();

        try {
            //  ---------------------------------------- Load actors data ----------------------------------------

            String jsonText = getJSONFromFile("/Users/laur/Desktop/Tema Poo/POO-TEMA-2023-input/actors.json");
            JSONArray jsonArray = (JSONArray) parser.parse(jsonText);

            // For every object in the JSON file
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;

                // Read the name and biography of the actor
                String name = (String) jsonObject.get("name");
                String biography = (String) jsonObject.get("biography");

                Actor newActor = new Actor(name, biography);

                // Read performances list
                JSONArray performancesList = (JSONArray) jsonObject.get("performances");
                List<Actor.NameTypePair> performances = new ArrayList<>();
                for (Object performance : performancesList) {
                    JSONObject performanceObject = (JSONObject) performance;
                    String performanceName = (String) performanceObject.get("title");

                    String typeString = (String) performanceObject.get("type");
                    String typeStringUpper = ((String) typeString).toUpperCase();
                    ProductionType performanceType = ProductionType.valueOf(typeStringUpper);

                    newActor.addProduction(performanceName, performanceType);
                }

                actorsList.add(newActor);
            }

            //  ---------------------------------------- Load productions data ----------------------------------------
            jsonText = getJSONFromFile("/Users/laur/Desktop/Tema Poo/POO-TEMA-2023-input/production.json");
            jsonArray = (JSONArray) parser.parse(jsonText);

            // For every object in the JSON file
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;

                // Read title & type
                String title = (String) jsonObject.get("title");
                String type = (String) jsonObject.get("type");

                // Read directors list
                JSONArray directorsList = (JSONArray) jsonObject.get("directors");
                List<String> directors = new ArrayList<>();
                for (Object director : directorsList) {
                    directors.add((String) director);
                }

                // Read actors list
                JSONArray actorsArray = (JSONArray) jsonObject.get("actors");
                List<String> actors = new ArrayList<>();
                for (Object actor : actorsArray) {
                    actors.add((String) actor);

                    boolean exists = false;
                    for (Actor a : actorsList) {
                        if (a.getName().equals((String) actor)) {
                            exists = true;
                            break;
                        }
                    }

                    if (!exists) {
                        Actor newActor = new Actor((String) actor, "");
                        newActor.addProduction(title, ProductionType.valueOf(type.toUpperCase()));
                        IMDB.actorsList.add(newActor);
                    }
                }

                // Read genres list
                JSONArray genresList = (JSONArray) jsonObject.get("genres");
                List<Genre> genres = new ArrayList<>();
                for (Object genre : genresList) {
                    String genreString = ((String) genre).toUpperCase();
                    genres.add(Genre.valueOf(genreString));
                }

                // Read ratings list
                JSONArray ratingList = (JSONArray) jsonObject.get("ratings");
                List<Rating> ratings = new ArrayList<>();
                for (Object rating : ratingList) {
                    JSONObject ratingObject = (JSONObject) rating;
                    String username = (String) ratingObject.get("username");
                    int value = ((Long) ratingObject.get("rating")).intValue();
                    String comment = (String) ratingObject.get("comment");
                    ratings.add(new Rating(username, value, comment));
                }

                // Read the description & averageRating
                String description = (String) jsonObject.get("plot");
                double averageRating = (double) jsonObject.get("averageRating");

                // Read the releaseYear if it is available
                int releaseYear = -1;
                if (jsonObject.containsKey("releaseYear")) {
                    releaseYear = ((Long) jsonObject.get("releaseYear")).intValue();
                }

                // If it is a Movie read the duration
                // If it is a Series read data for every episode(Name and duration) of every Season(Name)
                if (type.equals("Movie")) {
                    String durationString = (String) jsonObject.get("duration");
                    String[] durationArray = durationString.split(" ");
                    int duration = Integer.parseInt(durationArray[0]);
                    productionsList.add(new Movie(title, directors, actors, genres, ratings, description, averageRating, duration, releaseYear));
                } else if (type.equals("Series")){
                    int numberOfSeasons = ((Long) jsonObject.get("numSeasons")).intValue();
                    JSONObject seasonsObject = (JSONObject) jsonObject.get("seasons");
                    Series newSeries = new Series(title, directors, actors, genres, ratings, description, averageRating, releaseYear, numberOfSeasons);

                    for (int i = 0; i < numberOfSeasons; i++) {
                        int currentSeason = i + 1;
                        JSONArray seasonsList = (JSONArray) seasonsObject.get("Season " + currentSeason);

                        for (Object episode : seasonsList) {
                            JSONObject episodeObject = (JSONObject) episode;
                            String name = (String) episodeObject.get("episodeName");

                            String durationString = (String) episodeObject.get("duration");
                            String[] durationArray = durationString.split(" ");
                            int duration = Integer.parseInt(durationArray[0]);

                            newSeries.addEpisode(new Episode(name, duration), "Season " + currentSeason);
                        }
                    }

                    productionsList.add(newSeries);
                }
            }

            //  ---------------------------------------- Load accounts data ----------------------------------------

            jsonText = getJSONFromFile("/Users/laur/Desktop/Tema Poo/POO-TEMA-2023-input/accounts.json");
            jsonArray = (JSONArray) parser.parse(jsonText);

            // For every object in the JSON file
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;

                // Read the username and account type of the user
                String username = (String) jsonObject.get("username");
                String accountTypeString = (String) jsonObject.get("userType");
                accountTypeString = accountTypeString.toUpperCase();
                AccountType accountType = AccountType.valueOf(accountTypeString);

                // Create the user depending on the account type
                UserFactory userFactory = new UserFactory();
                User newUser = userFactory.getInstance(accountType);
                newUser.setUsername(username);

                // Read the experience of the user if it isn't an admin
                int experience = -1;
                if(!accountType.equals(AccountType.ADMIN)) {
                    experience = Integer.parseInt((String) jsonObject.get("experience"));
                    newUser.setExperience(experience);
                }

                // Get the information object
                JSONObject informationObject = (JSONObject) jsonObject.get("information");

                // Read the credentials of the user
                JSONObject credentialsObject = (JSONObject) informationObject.get("credentials");
                String email = (String) credentialsObject.get("email");
                String password = (String) credentialsObject.get("password");
                if (email == null || password == null) {
                    throw new InformationIncompleteException("Invalid credentials!");
                }

                // Read the rest of the information about the user
                String name = (String) informationObject.get("name");
                String country = (String) informationObject.get("country");
                int age = ((Long) informationObject.get("age")).intValue();
                char gender = ((String) informationObject.get("gender")).charAt(0);
                LocalDate dateOfBirth = LocalDate.parse((String) informationObject.get("birthDate"));

                // Create the information object using Builder pattern
                User.InformationBuilder informationBuilder = newUser.new InformationBuilder();
                informationBuilder.setCredentials(new Credentials(email, password));
                informationBuilder.setName(name);
                informationBuilder.setCountry(country);
                informationBuilder.setAge(age);
                informationBuilder.setGender(gender);
                informationBuilder.setDateOfBirth(dateOfBirth);
                User.Information information = informationBuilder.getInformation();

                // Set the information object
                newUser.setInfo(information);

                // Read the notifications of the user if there are some available
                if (jsonObject.containsKey("notifications")) {
                    JSONArray notificationsList = (JSONArray) jsonObject.get("notifications");
                    List<String> notifications = new ArrayList<>();
                    for (Object notification : notificationsList) {
                        notifications.add((String) notification);
                    }

                    newUser.setNotifications(notifications);
                }

                // Declare a comparator for the preferences and contributions -- Compares by name
                Comparator<Object> comparator = new Comparator<Object>() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        if (o1 instanceof Production && o2 instanceof Production) {
                            return ((Production) o1).compareTo((Production) o2);
                        } else if (o1 instanceof Actor && o2 instanceof Actor) {
                            return ((Actor) o1).getName().compareTo(((Actor) o2).getName());
                        } else if (o1 instanceof Production && o2 instanceof Actor) {
                            return ((Production) o1).title.compareTo(((Actor) o2).getName());
                        } else if (o1 instanceof Actor && o2 instanceof Production) {
                            return ((Actor) o1).getName().compareTo(((Production) o2).title);
                        }

                        return 0;
                    }
                };

                // Read the preferences of the user, if there are some available
                SortedSet<Object> preferences = new TreeSet<>(comparator);

                if (jsonObject.containsKey("favoriteProductions")) {
                    JSONArray preferencesList = (JSONArray) jsonObject.get("favoriteProductions");
                    for (Object preference : preferencesList) {
                        String preferenceName = (String) preference;
                        for (Production p : productionsList) {
                            if (p.title.equals(preferenceName)) {
                                preferences.add(p);
                                break;
                            }
                        }
                    }
                }

                if (jsonObject.containsKey("favoriteActors")) {
                    JSONArray preferencesList = (JSONArray) jsonObject.get("favoriteActors");
                    for (Object preference : preferencesList) {
                        String preferenceName = (String) preference;
                        for (Actor a : actorsList) {
                            if (a.getName().equals(preferenceName)) {
                                preferences.add(a);
                                break;
                            }
                        }
                    }
                }

                newUser.setPreferences(preferences);

                // Read the contributions of the user, if the user is an ADMIN or CONTRIBUTOR
                if (accountType.equals(AccountType.ADMIN) || accountType.equals(AccountType.CONTRIBUTOR)) {
                    SortedSet<Object> contributions = new TreeSet<>(comparator);

                    if (jsonObject.containsKey("productionsContribution")) {
                        JSONArray productionList = (JSONArray) jsonObject.get("productionsContribution");
                        for (Object production : productionList) {
                            String productionName = (String) production;
                            for (Production p : productionsList) {
                                if (p.title.equals(productionName)) {
                                    p.setContributor(newUser);
                                    contributions.add(p);
                                    break;
                                }
                            }
                        }
                    }

                    if (jsonObject.containsKey("actorsContribution")) {
                        JSONArray actorList = (JSONArray) jsonObject.get("actorsContribution");
                        for (Object actor : actorList) {
                            String actorName = (String) actor;
                            for (Actor a : actorsList) {
                                if (a.getName().equals(actorName)) {
                                    a.setContributor(newUser);
                                    contributions.add(a);
                                    break;
                                }
                            }
                        }
                    }

                    if (accountType.equals(AccountType.ADMIN)) {
                        ((Admin) newUser).setContributions(contributions);
                    } else {
                        ((Contributor) newUser).setContributions(contributions);
                    }
                }

                // Add the user to the users list
                usersList.add(newUser);
            }

            // ---------------------------------------- Load requests data ----------------------------------------

            jsonText = getJSONFromFile("/Users/laur/Desktop/Tema Poo/POO-TEMA-2023-input/requests.json");
            jsonArray = (JSONArray) parser.parse(jsonText);

            // For every object in the JSON file
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;

                // Read the type of the request
                String typeString = (String) jsonObject.get("type");
                RequestType type = RequestType.valueOf(typeString);

                // Read the creation date of the request
                String creationDateString = (String) jsonObject.get("createdDate");
                LocalDateTime creationDate = LocalDateTime.parse(creationDateString);

                // Read the requester and solver of the request
                String requester = (String) jsonObject.get("username");
                String solver = (String) jsonObject.get("to");

                // Read the description of the request
                String description = (String) jsonObject.get("description");

                // Create the request
                Request newRequest = null;
                if (type.equals(RequestType.DELETE_ACCOUNT) || type.equals(RequestType.OTHERS)) {
                    newRequest = new Request(type,null, creationDate, description, requester, solver);
                } else if (type.equals(RequestType.ACTOR_ISSUE)) {
                    String name = (String) jsonObject.get("actorName");
                    newRequest = new Request(type, name, creationDate, description, requester, solver);
                } else if (type.equals(RequestType.MOVIE_ISSUE)) {
                    String title = (String) jsonObject.get("movieTitle");
                    newRequest = new Request(type, title, creationDate, description, requester, solver);
                }

                // Add the request to the system
                RequestsHolder.addRequest(newRequest);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------- Main method -------------------------------------------------

    public static void main(String[] args) throws InvalidCommandException {
        IMDB imdb = IMDB.getInstance();

        System.out.println("Welcome to IMDB! Select if you want to run it on terminal or GUI\n\n");
        System.out.println("  1. Terminal");
        System.out.println("  2. GUI");
        System.out.print("\n  Option: ");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        if (option != 1 && option != 2) throw new InvalidCommandException("Invalid command!");

        try {
            if (option == 1)
                imdb.run();
            else
                new IMDBLoginScreen();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // ---------------------------------------- Methods for printing ---------------------------------------
    public void printProductions() {
        for (Production p : productionsList) {
            p.displayInfo();
            System.out.println("----------------------------------------");
        }
    }

    public void printActors() {
        for (Actor a : actorsList) {
            a.displayInfo();
            System.out.println("----------------------------------------");
        }
    }

    public void printUsers() {
        for (User<?> u : usersList) {
            u.displayInfo();
            System.out.println("----------------------------------------");
        }
    }

    public void printRequests() {
        for (Request r : requestsList) {
            System.out.println(r);
            System.out.println("----------------------------------------");
        }
    }
}
    // ----------------------------------------------------------------------------------------------------