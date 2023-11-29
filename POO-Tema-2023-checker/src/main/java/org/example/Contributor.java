import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.SortedSet;

public class Contributor<T extends Comparable<T>> extends Staff implements RequestsManager {
    public Contributor() {}

    public Contributor(String email, String name, String country,
                 int age, char gender, LocalDate dateOfBirth, int experience,
                 SortedSet<T> preferences, SortedSet<T> contributions) {
        super(email, name, country, age, gender, dateOfBirth, AccountType.CONTRIBUTOR,
                experience, preferences, contributions);
    }

    public Contributor(String email, String password, String username, String name,
                 String country, int age, char gender, LocalDate dateOfBirth,
                 int experience, SortedSet<T> preferences, SortedSet<T> contributions) {
        super(email, password, username, name, country, age, gender, dateOfBirth, AccountType.CONTRIBUTOR,
                experience, preferences, contributions);
    }

    @Override
    public void createRequest(Request r) {
        IMDB.requestsList.add(r);
        requestsToHandle.add(r);
    }

    @Override
    public void removeRequest(Request r) {
        IMDB.requestsList.remove(r);
        requestsToHandle.remove(r);
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Contributor)) return -2;

        Contributor c = (Contributor) o;
        if (experience < c.experience) return -1;
        else if (experience > c.experience) return 1;

        return this.username.compareTo(c.username);
    }
}
