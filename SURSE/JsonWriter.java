import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

public class JsonWriter {

    public void writeActorsToFile(List<Actor> actors, String fileName) throws IOException {
        JSONArray actorList = new JSONArray();
        for (Actor actor : actors) {
            JSONObject actorJson = new JSONObject();
            actorJson.put("name", actor.getName());

            JSONArray performancesJson = new JSONArray();
            for (Actor.FilmAppearance performance : actor.getFilmography()) {
                JSONObject performanceJson = new JSONObject();
                performanceJson.put("title", performance.getName());
                performanceJson.put("type", performance.getType());
                performancesJson.add(performanceJson);
            }
            actorJson.put("performances", performancesJson);

            actorJson.put("biography", actor.getBiography());

            actorList.add(actorJson);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(actorList.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Eroare la scriere in fisier Actors.");
        }
    }

    public void writeRequestsToFile(List<Request> requests, String fileName) throws IOException {
        JSONArray requestList = new JSONArray();
        for (Request request : requests) {
            JSONObject requestJson = new JSONObject();
            requestJson.put("type", request.getType().toString());
            if (request.getFormattedDateToJSON() != null)
                requestJson.put("createdDate", request.getFormattedDateToJSON());
            requestJson.put("username", request.getUserUsername());
            if (request.getTitleOrName() != null)
                if (request.getType() == RequestType.ACTOR_ISSUE)
                    requestJson.put("actorName", request.getTitleOrName());
                else
                    requestJson.put("movieTitle", request.getTitleOrName());
            requestJson.put("to", request.getResolverUsername());
            requestJson.put("description", request.getDescription());

            requestList.add(requestJson);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(requestList.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Eroare la scriere in fisier Request.");
        }
    }

    public void writeAccountsToFile(List<Regular<?>> regulars, List<Contributor<?>> contributors, List<Admin<?>> admins, String fileName) throws IOException {
        JSONArray accountList = new JSONArray();
        for (Regular<?> regular : regulars) {
            JSONObject userJson = new JSONObject();

            userJson.put("username", regular.getUsername());
            userJson.put("experience", String.valueOf(regular.getExperience()));

            JSONObject informationJson = new JSONObject();
            JSONObject credentialJson = new JSONObject();

            credentialJson.put("email", regular.getInfo().getCredentials().getEmail());
            credentialJson.put("password", regular.getInfo().getCredentials().getPassword());

            informationJson.put("credentials", credentialJson);
            informationJson.put("name", regular.getInfo().getName());
            informationJson.put("country", regular.getInfo().getCountry());
            informationJson.put("age", regular.getInfo().getAge());
            informationJson.put("gender", regular.getInfo().getGender());
            if (regular.getInfo().getFormattedBirthDateJSON() != null)
                informationJson.put("birthDate", regular.getInfo().getFormattedBirthDateJSON());

            userJson.put("information", informationJson);
            userJson.put("userType", "Regular");

            JSONArray favoriteProductionJson = new JSONArray();
            for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) regular.getFavorites())
                if (paa instanceof Movie || paa instanceof Series)
                    favoriteProductionJson.add(((Production) paa).getTitle());
            if (!favoriteProductionJson.isEmpty())
                userJson.put("favoriteProductions", favoriteProductionJson);

            JSONArray favoriteActorJson = new JSONArray();
            for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) regular.getFavorites())
                if (paa instanceof Actor)
                    favoriteActorJson.add(((Actor) paa).getName());
            if (!favoriteActorJson.isEmpty())
                userJson.put("favoriteActors", favoriteActorJson);

            JSONArray notificationJson = new JSONArray();
            for (String notification : regular.getNotifications())
                notificationJson.add(notification);
            if (!notificationJson.isEmpty())
                userJson.put("notifications", notificationJson);

            accountList.add(userJson);
        }

        for (Contributor<?> contributor : contributors) {
            JSONObject userJson = new JSONObject();

            userJson.put("username", contributor.getUsername());
            userJson.put("experience", String.valueOf(contributor.getExperience()));

            JSONObject informationJson = new JSONObject();
            JSONObject credentialJson = new JSONObject();

            credentialJson.put("email", contributor.getInfo().getCredentials().getEmail());
            credentialJson.put("password", contributor.getInfo().getCredentials().getPassword());

            informationJson.put("credentials", credentialJson);
            informationJson.put("name", contributor.getInfo().getName());
            informationJson.put("country", contributor.getInfo().getCountry());
            informationJson.put("age", contributor.getInfo().getAge());
            informationJson.put("gender", contributor.getInfo().getGender());
            if (contributor.getInfo().getFormattedBirthDateJSON() != null)
                informationJson.put("birthDate", contributor.getInfo().getFormattedBirthDateJSON());

            userJson.put("information", informationJson);
            userJson.put("userType", "Contributor");

            JSONArray contributionProductionJson = new JSONArray();
            for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) contributor.getAddedProdOrActors())
                if (paa instanceof Movie || paa instanceof Series)
                    contributionProductionJson.add(((Production) paa).getTitle());
            if (!contributionProductionJson.isEmpty())
                userJson.put("productionsContribution", contributionProductionJson);

            JSONArray contributionActorJson = new JSONArray();
            for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) contributor.getAddedProdOrActors())
                if (paa instanceof Actor)
                    contributionActorJson.add(((Actor) paa).getName());
            if (!contributionActorJson.isEmpty())
                userJson.put("actorsContribution", contributionActorJson);

            JSONArray favoriteProductionJson = new JSONArray();
            for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) contributor.getFavorites())
                if (paa instanceof Movie || paa instanceof Series)
                    favoriteProductionJson.add(((Production) paa).getTitle());
            if (!favoriteProductionJson.isEmpty())
                userJson.put("favoriteProductions", favoriteProductionJson);

            JSONArray favoriteActorJson = new JSONArray();
            for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) contributor.getFavorites())
                if (paa instanceof Actor)
                    favoriteActorJson.add(((Actor) paa).getName());
            if (!favoriteActorJson.isEmpty())
                userJson.put("favoriteActors", favoriteActorJson);

            JSONArray notificationJson = new JSONArray();
            for (String notification : contributor.getNotifications())
                notificationJson.add(notification);
            if (!notificationJson.isEmpty())
                userJson.put("notifications", notificationJson);

            accountList.add(userJson);
        }

        admins.subList(1, admins.size()).forEach(admin -> {
            JSONObject userJson = new JSONObject();

            userJson.put("username", admin.getUsername());
            userJson.put("experience",String.valueOf(admin.getExperience()));

            JSONObject informationJson = new JSONObject();
            JSONObject credentialJson = new JSONObject();

            credentialJson.put("email", admin.getInfo().getCredentials().getEmail());
            credentialJson.put("password", admin.getInfo().getCredentials().getPassword());

            informationJson.put("credentials", credentialJson);
            informationJson.put("name", admin.getInfo().getName());
            informationJson.put("country", admin.getInfo().getCountry());
            informationJson.put("age", admin.getInfo().getAge());
            informationJson.put("gender", admin.getInfo().getGender());
            if (admin.getInfo().getFormattedBirthDateJSON() != null)
                informationJson.put("birthDate", admin.getInfo().getFormattedBirthDateJSON());

            userJson.put("information", informationJson);
            userJson.put("userType", "Admin");

            JSONArray contributionProductionJson = new JSONArray();
            for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) admin.getAddedProdOrActors())
                if (paa instanceof Movie || paa instanceof Series)
                    contributionProductionJson.add(((Production) paa).getTitle());
            if (!contributionProductionJson.isEmpty())
                userJson.put("productionsContribution", contributionProductionJson);

            JSONArray contributionActorJson = new JSONArray();
            for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) admin.getAddedProdOrActors())
                if (paa instanceof Actor)
                    contributionActorJson.add(((Actor) paa).getName());
            if (!contributionActorJson.isEmpty())
                userJson.put("actorsContribution", contributionActorJson);

            JSONArray favoriteProductionJson = new JSONArray();
            for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) admin.getFavorites())
                if (paa instanceof Movie || paa instanceof Series)
                    favoriteProductionJson.add(((Production) paa).getTitle());
            if (!favoriteProductionJson.isEmpty())
                userJson.put("favoriteProductions", favoriteProductionJson);

            JSONArray favoriteActorJson = new JSONArray();
            for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) admin.getFavorites())
                if (paa instanceof Actor)
                    favoriteActorJson.add(((Actor) paa).getName());
            if (!favoriteActorJson.isEmpty())
                userJson.put("favoriteActors", favoriteActorJson);

            JSONArray notificationJson = new JSONArray();
            for (String notification : admin.getNotifications())
                notificationJson.add(notification);
            if (!notificationJson.isEmpty())
                userJson.put("notifications", notificationJson);

            accountList.add(userJson);
        });

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(accountList.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Eroare la scriere in fisier Account.");
        }
    }

    public void writeProductionsToFile(List<Movie> movies, List<Series> series, String fileName) throws IOException {
        JSONArray productionList = new JSONArray();
        for (Movie movie : movies) {
            JSONObject movieJson = new JSONObject();

            movieJson.put("title", movie.getTitle());
            movieJson.put("type", "Movie");

            JSONArray directorJSON = new JSONArray();
            for (String director : movie.getDirectors())
                directorJSON.add(director);
            if (!directorJSON.isEmpty())
                movieJson.put("directors", directorJSON);

            JSONArray actorsJSON = new JSONArray();
            for (String actor : movie.getActors())
                actorsJSON.add(actor);
            if (!actorsJSON.isEmpty())
                movieJson.put("actors", actorsJSON);

            JSONArray genresJSON = new JSONArray();
            for (Genre genre : movie.getGenres())
                genresJSON.add(genre.toString());
            if (!genresJSON.isEmpty())
                movieJson.put("genres", genresJSON);

            JSONArray ratingsJson = new JSONArray();
            for (Rating rating : movie.getRatings()) {
                JSONObject ratingJson = new JSONObject();
                ratingJson.put("username", rating.getUsername());
                ratingJson.put("rating", rating.getScore());
                ratingJson.put("comment", rating.getComment());
                ratingsJson.add(ratingJson);
            }
            if (!ratingsJson.isEmpty())
                movieJson.put("ratings", ratingsJson);

            movieJson.put("plot", movie.getPlot());
            movieJson.put("averageRating", movie.getAverageRating());
            movieJson.put("duration",movie.getDuration());
            movieJson.put("releaseYear",movie.getReleaseYear());

            productionList.add(movieJson);
        }

        for (Series serie : series) {
            JSONObject serieJson = new JSONObject();

            serieJson.put("title", serie.getTitle());
            serieJson.put("type", "Series");

            JSONArray directorJSON = new JSONArray();
            for (String director : serie.getDirectors())
                directorJSON.add(director);
            if (!directorJSON.isEmpty())
                serieJson.put("directors", directorJSON);

            JSONArray actorsJSON = new JSONArray();
            for (String actor : serie.getActors())
                actorsJSON.add(actor);
            if (!actorsJSON.isEmpty())
                serieJson.put("actors", actorsJSON);

            JSONArray genresJSON = new JSONArray();
            for (Genre genre : serie.getGenres())
                genresJSON.add(genre.toString());
            if (!genresJSON.isEmpty())
                serieJson.put("genres", genresJSON);

            JSONArray ratingsJson = new JSONArray();
            for (Rating rating : serie.getRatings()) {
                JSONObject ratingJson = new JSONObject();
                ratingJson.put("username", rating.getUsername());
                ratingJson.put("rating", rating.getScore());
                ratingJson.put("comment", rating.getComment());
                ratingsJson.add(ratingJson);
            }
            if (!ratingsJson.isEmpty())
                serieJson.put("ratings", ratingsJson);

            serieJson.put("plot", serie.getPlot());
            serieJson.put("averageRating", serie.getAverageRating());
            serieJson.put("releaseYear",serie.getReleaseYear());
            serieJson.put("numSeasons",serie.getNumberOfSeasons());

            JSONObject seasonsJson = new JSONObject();
            for (Map.Entry<String, List<Episode>> seasonEntry : serie.getSeasons().entrySet()) {
                String seasonName = seasonEntry.getKey();
                List<Episode> episodes = seasonEntry.getValue();

                JSONArray episodesJson = new JSONArray();
                for (Episode episode : episodes) {
                    JSONObject episodeJson = new JSONObject();
                    episodeJson.put("episodeName", episode.getName());
                    episodeJson.put("duration", episode.getDuration());
                    episodesJson.add(episodeJson);
                }

                seasonsJson.put(seasonName, episodesJson);
            }
            serieJson.put("seasons", seasonsJson);

            productionList.add(serieJson);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(productionList.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Eroare la scriere in fisier Production.");
        }
    }

}
