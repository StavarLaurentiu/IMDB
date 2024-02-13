import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.SortedSet;

public class Admin <T extends Comparable<T>> extends Staff {

    public Admin() {
        this.accountType = AccountType.ADMIN;
        this.experience = Integer.MAX_VALUE;
    }

    public void addUser(User<?> u) {
        IMDB.usersList.add(u);
    }

    public void removeUser(User<?> u) {
        if (u instanceof Staff<?>) {
            Staff<?> s = (Staff<?>) u;

            for (Object o : s.getContributions()) {
                if (o instanceof Production) {
                    ((Production) o).contributor = "STAFF";
                } else if (o instanceof Actor) {
                    ((Actor) o).setContributor("STAFF");
                }
            }

            for (Request request : RequestsHolder.getRequestsList(u)) {
                RequestsHolder.removeRequest(request);
                request.setSolver("ADMIN");
                RequestsHolder.addRequest(request);
            }
        }

        IMDB.usersList.remove(u);
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Admin)) return -2;

        Admin a = (Admin) o;
        return this.username.compareTo(a.username);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Request) {
            Request r = (Request) o;
            notifications.add("A new request for: " + r.getTitleOrName() +
                        ", with the description: \"" + r.getDescription() +
                    "\", has been added. You can solve it now.");
        } else if (o instanceof Rating) {
            String productionName = (String) arg;
            Rating r = (Rating) o;
            notifications.add(productionName + " has been rated by " + r.getUsername() + " with the rating: "
                    + r.getValue() + ". You've been notified because you added this production.");
        }
    }
}
