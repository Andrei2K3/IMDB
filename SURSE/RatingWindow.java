import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RatingWindow extends JFrame {
    private Production production;
    private List<Rating> ratings;
    private JTextArea ratingsTextArea;
    private User<?> user;
    IMDB imdb = IMDB.getInstance();

    public RatingWindow(Production production, User<?> user) {
        this.production = production;
        this.ratings = production.getRatings();
        this.user = user;

        setTitle("Ratings for " + production.getTitle());
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        ratingsTextArea = new JTextArea();
        ratingsTextArea.setEditable(false);
        ratingsTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(ratingsTextArea);
        getContentPane().add(scrollPane);

        for (Rating rating : ratings) {
            ratingsTextArea.append("Username: " + rating.getUsername() + "\n");
            ratingsTextArea.append("Rating: " + rating.getScore() + "\n");
            ratingsTextArea.append("Comment: " + rating.getComment() + "\n\n");
        }

        JButton addRatingButton = new JButton("Add Rating");
        JButton deleteRatingButton = new JButton("Delete Rating");

        addRatingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRating();
            }
        });

        deleteRatingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRating();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addRatingButton);
        buttonPanel.add(deleteRatingButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addRating() {
        if (hasUserRated()) {
            JOptionPane.showMessageDialog(this, "Rating already added");
        } else {
            JTextField scoreField = new JTextField(10);
            JTextField commentField = new JTextField(20);

            JPanel inputPanel = new JPanel(new GridLayout(2, 2));
            inputPanel.add(new JLabel("Score:"));
            inputPanel.add(scoreField);
            inputPanel.add(new JLabel("Comment:"));
            inputPanel.add(commentField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Add Rating",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    int score = Integer.parseInt(scoreField.getText());
                    String comment = commentField.getText();

                    if (score < 1 || score > 10) {
                        JOptionPane.showMessageDialog(this, "Score must be between 1 and 10", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Rating newRating = new Rating(user.getUsername(), score, comment);
                    production.addRating(newRating);
                    Regular<?> regular = (Regular<?>) user;
                    // Notificam userii care au oferit rating de aparitia unui rating nou
                    for (Rating r : ratings) {
                        String username = r.getUsername();
                        User<?> toNotify = imdb.isAccountWithUsername(username);
                        String notification = user.getUsername() + " added a new rating to " + production.getTitle();
                        if (toNotify != null)
                            user.notifyObserver(notification, toNotify);
                    }
                    // Notificam contributorul/adminul cu un rating nou
                    User<?> toNotify = imdb.isAccountContributedWithProduction(production.getTitle());
                    String notification = user.getUsername() + " added a new rating to your contribution " + production.getTitle();
                    if (toNotify != null)
                        user.notifyObserver(notification, toNotify);
                    //
                    regular.getRatingsMap().put(production, newRating);
                    if (!regular.getRemovedProductions().contains(production)) { // Daca productia e noua
                        // Crestem experienta
                        user.setExperienceStrategy(new AddRatingProduction());
                        user.updateExperience();
                    }
                    // Sortam lista de ratings a filmului dupa experienta utilizatorilor
                    for (int a = 0; a < ratings.size(); a++)
                        for (int b = a + 1; b < ratings.size(); b++)
                            if (imdb.comp(ratings.get(a), ratings.get(b))) {
                                Rating aux = ratings.get(a);
                                ratings.set(a, ratings.get(b));
                                ratings.set(b, aux);
                            }

                    refreshRatingsTextArea();
                    JOptionPane.showMessageDialog(this, "Rating successfully added.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Score must be integer", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteRating() {
        if (!hasUserRated()) {
            JOptionPane.showMessageDialog(this, "You have no added rating");
        } else {
            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the rating?", "Delete Rating",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                Rating ratingToDelete = findUserRating();

                //ratings.remove(ratingToDelete);
                //production.removeRating(ratingToDelete);
                Regular<?> regular = (Regular<?>) user;
                regular.getRatingsMap().remove(production);
                production.ratingList().remove(ratingToDelete);
                regular.getRemovedProductions().add(production);
                // Notificari la userii ce au dat rating ca si a sters rating ul user ul r
                List<Rating> ratingListProdDeleteReview = production.ratingList();
                for (Rating rating111 : ratingListProdDeleteReview) {
                    String username = rating111.getUsername();
                    User<?> toNotify = imdb.isAccountWithUsername(username);
                    String notification = regular.getUsername() + " removed the rating from " + production.getTitle();
                    if (toNotify != null)
                        user.notifyObserver(notification, toNotify);
                }
                // Notificam contributorul/adminul cu un rating nou
                User<?> toNotify = imdb.isAccountContributedWithProduction(production.getTitle());
                String notification = user.getUsername() + " removed the rating from your contribution " + production.getTitle();
                if (toNotify != null)
                    user.notifyObserver(notification, toNotify);

                refreshRatingsTextArea();
                JOptionPane.showMessageDialog(this, "Rating successfully added.");
            }
        }
    }

    private boolean hasUserRated() {
        String username = user.getUsername();
        for (Rating rating : ratings) {
            if (username.equals(rating.getUsername())) {
                return true;
            }
        }
        return false;
    }

    private Rating findUserRating() {
        String username = user.getUsername();
        for (Rating rating : ratings) {
            if (username.equals(rating.getUsername())) {
                return rating;
            }
        }
        return null;
    }

    private void refreshRatingsTextArea() {
        ratingsTextArea.setText("");
        for (Rating rating : ratings) {
            ratingsTextArea.append("Username: " + rating.getUsername() + "\n");
            ratingsTextArea.append("Rating: " + rating.getScore() + "\n");
            ratingsTextArea.append("Comment: " + rating.getComment() + "\n\n");
        }
    }
}
