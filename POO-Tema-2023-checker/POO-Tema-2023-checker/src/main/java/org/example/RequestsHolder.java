import java.util.*;

public class RequestsHolder {
    private static List<Request> requestsForAllAdmins = new ArrayList<>();

    public static void addRequest(Request request) {
        IMDB.requestsList.add(request);

        String solver = request.getSolver();
        if (solver.equals("ADMIN")) {
            requestsForAllAdmins.add(request);
        } else {
            for (User<?> user : IMDB.usersList) {
                if (user instanceof Staff<?> && user.username.equals(solver)) {
                    ((Staff<?>) user).requestsToHandle.add(request);
                    break;
                }
            }
        }

        request.notifyObserver("Added");
    }

    public static void removeRequest(Request request) {
        IMDB.requestsList.remove(request);

        String solver = request.getSolver();
        if (solver.equals("ADMIN")) {
            requestsForAllAdmins.remove(request);
        } else {
            for (User<?> user : IMDB.usersList) {
                if (user instanceof Staff<?> && user.username.equals(solver)) {
                    ((Staff<?>) user).requestsToHandle.remove(request);
                    break;
                }
            }
        }
    }

    public static List<Request> getRequestsList(User<?> user) {
        if (user instanceof Regular) return null;

        if (user instanceof Admin) {
            List<Request> toReturn = new ArrayList<>(requestsForAllAdmins);
            Admin<?> admin = (Admin<?>) user;
            toReturn.addAll(admin.requestsToHandle);

            return toReturn;
        } else {
            return ((Staff<?>) user).requestsToHandle;
        }
    }

    public static List<Request> getRequestsListForAllAdmins() {
        return requestsForAllAdmins;
    }

    public static void solveRequest(Request request, String answer) {
        IMDB.requestsList.remove(request);

        String solver = request.getSolver();
        if (solver.equals("ADMIN")) {
            requestsForAllAdmins.remove(request);
        } else {
            for (User<?> user : IMDB.usersList) {
                if (user instanceof Staff<?> && user.username.equals(solver)) {
                    ((Staff<?>) user).requestsToHandle.remove(request);
                    break;
                }
            }
        }

        request.notifyObserver("Solved");

        // Update experience of the requester if the request was solved and not rejected
        if (answer.equals(("Solved")) &&
                (request.getType().equals(RequestType.valueOf("ACTOR_ISSUE")) ||
                        request.getType().equals(RequestType.valueOf("MOVIE_ISSUE")))) {
            String requester = request.getRequester();
            for (User<?> user : IMDB.usersList) {
                if (user.username.equals(requester)) {
                    user.updateExperience(new SolvedRequest());
                    break;
                }
            }
        }
    }

    public static void updateSolver(Request request, String solver) {
        request.setSolver(solver);
    }
}
