import java.io.BufferedReader;
import java.io.FileReader;
import java.security.spec.ECField;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class IMDB {
    private static IMDB instance = null;
    public static List<User<?>> usersList = new ArrayList<>();
    public static List<Actor> actorsList = new ArrayList<>();
    public static List<Request> requestsList = new ArrayList<>();
    public static List<Production> productionsList = new ArrayList<>();

    private IMDB() {
    }

    public static IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();
        }

        return instance;
    }

    public void run() throws InvalidCommandException {
        // Load data from JSON files
        try {
            loadDataFromJSON();
        } catch (InformationIncompleteException e) {
            System.out.println(e.getMessage());
        }

        // Authenticate user
        User<?> currentUser = authenticateUser();

        // Display options
        while(true) {
            int option = displayOptions(currentUser);
        }
    }

    public static int displayOptions(User<?> currentUser) throws InvalidCommandException {
        Scanner scanner = new Scanner(System.in);
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
                System.out.println("  6. Create/Delete request");
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

    public static User<?> authenticateUser() {
        System.out.println("Welcome to IMDB! Please enter your credentials.\n\n");
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
                    System.out.println("Experience: " + user.experience);
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

    public static void loadDataFromJSON() throws InformationIncompleteException {
        JSONParser parser = new JSONParser();

        try {
            //  ---------------------------------------- Load productions data ----------------------------------------
            String jsonText = getJSONFromFile("/Users/laur/Desktop/Tema Poo/POO-TEMA-2023-input/production.json");
            JSONArray jsonArray = (JSONArray) parser.parse(jsonText);

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
                JSONArray actorsList = (JSONArray) jsonObject.get("actors");
                List<String> actors = new ArrayList<>();
                for (Object actor : actorsList) {
                    actors.add((String) actor);
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

//            for (Production p : productionsList) {
//                p.displayInfo();
//                System.out.println("----------------------------------------");
//            }

            //  ---------------------------------------- Load actors data ----------------------------------------

            jsonText = getJSONFromFile("/Users/laur/Desktop/Tema Poo/POO-TEMA-2023-input/actors.json");
            jsonArray = (JSONArray) parser.parse(jsonText);

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

//            for (Actor a : actorsList) {
//                a.displayInfo();
//                System.out.println("----------------------------------------");
//            }

            //  ---------------------------------------- Load accounts data ----------------------------------------

            jsonText = getJSONFromFile("/Users/laur/Desktop/Tema Poo/POO-TEMA-2023-input/accounts.json");
            jsonArray = (JSONArray) parser.parse(jsonText);

            // For every object in the JSON file
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;

                // Read the username, experience and account type of the user
                String username = (String) jsonObject.get("username");
                String accountTypeString = (String) jsonObject.get("userType");
                accountTypeString = accountTypeString.toUpperCase();
                AccountType accountType = AccountType.valueOf(accountTypeString);

                int experience = -1;
                if(!accountType.equals(AccountType.ADMIN)) experience = Integer.parseInt((String) jsonObject.get("experience"));

                // Read the credentials of the user
                JSONObject informationObject = (JSONObject) jsonObject.get("information");
                JSONObject credentialsObject = (JSONObject) informationObject.get("credentials");
                String email = (String) credentialsObject.get("email");
                String password = (String) credentialsObject.get("password");
                if (email == null || password == null) {
                    throw new InformationIncompleteException("Invalid credentials!");
                }

                // Read the information of the user
                String name = (String) informationObject.get("name");
                String country = (String) informationObject.get("country");
                int age = ((Long) informationObject.get("age")).intValue();
                char gender = ((String) informationObject.get("gender")).charAt(0);
                LocalDate dateOfBirth = LocalDate.parse((String) informationObject.get("birthDate"));

//                System.out.println(username);
//                System.out.println(accountType);
//                System.out.println(experience);
//                System.out.println(email);
//                System.out.println(password);
//                System.out.println(name);
//                System.out.println(country);
//                System.out.println(age);
//                System.out.println(gender);
//                System.out.println(dateOfBirth);
//                System.out.println("----------------------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getJSONFromFile(String filename) {
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

    public static void main(String[] args) {
        IMDB imdb = IMDB.getInstance();
        // System.out.println(IMDB.class.getCanonicalName()); // IMDB
        try {
            imdb.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
