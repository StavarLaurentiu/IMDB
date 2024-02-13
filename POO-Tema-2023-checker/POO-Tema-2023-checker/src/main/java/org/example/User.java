import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;
import java.time.LocalDateTime;

public abstract class User<T extends Comparable<T>> implements Comparable<User<T>>, Observer {

    class InformationBuilder {
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private char gender;
        private LocalDate dateOfBirth;

        public InformationBuilder() {}

        public InformationBuilder setCredentials(Credentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public InformationBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public InformationBuilder setCountry(String country) {
            this.country = country;
            return this;
        }

        public InformationBuilder setAge(int age) {
            this.age = age;
            return this;
        }

        public InformationBuilder setGender(char gender) {
            this.gender = gender;
            return this;
        }

        public InformationBuilder setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public User<?>.Information getInformation() {
            return new User<?>.Information(credentials, name, country, age, gender,
                    dateOfBirth);
        }
    }

    class Information {
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private char gender;
        private LocalDate dateOfBirth;

        public Information() {}

        private Information(Credentials credentials, String name,
                            String country, int age, char gender,
                            LocalDate dateOfBirth) {
            this.credentials = credentials;
            this.name = name;
            this.country = country;
            this.age = age;
            this.gender = gender;
            this.dateOfBirth = dateOfBirth;
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public String getName() {
            return name;
        }

        public String getCountry() {
            return country;
        }

        public int getAge() {
            return age;
        }

        public char getGender() {
            return gender;
        }

        public LocalDate getDateOfBirth() {
            return dateOfBirth;
        }

        public void setCredentials(Credentials credentials) {
            this.credentials = credentials;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setGender(char gender) {
            this.gender = gender;
        }

        public void setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }
    }

    Information info;
    AccountType accountType;
    String username;
    int experience;
    List<String> notifications = new ArrayList<>();
    SortedSet<T> preferences = new TreeSet<T>();

    public void setInfo(Information info) {
        this.info = info;
    }
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setExperience(int newExperience) {
        experience = newExperience;
    }
    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }
    public void addNotification(String notification) {
        notifications.add(notification);
    }
    public void removeNotification(String notification) {
        notifications.remove(notification);
    }
    public void setPreferences(SortedSet<T> preferences) {
        this.preferences = preferences;
    }
    public void addPreference(Object preference) {
        if (preference instanceof Production) {
            preferences.add((T) preference);
        } else if (preference instanceof Actor) {
            preferences.add((T) preference);
        }
    }
    public void removePreference(Object preference) {
        preferences.remove((T) preference);
    }
    public boolean hasPreference(Object preference) {
        return preferences.contains((T) preference);
    }
    public void signOut() {
        System.out.println("Signing out...");

        // Load data back to JSON
    }
    public String generateUsername() {
        String name = this.info.getName().toLowerCase();
        String[] nameParts = name.split(" ");

        String generatedUsername = "";

        for (String s : nameParts) {
            generatedUsername = generatedUsername + s + "_";
        }

        Random random = new Random();
        int randomNum = random.nextInt(1000);

        generatedUsername = generatedUsername + randomNum;
        return generatedUsername;
    }
    public static String generatePassword() {
        final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
        final String UPPERCASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String DIGITS = "0123456789";
        final String SPECIAL_CHARACTERS = "!@#$%^&*()_+-=[]{}|;:,.<>?";

        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();
        List<Character> chars = new ArrayList<>();

        String charSet = LOWERCASE_CHARACTERS + UPPERCASE_CHARACTERS + DIGITS + SPECIAL_CHARACTERS;

        for (char c : charSet.toCharArray()) {
            chars.add(c);
        }

        Collections.shuffle(chars, random);

        for (int i = 0; i < 12; i++) { // Fixed length of 12 characters
            int randomIndex = random.nextInt(chars.size());
            password.append(chars.get(randomIndex));
        }

        return password.toString();
    }
    public void displayInfo() {
        System.out.println("Username: " + username);
        if (experience < 2147483647) System.out.println("Experience: " + experience);
        else System.out.println("Experience: ∞");
        System.out.println("Account type: " + accountType);
        System.out.println("Information: ");
        System.out.println("    Name: " + info.getName());
        System.out.println("    Country: " + info.getCountry());
        System.out.println("    Age: " + info.getAge());
        System.out.println("    Gender: " + info.getGender());
        System.out.println("    Date of birth: " + info.getDateOfBirth());

        System.out.println("Credentials: ");
        System.out.println("    Email: " + info.getCredentials().getEmail());
        System.out.println("    Password: " + info.getCredentials().getPassword());

        System.out.println("Notifications: ");
        for (String notification : notifications) {
            System.out.println("    " + notification);
        }

        System.out.println("Preferences: ");
        for (Object preference : preferences) {
            if (preference instanceof Production) {
                Production preferenceCasted = (Production) preference;

                if (preferenceCasted instanceof Movie) {
                    System.out.println("    Type: Movie");
                } else if (preferenceCasted instanceof Series) {
                    System.out.println("    Type: Series");
                }
                System.out.println("    Title: " + preferenceCasted.title);
                System.out.println("    Description: " + preferenceCasted.description);
                System.out.println("    Average rating: " + preferenceCasted.averageRating);
                System.out.println();
            }

            if (preference instanceof Actor)  {
                Actor preferenceCasted = (Actor) preference;

                System.out.println("    Type: Actor");
                System.out.println("    Name: " + preferenceCasted.getName());
                System.out.println("    Biography: " + preferenceCasted.getBiography());
                System.out.println();
            }
        }
    }
    public void updateExperience(ExperienceStrategy strategy) {
        experience += strategy.calculateExperience();
    }
}