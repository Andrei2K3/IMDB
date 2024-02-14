import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class IMDB {
    private static IMDB instance = null;

    private List<Regular<?>> regulars;
    private List<Contributor<?>> contributors;
    private List<Admin<?>> admins;
    private List<Actor> actors;
    private List<Request> requests;
    private List<Movie> movies;
    private List<Series> series;

    private IMDB() {
        regulars = new ArrayList<>();
        contributors = new ArrayList<>();
        admins = new ArrayList<>();
        //actors = new ArrayList<>();
        //requests = new ArrayList<>();
        movies = new ArrayList<>();
        series = new ArrayList<>();
    }

    public static IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();
        }
        return instance;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public List<Series> getSeries() {
        return series;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public List<Admin<?>> getAdmins() {
        return admins;
    }

    public List<Request> getRequests(){
        return requests;
    }

    public List<Contributor<?>> getContributors() {
        return contributors;
    }

    public List<Regular<?>> getRegulars() {
        return regulars;
    }

    void actorsPrint() {
        for (Actor actor : actors)
            System.out.println(actor);
    }

    void requestsPrint() {
        for (Request request : requests)
            System.out.println(request);
    }

    void moviesPrint() {
        for (Movie movie : movies)
            System.out.println(movie);
    }

    void seriesPrint() {
        for (Series serie : series)
            System.out.println(serie);
    }

    void accountsPrint() {
        System.out.println("Regulars:" + regulars.size());
        /**
         try (PrintWriter out = new PrintWriter("output2.txt")) {
         //out.println(textToWrite);
         for (Regular r : regulars)
         //System.out.println(r.getUsername());
         //System.out.println(r);
         out.println(r);
         System.out.println("Contributors:" + contributors.size());
         for (Contributor c : contributors)
         //System.out.println(c.getUsername());
         //System.out.println(c);
         out.println(c);
         System.out.println("Admins:" + admins.size());
         for (Admin a : admins)
         //System.out.println(a.getUsername());
         //System.out.println(a);
         out.println(a);
         }catch (FileNotFoundException e) {
         e.printStackTrace();
         }
         */
        for (Regular r : regulars)
            System.out.println(r.getUsername());
        //System.out.println(r);
        System.out.println("Contributors:" + contributors.size());
        for (Contributor c : contributors)
            System.out.println(c.getUsername());
        //System.out.println(c);
        System.out.println("Admins:" + admins.size());
        for (Admin a : admins)
            System.out.println(a.getUsername());
        //System.out.println(a);
    }

    void loadReviewRegular() {
        for (Movie m : movies)
            for (Rating rating : m.ratingList())
                for (Regular<?> regular : regulars)
                    if (rating.getUsername().equals(regular.getUsername())) {
                        regular.getRatingsMap().put(m, rating);
                        break;
                    }
        for (Series s : series)
            for (Rating rating : s.ratingList())
                for (Regular<?> regular : regulars)
                    if (rating.getUsername().equals(regular.getUsername())) {
                        regular.getRatingsMap().put(s, rating);
                        break;
                    }
    }

    public void loadJsonToData() {
        JsonParser jsonParser = new JsonParser();

        // Incarcam actorii din actors.json
        actors = jsonParser.parseActorsFromFile("POO-TEMA-2023-INPUT/actors.json");
        //actorsPrint();

        // Incarcam requesturile
        requests = jsonParser.parseRequestsFromFile("POO-TEMA-2023-INPUT/requests.json");
        //requestsPrint();

        // Incarcam productiile in movies si series, iar actorii noi adaugati ii punem sub
        // responsabilitatea lui admin0, care reprezinta toata echipa de admini
        User.Information.Builder builder = new User.Information.Builder();
        builder.credentials(new User.Credentials("ADMIN", "ADMIN"))
                .name("ADMIN")
                .country("ADMIN");
        User.Information infoUser = builder.build();
        Admin admin0 = new Admin(infoUser, AccountType.ADMIN, "ADMIN", 9999);
        admins.add(admin0);
        jsonParser.parseProductionsFromFile("POO-TEMA-2023-INPUT/production.json", movies, series, actors, admin0);
        //moviesPrint();
        //seriesPrint();
        //System.out.println(actors);
        //System.out.println(admins.get(0).getAddedProdOrActors().size());

        //Incarcare conturilor
        jsonParser.parseAccountsFromFile("POO-TEMA-2023-INPUT/accounts.json", regulars, contributors, admins, actors, movies, series);
        //accountsPrint();

        // Incarcare ratings in conturile Regular
        loadReviewRegular();
    }

    // Metoda run
    public void run() {
        /// Incarcarea datelor din fisierele JSON
        JsonParser jsonParser = new JsonParser();

        // Incarcam actorii din actors.json
        actors = jsonParser.parseActorsFromFile("POO-TEMA-2023-INPUT/actors.json");
        //actorsPrint();

        // Incarcam requesturile
        requests = jsonParser.parseRequestsFromFile("POO-TEMA-2023-INPUT/requests.json");
        //requestsPrint();

        // Incarcam productiile in movies si series, iar actorii noi adaugati ii punem sub
        // responsabilitatea lui admin0, care reprezinta toata echipa de admini
        User.Information.Builder builder = new User.Information.Builder();
        builder.credentials(new User.Credentials("ADMIN", "ADMIN"))
                .name("ADMIN")
                .country("ADMIN");
        User.Information infoUser = builder.build();
        Admin admin0 = new Admin(infoUser, AccountType.ADMIN, "ADMIN", 9999);
        admins.add(admin0);
        jsonParser.parseProductionsFromFile("POO-TEMA-2023-INPUT/production.json", movies, series, actors, admin0);
        //moviesPrint();
        //seriesPrint();
        //System.out.println(actors);
        //System.out.println(admins.get(0).getAddedProdOrActors().size());

        //Incarcare conturilor
        jsonParser.parseAccountsFromFile("POO-TEMA-2023-INPUT/accounts.json", regulars, contributors, admins, actors, movies, series);
        //accountsPrint();

        // Incarcare ratings in conturile Regular
        loadReviewRegular();

        // Autentificarea utilizatorului
        startPlatform();

        System.out.println("merele");
    }

    public void inchidereProgramSiSalvareInFisiere() {
        // Actualizam informatiile in fisiere
        JsonWriter jsonWriter = new JsonWriter();
        try {
            jsonWriter.writeAccountsToFile(regulars, contributors, admins, "JsonScris/accounts.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            jsonWriter.writeActorsToFile(actors, "JsonScris/actors.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            jsonWriter.writeProductionsToFile(movies, series, "JsonScris/production.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            jsonWriter.writeRequestsToFile(requests, "JsonScris/requests.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        inchidereProgram();
    }

    public void inchidereProgram() {
        System.exit(0);
    }

    public void mesajIntrareCLI(String s1, String s2, String s3) {
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
    }

    public int alegereNumarValidCLI(int valMax) {
        boolean isValid = false;
        Scanner scanner = new Scanner(System.in);
        int number = 0;
        while (!isValid) {
            try {
                System.out.print("Choose action: ");
                number = scanner.nextInt();

                boolean ok = false;
                for (int i = 1; i <= valMax; i++)
                    if (number == i) {
                        ok = true;
                        break;
                    }
                if (ok == false)
                    throw new InvalidCommandException("Invalid number. It must be between 1 or " + valMax + ".");

                isValid = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid number. It must be between 1 or " + valMax + ".");
                scanner.nextLine();
            } catch (InvalidCommandException e) {
                System.out.println("An exception occurred: " + e.getMessage());
            }
        }
        return number;
    }

    public void startPlatform() {
        mesajIntrareCLI("Welcome back! Please choose CLI/GUI mode:", "\t1) CLI", "\t2) GUI");
        int number = alegereNumarValidCLI(2);
        if (number == 1)
            CLI();
        else
            GUI();
    }

    public void createAccountCLI() {
        String email = readText("email: ", "email");
        String name = readText("name: ", "name");
        String country = readText("contry: ", "country");
        int age = alegereDataLansariiSauNumSeasonsCLI("age: ");
        String gender = readText("gender: ", "gender");
        LocalDateTime birthDate = getBirthDate();
        AccountType accountType = readAccountType();
        String username = generateUsernameValid(name);
        int experience = alegereDataLansariiSauNumSeasonsCLI("experience: ");
        String password = generatePassword(14);
        // Creeare user
        User.Information.Builder builder = new User.Information.Builder();
        builder.credentials(new User.Credentials(email, password))
                .name(name)
                .country(country)
                .age(age)
                .gender(gender)
                .birthDate(birthDate);
        User.Information infoUser = builder.build();
        User<ProductionAndActor> userNou = (User<ProductionAndActor>) UserFactory.createUser(infoUser, accountType, username, experience, new ArrayList<>(), new TreeSet<>(), new TreeSet<>());
        //
        boolean isR = false, isC = false, isA = false;
        if (userNou instanceof Regular<ProductionAndActor>) {
            regulars.add((Regular<ProductionAndActor>) userNou);
            isR = true;
            System.out.println("User added:\n" + ((Regular<ProductionAndActor>) userNou));
        } else if (userNou instanceof Contributor<ProductionAndActor>) {
            contributors.add((Contributor<ProductionAndActor>) userNou);
            isC = true;
            System.out.println("User added:\n" + ((Contributor<ProductionAndActor>) userNou));
        } else {
            admins.add((Admin<ProductionAndActor>) userNou);
            isA = true;
            System.out.println("User added:\n" + ((Admin<ProductionAndActor>) userNou));
        }
        //
        if (isR) {
            //System.out.println(AccountR);
            printDetailsAccountAndMenuRegularCLI((Regular<?>) userNou);
        } else if (isC) {
            //System.out.println(AccountC);
            printDetailsAccountAndMenuContributorCLI((Contributor<?>) userNou);
        } else if (isA) {
            //System.out.println(AccountA);
            printDetailsAccountAndMenuAdminCLI((Admin<?>) userNou);
        }
    }

    public Boolean verifyAccountCLI(User<?> u, String email, String password) {
        User.Information info = u.getInfo();
        if (info == null)
            return false;
        User.Credentials cred = info.getCredentials();
        if (cred == null)
            return false;
        if (email.equals(cred.getEmail()) && password.equals(cred.getPassword()))
            return true;
        return false;
    }

    public void toTheMenu(User<?> u, AccountType accType) {
        if (accType == AccountType.REGULAR)
            printDetailsAccountAndMenuRegularCLI((Regular<?>) u);
        else if (accType == AccountType.CONTRIBUTOR)
            printDetailsAccountAndMenuContributorCLI((Contributor<?>) u);
        else
            printDetailsAccountAndMenuAdminCLI((Admin<?>) u);
    }

    public void toTheCriteriaProduction(User<?> u, AccountType accType, List<Movie> moviesList, List<Series> seriesList, int number) {
        if (number == 1)
            standardPrint(u, accType, moviesList, seriesList);
        else if (number == 2)
            numberRatingsPrint(u, accType, moviesList, seriesList);
        else if (number == 3)
            numberGenresPrint(u, accType, moviesList, seriesList);
        else if (number == 4)
            byTitlePrint(u, accType, moviesList, seriesList);
        else if (number == 5)
            byAverageRatingPrint(u, accType, moviesList, seriesList);
        else if (number == 6)
            numberActorsPrint(u, accType, moviesList, seriesList);
        else
            numberDirectorsPrint(u, accType, moviesList, seriesList);

    }

    public void printProductionWithCriteria(User<?> u, AccountType accType, List<Movie> moviesList, List<Series> seriesList, int number) {
        System.out.println("Movies:");
        for (int i = 0; i < moviesList.size(); i++)
            System.out.println("\t" + (i + 1) + ")  " + moviesList.get(i).getTitle() + " - Release Year " + moviesList.get(i).getReleaseYear());
        int j = moviesList.size();
        System.out.println("Series:");
        for (int i = 0; i < seriesList.size(); i++)
            System.out.println("\t" + (j + i + 1) + ")  " + seriesList.get(i).getTitle() + " - Release Year " + seriesList.get(i).getReleaseYear());
        System.out.println("\n\t" + (j + seriesList.size() + 1) + ")  To the previous page (there is the logout option)");
        int numberBack = alegereNumarValidCLI(j + seriesList.size() + 1);
        if (numberBack <= moviesList.size()) // number >= 1
        {
            System.out.println(moviesList.get(numberBack - 1));
            System.out.println("\n\t1)  To the previous page");
            System.out.println("\t2)  To the menu (there is the logout option)");
            numberBack = alegereNumarValidCLI(2);
            if (numberBack == 1)
                toTheCriteriaProduction(u, accType, moviesList, seriesList, number);
            else
                toTheMenu(u, accType);
        } else if (numberBack <= j + seriesList.size()) {
            System.out.println(seriesList.get(numberBack - moviesList.size() - 1));
            System.out.println("\n\t1)  To the previous page");
            System.out.println("\t2)  To the menu (there is the logout option)");
            numberBack = alegereNumarValidCLI(2);
            if (numberBack == 1)
                toTheCriteriaProduction(u, accType, moviesList, seriesList, number);
            else
                toTheMenu(u, accType);
        } else
            toTheMenu(u, accType);
    }

    public void standardPrint(User<?> u, AccountType accType, List<Movie> moviesList, List<Series> seriesList) {
        printProductionWithCriteria(u, accType, moviesList, seriesList, 1);
    }

    public void numberRatingsPrint(User<?> u, AccountType accType, List<Movie> moviesList, List<Series> seriesList) {
        Comparator<Production> sortByRatingCount = new Comparator<Production>() {
            @Override
            public int compare(Production p1, Production p2) {
                return Integer.compare(p1.ratingList().size(), p2.ratingList().size());
            }
        };

        Collections.sort(moviesList, sortByRatingCount);
        Collections.sort(seriesList, sortByRatingCount);
        printProductionWithCriteria(u, accType, moviesList, seriesList, 2);
    }

    public void numberGenresPrint(User<?> u, AccountType accType, List<Movie> moviesList, List<Series> seriesList) {
        Comparator<Production> sortByGenre = new Comparator<Production>() {
            @Override
            public int compare(Production p1, Production p2) {
                List<Genre> genresP1 = p1.getGenres(); // Le sorteaza si n lista originala
                List<Genre> genresP2 = p2.getGenres();
                Collections.sort(genresP1, (genre1, genre2) -> genre1.toString().compareTo(genre2.toString()));
                Collections.sort(genresP2, (genre1, genre2) -> genre1.toString().compareTo(genre2.toString()));

                int size = Math.min(genresP1.size(), genresP2.size());
                for (int i = 0; i < size; i++) {
                    int comparison = genresP1.get(i).toString().compareTo(genresP2.get(i).toString());
                    if (comparison != 0) {
                        return comparison;
                    }
                }
                return Integer.compare(genresP1.size(), genresP2.size());
            }
        };

        Collections.sort(moviesList, sortByGenre);
        Collections.sort(seriesList, sortByGenre);
        printProductionWithCriteria(u, accType, moviesList, seriesList, 3);

    }

    public void byTitlePrint(User<?> u, AccountType accType, List<Movie> moviesList, List<Series> seriesList) {
        Comparator<Production> sortByTitle = new Comparator<Production>() {
            public int compare(Production p1, Production p2) {
                return p1.getTitle().compareTo(p2.getTitle());
            }
        };

        Collections.sort(moviesList, sortByTitle);
        Collections.sort(seriesList, sortByTitle);
        printProductionWithCriteria(u, accType, moviesList, seriesList, 4);
    }

    public void byAverageRatingPrint(User<?> u, AccountType accType, List<Movie> moviesList, List<Series> seriesList) {
        Comparator<Production> sortByAverageRating = new Comparator<Production>() {
            public int compare(Production p1, Production p2) {
                return Double.compare(p1.getAverageRating(), p2.getAverageRating());
            }
        };

        Collections.sort(moviesList, sortByAverageRating);
        Collections.sort(seriesList, sortByAverageRating);
        printProductionWithCriteria(u, accType, moviesList, seriesList, 5);
    }

    public void numberActorsPrint(User<?> u, AccountType accType, List<Movie> moviesList, List<Series> seriesList) {
        Comparator<Production> sortByActorCount = new Comparator<Production>() {
            @Override
            public int compare(Production p1, Production p2) {
                return Integer.compare(p1.getActors().size(), p2.getActors().size());
            }
        };

        Collections.sort(moviesList, sortByActorCount);
        Collections.sort(seriesList, sortByActorCount);
        printProductionWithCriteria(u, accType, moviesList, seriesList, 6);
    }

    public void numberDirectorsPrint(User<?> u, AccountType accType, List<Movie> moviesList, List<Series> seriesList) {
        Comparator<Production> sortByDirectorCount = new Comparator<Production>() {
            @Override
            public int compare(Production p1, Production p2) {
                return Integer.compare(p1.getDirectors().size(), p2.getDirectors().size());
            }
        };

        Collections.sort(moviesList, sortByDirectorCount);
        Collections.sort(seriesList, sortByDirectorCount);
        printProductionWithCriteria(u, accType, moviesList, seriesList, 7);
    }

    public void viewProductionsDetailsCLI(User<?> u, AccountType accType) {
        System.out.println("1)  Standard print");
        System.out.println("2)  Sorted by number of ratings");
        System.out.println("3)  Sorted by genres");
        System.out.println("4)  Sorted by title");
        System.out.println("5)  Sorted by averageRating");
        System.out.println("6)  Sorted by number of actors");
        System.out.println("7)  Sorted by number of directors");
        System.out.println("8)  Back to the menu");
        int numberBack = alegereNumarValidCLI(8);
        if (numberBack == 1)
            standardPrint(u, accType, movies, series);
        else if (numberBack == 2)
            numberRatingsPrint(u, accType, new ArrayList<>(movies), new ArrayList<>(series));
        else if (numberBack == 3)
            numberGenresPrint(u, accType, new ArrayList<>(movies), new ArrayList<>(series));
        else if (numberBack == 4)
            byTitlePrint(u, accType, new ArrayList<>(movies), new ArrayList<>(series));
        else if (numberBack == 5)
            byAverageRatingPrint(u, accType, new ArrayList<>(movies), new ArrayList<>(series));
        else if (numberBack == 6)
            numberActorsPrint(u, accType, new ArrayList<>(movies), new ArrayList<>(series));
        else if (numberBack == 7)
            numberDirectorsPrint(u, accType, new ArrayList<>(movies), new ArrayList<>(series));
        else
            toTheMenu(u, accType);
    }

    public void viewActorsWithCriteria(User<?> u, AccountType accType, List<Actor> actorsList) {
        System.out.println("Actors:");
        for (int i = 0; i < actorsList.size(); i++)
            System.out.println("\t" + (i + 1) + ")  " + actorsList.get(i).getName());
        System.out.println("\n\t" + (actorsList.size() + 1) + ")  To the previous page");
        int number = alegereNumarValidCLI(actorsList.size() + 1);
        if (number <= actorsList.size()) {
            System.out.println(actorsList.get(number - 1));
            System.out.println("\n\t1)  To the previous page");
            System.out.println("\t2)  To the menu (there is the logout option)");
            int numberBack = alegereNumarValidCLI(2);
            if (numberBack == 1)
                viewActorsWithCriteria(u, accType, actorsList);
            else
                toTheMenu(u, accType);
        } else
            viewActorsDetailsCLI(u, accType);
    }

    public void viewActorsDetailsCLI(User<?> u, AccountType accType) {
        System.out.println("\t1)  Normal order");
        System.out.println("\t2)  Sorted order");
        System.out.println("\t3)  Back to the menu");
        int numberBack = alegereNumarValidCLI(3);
        if (numberBack == 1)
            viewActorsWithCriteria(u, accType, new ArrayList<>(actors));
        else if (numberBack == 2) {
            List<Actor> actorList = new ArrayList<>(actors);
            Comparator<Actor> sortByName = new Comparator<Actor>() {
                @Override
                public int compare(Actor a1, Actor a2) {
                    return a1.getName().compareTo(a2.getName());
                }
            };
            Collections.sort(actorList, sortByName);
            viewActorsWithCriteria(u, accType, actorList);
        } else
            toTheMenu(u, accType);
    }

    public void viewNotifications(User<?> u, AccountType accType) {
        if (!u.getNotifications().isEmpty())
            for (String s : u.getNotifications())
                System.out.println(s);
        else System.out.println("No notification");
        System.out.println("\n\t1)  To the previous page");
        int numberBack = alegereNumarValidCLI(1);
        toTheMenu(u, accType);
    }

    public ProductionAndActor isProductionOrActorInSystemWithName(String name) {
        for (Movie m : movies)
            if (name.equals(m.getTitle()))
                return m;
        for (Series s : series)
            if (name.equals(s.getTitle()))
                return s;
        for (Actor a : actors)
            if (name.equals(a.getName()))
                return a;
        return null;
    }

    public void searchActorOrProductionInSystemCLI(User<?> u, AccountType accType) {
        Scanner scanner = new Scanner(System.in);
        String name = null;
        do {
            System.out.print("Please enter the name of the actor/movie/series you are looking for: ");
            name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Invalid name. Please try again. You must enter at least one character other than a space.");
            }
        } while (name.isEmpty());
        //
        Movie mm = null;
        Series ss = null;
        Actor aa = null;
        for (Movie m : movies)
            if (name.equals(m.getTitle())) {
                mm = m;
                break;
            }

        if (mm == null) {
            for (Series s : series)
                if (name.equals(s.getTitle())) {
                    ss = s;
                    break;
                }

            if (ss == null)
                for (Actor a : actors)
                    if (name.equals(a.getName())) {
                        aa = a;
                        break;
                    }
        }

        if (mm != null) {
            System.out.println("Movie found in system");
            System.out.println("\n\t1)  Back to the menu");
            System.out.println("\t2)  More info");
            int numberBack = alegereNumarValidCLI(2);
            if (numberBack == 2) {
                System.out.println(mm);
                System.out.println("\t1)  Back to the menu");
                numberBack = alegereNumarValidCLI(1);
                toTheMenu(u, accType);
            } else toTheMenu(u, accType);

        } else if (ss != null) {
            System.out.println("Series found in system");
            System.out.println("\n\t1)  Back to the menu");
            System.out.println("\t2)  More info");
            int numberBack = alegereNumarValidCLI(2);
            if (numberBack == 2) {
                System.out.println(ss);
                System.out.println("\t1)  Back to the menu");
                numberBack = alegereNumarValidCLI(1);
                toTheMenu(u, accType);
            } else
                toTheMenu(u, accType);
        } else if (aa != null) {
            System.out.println("Actor found in system");
            System.out.println("\n\t1)  Back to the menu");
            System.out.println("\t2)  More info");
            int numberBack = alegereNumarValidCLI(2);
            if (numberBack == 2) {
                System.out.println(aa);
                System.out.println("\t1)  Back to the menu");
                numberBack = alegereNumarValidCLI(1);
                toTheMenu(u, accType);
            } else
                toTheMenu(u, accType);
        } else {
            System.out.println("Name not found in system");
            System.out.println("\n\t1)  Back to the menu");
            System.out.println("\t2)  Try again");
            int numberBack = alegereNumarValidCLI(2);
            if (numberBack == 2)
                searchActorOrProductionInSystemCLI(u, accType);
            else
                toTheMenu(u, accType);
        }
        // Asta nu se ruleaza
        //System.out.println("\n\t1)  Back to the menu");
        //int numberBack = alegereNumarValidCLI(1);
        //toTheMenu(u, accType);
    }

    public boolean isActorProductionInFavorites(User<?> u, String name) {
        for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) u.getFavorites())
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

    public String readText(String msg1, String msg2) {
        Scanner scanner = new Scanner(System.in);
        String name = null;
        do {
            System.out.print(msg1);
            name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Invalid " + msg2 + ". Please try again. You must enter at least one character other than a space.");
            }
        } while (name.isEmpty());
        return name;
    }

    public ProductionAndActor actorProductionSystem(String name) {
        for (Movie m : movies)
            if (name.equals(m.getTitle()))
                return m;
        for (Series s : series)
            if (name.equals(s.getTitle()))
                return s;
        for (Actor a : actors)
            if (name.equals(a.getName()))
                return a;
        return null;
    }

    public ProductionAndActor actorProductionInFavorites(User<?> u, String name) {
        for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) u.getFavorites())
            if (paa instanceof Actor) {
                Actor a = (Actor) paa;
                if (name.equals(a.getName()))
                    return paa;
            } else {
                Production p = (Production) paa;
                if (name.equals(p.getTitle()))
                    return paa;
            }
        return null;
    }

    public void addActorOrProductionFavorites(User<?> u, AccountType accType) {
        String name = readText("Please enter the name of the actor/movie/series: ", "name");

        if (isActorProductionInFavorites(u, name)) {
            System.out.println("The actor/movie/series is already added to the system");
            System.out.println("\n\t1)  Back to the menu");
            System.out.println("\t2)  Try another name");
            int numberBack = alegereNumarValidCLI(2);
            if (numberBack == 1)
                toTheMenu(u, accType);
            else
                addActorOrProductionFavorites(u, accType);
        } else {
            // Numele nu este la favorite, il adaug daca exista in sistem
            ProductionAndActor paa = actorProductionSystem(name);
            if (paa == null) {
                System.out.println("Name not found in system");
                System.out.println("\n\t1)  Back to the menu");
                System.out.println("\t2)  Try another name");
                int numberBack = alegereNumarValidCLI(2);
                if (numberBack == 1)
                    toTheMenu(u, accType);
                else
                    addActorOrProductionFavorites(u, accType);
            } else {
                System.out.println("Actor/Movie/Series was added in favorites");
                SortedSet<ProductionAndActor> fav = (SortedSet<ProductionAndActor>) u.getFavorites();
                fav.add(paa);
                System.out.println("\n\t1)  Back to the menu");
                int numberBack = alegereNumarValidCLI(1);
                if (numberBack == 1)
                    toTheMenu(u, accType);
            }
        }
    }

    public void deleteActorOrProductionFavorites(User<?> u, AccountType accType) {
        String name = readText("Please enter the name of the actor/movie/series: ", "name");

        ProductionAndActor paa = actorProductionInFavorites(u, name);
        if (paa == null) {
            System.out.println("Name not found in favorites");
            System.out.println("\n\t1)  Back to the menu");
            System.out.println("\t2)  Try another name");
            int numberBack = alegereNumarValidCLI(2);
            if (numberBack == 1)
                toTheMenu(u, accType);
            else
                deleteActorOrProductionFavorites(u, accType);
        } else {
            System.out.println("Actor/Movie/Series was removed from favorites");
            SortedSet<ProductionAndActor> fav = (SortedSet<ProductionAndActor>) u.getFavorites();
            fav.remove(paa);
            System.out.println("\n\t1)  Back to the menu");
            int numberBack = alegereNumarValidCLI(1);
            if (numberBack == 1)
                toTheMenu(u, accType);
        }
    }

    public void addOrDeleteActorOrProductionFavorites(User<?> u, AccountType accType) {
        System.out.println("\n\t1)  Lists actors and productions marked as favorites");
        System.out.println("\t2)  Add actor/movie/series to favorites");
        System.out.println("\t3)  Delete actor/movie/series from favorites");
        System.out.println("\t4)  Back to the menu");
        int number = alegereNumarValidCLI(4);
        if (number == 1) {
            if (u.getFavorites().isEmpty())
                System.out.println("List is empty");
            else
                for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) u.getFavorites())
                    System.out.println(paa);
            System.out.println("\n\t1)  Back to the menu");
            int numberBack = alegereNumarValidCLI(1);
            toTheMenu(u, accType);
        } else if (number == 2)
            addActorOrProductionFavorites(u, accType);
        else if (number == 3)
            deleteActorOrProductionFavorites(u, accType);
        else
            toTheMenu(u, accType);
    }

    public void createRequest(User<?> u, AccountType accType) {
        System.out.println("Please choose a type of request:");
        System.out.println("\n\t1)  DELETE_ACCOUNT");
        System.out.println("\t2)  ACTOR_ISSUE");
        System.out.println("\t3)  MOVIE_ISSUE");
        System.out.println("\t4)  OTHERS");
        System.out.println("\t5)  Back to the menu");
        int numberBack = alegereNumarValidCLI(5);
        if (numberBack == 1) {
            String description = readText("Please type the description: ", "description");
            Request r = new Request(RequestType.DELETE_ACCOUNT, LocalDateTime.now(), null, description, u.getUsername(), "ADMIN");
            requests.add(r);
            if (accType == AccountType.REGULAR)
                u = (Regular<?>) u;
            else
                u = (Contributor<?>) u;
            ((RequestsManager) u).createRequest(r);
            admins.get(0).receivedRequestHolder(r);//Se trimite la intreaga echipa de admini
            u.addObserver(admins.get(0));//Adaugam intreaga echipa de admini ca observator
            String notification = u.getUsername() + " made request " + RequestType.DELETE_ACCOUNT + " with description: " + description;
            u.notifyObserver(notification, admins.get(0));
            System.out.println("Request was sent");
            System.out.println("\t  1) Back to the menu");
            numberBack = alegereNumarValidCLI(1);
            toTheMenu(u, accType);
        } else if (numberBack == 2) {
            String name = readText("Please type the actor name: ", "name");
            String description = readText("Please type the description: ", "description");
            Contributor<?> cc = null;
            Admin<?> aa = null;
            for (Contributor<?> c : contributors)
                if (c.isActorContributedWithName(name)) {
                    cc = c;
                    break;
                }
            if (cc == null)
                for (Admin<?> a : admins)
                    if (a.isActorContributedWithName(name)) {
                        aa = a;
                        break;
                    }
            if (cc == null && aa == null) {
                System.out.println("Invalid actor.");
                System.out.println("\t1)  Try again");
                System.out.println("\t2)  Back to the menu");
                numberBack = alegereNumarValidCLI(2);
                if (numberBack == 1)
                    createRequest(u, accType);
                else
                    toTheMenu(u, accType);
            } else {
                if (u == cc) {
                    System.out.println("Request impossible because you added the actor");
                    System.out.println("\t  1) Back to the menu");
                    numberBack = alegereNumarValidCLI(1);
                    toTheMenu(u, accType);
                } else {
                    Request r;
                    if (cc == null)
                        r = new Request(RequestType.ACTOR_ISSUE, LocalDateTime.now(), name, description, u.getUsername(), aa.getUsername());
                    else
                        r = new Request(RequestType.ACTOR_ISSUE, LocalDateTime.now(), name, description, u.getUsername(), cc.getUsername());
                    requests.add(r);
                    if (accType == AccountType.REGULAR)
                        u = (Regular<?>) u;
                    else
                        u = (Contributor<?>) u;
                    ((RequestsManager) u).createRequest(r);
                    //
                    if (cc == null) {
                        aa.receivedRequest(r);
                        u.addObserver(aa);
                        String notification = u.getUsername() + " made request " + RequestType.ACTOR_ISSUE + " - " + name + " with description: " + description;
                        u.notifyObserver(notification, aa);
                    } else {
                        cc.receivedRequest(r);
                        u.addObserver(cc);
                        String notification = u.getUsername() + " made request " + RequestType.ACTOR_ISSUE + " - " + name + " with description: " + description;
                        u.notifyObserver(notification, cc);
                    }
                    System.out.println("Request was sent");
                    System.out.println("\t  1) Back to the menu");
                    numberBack = alegereNumarValidCLI(1);
                    toTheMenu(u, accType);
                }
            }
        } else if (numberBack == 3) {
            String name = readText("Please type the movie name: ", "name");
            String description = readText("Please type the description: ", "description");
            Contributor<?> cc = null;
            Admin<?> aa = null;
            for (Contributor<?> c : contributors)
                if (c.isProdContributedWithName(name)) {
                    cc = c;
                    break;
                }
            if (cc == null)
                for (Admin<?> a : admins)
                    if (a.isProdContributedWithName(name)) {
                        aa = a;
                        break;
                    }
            if (cc == null && aa == null) {
                System.out.println("Invalid movie name.");
                System.out.println("\t1)  Try again");
                System.out.println("\t2)  Back to the menu");
                numberBack = alegereNumarValidCLI(2);
                if (numberBack == 1)
                    createRequest(u, accType);
                else
                    toTheMenu(u, accType);
            } else {
                if (u == cc) {
                    System.out.println("Request impossible because you added the production");
                    System.out.println("\t  1) Back to the menu");
                    numberBack = alegereNumarValidCLI(1);
                    toTheMenu(u, accType);
                } else {
                    Request r;
                    if (cc == null)
                        r = new Request(RequestType.MOVIE_ISSUE, LocalDateTime.now(), name, description, u.getUsername(), aa.getUsername());
                    else
                        r = new Request(RequestType.MOVIE_ISSUE, LocalDateTime.now(), name, description, u.getUsername(), cc.getUsername());
                    requests.add(r);
                    if (accType == AccountType.REGULAR)
                        u = (Regular<?>) u;
                    else
                        u = (Contributor<?>) u;
                    ((RequestsManager) u).createRequest(r);
                    //
                    if (cc == null) {
                        aa.receivedRequest(r);
                        u.addObserver(aa);
                        String notification = u.getUsername() + " made request " + RequestType.MOVIE_ISSUE + " - " + name + " with description: " + description;
                        u.notifyObserver(notification, aa);
                    } else {
                        cc.receivedRequest(r);
                        u.addObserver(cc);
                        String notification = u.getUsername() + " made request " + RequestType.MOVIE_ISSUE + " - " + name + " with description: " + description;
                        u.notifyObserver(notification, cc);
                    }
                    System.out.println("Request was sent");
                    System.out.println("\t  1) Back to the menu");
                    numberBack = alegereNumarValidCLI(1);
                    toTheMenu(u, accType);
                }
            }
        } else if (numberBack == 4) {
            String description = readText("Please type the description: ", "description");
            Request r = new Request(RequestType.OTHERS, LocalDateTime.now(), null, description, u.getUsername(), "ADMIN");
            requests.add(r);
            if (accType == AccountType.REGULAR)
                u = (Regular<?>) u;
            else
                u = (Contributor<?>) u;
            ((RequestsManager) u).createRequest(r);
            admins.get(0).receivedRequestHolder(r);//Se trimite la intreaga echipa de admini
            u.addObserver(admins.get(0));//Adaugam intreaga echipa de admini ca observator
            String notification = u.getUsername() + " made request " + RequestType.OTHERS + " with description: " + description;
            u.notifyObserver(notification, admins.get(0));
            System.out.println("Request was sent");
            System.out.println("\t  1) Back to the menu");
            numberBack = alegereNumarValidCLI(1);
            toTheMenu(u, accType);
        } else
            toTheMenu(u, accType);
    }

    public void removeRequest(User<?> u, AccountType accType) {
        System.out.println("Here is a list of all requests created by you\n");
        Regular<?> r = null;
        Contributor<?> c = null;
        if (accType == AccountType.REGULAR)
            r = (Regular<?>) u;
        else
            c = (Contributor<?>) u;

        boolean executaCodulRecursiv = true;
        if (c == null) {
            if (r.getRequestsToUsers().size() == 0) {
                System.out.println("List is empty");
                System.out.println("\t1)  Back to the menu");
                int numberBack = alegereNumarValidCLI(1);
                toTheMenu(u, accType);
                executaCodulRecursiv = false;
            } else
                for (int i = 0; i < r.getRequestsToUsers().size(); i++)
                    System.out.println("\t" + (i + 1) + ")  " + r.getRequestsToUsers().get(i));
        } else {
            if (c.getRequestsToUsers().size() == 0) {
                System.out.println("List is empty");
                System.out.println("\t1)  Back to the menu");
                int numberBack = alegereNumarValidCLI(1);
                toTheMenu(u, accType);
                executaCodulRecursiv = false;
            } else
                for (int i = 0; i < c.getRequestsToUsers().size(); i++)
                    System.out.println("\t" + (i + 1) + ")  " + c.getRequestsToUsers().get(i));
        }
        if (executaCodulRecursiv)//Nu ne am dus in meniu, adica n vem apel recursiv
        {
            if (c != null)
                System.out.println("\t" + (c.getRequestsToUsers().size() + 1) + ")  Back to the menu");
            else
                System.out.println("\t" + (r.getRequestsToUsers().size() + 1) + ")  Back to the menu");

            System.out.println("\nPlease choose the request you want to delete:");

            if (c != null) {
                int numberBack = alegereNumarValidCLI(c.getRequestsToUsers().size() + 1);
                if (numberBack <= c.getRequestsToUsers().size()) {
                    Request request = c.getRequestsToUsers().get(numberBack - 1);
                    String toUsername = request.getResolverUsername();
                    if (toUsername.equals("ADMIN")) {
                        admins.get(0).removeReceiveRequestHolder(request);
                        String notification = u.getUsername() + " removed request " + request.getType() + " with description: " + request.getDescription();
                        u.notifyObserver(notification, admins.get(0));
                    } else {
                        //???
                        Contributor<?> toContributor = null;
                        Admin<?> toAdmin = null;
                        for (Contributor<?> contributor : contributors)
                            if (toUsername.equals(contributor.getUsername())) {
                                toContributor = contributor;
                                break;
                            }
                        if (toContributor == null)
                            for (Admin<?> admin : admins)
                                if (toUsername.equals(admin.getUsername())) {
                                    toAdmin = admin;
                                    break;
                                }
                        if (toContributor != null) {
                            toContributor.removeReceivedRequest(request);
                            String notification = u.getUsername() + " removed request " + request.getType() + " - " + request.getTitleOrName() + " with description: " + request.getDescription();
                            u.notifyObserver(notification, toContributor);
                        } else {
                            toAdmin.removeReceivedRequest(request);
                            String notification = u.getUsername() + " removed request " + request.getType() + " - " + request.getTitleOrName() + " with description: " + request.getDescription();
                            u.notifyObserver(notification, toAdmin);
                        }
                    }
                    c.removeRequest(request);
                    requests.remove(request);
                    System.out.println("Request was removed");
                    System.out.println("\t1)  Back to the menu");
                    numberBack = alegereNumarValidCLI(1);
                    toTheMenu(u, accType);
                } else
                    toTheMenu(u, accType);
            } else {
                int numberBack = alegereNumarValidCLI(r.getRequestsToUsers().size() + 1);
                if (numberBack <= r.getRequestsToUsers().size()) {
                    Request request = r.getRequestsToUsers().get(numberBack - 1);
                    String toUsername = request.getResolverUsername();
                    if (toUsername.equals("ADMIN")) {
                        admins.get(0).removeReceiveRequestHolder(request);
                        String notification = u.getUsername() + " removed request " + request.getType() + " with description: " + request.getDescription();
                        u.notifyObserver(notification, admins.get(0));
                    } else {
                        //???
                        Contributor<?> toContributor = null;
                        Admin<?> toAdmin = null;
                        for (Contributor<?> contributor : contributors)
                            if (toUsername.equals(contributor.getUsername())) {
                                toContributor = contributor;
                                break;
                            }
                        if (toContributor == null)
                            for (Admin<?> admin : admins)
                                if (toUsername.equals(admin.getUsername())) {
                                    toAdmin = admin;
                                    break;
                                }
                        if (toContributor != null) {
                            toContributor.removeReceivedRequest(request);
                            String notification = u.getUsername() + " removed request " + request.getType() + " with description: " + request.getDescription();
                            u.notifyObserver(notification, toContributor);
                        } else {
                            toAdmin.removeReceivedRequest(request);
                            String notification = u.getUsername() + " removed request " + request.getType() + " with description: " + request.getDescription();
                            u.notifyObserver(notification, toAdmin);
                        }
                    }
                    r.removeRequest(request);
                    requests.remove(request);
                    System.out.println("Request was removed");
                    System.out.println("\t1)  Back to the menu");
                    numberBack = alegereNumarValidCLI(1);
                    toTheMenu(u, accType);
                } else
                    toTheMenu(u, accType);
            }
        }
    }

    public void createRemoveRequest(User<?> u, AccountType accType) {
        System.out.println("\n\t1)  Create request");
        System.out.println("\t2)  Remove request");
        int numberBack = alegereNumarValidCLI(2);
        if (numberBack == 1)
            createRequest(u, accType);
        else removeRequest(u, accType);
    }

    public int alegereScorValidCLI(int valMax) {
        boolean isValid = false;
        Scanner scanner = new Scanner(System.in);
        int number = 0;
        while (!isValid) {
            try {
                System.out.print("Introduce score: ");
                number = scanner.nextInt();

                boolean ok = false;
                for (int i = 1; i <= valMax; i++)
                    if (number == i) {
                        ok = true;
                        break;
                    }
                if (ok == false)
                    throw new InvalidCommandException("Invalid number. It must be between 1 or " + valMax + ".");

                isValid = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid number. It must be between 1 or " + valMax + ".");
                scanner.nextLine();
            } catch (InvalidCommandException e) {
                System.out.println("An exception occurred: " + e.getMessage());
            }
        }
        return number;
    }

    public boolean comp(Rating A, Rating B) {
        String usernameA = A.getUsername();
        String usernameB = B.getUsername();
        int experienceA = -1, experienceB = -1;

        for (Regular<?> r : regulars) {
            if (usernameA.equals(r.getUsername()))
                experienceA = r.getExperience();
            if (usernameB.equals(r.getUsername()))
                experienceB = r.getExperience();
        }

        // Chiar daca se garanteaza in enunt ca avem
        // de a face doar cu utilizatori Regular
        for (Contributor<?> c : contributors) {
            if (usernameA.equals(c.getUsername()))
                experienceA = c.getExperience();
            if (usernameB.equals(c.getUsername()))
                experienceB = c.getExperience();
        }

        for (Admin<?> a : admins) {
            if (usernameA.equals(a.getUsername()))
                experienceA = a.getExperience();
            if (usernameB.equals(a.getUsername()))
                experienceB = a.getExperience();
        }
        if (experienceA == -1 || experienceB == -1)
            return false;
        return experienceA < experienceB;
    }

    public User<?> isAccountWithUsername(String username) {
        for (Regular<?> r : regulars)
            if (username.equals(r.getUsername()))
                return r;
        for (Contributor<?> c : contributors)
            if (username.equals(c.getUsername()))
                return c;
        for (Admin<?> a : admins)
            if (username.equals(a.getUsername()))
                return a;
        return null;
    }

    public User<?> isAccountContributedWithProduction(String title) {
        for (Contributor<?> c : contributors)
            if (c.isProdContributedWithName(title))
                return c;
        for (Admin<?> a : admins)
            if (a.isProdContributedWithName(title))
                return a;
        return null;
    }

    public void addReviewProd(User<?> u, AccountType accType) {
        System.out.println("Please choose Production:");
        System.out.println("Movies:");
        for (int i = 0; i < movies.size(); i++)
            System.out.println("\t" + (i + 1) + ")  " + movies.get(i).getTitle() + " - Release Year " + movies.get(i).getReleaseYear());
        int j = movies.size();
        System.out.println("Series:");
        for (int i = 0; i < series.size(); i++)
            System.out.println("\t" + (j + i + 1) + ")  " + series.get(i).getTitle() + " - Release Year " + series.get(i).getReleaseYear());
        System.out.println("\n\t" + (j + series.size() + 1) + ")  Back to the menu");
        int number = alegereNumarValidCLI(j + series.size() + 1);
        if (number <= movies.size()) // number >= 1
        {
            //System.out.println(movies.get(number - 1));
            Movie movieToBeReviewed = movies.get(number - 1);
            if (movieToBeReviewed.didUserAddRating(u.getUsername())) // E deja revizuit
            {
                System.out.println("You have already added a rating to this production");
                System.out.println("\t1)  Back to the menu");
                number = alegereNumarValidCLI(1);
                toTheMenu(u, accType);
            } else {
                // introdu scorul si comentariul
                int score = alegereScorValidCLI(10);
                String comment = readText("Comment :", "text");
                // Adaugam recenzia si actuailizam averageScore
                Rating rrrrrrrrrr = new Rating(u.getUsername(), score, comment);
                movieToBeReviewed.addRating(rrrrrrrrrr);
                Regular<?> regular = (Regular<?>) u;
                // Notificam userii care au oferit rating de aparitia unui rating nou
                List<Rating> ratingListMovieToBeReviewed = movieToBeReviewed.ratingList();
                for (Rating r : ratingListMovieToBeReviewed) {
                    String username = r.getUsername();
                    User<?> toNotify = isAccountWithUsername(username);
                    String notification = u.getUsername() + " added a new rating to " + movieToBeReviewed.getTitle();
                    if (toNotify != null)
                        u.notifyObserver(notification, toNotify);
                }
                // Notificam contributorul/adminul cu un rating nou
                User<?> toNotify = isAccountContributedWithProduction(movieToBeReviewed.getTitle());
                String notification = u.getUsername() + " added a new rating to your contribution " + movieToBeReviewed.getTitle();
                if (toNotify != null)
                    u.notifyObserver(notification, toNotify);
                //
                regular.getRatingsMap().put(movieToBeReviewed, rrrrrrrrrr);
                if (!regular.getRemovedProductions().contains(movieToBeReviewed)) { // Daca productia e noua
                    // Crestem experienta
                    u.setExperienceStrategy(new AddRatingProduction());
                    u.updateExperience();
                }
                // Sortam lista de ratings a filmului dupa experienta utilizatorilor
                for (int a = 0; a < ratingListMovieToBeReviewed.size(); a++)
                    for (int b = a + 1; b < ratingListMovieToBeReviewed.size(); b++)
                        if (comp(ratingListMovieToBeReviewed.get(a), ratingListMovieToBeReviewed.get(b))) {
                            Rating aux = ratingListMovieToBeReviewed.get(a);
                            ratingListMovieToBeReviewed.set(a, ratingListMovieToBeReviewed.get(b));
                            ratingListMovieToBeReviewed.set(b, aux);
                        }
                System.out.println("Rating was added");
                System.out.println("\t1)  Back to the menu");
                number = alegereNumarValidCLI(1);
                toTheMenu(u, accType);
            }
        } else if (number <= j + series.size()) {
            //System.out.println(series.get(number - movies.size() - 1));
            Series seriesToBeReviewed = series.get(number - movies.size() - 1);
            if (seriesToBeReviewed.didUserAddRating(u.getUsername())) // E deja revizuit
            {
                System.out.println("You have already added a rating to this production");
                System.out.println("\t1)  Back to the menu");
                number = alegereNumarValidCLI(1);
                toTheMenu(u, accType);
            } else {
                // introdu scorul si comentariul
                int score = alegereScorValidCLI(10);
                String comment = readText("Comment :", "text");
                // Adaugam recenzia si actuailizam averageScore
                Rating ssssssssssssssssss = new Rating(u.getUsername(), score, comment);
                seriesToBeReviewed.addRating(ssssssssssssssssss);
                Regular<?> regular = (Regular<?>) u;
                // Notificam userii care au oferit rating de aparitia unui rating nou
                List<Rating> ratingListSeriesToBeReviewed = seriesToBeReviewed.ratingList();
                for (Rating r : ratingListSeriesToBeReviewed) {
                    String username = r.getUsername();
                    User<?> toNotify = isAccountWithUsername(username);
                    String notification = u.getUsername() + " added a new rating to " + seriesToBeReviewed.getTitle();
                    if (toNotify != null)
                        u.notifyObserver(notification, toNotify);
                }
                // Notificam contributorul/adminul cu un rating nou
                User<?> toNotify = isAccountContributedWithProduction(seriesToBeReviewed.getTitle());
                String notification = u.getUsername() + " added a new rating to your contribution " + seriesToBeReviewed.getTitle();
                if (toNotify != null)
                    u.notifyObserver(notification, toNotify);
                //
                regular.getRatingsMap().put(seriesToBeReviewed, ssssssssssssssssss);
                if (!regular.getRemovedProductions().contains(seriesToBeReviewed)) { // Daca productia e noua
                    // Crestem experienta
                    u.setExperienceStrategy(new AddRatingProduction());
                    u.updateExperience();
                }
                // Sortam lista de ratings a serialului dupa experienta utilizatorilor
                for (int a = 0; a < ratingListSeriesToBeReviewed.size(); a++)
                    for (int b = a + 1; b < ratingListSeriesToBeReviewed.size(); b++)
                        if (comp(ratingListSeriesToBeReviewed.get(a), ratingListSeriesToBeReviewed.get(b))) {
                            Rating aux = ratingListSeriesToBeReviewed.get(a);
                            ratingListSeriesToBeReviewed.set(a, ratingListSeriesToBeReviewed.get(b));
                            ratingListSeriesToBeReviewed.set(b, aux);
                        }
                System.out.println("Rating was added");
                System.out.println("\t1)  Back to the menu");
                number = alegereNumarValidCLI(1);
                toTheMenu(u, accType);
            }
        } else
            toTheMenu(u, accType);
    }

    public void deleteReviewProd(User<?> u, AccountType accType) {
        System.out.println("Here you have a list with productions reviewed");
        Regular<?> r = (Regular<?>) u;
        int index = 1;
        for (Map.Entry<Production, Rating> entry : r.getRatingsMap().entrySet()) {
            Production production = entry.getKey();
            Rating rating = entry.getValue();
            System.out.println(index + ")Production: " + production.getTitle() + rating);
            index++;
        }
        System.out.println(index + ")  Back to the menu");
        int numberBack = alegereNumarValidCLI(index);
        if (numberBack < index) {
            int trackingVariable = 1;
            Production production = null;
            Rating rating = null;
            for (Map.Entry<Production, Rating> entry : r.getRatingsMap().entrySet()) {
                production = entry.getKey();
                rating = entry.getValue();
                if (trackingVariable == numberBack)
                    break;
                trackingVariable++;
            }
            // Eliminam rating ul din lista de rating uri ale utilizatorului + sistem
            r.getRatingsMap().remove(production);
            production.ratingList().remove(rating);
            r.getRemovedProductions().add(production);
            // Notificari la useri ce au dat rating ca si a sters rating ul user ul r
            List<Rating> ratingListProdDeleteReview = production.ratingList();
            for (Rating rating111 : ratingListProdDeleteReview) {
                String username = rating111.getUsername();
                User<?> toNotify = isAccountWithUsername(username);
                String notification = r.getUsername() + " removed the rating from " + production.getTitle();
                if (toNotify != null)
                    u.notifyObserver(notification, toNotify);
            }
            // Notificam contributorul/adminul cu un rating nou
            User<?> toNotify = isAccountContributedWithProduction(production.getTitle());
            String notification = u.getUsername() + " removed the rating from your contribution " + production.getTitle();
            if (toNotify != null)
                u.notifyObserver(notification, toNotify);
            //
            System.out.println("Rating was removed with success");
            System.out.println("\t1)  Back to the menu");
            numberBack = alegereNumarValidCLI(1);
            toTheMenu(u, accType);
        } else
            toTheMenu(u, accType);
    }

    public void addDeleteReviewProd(User<?> u, AccountType accType) {
        System.out.println("\n\t1)  Add review production");
        System.out.println("\t2)  Delete review production");
        int numberBack = alegereNumarValidCLI(2);
        if (numberBack == 1)
            addReviewProd(u, accType);
        else
            deleteReviewProd(u, accType);
    }

    public void printMyProfile(User<?> u, AccountType accType) {
        System.out.println(u);
        System.out.println("\t  1) Back to the menu");
        int numberBack = alegereNumarValidCLI(1);
        toTheMenu(u, accType);
    }

    public void printDetailsAccountAndMenuRegularCLI(Regular<?> u) {
        System.out.println("Welcome back user " + u.getUsername());
        System.out.println("Username: " + u.getUsername());
        System.out.println("User experience: " + ((u.getExperience() == 0) ? "-" : u.getExperience()));
        System.out.println("Choose action:");
        System.out.println("\t1)  View productions details");
        System.out.println("\t2)  View actors details");
        System.out.println("\t3)  View notifications");
        System.out.println("\t4)  Search for actor/movie/series");
        System.out.println("\t5)  Add/Delete actor/movie/series to/from favorites");
        System.out.println("\t6)  Create/Remove a request");
        System.out.println("\t7)  Add/Delete a review for a production");
        System.out.println("\t8)  Logout");
        System.out.println("\t9)  My profile");
        int number = alegereNumarValidCLI(9);
        if (number == 1)
            viewProductionsDetailsCLI(u, AccountType.REGULAR);
        else if (number == 2)
            viewActorsDetailsCLI(u, AccountType.REGULAR);
        else if (number == 3)
            viewNotifications(u, AccountType.REGULAR);
        else if (number == 4)
            searchActorOrProductionInSystemCLI(u, AccountType.REGULAR);
        else if (number == 5)
            addOrDeleteActorOrProductionFavorites(u, AccountType.REGULAR);
        else if (number == 6)
            createRemoveRequest(u, AccountType.REGULAR);
        else if (number == 7)
            addDeleteReviewProd(u, AccountType.REGULAR);
        else if (number == 8)
            u.logoutCLI();
        else
            printMyProfile(u, AccountType.REGULAR);
        //TODO
    }

    public Production isProductionInSystem(String title) {
        for (Movie m : movies)
            if (title.equals(m.getTitle()))
                return m;
        for (Series s : series)
            if (title.equals(s.getTitle()))
                return s;
        return null;
    }

    public boolean isActorInSystem(String name) {
        for (Actor a : actors)
            if (name.equals(a.getName()))
                return true;
        return false;
    }

    public int alegereDataLansariiSauNumSeasonsCLI(String msg1) {
        boolean isValid = false;
        Scanner scanner = new Scanner(System.in);
        int number = 0;
        while (!isValid) {
            try {
                System.out.print(msg1);
                number = scanner.nextInt();

                isValid = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid number.");
                scanner.nextLine();
            }
        }
        return number;
    }

    public void addActorSystem(User<?> u, AccountType accType) {
        String name = readText("The name of the actor: ", "name");
        if (isActorInSystem(name)) {
            System.out.println("Thee actor has already been added to the system");
            System.out.println("\t1)  Back to the menu");
            int numberBack = alegereNumarValidCLI(1);
            toTheMenu(u, accType);
        } else {
            String biography = readText("The biography: ", "description");
            Actor actor = new Actor(name, biography);
            String title = null;
            System.out.println("Write the name of the productions in which he appeared\n" +
                    "Write STOP when you want to stop");
            while (!"STOP".equals(title)) {
                title = readText("Production: ", "name");
                if (!"STOP".equals(title)) {
                    Production production = isProductionInSystem(title);
                    if (production == null)
                        System.out.println("Production is not in the system");
                    else if (production instanceof Movie)
                        actor.addFilmAppearance(title, "Movie");
                    else if (production instanceof Series)
                        actor.addFilmAppearance(title, "Series");
                }
            }
            // Adaugat in system
            actors.add(actor);
            // Adaugat ca contributor/admin
            Staff<ProductionAndActor> staff = (Staff<ProductionAndActor>) u;
            staff.addActorSystem(actor);
            // Crestere experienta
            if (u instanceof Contributor<?>) { // Adminii au experienta infinita
                u.setExperienceStrategy(new AddProductionOrActor());
                u.updateExperience();
            }
            //
            System.out.println("Actor was added in system");
            System.out.println("\t1)  Back to the menu");
            int numberBack = alegereNumarValidCLI(1);
            toTheMenu(u, accType);
        }
    }

    public void addProductionSystem(User<?> u, AccountType accType) {
        String typeProd = readText("Choose Movie or Series:", "name");
        while (!"Movie".equals(typeProd) && !"Series".equals(typeProd)) {
            System.out.println("Invalid option, please write Movie or Series");
            typeProd = readText("Choose Movie or Series:", "name");
        }

        String title = readText("Title: ", "title");
        if (isProductionInSystem(title) != null) {
            System.out.println("Thee production has already been added to the system");
            System.out.println("\t1)  Back to the menu");
            int numberBack = alegereNumarValidCLI(1);
            toTheMenu(u, accType);
        } else {
            System.out.println("Write the name of the directors\n" +
                    "Write STOP when you want to stop");
            String director = null;
            List<String> directorsProd = new ArrayList<>();
            while (!"STOP".equals(director)) {
                director = readText("Director: ", "name");
                if (!"STOP".equals(director))
                    directorsProd.add(director);
            }

            System.out.println("Write the name of the actors\n" +
                    "Write STOP when you want to stop");
            String actor = null;
            List<String> actorsProd = new ArrayList<>();
            while (!"STOP".equals(actor)) {
                actor = readText("Actor: ", "name");
                if (!"STOP".equals(actor)) {
                    if (isActorInSystem(actor))
                        actorsProd.add(actor);
                    else
                        System.out.println("Actor is not in the system");
                }
            }

            System.out.println("Write the genres\n" +
                    "Write STOP when you want to stop");
            String genreProd = null;
            List<Genre> genresProd = new ArrayList<>();
            while (!"STOP".equals(genreProd)) {
                genreProd = readText("Genre: ", "genre");
                if (!"STOP".equals(genreProd)) {
                    try {
                        Genre myGenre = Genre.fromString(genreProd);
                        genresProd.add(myGenre);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid Genre");
                    }
                }
            }

            String plot = readText("Write the plot: ", "description");

            if (typeProd.equals("Movie")) {
                String duration = readText("Write the duration: ", "duration");
                int releaseYearProd = alegereDataLansariiSauNumSeasonsCLI("Introduce release year: ");
                Movie newMovie = new Movie(title, directorsProd, actorsProd, genresProd, plot, duration, releaseYearProd);
                // Adaugat in system
                movies.add(newMovie);
                // Adaugat ca contributor/admin
                Staff<ProductionAndActor> staff = (Staff<ProductionAndActor>) u;
                staff.addProductionSystem(newMovie);
                // Crestere experienta
                if (u instanceof Contributor<?>) { // Adminii au experienta infinita
                    u.setExperienceStrategy(new AddProductionOrActor());
                    u.updateExperience();
                }
                //
                System.out.println("Movie was added in system");
                System.out.println("\t1)  Back to the menu");
                int numberBack = alegereNumarValidCLI(1);
                toTheMenu(u, accType);
            } else {
                int releaseYearProd = alegereDataLansariiSauNumSeasonsCLI("Introduce release year: ");
                int numSeasons = alegereDataLansariiSauNumSeasonsCLI("Introduce number of seasons: ");
                Map<String, List<Episode>> seasons = new TreeMap<>();
                for (int i = 1; i <= numSeasons; i++) {
                    System.out.println("Season " + i + ":");
                    int nrEprisodes = alegereDataLansariiSauNumSeasonsCLI("Introduce number of episoded: ");
                    List<Episode> episodes = new ArrayList<>();
                    for (int j = 1; j <= nrEprisodes; j++) {
                        System.out.println("\tEpisode " + j + ":");
                        String nameEp = readText("\tEpisode Name: ", "name");
                        String durationEp = readText("\tDuration episode: ", " duration");
                        episodes.add(new Episode(nameEp, durationEp));
                    }
                    String seasonName = "Season " + i;
                    seasons.put(seasonName, episodes);
                }
                Series newSeries = new Series(title, directorsProd, actorsProd, genresProd, plot, releaseYearProd, numSeasons, seasons);
                // Adaugat in system
                series.add(newSeries);
                // Adaugat ca contributor/admin
                Staff<ProductionAndActor> staff = (Staff<ProductionAndActor>) u;
                staff.addProductionSystem(newSeries);
                // Crestere experienta
                if (u instanceof Contributor<?>) { // Adminii au experienta infinita
                    u.setExperienceStrategy(new AddProductionOrActor());
                    u.updateExperience();
                }
                //
                System.out.println("Series was added in system");
                System.out.println("\t1)  Back to the menu");
                int numberBack = alegereNumarValidCLI(1);
                toTheMenu(u, accType);
            }
        }
    }

    public void deleteActorSystem(User<?> u, AccountType accType) {
        System.out.println("Here you have a list of actors that you can delete from the system:");
        Staff<ProductionAndActor> staff = (Staff<ProductionAndActor>) u;
        if (accType == AccountType.CONTRIBUTOR) {
            int index = 1;
            index = staff.printActorsAdded(index);
            System.out.println(index + ")  Back to the menu");
            int numberBack = alegereNumarValidCLI(index);
            if (numberBack < index) {
                Actor actorDeleted = staff.getIndexActorContributed(numberBack);
                System.out.println(actorDeleted.getName());
                // Stergere din sistem
                actors.remove(actorDeleted);
                // Stergere din lista user ului de la contributori
                staff.removeActorSystem(actorDeleted);
                // Stergere din productii
                for (Movie m : movies)
                    m.removeActor(actorDeleted.getName());
                for (Series s : series)
                    s.removeActor(actorDeleted.getName());
                //
                System.out.println("Actor was deleted");
                System.out.println("\t1)  Back to the menu");
                numberBack = alegereNumarValidCLI(1);
                toTheMenu(u, accType);
            } else
                toTheMenu(u, accType);
        } else // E admin, deci poate elimina actor si din lista celorlati admini
        {
            List<Integer> indexis = new ArrayList<>();
            int index = 1;
            indexis.add(index);
            for (int i = 0; i < admins.size(); i++) {
                index = admins.get(i).printActorsAdded(index);
                indexis.add(index);
            }
            System.out.println(index + ")  Back to the menu");
            int numberBack = alegereNumarValidCLI(index);
            if (numberBack < index) {
                int indexAdmin = -1;
                for (int i = 0; i < indexis.size(); i++)
                    if (numberBack < indexis.get(i)) {
                        indexAdmin = i - 1;
                        break;
                    }
                Actor actorDeleted = admins.get(indexAdmin).getIndexActorContributed(numberBack - indexis.get(indexAdmin) + 1);
                System.out.println(actorDeleted.getName());
                // Stergere din sistem
                actors.remove(actorDeleted);
                // Stergere din lista user ului de la contributori
                admins.get(indexAdmin).removeActorSystem(actorDeleted);
                // Stergere din productii
                for (Movie m : movies)
                    m.removeActor(actorDeleted.getName());
                for (Series s : series)
                    s.removeActor(actorDeleted.getName());
                //
                System.out.println("Actor was deleted");
                System.out.println("\t1)  Back to the menu");
                numberBack = alegereNumarValidCLI(1);
                toTheMenu(u, accType);
            } else
                toTheMenu(u, accType);
        }
    }

    public void deleteProductionSystem(User<?> u, AccountType accType) {
        System.out.println("Here you have a list of Production that you can delete from the system:");
        Staff<ProductionAndActor> staff = (Staff<ProductionAndActor>) u;
        if (accType == AccountType.CONTRIBUTOR) {
            int index = 1;
            index = staff.printProductionsAdded(index);
            System.out.println(index + ")  Back to the menu");
            int numberBack = alegereNumarValidCLI(index);
            if (numberBack < index) {
                Production productionDeleted = staff.getIndexProductionContributed(numberBack);
                System.out.println(productionDeleted.getTitle());
                // Stergere din sistem
                if (productionDeleted instanceof Movie)
                    movies.remove(productionDeleted);
                else
                    series.remove(productionDeleted);
                // Stergere din lista user ului de la contributori
                staff.removeProductionSystem(productionDeleted);
                // Stergere de la actori
                for (Actor a : actors)
                    a.removeFilmAppearanceByName(productionDeleted.getTitle());
                // Stergere din lista rating urilor userilor regular
                for (Regular<?> r : regulars)
                    r.removeProductionSystemWithRatingAndAll(productionDeleted);
                //
                System.out.println("Production was deleted");
                System.out.println("\t1)  Back to the menu");
                numberBack = alegereNumarValidCLI(1);
                toTheMenu(u, accType);
            } else
                toTheMenu(u, accType);
        } else // E admin, deci poate elimina actor si din lista celorlati admini
        {
            List<Integer> indexis = new ArrayList<>();
            int index = 1;
            indexis.add(index);
            for (int i = 0; i < admins.size(); i++) {
                index = admins.get(i).printProductionsAdded(index);
                indexis.add(index);
            }
            System.out.println(index + ")  Back to the menu");
            int numberBack = alegereNumarValidCLI(index);
            if (numberBack < index) {
                int indexAdmin = -1;
                for (int i = 0; i < indexis.size(); i++)
                    if (numberBack < indexis.get(i)) {
                        indexAdmin = i - 1;
                        break;
                    }
                Production productionDeleted = admins.get(indexAdmin).getIndexProductionContributed(numberBack - indexis.get(indexAdmin) + 1);
                System.out.println(productionDeleted.getTitle());
                // Stergere din sistem
                if (productionDeleted instanceof Movie)
                    movies.remove(productionDeleted);
                else
                    series.remove(productionDeleted);
                // Stergere din lista user ului de la contributori
                admins.get(indexAdmin).removeProductionSystem(productionDeleted);
                // Stergere de la actori
                for (Actor a : actors)
                    a.removeFilmAppearanceByName(productionDeleted.getTitle());
                // Stergere din lista rating urilor userilor regular
                for (Regular<?> r : regulars)
                    r.removeProductionSystemWithRatingAndAll(productionDeleted);
                //
                System.out.println("Production was deleted");
                System.out.println("\t1)  Back to the menu");
                numberBack = alegereNumarValidCLI(1);
                toTheMenu(u, accType);
            } else
                toTheMenu(u, accType);
        }
    }

    public void addActorOrProductionSystem(User<?> u, AccountType accType) {
        System.out.println("\n\t1)  Add Actor in system");
        System.out.println("\t2)  Add Production in system");
        System.out.println("\t3)  Back to the menu");
        int numberBack = alegereNumarValidCLI(3);
        if (numberBack == 1)
            addActorSystem(u, accType);
        else if (numberBack == 2)
            addProductionSystem(u, accType);
        else
            toTheMenu(u, accType);
    }


    public void deleteActorOrProductionSystem(User<?> u, AccountType accType) {
        System.out.println("\n\t1)  Delete actor from the system");
        System.out.println("\t2)  Delete production from system");
        System.out.println("\t3)  Back to the menu");
        int numberBack = alegereNumarValidCLI(3);
        if (numberBack == 1)
            deleteActorSystem(u, accType);
        else if (numberBack == 2)
            deleteProductionSystem(u, accType);
        else
            toTheMenu(u, accType);
    }

    public void addOrDeleteActorOrProductionSystem(User<?> u, AccountType accType) {
        // si experienta pentru adaugare, contributorul sterge doar ce a adaugat el, adminii tot ce e sub responsabilitatea intregii echipe de admini
        System.out.println("\n\t1)  Add Actor/Production in system");
        System.out.println("\t2)  Delete Actor/Production from system");
        int numberBack = alegereNumarValidCLI(2);
        if (numberBack == 1)
            addActorOrProductionSystem(u, accType);
        else
            deleteActorOrProductionSystem(u, accType);
    }

    public User<?> searchRegularOrContributorWithName(String name) {
        for (Regular<?> r : regulars)
            if (name.equals(r.getUsername()))
                return r;
        for (Contributor<?> c : contributors)
            if (name.equals(c.getUsername()))
                return c;
        return null;
    }

    public void viewSolveReceivedRequests(User<?> u, AccountType accType) {
        System.out.println("\n\t1)  View received requests");
        System.out.println("\t2)  Solve received requests");
        int numberBack = alegereNumarValidCLI(2);
        if (numberBack == 1) {
            ((Staff<?>) u).printListOfRequest();
            System.out.println("\t1)  Back to the menu");
            numberBack = alegereNumarValidCLI(1);
            toTheMenu(u, accType);
        } else {
            System.out.println("Please choose the request you solved/denied:");
            Staff<?> staff = (Staff<?>) u;
            int nrRequests = staff.getListOfRequestsFromUsers().size();
            for (int i = 0; i < nrRequests; i++)
                System.out.println((i + 1) + ")  " + staff.getListOfRequestsFromUsers().get(i));
            System.out.println((nrRequests + 1) + ")  Back to the menu");
            numberBack = alegereNumarValidCLI(nrRequests + 1);
            if (numberBack <= nrRequests) {
                System.out.println(staff.getRequestFromUser(numberBack - 1));
                System.out.println("1)  The request was solved");
                System.out.println("2)  The request was denied");
                int numberBackBack = alegereNumarValidCLI(2);
                // Stergem cererea din lista de cereri atat de la emitator, cat si de la receptor + Notificare + Experienta
                Request r = staff.getRequestFromUser(numberBack - 1);
                staff.getListOfRequestsFromUsers().remove(r);
                User<?> userRequest = searchRegularOrContributorWithName(r.getUserUsername());
                if (userRequest instanceof Regular<?>)
                    ((Regular<?>) userRequest).getRequestsToUsers().remove(r);
                else
                    ((Contributor<?>) userRequest).getRequestsToUsers().remove(r);
                requests.remove(r);
                //
                if (numberBackBack == 1) {
                    System.out.println(":)))");
                    String notification = staff.getUsername() + " resolved your request " + r.getType() + " wih description " + r.getDescription();
                    staff.notifyObserver(notification, userRequest);
                    //
                    /**
                     if (staff instanceof Contributor<?>) { // Adminii au experienta infinita
                     staff.setExperienceStrategy(new RequestAccepted());
                     staff.updateExperience();
                     }*/
                    userRequest.setExperienceStrategy(new RequestAccepted());
                    userRequest.updateExperience();
                } else {
                    System.out.println(":((");
                    String notification = staff.getUsername() + " rejected your request " + r.getType() + " wih description " + r.getDescription();
                    staff.notifyObserver(notification, userRequest);
                }
                System.out.println("\t1)  Back to the menu");
                numberBackBack = alegereNumarValidCLI(1);
                toTheMenu(u, accType);
            } else
                toTheMenu(u, accType);
        }
    }

    public void updateTitle(Production movieUpdate) {
        String title = readText("New title: ", "title");
        // Updatam la actorii filmului noul nume
        List<String> actorsMovie = movieUpdate.getActors();
        for (String actorString : actorsMovie)
            for (Actor actorBun : actors)
                if (actorString.equals(actorBun.getName())) {
                    actorBun.updateFilmName(movieUpdate.getTitle(), title);
                    break;
                }
        //
        movieUpdate.setTitle(title);
    }

    public void updateDirectors(Production movieUpdate) {
        System.out.println("Write the new name of the directors\n" +
                "Write STOP when you want to stop");
        String director = null;
        List<String> directorsProd = new ArrayList<>();
        while (!"STOP".equals(director)) {
            director = readText("Director: ", "name");
            if (!"STOP".equals(director))
                directorsProd.add(director);
        }
        movieUpdate.setDirectors(directorsProd);
    }

    public void updateActors(Production movieUpdate) {
        System.out.println("Write the new name of the actors\n" +
                "Write STOP when you want to stop");
        String actor = null;
        List<String> actorsProd = new ArrayList<>();
        while (!"STOP".equals(actor)) {
            actor = readText("Actor: ", "name");
            if (!"STOP".equals(actor)) {
                if (isActorInSystem(actor))
                    actorsProd.add(actor);
                else
                    System.out.println("Actor is not in the system");
            }
        }
        // Stergem de la actorii vechi cum ca a aparut acest film la ei
        List<String> oldActorsMovie = movieUpdate.getActors();
        for (String actorString : oldActorsMovie)
            for (Actor actorBun : actors)
                if (actorString.equals(actorBun.getName())) {
                    actorBun.removeFilmAppearanceByName(movieUpdate.getTitle());
                    break;
                }
        // Adaugam la actorii noi in lista filmul
        for (String actorString : actorsProd)
            for (Actor actorBun : actors)
                if (actorString.equals(actorBun.getName())) {
                    if (movieUpdate instanceof Movie)
                        actorBun.addFilmAppearance(movieUpdate.getTitle(), "Movie");
                    else
                        actorBun.addFilmAppearance(movieUpdate.getTitle(), "Series");
                    break;
                }
        //
        movieUpdate.setActors(actorsProd);
    }

    public void updateGenres(Production movieUpdate) {
        System.out.println("Write the new genres\n" +
                "Write STOP when you want to stop");
        String genreProd = null;
        List<Genre> genresProd = new ArrayList<>();
        while (!"STOP".equals(genreProd)) {
            genreProd = readText("Genre: ", "genre");
            if (!"STOP".equals(genreProd)) {
                try {
                    Genre myGenre = Genre.fromString(genreProd);
                    genresProd.add(myGenre);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid Genre");
                }
            }
        }
        movieUpdate.setGenres(genresProd);
    }

    public void updatePlot(Production movieUpdate) {
        String plot = readText("Write the new plot: ", "description");
        movieUpdate.setPlot(plot);
    }

    public void updateDurationMovie(Movie movieUpdate) {
        String duration = readText("Write the new duration: ", "duration");
        movieUpdate.setDuration(duration);
    }

    public void updateReleaseYearMovie(Movie movieUpdate) {
        int releaseYearProd = alegereDataLansariiSauNumSeasonsCLI("Introduce new release year: ");
        movieUpdate.setReleaseYear(releaseYearProd);
    }

    public void toTheMovieChosen(Movie movieUpdate, User<?> u, AccountType accType, String msg) {
        System.out.println(msg + " updated");
        System.out.println("\t1)  Back to update the movie");
        System.out.println("\t2)  Back to the menu");
        int numberBack = alegereNumarValidCLI(2);
        if (numberBack == 1)
            updateMovieChosen(movieUpdate, u, accType);
        else
            toTheMenu(u, accType);
    }

    public void updateMovieChosen(Movie movieUpdate, User<?> u, AccountType accType) {
        System.out.println("  Choose what you want to change:");
        System.out.println("1)Title");
        System.out.println("2)Directors");
        System.out.println("3)Actors");
        System.out.println("4)Genres");
        System.out.println("5)Plot");
        System.out.println("6)Duration");
        System.out.println("7)Release Year");
        int numberBack = alegereNumarValidCLI(7);
        if (numberBack == 1) {
            updateTitle(movieUpdate);
            toTheMovieChosen(movieUpdate, u, accType, "Title");
        } else if (numberBack == 2) {
            updateDirectors(movieUpdate);
            toTheMovieChosen(movieUpdate, u, accType, "Directors");
        } else if (numberBack == 3) {
            updateActors(movieUpdate);
            toTheMovieChosen(movieUpdate, u, accType, "Actors");
        } else if (numberBack == 4) {
            updateGenres(movieUpdate);
            toTheMovieChosen(movieUpdate, u, accType, "Genres");
        } else if (numberBack == 5) {
            updatePlot(movieUpdate);
            toTheMovieChosen(movieUpdate, u, accType, "Plot");
        } else if (numberBack == 6) {
            updateDurationMovie(movieUpdate);
            toTheMovieChosen(movieUpdate, u, accType, "Duration");
        } else {
            updateReleaseYearMovie(movieUpdate);
            toTheMovieChosen(movieUpdate, u, accType, "Release year");
        }
    }

    public void updateReleaseYearSeries(Series movieUpdate) {
        int releaseYearProd = alegereDataLansariiSauNumSeasonsCLI("Introduce new release year: ");
        movieUpdate.setReleaseYear(releaseYearProd);
    }

    public void updateStructureOfSeasons(Series movieUpdate) {
        int numSeasons = alegereDataLansariiSauNumSeasonsCLI("Introduce number of seasons: ");
        Map<String, List<Episode>> seasons = new TreeMap<>();
        for (int i = 1; i <= numSeasons; i++) {
            System.out.println("Season " + i + ":");
            int nrEprisodes = alegereDataLansariiSauNumSeasonsCLI("Introduce number of episoded: ");
            List<Episode> episodes = new ArrayList<>();
            for (int j = 1; j <= nrEprisodes; j++) {
                System.out.println("\tEpisode " + j + ":");
                String nameEp = readText("\tEpisode Name: ", "name");
                String durationEp = readText("\tDuration episode: ", " duration");
                episodes.add(new Episode(nameEp, durationEp));
            }
            String seasonName = "Season " + i;
            seasons.put(seasonName, episodes);
        }
        //
        movieUpdate.setNumberOfSeasons(numSeasons);
        movieUpdate.setSeasons(seasons);
    }

    public void toTheSeriesChosen(Series movieUpdate, User<?> u, AccountType accType, String msg) {
        System.out.println(msg + " updated");
        System.out.println("\t1)  Back to update the series");
        System.out.println("\t2)  Back to the menu");
        int numberBack = alegereNumarValidCLI(2);
        if (numberBack == 1)
            updateSeriesChosen(movieUpdate, u, accType);
        else
            toTheMenu(u, accType);
    }

    public void updateSeriesChosen(Series movieUpdate, User<?> u, AccountType accType) {
        System.out.println("  Choose what you want to change:");
        System.out.println("1)Title");
        System.out.println("2)Directors");
        System.out.println("3)Actors");
        System.out.println("4)Genres");
        System.out.println("5)Plot");
        System.out.println("6)Number of seasons and the structure of seasons");
        System.out.println("7)Release Year");
        int numberBack = alegereNumarValidCLI(7);
        if (numberBack == 1) {
            updateTitle(movieUpdate);
            toTheSeriesChosen(movieUpdate, u, accType, "Title");
        } else if (numberBack == 2) {
            updateDirectors(movieUpdate);
            toTheSeriesChosen(movieUpdate, u, accType, "Directors");
        } else if (numberBack == 3) {
            updateActors(movieUpdate);
            toTheSeriesChosen(movieUpdate, u, accType, "Actors");
        } else if (numberBack == 4) {
            updateGenres(movieUpdate);
            toTheSeriesChosen(movieUpdate, u, accType, "Genres");
        } else if (numberBack == 5) {
            updatePlot(movieUpdate);
            toTheSeriesChosen(movieUpdate, u, accType, "Plot");
        } else if (numberBack == 6) {
            updateStructureOfSeasons(movieUpdate);
            toTheSeriesChosen(movieUpdate, u, accType, "Structure of seasons");
        } else {
            updateReleaseYearSeries(movieUpdate);
            toTheSeriesChosen(movieUpdate, u, accType, "Release year");
        }
    }

    public void updateMovie(User<?> u, AccountType accType) {
        System.out.println("Here is a list of movies from system");
        int numberMovies = movies.size();
        for (int i = 0; i < numberMovies; i++)
            System.out.println((i + 1) + ")  " + movies.get(i).getTitle());
        System.out.println((numberMovies + 1) + ")  Back to the menu");
        System.out.println("Please choose the number");
        int numberBack = alegereNumarValidCLI(numberMovies + 1);
        if (numberBack <= numberMovies) {
            System.out.println("You chose " + movies.get(numberBack - 1).getTitle());
            updateMovieChosen(movies.get(numberBack - 1), u, accType);
        } else
            toTheMenu(u, accType);
    }

    public void updateSeries(User<?> u, AccountType accType) {
        System.out.println("Here is a list of series from system");
        int numberSeries = series.size();
        for (int i = 0; i < numberSeries; i++)
            System.out.println((i + 1) + ")  " + series.get(i).getTitle());
        System.out.println((numberSeries + 1) + ")  Back to the menu");
        System.out.println("Please choose the number");
        int numberBack = alegereNumarValidCLI(numberSeries + 1);
        if (numberBack <= numberSeries) {
            System.out.println("You chose " + series.get(numberBack - 1).getTitle());
            updateSeriesChosen(series.get(numberBack - 1), u, accType);
        } else
            toTheMenu(u, accType);
    }

    public void updateMovieDetails(User<?> u, AccountType accType) {
        System.out.println("1)  Update Movie");
        System.out.println("2)  Update Series");
        System.out.println("3)  Back to the menu");
        int numberBack = alegereNumarValidCLI(3);
        if (numberBack == 1)
            updateMovie(u, accType);
        else if (numberBack == 2)
            updateSeries(u, accType);
        else
            toTheMenu(u, accType);
    }

    public void updateNameActor(Actor a) {
        String name = readText("New name: ", "name");
        // Actualizam numele prin Productii
        for (Movie m : movies) {
            List<String> actorsString = m.getActors();
            for (int i = 0; i < actorsString.size(); i++)
                if (a.getName().equals(actorsString.get(i))) {
                    actorsString.set(i, name);
                    break;
                }
        }
        for (Series s : series) {
            List<String> actorsString = s.getActors();
            for (int i = 0; i < actorsString.size(); i++)
                if (a.getName().equals(actorsString.get(i))) {
                    actorsString.set(i, name);
                    break;
                }
        }
        //
        a.setName(name);
    }

    public void updateFilmographyActor(Actor a) {
        System.out.println("Write the new filmography\n" +
                "Write STOP when you want to stop");
        String productionName = null;
        List<Actor.FilmAppearance> film = new ArrayList<>();
        // Citim noua filmografie
        while (!"STOP".equals(productionName)) {
            productionName = readText("Production: ", "name");
            if (!"STOP".equals(productionName)) {
                Production p = isProductionInSystem(productionName);
                if (p != null) {
                    if (p instanceof Movie)
                        film.add(new Actor.FilmAppearance(p.getTitle(), "Movie"));
                    else
                        film.add(new Actor.FilmAppearance(p.getTitle(), "Series"));
                } else
                    System.out.println("Production is not in the system");
            }
        }
        // Sterg actorii din vechea filmografie
        List<Actor.FilmAppearance> filmOld = a.getFilmography();
        for (Actor.FilmAppearance f : filmOld) {
            Production p = isProductionInSystem(f.getName());
            if (p != null)
                p.removeActor(a.getName());
        }
        // Pun actorii in noua filmografie
        for (Actor.FilmAppearance f : film) {
            Production p = isProductionInSystem(f.getName());
            if (p != null)
                p.addActor(a.getName());
        }
        //
        a.setFilmography(film);
    }

    public void updateBiography(Actor a) {
        String biography = readText("Write the new biography: ", "biography");
        a.setBiography(biography);
    }

    public void toTheActorChosen(Actor a, User<?> u, AccountType accType, String msg) {
        System.out.println(msg + " updated");
        System.out.println("\t1)  Back to update the actor");
        System.out.println("\t2)  Back to the menu");
        int numberBack = alegereNumarValidCLI(2);
        if (numberBack == 1)
            updateActorChosen(a, u, accType);
        else
            toTheMenu(u, accType);
    }

    public void updateActorChosen(Actor a, User<?> u, AccountType accType) {
        System.out.println("  Choose what you want to change:");
        System.out.println("1)Name");
        System.out.println("2)Filmography");
        System.out.println("3)Biography");
        int numberBack = alegereNumarValidCLI(3);
        if (numberBack == 1) {
            updateNameActor(a);
            toTheActorChosen(a, u, accType, "Name");
        } else if (numberBack == 2) {
            updateFilmographyActor(a);
            toTheActorChosen(a, u, accType, "Filmography");
        } else {
            updateBiography(a);
            toTheActorChosen(a, u, accType, "Biography");
        }
    }

    public void updateActorDetails(User<?> u, AccountType accType) {
        System.out.println("Here is a list of actors from system");
        int numberActors = actors.size();
        for (int i = 0; i < numberActors; i++)
            System.out.println((i + 1) + ")  " + actors.get(i).getName());
        System.out.println((numberActors + 1) + ")  Back to the menu");
        System.out.println("Please choose the number");
        int numberBack = alegereNumarValidCLI(numberActors + 1);
        if (numberBack <= numberActors) {
            System.out.println("You chose " + actors.get(numberBack - 1).getName());
            updateActorChosen(actors.get(numberBack - 1), u, accType);
        } else
            toTheMenu(u, accType);
    }

    public void printDetailsAccountAndMenuContributorCLI(Contributor<?> u) {
        System.out.println("Welcome back user " + u.getUsername());
        System.out.println("Username: " + u.getUsername());
        System.out.println("User experience: " + ((u.getExperience() == 0) ? "-" : u.getExperience()));
        System.out.println("Choose action:");
        System.out.println("\t1)  View productions details");
        System.out.println("\t2)  View actors details");
        System.out.println("\t3)  View notifications");
        System.out.println("\t4)  Search for actor/movie/series");
        System.out.println("\t5)  Add/Delete actor/movie/series to/from favorites");
        System.out.println("\t6)  Create/Remove a request");
        System.out.println("\t7)  Add/Delete actor/movie/series to/from system");
        System.out.println("\t8)  View/Solve received requests");//Sa se vada si cu ce a contribuit
        System.out.println("\t9)  Update Movie Details");
        System.out.println("\t10) Update Actor Details");
        System.out.println("\t11) Logout");
        System.out.println("\t12)  My profile");
        int number = alegereNumarValidCLI(12);
        if (number == 1)
            viewProductionsDetailsCLI(u, AccountType.CONTRIBUTOR);
        else if (number == 2)
            viewActorsDetailsCLI(u, AccountType.CONTRIBUTOR);
        else if (number == 3)
            viewNotifications(u, AccountType.CONTRIBUTOR);
        else if (number == 4)
            searchActorOrProductionInSystemCLI(u, AccountType.CONTRIBUTOR);
        else if (number == 5)
            addOrDeleteActorOrProductionFavorites(u, AccountType.CONTRIBUTOR);
        else if (number == 6)
            createRemoveRequest(u, AccountType.CONTRIBUTOR);
        else if (number == 7)
            addOrDeleteActorOrProductionSystem(u, AccountType.CONTRIBUTOR);
        else if (number == 8)
            viewSolveReceivedRequests(u, AccountType.CONTRIBUTOR);
        else if (number == 9)
            updateMovieDetails(u, AccountType.CONTRIBUTOR);
        else if (number == 10)
            updateActorDetails(u, AccountType.CONTRIBUTOR);
        else if (number == 11)
            u.logoutCLI();
        else
            printMyProfile(u, AccountType.CONTRIBUTOR);
        //TODO
    }

    public void viewAllNotifications(User<?> u, AccountType accType) {
        if (!admins.get(0).getNotifications().isEmpty())
            for (String s : admins.get(0).getNotifications())
                System.out.println(s);
        else System.out.println("No notification");
        System.out.println("\n\t1)  To the previous page");
        int numberBack = alegereNumarValidCLI(1);
        toTheMenu(u, accType);
    }

    public LocalDateTime getBirthDate() {
        Scanner scanner = new Scanner(System.in);
        LocalDateTime birthDateFormat = null;
        while (birthDateFormat == null) {
            System.out.println("BirthDate(YYYY-MM-DD): ");
            String birthDateString = scanner.nextLine();

            try {
                LocalDate birthDate = LocalDate.parse(birthDateString);
                birthDateFormat = birthDate.atStartOfDay();
            } catch (DateTimeParseException e) {
                System.out.println("Format is invalid. Try again.");
            }
        }
        return birthDateFormat;
    }

    public AccountType readAccountType() {
        Scanner scanner = new Scanner(System.in);
        AccountType accountType = AccountType.NULL;
        boolean isValid = false;

        while (!isValid) {
            System.out.println("Type of account (Regular, Contributor, Admin): ");
            String input = scanner.nextLine();

            try {
                accountType = AccountType.fromString(input);
                isValid = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid type. Try again.");
            }
        }

        return accountType;
    }

    public String generateUsername(String nameUser) {
        String username = nameUser.replaceAll("\\s+", "_").toLowerCase();
        Random random = new Random();
        int number = random.nextInt(9000) + 1000;
        return username + "_" + number;
    }

    public boolean isValidUsername(String username) {
        for (Regular<?> r : regulars)
            if (username.equals(r.getUsername()))
                return false;
        for (Contributor<?> c : contributors)
            if (username.equals(c.getUsername()))
                return false;
        for (Admin<?> a : admins)
            if (username.equals(a.getUsername()))
                return false;
        return true;
    }

    public String generateUsernameValid(String nameUser) {
        String username = generateUsername(nameUser);
        while (!isValidUsername(username))
            username = generateUsername(nameUser);
        return username;
    }

    public String generatePassword(int length) {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialCharacters = "!@#$%^&*()_+{}[]";
        String combinedChars = upperCaseLetters + lowerCaseLetters + numbers + specialCharacters;

        Random random = new Random();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            password.append(combinedChars.charAt(random.nextInt(combinedChars.length())));
        }

        return password.toString();
    }

    public void addUser(User<?> u, AccountType accType) {
        String email = readText("email: ", "email");
        String name = readText("name: ", "name");
        String country = readText("contry: ", "country");
        int age = alegereDataLansariiSauNumSeasonsCLI("age: ");
        String gender = readText("gender: ", "gender");
        LocalDateTime birthDate = getBirthDate();
        AccountType accountType = readAccountType();
        String username = generateUsernameValid(name);// Generare username
        int experience = alegereDataLansariiSauNumSeasonsCLI("experience: ");
        String password = generatePassword(14);// Generare parola de 14 caractere
        // Creeare user
        User.Information.Builder builder = new User.Information.Builder();
        builder.credentials(new User.Credentials(email, password))
                .name(name)
                .country(country)
                .age(age)
                .gender(gender)
                .birthDate(birthDate);
        User.Information infoUser = builder.build();
        // Adaugare user in lista de useri ai lui u
        User<ProductionAndActor> userNou = (User<ProductionAndActor>) UserFactory.createUser(infoUser, accountType, username, experience, new ArrayList<>(), new TreeSet<>(), new TreeSet<>());
        ((Admin<ProductionAndActor>) u).addUser(userNou);
        //
        if (userNou instanceof Regular<ProductionAndActor>) {
            regulars.add((Regular<ProductionAndActor>) userNou);
            System.out.println("User added:\n" + ((Regular<ProductionAndActor>) userNou));
        } else if (userNou instanceof Contributor<ProductionAndActor>) {
            contributors.add((Contributor<ProductionAndActor>) userNou);
            System.out.println("User added:\n" + ((Contributor<ProductionAndActor>) userNou));
        } else {
            admins.add((Admin<ProductionAndActor>) userNou);
            System.out.println("User added:\n" + ((Admin<ProductionAndActor>) userNou));
        }
        //
        System.out.println("\t1)  Back to the menu");
        int numberBack = alegereNumarValidCLI(1);
        toTheMenu(u, accType);
    }

    public void deleteRegular(User<?> u, AccountType accType) {
        System.out.println("Here is a list of users:");
        int numberRegulars = regulars.size();
        for (int i = 0; i < numberRegulars; i++)
            System.out.println((i + 1) + ")  " + regulars.get(i).getUsername());
        System.out.println((numberRegulars + 1) + ")  Back to the menu");
        int numberBack = alegereNumarValidCLI(numberRegulars + 1);
        if (numberBack <= numberRegulars) {
            Regular<?> alesul = regulars.get(numberBack - 1);
            List<Request> requestsTo = alesul.getRequestsToUsers();
            // Stergem cererile spre persoanele la care le-a trimis
            for (Request r : requestsTo) {
                User<?> destinatar = isAccountWithUsername(r.getResolverUsername());
                if (destinatar != null) {
                    if (destinatar != admins.get(0)) // Are destinatar unic
                        ((Staff<ProductionAndActor>) destinatar).removeRequestFromUser(r);
                    else // Daca e intreaga echipa de admini
                        ((Admin<ProductionAndActor>) destinatar).removeReceiveRequestHolder(r);
                }
            }
            // Stergem ratingurile date
            Map<Production, Rating> ratingsAles = alesul.getRatingsMap();
            for (Map.Entry<Production, Rating> entry : ratingsAles.entrySet()) {
                Production production = entry.getKey();
                Rating rating = entry.getValue();
                production.removeRating(rating);
                //System.out.println(production.getTitle());
                //System.out.println(rating);
                //System.out.println();
            }
            // Il stergem si daca apare in vreo lista a vreunui admin
            for (Admin<ProductionAndActor> a : ((List<Admin<ProductionAndActor>>) (List<?>) admins))
                a.removeUser((User<ProductionAndActor>) alesul);// il elimina doar daca exista
            // Il stergem pe boss din sistem
            regulars.remove(alesul);
            //
            System.out.println("User " + alesul.getUsername() + " was removed from system");
            System.out.println("\t1)  Back to the menu");
            numberBack = alegereNumarValidCLI(1);
            toTheMenu(u, accType);
        } else
            toTheMenu(u, accType);
    }

    public void deleteContributor(User<?> u, AccountType accType) {
        System.out.println("Here is a list of users:");
        int numberContributors = contributors.size();
        for (int i = 0; i < numberContributors; i++)
            System.out.println((i + 1) + ")  " + contributors.get(i).getUsername());
        System.out.println((numberContributors + 1) + ")  Back to the menu");
        int numberBack = alegereNumarValidCLI(numberContributors + 1);
        if (numberBack <= numberContributors) {
            Contributor<?> alesul = contributors.get(numberBack - 1);
            List<Request> requestsTo = alesul.getRequestsToUsers();

            // Stergem cererile spre persoanele la care le-a trimis
            for (Request r : requestsTo) {
                User<?> destinatar = isAccountWithUsername(r.getResolverUsername());
                if (destinatar != null) {
                    if (destinatar != admins.get(0)) // Are destinatar unic
                        ((Staff<ProductionAndActor>) destinatar).removeRequestFromUser(r);
                    else // Daca e intreaga echipa de admini
                        ((Admin<ProductionAndActor>) destinatar).removeReceiveRequestHolder(r);
                }
            }

            // Stergem cererile din contul persoanelor care ne au trimis
            List<Request> requestsFrom = alesul.getListOfRequestsFromUsers();
            for (Request r : requestsFrom) {
                User<?> emitator = isAccountWithUsername(r.getUserUsername());
                if (emitator != null) {
                    if (emitator instanceof Regular<?>) {
                        Regular<?> emitorR = (Regular<?>) emitator;
                        emitorR.removeRequest(r);
                    } else { //Ar trebui sa fie contributor
                        if (emitator instanceof Contributor<?>) // Are emitator unic
                            ((Contributor<ProductionAndActor>) emitator).removeRequest(r);
                    }
                }
            }

            // Mutam la echipa de admini contributiile
            List<Admin<ProductionAndActor>> adminList = (List<Admin<ProductionAndActor>>) (List<?>) admins;
            SortedSet<ProductionAndActor> addedProdOrActors = (SortedSet<ProductionAndActor>) alesul.getAddedProdOrActors();
            adminList.get(0).addSetToSet(addedProdOrActors);

            // Il stergem si daca apare in vreo lista a vreunui admin
            for (Admin<ProductionAndActor> a : ((List<Admin<ProductionAndActor>>) (List<?>) admins))
                a.removeUser((User<ProductionAndActor>) alesul);// il elimina doar daca exista

            // Il stergem pe boss din sistem
            contributors.remove(alesul);
            //
            System.out.println("User " + alesul.getUsername() + " was removed from system");
            System.out.println("\t1)  Back to the menu");
            numberBack = alegereNumarValidCLI(1);
            toTheMenu(u, accType);
        } else
            toTheMenu(u, accType);
    }

    public void deleteAdmin(User<?> u, AccountType accType) {
        System.out.println("Here is a list of users:");
        int numberAdmins = admins.size();
        for (int i = 0; i < numberAdmins; i++)
            System.out.println((i + 1) + ")  " + admins.get(i).getUsername());
        System.out.println((numberAdmins + 1) + ")  Back to the menu");
        int numberBack = alegereNumarValidCLI(numberAdmins + 1);
        if (numberBack <= numberAdmins) {
            Admin<?> alesul = admins.get(numberBack - 1);

            // Stergem cererile din contul persoanelor care ne au trimis
            List<Request> requestsFrom = alesul.getListOfRequestsFromUsers();
            for (Request r : requestsFrom) {
                User<?> emitator = isAccountWithUsername(r.getUserUsername());
                if (emitator != null) {
                    if (emitator instanceof Regular<?>) {
                        Regular<?> emitorR = (Regular<?>) emitator;
                        emitorR.removeRequest(r);
                    } else { //Ar trebui sa fie contributor
                        if (emitator instanceof Contributor<?>) // Are emitator unic
                            ((Contributor<ProductionAndActor>) emitator).removeRequest(r);
                    }
                }
            }

            // Mutam la echipa de admini contributiile
            List<Admin<ProductionAndActor>> adminList = (List<Admin<ProductionAndActor>>) (List<?>) admins;
            SortedSet<ProductionAndActor> addedProdOrActors = (SortedSet<ProductionAndActor>) alesul.getAddedProdOrActors();
            adminList.get(0).addSetToSet(addedProdOrActors);

            // Il stergem si daca apare in vreo lista a vreunui admin
            for (Admin<ProductionAndActor> a : ((List<Admin<ProductionAndActor>>) (List<?>) admins))
                a.removeUser((User<ProductionAndActor>) alesul);// il elimina doar daca exista

            // Il stergem pe boss din sistem
            admins.remove(alesul);
            //
            System.out.println("User " + alesul.getUsername() + " was removed from system");
            if (alesul == u) { // Eliminam contul curent
                System.out.println("\t1)  Back to the login");
                numberBack = alegereNumarValidCLI(1);
                CLI();
            } else {
                System.out.println("\t1)  Back to the menu");
                numberBack = alegereNumarValidCLI(1);
                toTheMenu(u, accType);
            }
        } else
            toTheMenu(u, accType);
    }

    public void deleteUser(User<?> u, AccountType accType) {
        System.out.println("\t    Choose the type of user you want to delete:");
        System.out.println("\t1)  Regular");
        System.out.println("\t2)  Contributor");
        System.out.println("\t3)  Admin");
        System.out.println("\t4)  Back to the menu");
        int numberBack = alegereNumarValidCLI(4);
        if (numberBack == 1)
            deleteRegular(u, accType);
        else if (numberBack == 2)
            deleteContributor(u, accType);
        else if (numberBack == 3)
            deleteAdmin(u, accType);
        else
            toTheMenu(u, accType);
    }

    public void viewUsers(User<?> u, AccountType accType) {
        System.out.println("Here is a list of users added:");
        List<User<ProductionAndActor>> users = ((Admin<ProductionAndActor>) u).getUsers();
        int numberUsers = users.size();
        for (int i = 0; i < numberUsers; i++)
            System.out.println((i + 1) + ")  " + users.get(i).getUsername());
        System.out.println((numberUsers + 1) + ")  Back to the menu");
        int numberBack = alegereNumarValidCLI(numberUsers + 1);
        if (numberBack <= numberUsers) {
            User<ProductionAndActor> alesul = users.get(numberBack - 1);
            if (alesul instanceof Regular<ProductionAndActor>)
                System.out.println((Regular<ProductionAndActor>) alesul);
            else if (alesul instanceof Contributor<ProductionAndActor>)
                System.out.println((Contributor<ProductionAndActor>) alesul);
            else
                System.out.println((Admin<ProductionAndActor>) alesul);
            System.out.println("\t1)  Back to the menu");
            numberBack = alegereNumarValidCLI(1);
            toTheMenu(u, accType);
        } else
            toTheMenu(u, accType);
    }

    public void addDeleteViewUser(User<?> u, AccountType accType) {
        System.out.println("Choose action:");
        System.out.println("\t1)  Add user");
        System.out.println("\t2)  Delete user");
        System.out.println("\t3)  View list of users added");
        System.out.println("\t4)  Back to the menu");
        int numberBack = alegereNumarValidCLI(4);
        if (numberBack == 1)
            addUser(u, accType);
        else if (numberBack == 2)
            deleteUser(u, accType);
        else if (numberBack == 3)
            viewUsers(u, accType);
        else
            toTheMenu(u, accType);
    }

    public void viewSolveOurReceivedRequests(User<?> u, AccountType accType) {
        Admin<?> adminBOSS = admins.get(0);
        System.out.println("\n\t1)  View received requests");
        System.out.println("\t2)  Solve received requests");
        int numberBack = alegereNumarValidCLI(2);
        if (numberBack == 1) {
            adminBOSS.printListOfRequestHolder();
            System.out.println("\t1)  Back to the menu");
            numberBack = alegereNumarValidCLI(1);
            toTheMenu(u, accType);
        } else {
            System.out.println("Please choose the request you solved/denied:");
            int nrRequests = adminBOSS.getListOfRequestsHolderFromUsers().size();
            for (int i = 0; i < nrRequests; i++)
                System.out.println((i + 1) + ")  " + adminBOSS.getListOfRequestsHolderFromUsers().get(i));
            System.out.println((nrRequests + 1) + ")  Back to the menu");
            numberBack = alegereNumarValidCLI(nrRequests + 1);
            if (numberBack <= nrRequests) {
                System.out.println(adminBOSS.getListOfRequestsHolderFromUsers().get(numberBack - 1));
                System.out.println("1)  The request was solved");
                System.out.println("2)  The request was denied");
                int numberBackBack = alegereNumarValidCLI(2);
                // Stergem cererea din lista de cereri atat de la emitator, cat si de la receptor + Notificare (Fara Experienta)
                Request r = adminBOSS.getListOfRequestsHolderFromUsers().get(numberBack - 1);
                adminBOSS.getListOfRequestsHolderFromUsers().remove(r);
                User<?> userRequest = searchRegularOrContributorWithName(r.getUserUsername());
                if (userRequest instanceof Regular<?>)// exp ? in enunt zice nu
                    ((Regular<?>) userRequest).getRequestsToUsers().remove(r);
                else
                    ((Contributor<?>) userRequest).getRequestsToUsers().remove(r);
                requests.remove(r);
                //
                if (numberBackBack == 1) {
                    System.out.println(":)))");
                    String notification = adminBOSS.getUsername() + " resolved your request " + r.getType() + " wih description " + r.getDescription();
                    adminBOSS.notifyObserver(notification, userRequest);
                    //
                    String notificationToAdmins = u.getUsername() + " resolved request " + r.getType() + " with description " + r.getDescription();
                    for (Admin<?> a : admins)
                        if (a != u)
                            adminBOSS.notifyObserver(notificationToAdmins, a);
                    //
                    userRequest.setExperienceStrategy(new RequestAccepted());
                    userRequest.updateExperience();
                } else {
                    System.out.println(":((");
                    String notification = adminBOSS.getUsername() + " rejected your request " + r.getType() + " wih description " + r.getDescription();
                    adminBOSS.notifyObserver(notification, userRequest);
                    //
                    String notificationToAdmins = u.getUsername() + " rejected request " + r.getType() + " with description " + r.getDescription();
                    for (Admin<?> a : admins)
                        if (a != u)
                            adminBOSS.notifyObserver(notificationToAdmins, a);
                }
                System.out.println("\t1)  Back to the menu");
                numberBackBack = alegereNumarValidCLI(1);
                toTheMenu(u, accType);
            } else
                toTheMenu(u, accType);
        }
    }

    public void printDetailsAccountAndMenuAdminCLI(Admin<?> u) {
        System.out.println("Welcome back user " + u.getUsername());
        System.out.println("Username: " + u.getUsername());
        System.out.println("User experience: " + ((u.getExperience() == 0) ? "-" : u.getExperience()));
        System.out.println("Choose action:");
        System.out.println("\t1)  View productions details");
        System.out.println("\t2)  View actors details");
        System.out.println("\t3)  View your notifications");//Sa vedem si notificari cu cereri pt toti adminii
        System.out.println("\t4)  Search for actor/movie/series");
        System.out.println("\t5)  Add/Delete actor/movie/series to/from favorites");
        System.out.println("\t6)  See the notifications of the entire admins team");
        //System.out.println("\t6)  Create/Remove a request");// Fara asta
        System.out.println("\t7)  Add/Delete actor/movie/series to/from system");//Sa se vada si cu ce a contribuit
        System.out.println("\t8)  View/Solve received my own requests");// Crestere experienta
        System.out.println("\t9)  Update Movie Details");
        System.out.println("\t10) Update Actor Details");
        System.out.println("\t11) Add/Delete/View a user");
        System.out.println("\t12) Logout");
        System.out.println("\t13) My profile");
        System.out.println("\t14) View/Solved received our own requests");
        int number = alegereNumarValidCLI(14);
        if (number == 1)
            viewProductionsDetailsCLI(u, AccountType.ADMIN);
        else if (number == 2)
            viewActorsDetailsCLI(u, AccountType.ADMIN);
        else if (number == 3)
            viewNotifications(u, AccountType.ADMIN);
        else if (number == 4)
            searchActorOrProductionInSystemCLI(u, AccountType.ADMIN);
        else if (number == 5)
            addOrDeleteActorOrProductionFavorites(u, AccountType.ADMIN);
        else if (number == 6)
            viewAllNotifications(u, AccountType.ADMIN);
        else if (number == 7)
            addOrDeleteActorOrProductionSystem(u, AccountType.ADMIN);
        else if (number == 8)
            viewSolveReceivedRequests(u, AccountType.ADMIN);
        else if (number == 9)
            updateMovieDetails(u, AccountType.ADMIN);
        else if (number == 10)
            updateActorDetails(u, AccountType.ADMIN);
        else if (number == 11)
            addDeleteViewUser(u, AccountType.ADMIN);
        else if (number == 12)
            u.logoutCLI();
        else if (number == 13)
            printMyProfile(u, AccountType.ADMIN);
        else
            viewSolveOurReceivedRequests(u, AccountType.ADMIN);
        //TODO
    }

    public User<?> getUser(String email, String password) {
        boolean isValidR = false, isValidC = false, isValidA = false;
        Regular<?> AccountR = null;
        Contributor<?> AccountC = null;
        Admin<?> AccountA = null;
        for (Regular<?> r : regulars)
            if (verifyAccountCLI(r, email, password)) {
                isValidR = true;
                AccountR = r;
                break;
            }
        if (isValidR == false) {
            //boolean isValidC = false;
            for (Contributor<?> c : contributors)
                if (verifyAccountCLI(c, email, password)) {
                    isValidC = true;
                    AccountC = c;
                    break;
                }
            if (isValidC == false) {
                //boolean isValidA = false;
                for (Admin<?> a : admins)
                    if (verifyAccountCLI(a, email, password)) {
                        isValidA = true;
                        AccountA = a;
                        break;
                    }
            }
        }

        if (isValidR)
            return AccountR;
        else if (isValidC)
            return AccountC;
        else if (isValidA)
            return AccountA;
        return null;
    }

    public void loginExistentAccountCLI() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your credentials:");
        System.out.print("\temail:");
        String email = scanner.nextLine().trim();
        System.out.print("\tpassword:");
        String password = scanner.nextLine().trim();

        // Cautam prin lista de conturi pe cel cu "email" si "password"
        boolean isValidR = false, isValidC = false, isValidA = false;
        Regular<?> AccountR = null;
        Contributor<?> AccountC = null;
        Admin<?> AccountA = null;
        for (Regular<?> r : regulars)
            if (verifyAccountCLI(r, email, password)) {
                isValidR = true;
                AccountR = r;
                break;
            }
        if (isValidR == false) {
            //boolean isValidC = false;
            for (Contributor<?> c : contributors)
                if (verifyAccountCLI(c, email, password)) {
                    isValidC = true;
                    AccountC = c;
                    break;
                }
            if (isValidC == false) {
                //boolean isValidA = false;
                for (Admin<?> a : admins)
                    if (verifyAccountCLI(a, email, password)) {
                        isValidA = true;
                        AccountA = a;
                        break;
                    }
            }
        }

        // Afisarea rezultatelor
        if (isValidR) {
            //System.out.println(AccountR);
            printDetailsAccountAndMenuRegularCLI(AccountR);
        } else if (isValidC) {
            //System.out.println(AccountC);
            printDetailsAccountAndMenuContributorCLI(AccountC);
        } else if (isValidA) {
            //System.out.println(AccountA);
            printDetailsAccountAndMenuAdminCLI(AccountA);
        } else {
            System.out.println("Your account is not registered in the system..");
            mesajIntrareCLI("Do you want to try again?\nDo you want to return to the authentication step?\nDo you want to choose your interface (CLI/GUI)?\n",
                    "\t1)  Try again\n\t2)  To the authentication.", "\t3)  To CLI/GUI");
            int number = alegereNumarValidCLI(3);
            if (number == 1)
                loginExistentAccountCLI();
            else if (number == 2)
                CLI();
            else
                startPlatform();
        }
    }

    public void CLI() {
        mesajIntrareCLI("Already have an account? Do you want to create a new account?",
                "\t1)  Create a new account\n\t2)  Login with an already existing account",
                "\t3)  Close app");
        int number = alegereNumarValidCLI(3);
        if (number == 1)
            createAccountCLI();
        else if (number == 2)
            loginExistentAccountCLI();
        else
            inchidereProgramSiSalvareInFisiere();
    }

    public void GUI() {
        /**
         System.out.println("The GUI mode is not yet implemented.\nIn 4 seconds you will be redirected to the previous page.");
         try {
         Thread.sleep(4000);
         } catch (InterruptedException e) {
         System.err.println("An interruption occurred during the sleep.");
         }
         startPlatform();
         */
        new LoginInterface();
    }

    public static void main(String[] args) {
        IMDB imdb = IMDB.getInstance();
        imdb.run();
    }

}
