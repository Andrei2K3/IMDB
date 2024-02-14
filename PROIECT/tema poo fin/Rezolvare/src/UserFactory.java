import java.util.List;
import java.util.SortedSet;

public class UserFactory {

    public static User<?> createUser(User.Information infoUser, AccountType userType, String username, int experience, List<String> notifications, SortedSet<ProductionAndActor> favorites, SortedSet<ProductionAndActor> contributorsProdAndActor) {
        switch (userType) {
            case REGULAR:
                return new Regular<>(infoUser, userType, username, experience, notifications, favorites);
            case CONTRIBUTOR:
                return new Contributor<>(infoUser, userType, username, experience, notifications, favorites, contributorsProdAndActor);
            case ADMIN:
                return new Admin<>(infoUser, userType, username, experience, notifications, favorites, contributorsProdAndActor);
            default:
                throw new IllegalArgumentException("Invalid account type");
        }
    }
}
