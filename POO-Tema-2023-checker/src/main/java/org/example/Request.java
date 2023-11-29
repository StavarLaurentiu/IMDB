import java.time.LocalDateTime;
import java.util.*;

public class Request {
    private RequestType type;
    private LocalDateTime date;
    private String titleOrName;
    private String description;
    private String requester;
    private String solver;

    public Request(RequestType type, String titleOrName, String description, String requester) {
        this.type = type;
        this.date = LocalDateTime.now();
        this.titleOrName = titleOrName;
        this.description = description;
        this.requester = requester;
        this.solver = determineSolver(type);
    }

    private String determineSolver(RequestType type) {
        switch (type) {
            case DELETE_ACCOUNT:
            case OTHERS:
                return "ADMIN";
            case ACTOR_ISSUE:
            case MOVIE_ISSUE:
                for (User<?> user : IMDB.getInstance().usersList) {
                    if (user.accountType == AccountType.ADMIN) {
                        // Search for the admin who introduced the actor/movie
                    } else if (user.accountType == AccountType.CONTRIBUTOR) {
                        // Search for the contributor who introduced the actor/movie
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

    public boolean equals(Request request) {
        return type == request.getType() && titleOrName.equals(request.getTitleOrName())
                && description.equals(request.getDescription()) &&
                requester.equals(request.getRequester());
    }

    public String toString() {
        return "Request type: " + type + "\nTitle or name: " +
                titleOrName + "\nDescription: " + description + "\nRequester: "
                + requester + "\nDate: " + date;
    }
}
