import java.util.*;

public abstract class Staff<T extends Comparable<T>> extends User<T> implements StaffInterface {
    protected List<Request> requestsFromUsers;
    protected SortedSet<T> addedProdOrActors;

    public Staff(Information info, AccountType accountType, String username, int experience) {
        super(info, accountType, username, experience);
        this.requestsFromUsers = new ArrayList<>();
        this.addedProdOrActors = new TreeSet<>();
    }

    public Staff(Information info, AccountType accountType, String username, int experience, List<String> notifications, SortedSet<T> favorites, SortedSet<T> addedProdOrActors) {
        super(info, accountType, username, experience, notifications, favorites);
        this.addedProdOrActors = addedProdOrActors;
        this.requestsFromUsers = new ArrayList<>();
    }

    public List<Request> getListOfRequestsFromUsers()
    {
        return requestsFromUsers;
    }

    public Request getRequestFromUser(int index)
    {
        return requestsFromUsers.get(index);
    }

    public void removeRequestFromUser(Request r)
    {
        requestsFromUsers.remove(r);
    }

    public void printListOfRequest()
    {
        for(Request request : requestsFromUsers)
            System.out.println(request);
    }

    public void addSetToSet(SortedSet<T> set2) {
        addedProdOrActors.addAll(set2);
    }

    public SortedSet<T> getAddedProdOrActors() {
        return addedProdOrActors;
    }

    public int printProductionsAdded(int index) {
        for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) addedProdOrActors)
            if (paa instanceof Movie || paa instanceof Series) {
                System.out.println(index + ")  " + ((Production) paa).getTitle());
                index++;
            }
        return index;
    }

    public Production getIndexProductionContributed(int index) {
        int i = 1;
        for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) addedProdOrActors)
            if (paa instanceof Movie || paa instanceof Series) {
                if (i == index)
                    return (Production) paa;
                i++;
            }
        return null;
    }

    public int printActorsAdded(int index) {
        for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) addedProdOrActors)
            if (paa instanceof Actor) {
                System.out.println(index + ")  " + ((Actor) paa).getName());
                index++;
            }
        return index;
    }

    public Actor getIndexActorContributed(int index) {
        int i = 1;
        for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) addedProdOrActors)
            if (paa instanceof Actor) {
                if (i == index)
                    return (Actor) paa;
                i++;
            }
        return null;
    }

    @Override
    public void addProductionSystem(Production p) {
        addedProdOrActors.add((T) p);
    }

    @Override
    public void addActorSystem(Actor a) {
        addedProdOrActors.add((T) a);
    }

    public boolean isActorProdContributedWithName(String name) {
        for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) addedProdOrActors)
            if (paa instanceof Actor) {
                Actor a = (Actor) paa;
                if (name.equals(a.getName()))
                    return true;
            } else {
                Production p = (Production) paa;
                if (name.equals(p.getTitle()))
                    return true;
            }
        return false;
    }

    public boolean isActorContributedWithName(String name) {
        for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) addedProdOrActors)
            if (paa instanceof Actor) {
                Actor a = (Actor) paa;
                if (name.equals(a.getName()))
                    return true;
            }
        return false;
    }

    public boolean isProdContributedWithName(String name) {
        for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) addedProdOrActors)
            if (paa instanceof Production) {
                Production p = (Production) paa;
                if (name.equals(p.getTitle()))
                    return true;
            }
        return false;
    }

    public void removeProductionSystem(Production p) {
         addedProdOrActors.remove((T) p);
    }


    public void removeActorSystem(Actor a) {
        addedProdOrActors.remove((T) a);
    }

    @Override
    public void updateProduction(Production p) {

    }

    @Override
    public void updateActor(Actor a) {
    }

    @Override
    public void resolveUserRequests() {
    }

    public void receivedRequest(Request r) {
        requestsFromUsers.add(r);
    }

    public void removeReceivedRequest(Request r) {
        requestsFromUsers.remove(r);
    }

    @Override
    public String toString() {
        return "Staff{\n" +
                super.toString() +
                "   requestsFromUsers=" + requestsFromUsers + ",\n" +
                "   addedProdOrActors=" + addedProdOrActors + "\n" +
                '}';
    }

}
