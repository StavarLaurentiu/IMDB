import java.time.LocalDate;
import java.util.*;
import java.time.LocalDateTime;

public abstract class User<T extends Comparable<T>> implements Comparable<User<T>> {

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
    List<String> notifications;
    SortedSet<T> preferences;

    public User() {
        info = new Information();
        accountType = AccountType.REGULAR;
        username = "";
        experience = 0;
        notifications = new ArrayList<>();
        preferences = new TreeSet<>();
    }

    public User(String email, String name, String country,
                int age, char gender, LocalDate dateOfBirth,
                AccountType accountType, int experience, SortedSet<T> preferences) {
        this.info = new Information(new Credentials(email),
                name, country, age, gender, dateOfBirth);
        this.accountType = accountType;
        this.username = generateUsername();
        this.experience = experience;
        this.notifications = new ArrayList<>();
        this.preferences = preferences;
    }

    public User(String email, String password, String username, String name,
                String country, int age, char gender, LocalDate dateOfBirth,
                AccountType accountType, int experience, SortedSet<T> preferences) {
        this.info = new Information(new Credentials(email, password),
                name, country, age, gender, dateOfBirth);
        this.accountType = accountType;
        this.username = username;
        this.experience = experience;
        this.notifications = new ArrayList<>();
        this.preferences = preferences;
    }

    public int addPreference(T preference) {
        preferences.add(preference);
        return preferences.size();
    }
    public void removePreference(T preference) {
        preferences.remove(preference);
    }

    public void updateExperience(int newExperience) {
        experience = newExperience;
    }
    public void signOut() {
        System.out.println("Signing out...");
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

    public void addNotification(String notification) {
        notifications.add(notification);
    }

    public void removeNotification(String notification) {
        notifications.remove(notification);
    }
}