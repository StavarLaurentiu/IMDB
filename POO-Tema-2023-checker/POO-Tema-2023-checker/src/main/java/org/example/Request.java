import java.lang.module.ResolvedModule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Request extends Observable{
    private RequestType type;
    private LocalDateTime date;
    private String titleOrName;
    private String description;
    private String requester;
    private String solver;
    private List<User<?>> observers = new ArrayList<>();

    public Request(RequestType type, String titleOrName, LocalDateTime date,
                   String description, String requester) {
        this.type = type;
        this.date = date;
        this.titleOrName = titleOrName;
        this.description = description;
        this.requester = requester;
        this.solver = determineSolver(type, titleOrName);

        int count;
        if (solver.equals("ADMIN")) count = 1;
        else count = 0;

        for (User<?> user : IMDB.usersList) {
            if (count == 2) break;

            if (user.username.equals(requester)) {
                addObserver(user);
                count++;
            } else if (user.username.equals(solver)) {
                addObserver(user);
                count++;
            }
        }
    }

    public Request(RequestType type, String titleOrName, LocalDateTime date,
                   String description, String requester, String solver) {
        this.type = type;
        this.date = date;
        this.titleOrName = titleOrName;
        this.description = description;
        this.requester = requester;
        this.solver = solver;

        int count;
        if (solver.equals("ADMIN")) count = 1;
        else count = 0;

        for (User<?> user : IMDB.usersList) {
            if (count == 2) break;

            if (user.username.equals(requester)) {
                addObserver(user);
                count++;
            } else if (user.username.equals(solver)) {
                addObserver(user);
                count++;
            }
        }
    }

    private String determineSolver(RequestType type, String titleOrName) {
        switch (type) {
            case DELETE_ACCOUNT:
            case OTHERS:
                return "ADMIN";
            case ACTOR_ISSUE:
                for (Actor actor : IMDB.actorsList) {
                    if (actor.getName().equals(titleOrName)) {
                        if (actor.getContributor().equals("STAFF")) return "ADMIN";
                        return actor.getContributor();
                    }
                }
            case MOVIE_ISSUE:
                for (Production production : IMDB.productionsList) {
                    if (production.title.equals(titleOrName)) {
                        if (production.contributor.equals("STAFF")) return "ADMIN";
                        return production.contributor;
                    }
                }
            default:
                return "";
        }
    }

    public RequestType getType() {
        return type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getTitleOrName() {
        return titleOrName;
    }

    public String getDescription() {
        return description;
    }

    public String getRequester() {
        return requester;
    }

    public String getSolver() {
        return solver;
    }

    public void setSolver(String solver) {
        int count;
        if (solver.equals("ADMIN")) count = 1;
        else count = 0;

        for (User<?> user : IMDB.usersList) {
            if (count == 2) break;

            if (user.username.equals(this.solver)) {
                removeObserver(user);
                count++;
            } else if (user.username.equals(solver)) {
                addObserver(user);
                count++;
            }
        }

        this.solver = solver;
    }

    public void addObserver(User<?> user) {
        observers.add(user);
    }

    public void removeObserver(User<?> user) {
        observers.remove(user);
    }

    public void notifyObserver(String arg) {
        if (arg.equals("Added")) {
            for (User<?> user : observers) {
                if (user.username.equals(solver)) {
                    user.update(this, "to solve");
                    break;
                }
            }
        } else if (arg.equals("Solved")) {
            for (User<?> user : observers) {
                if (user.username.equals(requester)) {
                    user.update(this, "solved");
                    break;
                }
            }
        }
    }

    public boolean equals(Request request) {
        return type == request.getType() && titleOrName.equals(request.getTitleOrName())
                && description.equals(request.getDescription()) &&
                requester.equals(request.getRequester());
    }

    public String toString() {
        return "Request type: " + type + "\nTitle or name: " +
                titleOrName + "\nDescription: " + description + "\nRequester: "
                + requester + "\nSolver: " + solver + "\nDate: " + date;
    }
}
