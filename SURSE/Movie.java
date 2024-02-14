import java.util.List;

public class Movie extends Production {
    private String duration;
    private int releaseYear;

    public Movie(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String plot, Double averageRating, String duration, int releaseYear) {
        super(title, directors, actors, genres, ratings, plot, averageRating);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    public Movie(String title, List<String> directors, List<String> actors, List<Genre> genres, String plot, String duration, int releaseYear) {
        super(title, directors, actors, genres, plot);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    public int compareTo(ProductionAndActor other) {
        if (other instanceof Movie) {
            Movie otherMovie = (Movie) other;
            return this.title.compareTo(otherMovie.title);
        }
        return this.getClass().getName().compareTo(other.getClass().getName());
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public void displayInfo() {
    }

    @Override
    public String toString() {
        return "{  Movie\n" +
                "  \"title\": \"" + title + "\",\n" +
                "  \"directors\": " + directors.toString() + ",\n" +
                "  \"actors\": " + actors.toString() + ",\n" +
                "  \"genres\": " + genres.toString() + ",\n" +
                "  \"ratings\": " + ratings.toString() + ",\n" +
                "  \"plot\": \"" + plot + "\",\n" +
                "  \"averageRating\": " + averageRating + ",\n" +
                "  \"duration\": \"" + duration + "\",\n" +
                "  \"releaseYear\": " + releaseYear + "\n" +
                "}";
    }

}
