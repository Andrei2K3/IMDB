import java.util.ArrayList;
import java.util.List;

public abstract class Production implements ProductionAndActor {
    protected String title;
    protected List<String> directors;
    protected List<String> actors;
    protected List<Genre> genres;
    protected List<Rating> ratings;
    protected String plot;
    protected Double averageRating;


    public Production(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String plot, Double averageRating) {
        this.title = title;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.plot = plot;
        this.averageRating = averageRating;
    }

    public Production(String title, List<String> directors, List<String> actors, List<Genre> genres, String plot) {
        this.title = title;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.plot = plot;
        this.ratings = new ArrayList<>();
        this.averageRating = 0.0;
    }

    @Override
    public int compareTo(ProductionAndActor other) {
        if (other instanceof Production) {
            Production otherProduction = (Production) other;
            return this.title.compareTo(otherProduction.title);
        }
        return this.getClass().getName().compareTo(other.getClass().getName());
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getDirectors(){
        return directors;
    }

    public void setDirectors(List<String> directors)
    {
        this.directors = directors;
    }

    public Double getAverageRating()
    {
        return averageRating;
    }
    public List<String> getActors()
    {
        return this.actors;
    }

    public String getPlot()
    {
        return plot;
    }

    public List<Rating> getRatings()
    {
        return ratings;
    }
    public void setActors(List<String> actors)
    {
        this.actors = actors;
    }

    public void setGenres(List<Genre> genres)
    {
        this.genres = genres;
    }

    public List<Genre> getGenres()
    {
        return genres;
    }

    public void setPlot(String plot)
    {
        this.plot = plot;
    }

    public String getTitle() {
        return title;
    }

    public List<Rating> ratingList() {
        return ratings;
    }

    public abstract void displayInfo();

    // Alte metode comune
    // Metoda pentru utilizatorul Regular cand adauga rating acesta
    public boolean didUserAddRating(String username) {
        for (Rating r : ratings)
            if (username.equals(r.getUsername()))
                return true;
        return false;
    }

    public void addActor(String name)
    {
        actors.add(name);
    }

    public void removeActor(String name) {
        actors.remove(name);
    }

    public void removeRating(Rating rating)
    {
        ratings.remove(rating);
    }

    public void addRating(Rating rating) {
        ratings.add(rating);
        updateAverageRating();

    }


    // Actualizarea ratingului mediu
    private void updateAverageRating() {
        double total = 0;
        for (Rating r : ratings) {
            total += r.getScore();
        }
        averageRating = ratings.isEmpty() ? 0.0 : total / ratings.size();
    }
}
