import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public abstract class Staff<T extends Comparable<T>> extends User implements StaffInterface {
    List<Request> requestsToHandle = new ArrayList<>();
    private SortedSet<T> contributions;

    public Staff() {}

    public Staff(String email, String name, String country,
                 int age, char gender, LocalDate dateOfBirth,
                 AccountType accountType, int experience, SortedSet<T> preferences,SortedSet<T> contributions) {
        super(email, name, country, age, gender, dateOfBirth, accountType, experience, preferences);
        this.contributions = contributions;
    }

    public Staff(String email, String password, String username, String name,
                 String country, int age, char gender, LocalDate dateOfBirth,
                 AccountType accountType, int experience, SortedSet<T> preferences,
                 SortedSet<T> contributions) {
        super(email, password, username, name, country, age, gender, dateOfBirth, accountType, experience, preferences);
        this.contributions = contributions;
    }

    public void addProductionSystem(Production p) {
        contributions.add((T) p);
        IMDB.productionsList.add(p);
    }
    public void addActorSystem(Actor a) {
        contributions.add((T) a);
        IMDB.actorsList.add(a);
    }
    public void removeProductionSystem(String name) {
        // contributions.remove(name);

        for (Production p : IMDB.productionsList) {
            if (p.title.equals(name)) {
                IMDB.productionsList.remove(p);
                break;
            }
        }
    }
    public void removeActorSystem(String name) {
        // contributions.remove(name);

        for (Actor a : IMDB.actorsList) {
            if (a.getName().equals(name)) {
                IMDB.actorsList.remove(a);
                break;
            }
        }
    }
    public void updateProduction(Production p) {
        for (Production production : IMDB.productionsList) {
            if (production.title.equals(p.title)) {
                production = p;
                break;
            }
        }
    }
    public void updateActor(Actor a) {
        for (Actor actor : IMDB.actorsList) {
            if (actor.getName().equals(a.getName())) {
                actor = a;
                break;
            }
        }
    }
}
