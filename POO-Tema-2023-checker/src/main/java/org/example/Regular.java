import java.io.PushbackInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
public class Regular<T extends Comparable<T>> extends User implements RequestsManager {

    public Regular(String email, String name, String country,
                   int age, char gender, LocalDate dateOfBirth,
                   int experience, SortedSet<T> preferences) {
        super(email, name, country, age, gender, dateOfBirth, AccountType.REGULAR, experience, preferences);
    }

    public Regular(String email, String password, String username, String name,
                   String country, int age, char gender, LocalDate dateOfBirth,
                   int experience, SortedSet<T> preferences) {
        super(email, password, username, name, country, age, gender, dateOfBirth,
                AccountType.REGULAR, experience, preferences);
    }

    @Override
    public void createRequest(Request r) {
        RequestsHolder.addRequest(r);
    }

    @Override
    public void removeRequest(Request r) {
        RequestsHolder.removeRequest(r);
    }

    public void addRating(Production p, Rating r) {
        p.addRating(r);
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Regular)) return -2;

        Regular r = (Regular) o;
        if (experience < r.experience) return -1;
        else if (experience > r.experience) return 1;

        return this.username.compareTo(r.username);
    }
}
