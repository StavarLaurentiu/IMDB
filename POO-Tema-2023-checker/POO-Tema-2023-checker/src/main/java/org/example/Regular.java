import java.io.PushbackInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
public class Regular<T extends Comparable<T>> extends User implements RequestsManager {
    private List<Production> reviwedProductions = new ArrayList<>();

    public Regular() {
        this.accountType = AccountType.REGULAR;
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

        boolean isReviewed = false;
        for (Production production : reviwedProductions) {
            if (production.equals(p)) {
                isReviewed = true;
                break;
            }
        }

        if (!isReviewed) {
            updateExperience(new AddReview());
            reviwedProductions.add(p);
        }
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Regular)) return -2;

        Regular r = (Regular) o;
        if (experience < r.experience) return -1;
        else if (experience > r.experience) return 1;

        return this.username.compareTo(r.username);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Request) {
            Request r = (Request) o;
            notifications.add("Your request for: " + r.getTitleOrName() +
                    ", with the description: \"" + r.getDescription() + "\", has been solved or rejected.");
        } else if (o instanceof Rating) {
            String productionName = (String) arg;
            Rating r = (Rating) o;
            notifications.add(productionName + " has been rated by " + r.getUsername() + " with the rating: "
                    + r.getValue() + ". You've been notified because you evaluated this production as well.");
        }
    }
}
