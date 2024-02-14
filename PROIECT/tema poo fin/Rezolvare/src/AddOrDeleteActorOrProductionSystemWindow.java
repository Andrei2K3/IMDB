import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class AddOrDeleteActorOrProductionSystemWindow extends JFrame {
    private User<?> user;
    private IMDB imdb;

    public AddOrDeleteActorOrProductionSystemWindow(User<?> user) {
        this.user = user;
        this.imdb = IMDB.getInstance();

        setTitle("Add or Remove Actor/Production from System");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Remove");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddOptions();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDeleteOptions();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    private void showAddOptions() {
        JDialog addDialog = new JDialog(this, "Add Actor/Production", true);
        addDialog.setSize(400, 300);
        addDialog.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1));

        JButton addActorButton = new JButton("Add Actor");
        JButton addProductionButton = new JButton("Add Production");

        addActorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog addActorDialog = new JDialog(AddOrDeleteActorOrProductionSystemWindow.this, "Add Actor", true);
                addActorDialog.setSize(400, 300);
                addActorDialog.setLayout(new BorderLayout());

                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new GridLayout(4, 1));


                JTextField nameField = new JTextField();
                JTextArea biographyArea = new JTextArea();
                JTextArea moviesArea = new JTextArea();
                JButton addButton = new JButton("Add Actor");

                mainPanel.add(new JLabel("Name:"));
                mainPanel.add(nameField);
                mainPanel.add(new JLabel("Biography:"));
                mainPanel.add(new JScrollPane(biographyArea));
                mainPanel.add(new JLabel("Production in which he appeared (one per line):"));
                mainPanel.add(new JScrollPane(moviesArea));
                mainPanel.add(addButton);

                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        String name = nameField.getText();
                        String biography = biographyArea.getText();
                        String[] moviesArray = moviesArea.getText().split("\n");
                        List<String> moviesList = Arrays.asList(moviesArray);

                        if (imdb.isActorInSystem(name)) {
                            JOptionPane.showMessageDialog(addActorDialog, "Actor already added.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            Actor actor = new Actor(name, biography);
                            for (String movieTitle : moviesList) {
                                Production production = imdb.isProductionInSystem(movieTitle);
                                if (production != null) {
                                    actor.addFilmAppearance(movieTitle, production instanceof Movie ? "Movie" : "Series");
                                }
                            }

                            imdb.getActors().add(actor);
                            //
                            Staff<ProductionAndActor> staff = (Staff<ProductionAndActor>) user;
                            staff.addActorSystem(actor);
                            // Crestere experienta
                            if (user instanceof Contributor<?>) { // Adminii au experienta infinita
                                user.setExperienceStrategy(new AddProductionOrActor());
                                user.updateExperience();
                            }
                            //
                            JOptionPane.showMessageDialog(addActorDialog, "Actor succesfully added.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            addActorDialog.dispose();
                        }
                    }
                });

                addActorDialog.add(mainPanel, BorderLayout.CENTER);
                addActorDialog.setVisible(true);
            }

        });

        addProductionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog addProductionDialog = new JDialog(AddOrDeleteActorOrProductionSystemWindow.this, "Add Production", true);
                addProductionDialog.setSize(600, 400);
                addProductionDialog.setLayout(new BorderLayout());

                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new GridLayout(10, 2));

                JTextField titleField = new JTextField();
                JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Movie", "Series"});
                JTextField directorsField = new JTextField();
                JTextField actorsField = new JTextField();
                JTextField genresField = new JTextField();
                JTextArea plotArea = new JTextArea();
                JTextField releaseYearField = new JTextField();
                JTextField durationField = new JTextField();
                JTextField numSeasonsField = new JTextField();

                JButton addButton = new JButton("Add Production");

                mainPanel.add(new JLabel("Title:"));
                mainPanel.add(titleField);
                mainPanel.add(new JLabel("Type:"));
                mainPanel.add(typeComboBox);
                mainPanel.add(new JLabel("Directors (comma-separated):"));
                mainPanel.add(directorsField);
                mainPanel.add(new JLabel("Actors (comma-separated):"));
                mainPanel.add(actorsField);
                mainPanel.add(new JLabel("Genres (comma-separated):"));
                mainPanel.add(genresField);
                mainPanel.add(new JLabel("Plot:"));
                mainPanel.add(new JScrollPane(plotArea));
                mainPanel.add(new JLabel("Release Year:"));
                mainPanel.add(releaseYearField);
                mainPanel.add(new JLabel("Number of Seasons (only for Series):"));
                mainPanel.add(numSeasonsField);
                mainPanel.add(new JLabel("Duration:"));
                mainPanel.add(durationField);
                mainPanel.add(addButton);

                typeComboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String selectedType = (String) typeComboBox.getSelectedItem();
                        if ("Movie".equals(selectedType)) {
                            numSeasonsField.setVisible(false);
                            durationField.setVisible(true);
                        } else if ("Series".equals(selectedType)) {
                            durationField.setVisible(false);
                            numSeasonsField.setVisible(true);
                        }
                    }
                });

                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        String title = titleField.getText();
                        String type = (String) typeComboBox.getSelectedItem();
                        String directors = directorsField.getText();
                        String actors = actorsField.getText();
                        String genres = genresField.getText();
                        String plot = plotArea.getText();
                        String releaseYear = releaseYearField.getText();
                        String numSeasons = numSeasonsField.getText();
                        String duration = durationField.getText();

                        if (imdb.isProductionInSystem(title) != null) {
                            JOptionPane.showMessageDialog(addProductionDialog, "Actor already added.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            //
                            String[] directorArray = directors.split(",");
                            List<String> directorsProd = new ArrayList<>();
                            for (String director : directorArray) {
                                directorsProd.add(director.trim());
                            }
                            String[] actorArray = actors.split(",");
                            List<String> actorsProd = new ArrayList<>();
                            for (String actor : actorArray) {
                                if (imdb.isActorInSystem(actor.trim()))
                                    actorsProd.add(actor.trim());
                                else
                                    JOptionPane.showMessageDialog(addDialog, "Actor is not in system: " + actor, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            String[] genreArray = genres.split(",");
                            List<String> genresProd = new ArrayList<>();
                            for (String genre : genreArray) {
                                genresProd.add(genre.trim());
                            }
                            List<Genre> genresProdG = new ArrayList<>();
                            for (String genre : genresProd) {
                                try {
                                    Genre genreEnum = Genre.fromString(genre);
                                    genresProdG.add(genreEnum);
                                } catch (IllegalArgumentException ex) {
                                    JOptionPane.showMessageDialog(addDialog, "Invalid genre: " + genre, "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }

                            int releaseYearProd = 0;
                            try {
                                releaseYearProd = Integer.parseInt(releaseYear);
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(addDialog, "Invalid release year: " + releaseYear, "Error", JOptionPane.ERROR_MESSAGE);

                            }

                            if ("Movie".equals(type)) {

                                Movie newMovie = new Movie(title, directorsProd, actorsProd, genresProdG, plot, duration, releaseYearProd);
                                imdb.getMovies().add(newMovie);
                                Staff<ProductionAndActor> staff = (Staff<ProductionAndActor>) user;
                                staff.addProductionSystem(newMovie);
                                // Crestere experienta
                                if (user instanceof Contributor<?>) { // Adminii au experienta infinita
                                    user.setExperienceStrategy(new AddProductionOrActor());
                                    user.updateExperience();
                                }
                            } else if ("Series".equals(type)) {
                                int numSeasonsInt = Integer.parseInt(numSeasons);
                                Map<String, List<Episode>> seasons = new TreeMap<>();
                                for (int i = 1; i <= numSeasonsInt; i++) {
                                    String seasonName = "Season " + i;
                                    String episodesText = JOptionPane.showInputDialog(addProductionDialog, "Introduce number of episodes for " + seasonName + ":");
                                    int numEpisodes = Integer.parseInt(episodesText);
                                    List<Episode> episodes = new ArrayList<>();
                                    for (int j = 1; j <= numEpisodes; j++) {
                                        String episodeName = JOptionPane.showInputDialog(addProductionDialog, "Introduce episode name for " + seasonName + " - Episode " + j + ":");
                                        String episodeDuration = JOptionPane.showInputDialog(addProductionDialog, "Introduce episode duration for " + seasonName + " - Episode " + j + ":");
                                        episodes.add(new Episode(episodeName, episodeDuration));
                                    }
                                    seasons.put(seasonName, episodes);
                                }
                                Series newSeries = new Series(title, directorsProd, actorsProd, genresProdG, plot, releaseYearProd, numSeasonsInt, seasons);
                                imdb.getSeries().add(newSeries);
                                Staff<ProductionAndActor> staff = (Staff<ProductionAndActor>) user;
                                staff.addProductionSystem(newSeries);
                                // Crestere experienta
                                if (user instanceof Contributor<?>) { // Adminii au experienta infinita
                                    user.setExperienceStrategy(new AddProductionOrActor());
                                    user.updateExperience();
                                }
                            }
                            JOptionPane.showMessageDialog(addProductionDialog, "Production succesfully added.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            addProductionDialog.dispose();
                        }
                    }
                });

                durationField.setVisible(false);
                numSeasonsField.setVisible(false);

                addProductionDialog.add(mainPanel, BorderLayout.CENTER);
                addProductionDialog.setVisible(true);
            }
        });


        mainPanel.add(addActorButton);
        mainPanel.add(addProductionButton);

        addDialog.add(mainPanel, BorderLayout.CENTER);
        addDialog.setVisible(true);
    }


    private void showDeleteOptions() {
        JDialog deleteDialog = new JDialog(this, "Delete Actor/Production", true);
        deleteDialog.setSize(400, 300);
        deleteDialog.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1));

        JButton deleteActorButton = new JButton("Delete Actor");
        JButton deleteProductionButton = new JButton("Delete Production");

        deleteActorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actorName = JOptionPane.showInputDialog(deleteDialog, "Enter the name of the actor to delete:");
                Contributor<?> contributor = null;
                Admin<?> admin = null;
                if (user instanceof Contributor<?>) {
                    contributor = (Contributor<?>) user;
                    Actor actorRemoved = null;
                    for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) contributor.getAddedProdOrActors())
                        if (paa instanceof Actor && ((Actor) paa).getName().equals(actorName))
                            actorRemoved = (Actor) paa;
                    if (actorRemoved != null) {
                        JOptionPane.showMessageDialog(deleteDialog, "Actor deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        imdb.getActors().remove(actorRemoved);
                        // Stergere din lista user ului de la contributori
                        contributor.removeActorSystem(actorRemoved);
                        // Stergere din productii
                        for (Movie m : imdb.getMovies())
                            m.removeActor(actorRemoved.getName());
                        for (Series s : imdb.getSeries())
                            s.removeActor(actorRemoved.getName());
                    } else
                        JOptionPane.showMessageDialog(deleteDialog, "The actor was not found or you do not have permission to delete it.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    admin = (Admin<?>) user;
                    Admin<?> gasit = null;
                    Actor actorRemoved = null;
                    for (Admin<?> adminss : imdb.getAdmins())
                        for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) adminss.getAddedProdOrActors())
                            if (paa instanceof Actor && ((Actor) paa).getName().equals(actorName)) {
                                actorRemoved = (Actor) paa;
                                gasit = adminss;
                            }
                    if (actorRemoved != null) {
                        JOptionPane.showMessageDialog(deleteDialog, "Actor deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        imdb.getActors().remove(actorRemoved);
                        // Stergere din lista user ului de la contributori
                        gasit.removeActorSystem(actorRemoved);
                        // Stergere din productii
                        for (Movie m : imdb.getMovies())
                            m.removeActor(actorRemoved.getName());
                        for (Series s : imdb.getSeries())
                            s.removeActor(actorRemoved.getName());
                    } else
                        JOptionPane.showMessageDialog(deleteDialog, "The actor was not found or you do not have permission to delete it.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteProductionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String productionTitle = JOptionPane.showInputDialog(deleteDialog, "Enter the title of the production to delete:");
                Contributor<?> contributor = null;
                Admin<?> admin = null;
                if (user instanceof Contributor<?>) {
                    contributor = (Contributor<?>) user;
                    Production productionDeleted = null;
                    for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) contributor.getAddedProdOrActors())
                        if (paa instanceof Production && ((Production) paa).getTitle().equals(productionTitle))
                            productionDeleted = (Production) paa;
                    if (productionDeleted != null) {
                        JOptionPane.showMessageDialog(deleteDialog, "Production deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        if (productionDeleted instanceof Movie)
                            imdb.getMovies().remove(productionDeleted);
                        else
                            imdb.getSeries().remove(productionDeleted);
                        // Stergere din lista user ului de la contributori
                        contributor.removeProductionSystem(productionDeleted);
                        // Stergere de la actori
                        for (Actor a : imdb.getActors())
                            a.removeFilmAppearanceByName(productionDeleted.getTitle());
                        // Stergere din lista rating urilor userilor regular
                        for (Regular<?> r : imdb.getRegulars())
                            r.removeProductionSystemWithRatingAndAll(productionDeleted);
                    } else
                        JOptionPane.showMessageDialog(deleteDialog, "The production was not found or you do not have permission to delete it.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    admin = (Admin<?>) user;
                    Production productionDeleted = null;
                    Admin<?> gasit = null;
                    for (Admin<?> adminss : imdb.getAdmins())
                        for (ProductionAndActor paa : (SortedSet<ProductionAndActor>) contributor.getAddedProdOrActors())
                            if (paa instanceof Production && ((Production) paa).getTitle().equals(productionTitle)) {
                                productionDeleted = (Production) paa;
                                gasit = adminss;
                            }
                    if (productionDeleted != null) {
                        JOptionPane.showMessageDialog(deleteDialog, "Production deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        if (productionDeleted instanceof Movie)
                            imdb.getMovies().remove(productionDeleted);
                        else
                            imdb.getSeries().remove(productionDeleted);
                        // Stergere din lista user ului de la contributori
                        gasit.removeProductionSystem(productionDeleted);
                        // Stergere de la actori
                        for (Actor a : imdb.getActors())
                            a.removeFilmAppearanceByName(productionDeleted.getTitle());
                        // Stergere din lista rating urilor userilor regular
                        for (Regular<?> r : imdb.getRegulars())
                            r.removeProductionSystemWithRatingAndAll(productionDeleted);
                    } else
                        JOptionPane.showMessageDialog(deleteDialog, "The production was not found or you do not have permission to delete it.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        mainPanel.add(deleteActorButton);
        mainPanel.add(deleteProductionButton);

        deleteDialog.add(mainPanel, BorderLayout.CENTER);
        deleteDialog.setVisible(true);
    }

}
