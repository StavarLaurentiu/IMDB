import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.SortedSet;

public class Admin <T extends Comparable<T>> extends Staff {

    public Admin() {}

    public Admin(String email, String name, String country,
                       int age, char gender, LocalDate dateOfBirth,
                       SortedSet<T> preferences, SortedSet<T> contributions) {
        super(email, name, country, age, gender, dateOfBirth, AccountType.ADMIN,
                Integer.MAX_VALUE, preferences, contributions);
    }

    public Admin(String email, String password, String username, String name,
                       String country, int age, char gender, LocalDate dateOfBirth,
                       int experience, SortedSet<T> preferences, SortedSet<T> contributions) {
        super(email, password, username, name, country, age, gender, dateOfBirth, AccountType.ADMIN,
                Integer.MAX_VALUE, preferences, contributions);
    }

    public void addUser(User u) {
        IMDB.usersList.add(u);
    }

    public void removeUser(User u) {
        IMDB.usersList.remove(u);
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Admin)) return -2;

        Admin a = (Admin) o;
        return this.username.compareTo(a.username);
    }
}
