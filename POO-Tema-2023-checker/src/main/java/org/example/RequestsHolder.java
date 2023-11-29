import java.util.*;

public class RequestsHolder {
    private static List<Request> requestsForAllAdmins = new ArrayList<>();

    public static void addRequest(Request request) {
        requestsForAllAdmins.add(request);
    }

    public static Request getRequest(int index) {
        return requestsForAllAdmins.get(index);
    }

    public static void removeRequest(Request request) {
        requestsForAllAdmins.remove(request);
    }

    public static void removeRequest(int index) {
        requestsForAllAdmins.remove(index);
    }

    public static List<Request> getAllRequests() {
        return requestsForAllAdmins;
    }

    public static void clearAllRequests() {
        requestsForAllAdmins.clear();
    }
}
