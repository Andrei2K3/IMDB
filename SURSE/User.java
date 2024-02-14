import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class User<T extends Comparable<T>> implements Observer, Subject{
    protected Information info;
    protected AccountType accountType;
    protected String username;
    protected int experience;
    protected List<String> notifications;
    protected SortedSet<T> favorites;
    private ExperienceStrategy experienceStrategy;
    protected List<Observer> observers = new ArrayList<>();

    public User(Information info, AccountType accountType, String username, int experience) {
        this.info = info;
        this.accountType = accountType;
        this.username = username;
        this.experience = experience;
        this.notifications = new ArrayList<>();
        this.favorites = new TreeSet<>();
    }

    public User(Information info, AccountType accountType, String username, int experience, List<String> notifications,SortedSet<T> favorites) {
        this.info = info;
        this.accountType = accountType;
        this.username = username;
        this.experience = experience;
        this.notifications = notifications;
        this.favorites = favorites;
    }

    @Override
    public void addObserver(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String notification) {
        for (Observer observer : observers) {
            observer.update(notification);
        }
    }

    public void notifyObserver(String notification, Observer o)
    {
        if(!observers.contains(o))
            observers.add(o);
        o.update(notification);
    }

    @Override
    public void update(String notification) {
        if(notifications == null)
            notifications = new ArrayList<>();
        notifications.add(notification);
    }

    public static class Information {
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private String gender;
        private LocalDateTime birthDate;

        private Information(Builder builder) {
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.gender = builder.gender;
            this.birthDate = builder.birthDate;
        }

        public String getFormattedBirthDate() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if(birthDate == null)
                return "NULL";
            return birthDate.format(formatter);
        }

        // Clasa Builder
        public static class Builder {
            private Credentials credentials;
            private String name;
            private String country;
            private int age;
            private String gender;
            private LocalDateTime birthDate;

            public Builder credentials(Credentials credentials) { this.credentials = credentials; return this; }
            public Builder name(String name) { this.name = name; return this; }
            public Builder country(String country) { this.country = country; return this; }
            public Builder age(int age) { this.age = age; return this; }
            public Builder gender(String gender) { this.gender = gender; return this; }
            public Builder birthDate(LocalDateTime birthDate) { this.birthDate = birthDate; return this; }

            public Information build() {
                return new Information(this);
            }
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public String getName()
        {
            return name;
        }

        public String getCountry()
        {
            return country;
        }

        public int getAge()
        {
            return age;
        }

        public String getGender()
        {
            return gender;
        }

        public String getFormattedBirthDateJSON() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if(birthDate == null)
                return null;
            return birthDate.format(formatter);
        }

        @Override
        public String toString() {
            return "{\n" +
                    "   credentials:" + credentials + ",\n" +
                    "   name='" + name + '\'' + ",\n" +
                    "   country='" + country + '\'' + ",\n" +
                    "   age=" + age + ",\n" +
                    "   gender='" + gender + '\'' + ",\n" +
                    "   birthDate=" + getFormattedBirthDate() + "\n" +
                    '}';
        }
    }

    // Clasa Credentials
    public static class Credentials {
        private String email;
        private String password;

        public Credentials(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "{\n" +
                    "   email='" + email + '\'' + ",\n" +
                    "   password='" + password + '\'' + "\n" +
                    '}';
        }
    }

    // Metode
    public String getUsername()
    {
        return username;
    }

    public List<String> getNotifications()
    {
        return notifications;
    }

    public SortedSet<T> getFavorites()
    {
        return favorites;
    }

    public int getExperience()
    {
        return experience;
    }

    public AccountType getAccountType()
    {
        return accountType;
    }
    public Information getInfo() {
        return info;
    }

    public void addFavorite(T item) {
        favorites.add(item);
    }

    public void removeFavorite(T item) {
        favorites.remove(item);
    }

    // Setarea strategiei + experienta
    public void setExperienceStrategy(ExperienceStrategy experienceStrategy)
    {
        this.experienceStrategy=experienceStrategy;
    }
    public void updateExperience() {
        this.experience += experienceStrategy.calculateExperience();
    }


    public void logoutCLI(){
        IMDB imdb = IMDB.getInstance();
        System.out.println("Choose action:");
        System.out.println("\t1)  To the interface(CLI/GUI)");
        System.out.println("\t2)  To the authentication page");
        System.out.println("\t3)  Close app");
        int number = imdb.alegereNumarValidCLI(3);
        if(number == 1)
            imdb.startPlatform();
        else if(number == 2)
            imdb.CLI();
        else
            imdb.inchidereProgramSiSalvareInFisiere();
    }

    @Override
    public String toString() {
        return "User{\n" +
                "   username='" + username + '\'' + ",\n" +
                "   experience=" + experience + ",\n" +
                "   info:" + info + ",\n" +
                "   userType=" + accountType + ",\n" +
                "   notifications=" + notifications + ",\n" +
                "   favorites=" + favorites + "\n" +
                '}';
    }
}
