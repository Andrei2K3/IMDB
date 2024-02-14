import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class Admin<T extends Comparable<T>> extends Staff<T> {
    private List<User<T>> users; // Lista de utilizatori din sistem (inclusiv admini și contribuitori)

    public Admin(Information info, AccountType accountType, String username, int experience) {
        super(info, accountType, username, experience);
        this.users = new ArrayList<>();
    }

    public Admin(Information info, AccountType accountType, String username, int experience, List<String> notifications, SortedSet<T> favorites, SortedSet<T> addedProdOrActors) {
        super(info, accountType, username, experience, notifications, favorites, addedProdOrActors);
        this.users = new ArrayList<>();
    }

    public void addUser(User<T> user) {
        users.add(user);
    }

    public void removeUser(User<T> user) {
        users.remove(user);
    }

    public List<User<T>> getUsers() {
        return users;
    }

    @Override
    public void removeProductionSystem(Production p) {
        super.removeProductionSystem(p);

    }

    @Override
    public void removeActorSystem(Actor a) {
        super.removeActorSystem(a);

    }


    public static class RequestsHolder {
        private static List<Request> requestsHolder = new ArrayList<>();

        public static void addRequest(Request request) {
            requestsHolder.add(request);
        }

        public static void removeRequest(Request request) {
            requestsHolder.remove(request);
        }

        public static List<Request> getRequests() {
            return new ArrayList<>(requestsHolder);
        }
    }

    public void printListOfRequestHolder() {
        for (Request r : RequestsHolder.requestsHolder)
            System.out.println(r);
    }

    public List<Request> getListOfRequestsHolderFromUsers()
    {
        return RequestsHolder.requestsHolder;
    }

    public void receivedRequestHolder(Request request) {
        RequestsHolder.addRequest(request);
    }

    public void removeReceiveRequestHolder(Request request) {
        RequestsHolder.removeRequest(request);
    }

    @Override
    public String toString() {
        return "Admin{\n" +
                super.toString() +
                "   users=" + users + "\n" +
                '}';
    }

}
