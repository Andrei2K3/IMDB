import java.util.List;
import java.util.Map;

public class Series extends Production {
    private int releaseYear;
    private int numSeasons;
    private Map<String, List<Episode>> seasons;

    public Series(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String plot, Double averageRating, int releaseYear, int numberOfSeasons, Map<String, List<Episode>> seasons) {
        super(title, directors, actors, genres, ratings, plot, averageRating);
        this.releaseYear = releaseYear;
        this.numSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    public Series(String title, List<String> directors, List<String> actors, List<Genre> genres, String plot, int releaseYear, int numberOfSeasons, Map<String, List<Episode>> seasons) {
        super(title, directors, actors, genres, plot);
        this.releaseYear = releaseYear;
        this.numSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    public int compareTo(ProductionAndActor other) {
        if (other instanceof Series) {
            Series otherSeries = (Series) other;
            return this.title.compareTo(otherSeries.title);
        }
        return this.getClass().getName().compareTo(other.getClass().getName());
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getNumberOfSeasons() {
        return numSeasons;
    }

    public void setNumberOfSeasons(int numSeasons) {
        this.numSeasons = numSeasons;
    }

    public Map<String, List<Episode>> getSeasons() {
        return seasons;
    }

    public void setSeasons(Map<String, List<Episode>> seasons) {
        this.seasons = seasons;
    }

    @Override
    public void displayInfo() {}

    @Override
    public String toString() {
        return "{  Series\n" +
                "  \"title\": \"" + title + "\",\n" +
                "  \"directors\": " + directors.toString() + ",\n" +
                "  \"actors\": " + actors.toString() + ",\n" +
                "  \"genres\": " + genres.toString() + ",\n" +
                "  \"ratings\": " + ratings.toString() + ",\n" +
                "  \"plot\": \"" + plot + "\",\n" +
                "  \"averageRating\": " + averageRating + ",\n" +
                "  \"releaseYear\": " + releaseYear + ",\n" +
                "  \"numSeasons\": " + numSeasons + ",\n" +
                "  \"seasons\": " + seasons.toString() + "\n" +
                "}";
    }

}
