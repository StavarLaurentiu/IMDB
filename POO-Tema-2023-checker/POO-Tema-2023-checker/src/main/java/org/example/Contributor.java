import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.SortedSet;

public class Contributor<T extends Comparable<T>> extends Staff implements RequestsManager {
    public Contributor() {
        this.accountType = AccountType.CONTRIBUTOR;
    }

    @Override
    public void createRequest(Request r) {
        RequestsHolder.addRequest(r);
    }

    @Override
    public void removeRequest(Request r) {
        RequestsHolder.removeRequest(r);
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Contributor)) return -2;

        Contributor c = (Contributor) o;
        if (experience < c.experience) return -1;
        else if (experience > c.experience) return 1;

        return this.username.compareTo(c.username);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Request) {
            String argString = (String) arg;
            Request r = (Request) o;
            if (argString.equals("solved")) {
                notifications.add("Your request for: " + r.getTitleOrName() +
                        ", with the description: \"" + r.getDescription() + "\", has been solved or rejected.");
            } else if (argString.equals("to solve")) {
                notifications.add("A new request for: " + r.getTitleOrName() +
                        ", with the description: \"" + r.getDescription() + "\", has been added. You can solve it now.");
            }
        } else if (o instanceof Rating) {
            String productionName = (String) arg;
            Rating r = (Rating) o;
            notifications.add(productionName + " has been rated by " + r.getUsername() + " with the rating: "
                    + r.getValue() + ". You've been notified because you added this production.");
        }
    }
}
