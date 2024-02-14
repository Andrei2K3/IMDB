import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ProductionDetailsWindow extends JFrame {
    public ProductionDetailsWindow(Production production) {
        setTitle("Production Details: " + production.getTitle());
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Title: " + production.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel);

        JLabel directorsLabel = new JLabel("Directors:");
        panel.add(directorsLabel);
        JList<String> directorsList = new JList<>(production.getDirectors().toArray(new String[0]));
        JScrollPane directorsScrollPane = new JScrollPane(directorsList);
        panel.add(directorsScrollPane);

        JLabel actorsLabel = new JLabel("Actors:");
        panel.add(actorsLabel);
        JList<String> actorsList = new JList<>(production.getActors().toArray(new String[0]));
        JScrollPane actorsScrollPane = new JScrollPane(actorsList);
        panel.add(actorsScrollPane);

        JLabel genresLabel = new JLabel("Genres:");
        panel.add(genresLabel);
        List<Genre> genres = production.getGenres();
        String[] genreStrings = new String[genres.size()];
        for (int i = 0; i < genres.size(); i++) {
            genreStrings[i] = genres.get(i).toString();
        }
        JList<String> genresList = new JList<>(genreStrings);
        JScrollPane genresScrollPane = new JScrollPane(genresList);
        panel.add(genresScrollPane);

        JLabel plotLabel = new JLabel("Plot:");
        panel.add(plotLabel);
        JTextArea plotTextArea = new JTextArea(production.getPlot());
        plotTextArea.setEditable(false);
        plotTextArea.setWrapStyleWord(true);
        plotTextArea.setLineWrap(true);
        JScrollPane plotScrollPane = new JScrollPane(plotTextArea);
        panel.add(plotScrollPane);

        JLabel ratingsLabel = new JLabel("Ratings:");
        panel.add(ratingsLabel);

        DefaultTableModel ratingsTableModel = new DefaultTableModel(
                new Object[]{"Username", "Score", "Comment"}, 0);
        for (Rating rating : production.getRatings()) {
            ratingsTableModel.addRow(new Object[]{rating.getUsername(), rating.getScore(), rating.getComment()});
        }
        JTable ratingsTable = new JTable(ratingsTableModel);
        JScrollPane ratingsScrollPane = new JScrollPane(ratingsTable);
        panel.add(ratingsScrollPane);

        JLabel averageRatingLabel = new JLabel("Average Rating: " + production.getAverageRating());
        panel.add(averageRatingLabel);

        if (production instanceof Movie) {
            Movie movie = (Movie) production;
            JLabel durationLabel = new JLabel("Duration: " + movie.getDuration());
            panel.add(durationLabel);
            JLabel releaseYearLabel = new JLabel("Release Year: " + movie.getReleaseYear());
            panel.add(releaseYearLabel);
        } else if (production instanceof Series) {
            Series series = (Series) production;
            JLabel releaseYearLabel = new JLabel("Release Year: " + series.getReleaseYear());
            panel.add(releaseYearLabel);
            JLabel numSeasonsLabel = new JLabel("Number of Seasons: " + series.getNumberOfSeasons());
            panel.add(numSeasonsLabel);

            JPanel seasonsPanel = new JPanel();
            seasonsPanel.setLayout(new GridLayout(0, 1, 10, 10));

            Map<String, List<Episode>> seasons = series.getSeasons();
            for (Map.Entry<String, List<Episode>> seasonEntry : seasons.entrySet()) {
                String seasonName = seasonEntry.getKey();
                List<Episode> episodes = seasonEntry.getValue();

                JLabel seasonLabel = new JLabel("Season: " + seasonName);
                seasonsPanel.add(seasonLabel);

                for (Episode episode : episodes) {
                    JLabel episodeLabel = new JLabel("Episode: " + episode.getName() + ", Duration: " + episode.getDuration());
                    seasonsPanel.add(episodeLabel);
                }
            }

            JScrollPane scrollPane = new JScrollPane(seasonsPanel);
            panel.add(scrollPane);
        }
        getContentPane().add(panel);

        setLocationRelativeTo(null);
    }
}
