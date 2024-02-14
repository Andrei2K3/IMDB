import java.util.*;

public class Regular<T extends Comparable<T>> extends User<T> implements RequestsManager {
    private List<Request> requestsToUsers;
    Map<Production, Rating> ratingsMap;

    Set<Production> removedProductions;

    public Regular(Information info, AccountType accountType, String username, int experience) {
        super(info, accountType, username, experience);
        this.requestsToUsers = new ArrayList<>();
        this.ratingsMap = new TreeMap<>();
        this.removedProductions = new TreeSet<>();
    }

    public Regular(Information info, AccountType accountType, String username, int experience, List<String> notifications, SortedSet<T> favorites) {
        super(info, accountType, username, experience, notifications, favorites);
        this.requestsToUsers = new ArrayList<>();
        this.ratingsMap = new TreeMap<>();
        this.removedProductions = new TreeSet<>();
    }

    public void removeProductionSystemWithRatingAndAll(Production production) {
        if (ratingsMap.containsKey(production)) {
            ratingsMap.remove(production);
            removedProductions.remove(production);
        }
    }

    public List<Request> getRequestsToUsers() {
        return requestsToUsers;
    }

    public Map<Production, Rating> getRatingsMap() {
        return ratingsMap;
    }

    public Set<Production> getRemovedProductions() {
        return removedProductions;
    }

    @Override
    public void createRequest(Request r) {
        requestsToUsers.add(r);
    }

    @Override
    public void removeRequest(Request r) {
        requestsToUsers.remove(r);
    }

    public void addReview(Rating rating, Production production) {
        production.addRating(rating);
    }

    @Override
    public String toString() {
        return "Regular{\n" +
                super.toString() +
                "   requestsToUsers=" + requestsToUsers + "\n" +
                '}';
    }

}
