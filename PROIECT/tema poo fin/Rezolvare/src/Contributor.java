import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class Contributor<T extends Comparable<T>> extends Staff<T> implements RequestsManager {
    private List<Request> requestsToUsers;

    public Contributor(Information info, AccountType accountType, String username, int experience) {
        super(info, accountType, username, experience);
        this.requestsToUsers = new ArrayList<>();
    }

    public Contributor(Information info, AccountType accountType, String username, int experience, List<String> notifications, SortedSet<T> favorites, SortedSet<T> addedProdOrActors) {
        super(info, accountType, username, experience, notifications, favorites, addedProdOrActors);
        this.requestsToUsers = new ArrayList<>();
    }

    public List<Request> getRequestsToUsers()
    {
        return requestsToUsers;
    }
    @Override
    public void createRequest(Request request) {
        requestsToUsers.add(request);
    }

    @Override
    public void removeRequest(Request request) {
        requestsToUsers.remove(request);
    }

    @Override
    public String toString() {
        return "Contributor{\n" +
                super.toString() +
                "   requestsToUsers=" + requestsToUsers + "\n" +
                '}';
    }

}