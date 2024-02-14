import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JsonParser {
    public List<Actor> parseActorsFromFile(String fileName) {
        List<Actor> actors = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(fileName)) {
            // Parsare fișier JSON
            Object obj = parser.parse(reader);
            JSONArray actorList = (JSONArray) obj;

            //Parcurgere lista de actori
            for (Object o : actorList) {
                JSONObject actorJson = (JSONObject) o;

                // Extragerea datelor si crearea obiectelor Actor
                String name = (String) actorJson.get("name");
                String biography = (String) actorJson.get("biography");
                Actor actor = new Actor(name, biography);

                JSONArray performances = (JSONArray) actorJson.get("performances");
                if (performances != null) {
                    for (Object p : performances) {
                        JSONObject performance = (JSONObject) p;
                        String title = (String) performance.get("title");
                        String type = (String) performance.get("type");

                        actor.addFilmAppearance(title, type);
                    }
                }

                actors.add(actor);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return actors;
    }

    public List<Request> parseRequestsFromFile(String fileName) {
        List<Request> requests = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(fileName)) {
            // Parsare fișier JSON
            Object obj = parser.parse(reader);
            JSONArray requestList = (JSONArray) obj;

            //Parcurgere lista de requesturi
            for (Object o : requestList) {
                JSONObject requestJson = (JSONObject) o;

                // Extragerea datelor si crearea obiectelor Request
                // tip
                String typeString = (String) requestJson.get("type");
                RequestType type = RequestType.NULL;
                if (typeString != null)
                    type = RequestType.fromString(typeString);
                // data
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                String stringLocalDateTime = (String) requestJson.get("createdDate");
                LocalDateTime localDateTime = null;
                if (stringLocalDateTime != null)
                    localDateTime = LocalDateTime.parse(stringLocalDateTime, formatter);
                // titlu sau nume
                String title = (String) requestJson.get("movieTitle");
                String name = (String) requestJson.get("actorName");
                String titleOrName = title;
                if (titleOrName == null)
                    titleOrName = name;
                // descriere
                String description = (String) requestJson.get("description");
                // user-ul ce face gat
                String userUsername = (String) requestJson.get("username");
                // user-ul la care se face gat
                String resolverUsername = (String) requestJson.get("to");
                //
                Request request = new Request(type, localDateTime, titleOrName, description, userUsername, resolverUsername);

                requests.add(request);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return requests;
    }


    public void parseAccountsFromFile(String fileName, List<Regular<?>> regulars, List<Contributor<?>> contributors, List<Admin<?>> admins, List<Actor> actors, List<Movie> movies, List<Series> series) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(fileName)) {
            // Parsare fișier JSON
            Object obj = parser.parse(reader);
            JSONArray accountsList = (JSONArray) obj;
            //Parcurgere lista de accounts
            for (Object o : accountsList) {
                JSONObject accountJson = (JSONObject) o;

                //username
                String username = (String) accountJson.get("username");
                //experience
                String experienceString = (String) accountJson.get("experience");
                int experience = 0;
                if (experienceString != null)
                    experience = Integer.parseInt(experienceString);

                //information
                JSONObject info = (JSONObject) accountJson.get("information");
                JSONObject cred = (JSONObject) info.get("credentials");
                //--credentials
                String email = (String) cred.get("email");
                String password = (String) cred.get("password");
                //info in continuare
                String name = (String) info.get("name");
                String country = (String) info.get("country");
                Number ageNumber = (Number) info.get("age");
                int age = 0;
                if (ageNumber != null)
                    age = ageNumber.intValue();
                String gender = (String) info.get("gender");
                //data nasterii
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String stringLocalDate = (String) info.get("birthDate");
                LocalDateTime localDateTime = null;
                if (stringLocalDate != null) {
                    LocalDate localDate = LocalDate.parse(stringLocalDate, formatter);
                    localDateTime = localDate.atStartOfDay();
                }
                //userType
                String typeString = (String) accountJson.get("userType");
                AccountType userType = AccountType.NULL;
                if (typeString != null) {
                    userType = AccountType.fromString(typeString);
                    if(userType == AccountType.ADMIN)
                        experience = 9999;
                }

                ///Actorii/Productiile adaugate de utilizator
                SortedSet<ProductionAndActor> contributorsProdAndActor = new TreeSet<>();
                //productionsContribution
                JSONArray productionsContribution = (JSONArray) accountJson.get("productionsContribution");
                if (productionsContribution != null)
                    for (Object p : productionsContribution) {
                        String productionContribution = (String) p;
                        //???
                        for (Movie movie : movies)
                            if (productionContribution.equals(movie.getTitle())) {
                                contributorsProdAndActor.add(movie);
                                break;
                            }
                        for (Series serie : series)
                            if (productionContribution.equals(serie.getTitle())) {
                                contributorsProdAndActor.add(serie);
                                break;
                            }
                    }
                //actorsContribution
                JSONArray actorsContribution = (JSONArray) accountJson.get("actorsContribution");
                if (actorsContribution != null) {
                    for (Object p : actorsContribution) {
                        String actorContribution = (String) p;
                        //???
                        for (Actor actor : actors)
                            if (actorContribution.equals(actor.getName())) {
                                contributorsProdAndActor.add(actor);
                                break;
                            }
                    }
                }

                ///Actorii/Productiile preferate ai utilizatorului
                SortedSet<ProductionAndActor> favorites = new TreeSet<>();
                //favoriteProductions
                JSONArray favoriteProductions = (JSONArray) accountJson.get("favoriteProductions");
                if (favoriteProductions != null)
                    for (Object p : favoriteProductions) {
                        String favoriteProduction = (String) p;
                        //???
                        for (Movie movie : movies)
                            if (favoriteProduction.equals(movie.getTitle())) {
                                favorites.add(movie);
                                break;
                            }
                        for (Series serie : series)
                            if (favoriteProduction.equals(serie.getTitle())) {
                                favorites.add(serie);
                                break;
                            }
                    }
                //favoriteActors
                JSONArray favoriteActors = (JSONArray) accountJson.get("favoriteActors");
                if (favoriteActors != null) {
                    for (Object p : favoriteActors) {
                        String favoriteActor = (String) p;
                        //???
                        for (Actor actor : actors)
                            if (favoriteActor.equals(actor.getName())) {
                                favorites.add(actor);
                                break;
                            }
                    }
                }

                //notifications
                JSONArray notificationsJSON = (JSONArray) accountJson.get("notifications");
                List<String> notifications = new ArrayList<>();
                if (notificationsJSON != null) {
                    for (Object p : notificationsJSON) {
                        String notification = (String) p;
                        //???
                        notifications.add(notification);
                    }
                }

                ///Crearea efectiva a userilor
                User.Information.Builder builder = new User.Information.Builder();
                builder.credentials(new User.Credentials(email, password))
                        .name(name)
                        .country(country)
                        .age(age)
                        .gender(gender)
                        .birthDate(localDateTime);

                User.Information infoUser = builder.build();
                /**
                 if (userType == AccountType.REGULAR)
                 regulars.add(new Regular<>(infoUser, userType, username, experience, notifications, favorites));
                 else if (userType == AccountType.CONTRIBUTOR)
                 contributors.add(new Contributor<>(infoUser, userType, username, experience, notifications, favorites, contributorsProdAndActor));
                 else if (userType == AccountType.ADMIN)
                 admins.add(new Admin<>(infoUser, userType, username, experience, notifications, favorites, contributorsProdAndActor));
                 */
                User<?> userNou = UserFactory.createUser(infoUser, userType, username, experience, notifications, favorites, contributorsProdAndActor);
                if (userType == AccountType.REGULAR)
                    regulars.add((Regular<?>) userNou);
                else if (userType == AccountType.CONTRIBUTOR)
                    contributors.add((Contributor<?>) userNou);
                else if (userType == AccountType.ADMIN)
                    admins.add((Admin<?>) userNou);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseProductionsFromFile(String fileName, List<Movie> movies, List<Series> series, List<Actor> actorsIMDB, Admin<ProductionAndActor> admin0) {
        //Le fac in constructorul lui IMDB
        //movies = new ArrayList<>();
        //series = new ArrayList<>();
        List<Actor> actoriNoi = new ArrayList<>();// lista asta va fi concatenata la actorsIMDB

        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(fileName)) {
            // Parsare fișier JSON
            Object obj = parser.parse(reader);
            JSONArray productionList = (JSONArray) obj;

            //Parcurgere lista de productii
            for (Object o : productionList) {
                JSONObject productionJson = (JSONObject) o;

                // Extragerea datelor si crearea obiectelor production

                String title = (String) productionJson.get("title");
                String typeProduction = (String) productionJson.get("type");

                // Directori
                JSONArray directorsJSON = (JSONArray) productionJson.get("directors");
                List<String> directors = new ArrayList<>();
                for (Object d : directorsJSON) {
                    String director = (String) d;
                    directors.add(director);
                }

                // Actori
                JSONArray actorsJSON = (JSONArray) productionJson.get("actors");
                List<String> actors = new ArrayList<>();
                for (Object a : actorsJSON) {
                    String actor = (String) a;
                    actors.add(actor);
                }

                // Genuri
                JSONArray genresJSON = (JSONArray) productionJson.get("genres");
                List<Genre> genres = new ArrayList<>();
                for (Object g : genresJSON) {
                    String genreString = (String) g;
                    Genre genre = Genre.NULL;
                    if (genreString != null)
                        genre = Genre.fromString(genreString);
                    genres.add(genre);
                }

                // Ratings
                JSONArray ratingsJSON = (JSONArray) productionJson.get("ratings");
                List<Rating> ratings = new ArrayList<>();
                for (Object r : ratingsJSON) {
                    JSONObject ratingJSON = (JSONObject) r;
                    String username = (String) ratingJSON.get("username");
                    Number ratingNumber = (Number) ratingJSON.get("rating");
                    int rating = 0;
                    if (ratingNumber != null)
                        rating = ratingNumber.intValue();
                    String comment = (String) ratingJSON.get("comment");
                    ratings.add(new Rating(username, rating, comment));
                }

                // Plot, averageRating, duration, releaseYear, numSeasons
                String plot = (String) productionJson.get("plot");
                Number averageRatingNumber = (Number) productionJson.get("averageRating");
                Double averageRating = 0.0;
                if (averageRatingNumber != null)
                    averageRating = averageRatingNumber.doubleValue();
                String duration = (String) productionJson.get("duration");
                Number releaseYearNumber = (Number) productionJson.get("releaseYear");
                int releaseYear = 0;
                if (releaseYearNumber != null)
                    releaseYear = releaseYearNumber.intValue();
                Number numSeasonsNumber = (Number) productionJson.get("numSeasons");
                int numSeasons = 0;
                if (numSeasonsNumber != null)
                    numSeasons = numSeasonsNumber.intValue();

                //
                JSONObject seasonsJSON = (JSONObject) productionJson.get("seasons");
                Map<String, List<Episode>> seasons = new TreeMap<>();//??
                if (seasonsJSON != null) {
                    for (Object seasonKey : seasonsJSON.keySet()) {
                        String seasonName = (String) seasonKey;
                        JSONArray episodesJSON = (JSONArray) seasonsJSON.get(seasonName);

                        List<Episode> episodes = new ArrayList<>();
                        for (Object e : episodesJSON) {
                            JSONObject episodeJSON = (JSONObject) e;
                            String episodeName = (String) episodeJSON.get("episodeName");
                            String episodeDuration = (String) episodeJSON.get("duration");
                            episodes.add(new Episode(episodeName, episodeDuration));
                        }
                        seasons.put(seasonName, episodes);
                    }
                }

                if (typeProduction != null)//Verificam daca avem productie valida
                {
                    if (typeProduction.equals("Movie")) {
                        for (String actor : actors) {
                            Boolean ok = false;
                            for (Actor Actorr : actorsIMDB)
                                if (actor.equals(Actorr.getName())) {
                                    ok = true;
                                    break;
                                }
                            if (!ok) {
                                //System.out.println(actor);
                                Boolean okk = false;
                                for (Actor Actorr : actoriNoi)
                                    if (actor.equals(Actorr.getName())) {
                                        Actorr.addFilmAppearance(title, "Movie");
                                        okk = true;
                                        break;
                                    }
                                if (!okk) {
                                    Actor newActor = new Actor(actor, null);
                                    newActor.addFilmAppearance(title, "Movie");
                                    actoriNoi.add(newActor);
                                }
                            }
                        }
                        movies.add(new Movie(title, directors, actors, genres, ratings, plot, averageRating, duration, releaseYear));
                    } else if (typeProduction.equals("Series")) {
                        for (String actor : actors) {
                            Boolean ok = false;
                            for (Actor Actorr : actorsIMDB)
                                if (actor.equals(Actorr.getName())) {
                                    ok = true;
                                    break;
                                }
                            if (!ok) {
                                //System.out.println(actor);
                                Boolean okk = false;
                                for (Actor Actorr : actoriNoi)
                                    if (actor.equals(Actorr.getName())) {
                                        Actorr.addFilmAppearance(title, "Series");
                                        okk = true;
                                        break;
                                    }
                                if (!okk) {
                                    Actor newActor = new Actor(actor, null);
                                    newActor.addFilmAppearance(title, "Series");
                                    actoriNoi.add(newActor);
                                }
                            }
                        }
                        series.add(new Series(title, directors, actors, genres, ratings, plot, averageRating, releaseYear, numSeasons, seasons));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Actor a : actoriNoi)
            admin0.addActorSystem(a);
        actorsIMDB.addAll(actoriNoi);

        //for (Movie movie : movies)
        //    System.out.println(movie);
        //for (Series serie : series)
        //    System.out.println(serie);
    }

}
