import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductionsListWindow extends JFrame {
    private List<Movie> movieList;
    private List<Series> seriesList;
    private JPanel panel;
    private String DetailsOrRating;
    User<?> user;

    public ProductionsListWindow(User<?> user, List<Movie> movieList, List<Series> seriesList, String DetailsOrRating) {
        this.user = user;
        this.DetailsOrRating = DetailsOrRating;
        this.movieList = movieList;
        this.seriesList = seriesList;

        setTitle("Production Titles");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        addSortButtons();
        populateList();

        JScrollPane scrollPane = new JScrollPane(panel);
        getContentPane().add(scrollPane);

        setLocationRelativeTo(null);
    }

    private void addSortButtons() {
        JButton sortByNameButton = createStyledButton("Sort by Name");
        sortByNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortProductionsByName();
                refreshList();
            }
        });
        panel.add(sortByNameButton);

        JButton sortByDateButton = createStyledButton("Decrease sort by Average Rating ");
        sortByDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortProductionsByDate();
                refreshList();
            }
        });
        panel.add(sortByDateButton);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.WHITE);
        Border lineBorder = BorderFactory.createLineBorder(Color.BLUE, 2);
        button.setBorder(lineBorder);
        button.setOpaque(true);
        button.setBorderPainted(true);
        return button;
    }

    private void populateList() {
        for (Movie movie : movieList) {
            addButtonForProduction(movie);
        }

        for (Series series : seriesList) {
            addButtonForProduction(series);
        }
    }

    private void addButtonForProduction(Production production) {
        JButton button = new JButton(production.getTitle());
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        if ("Details".equals(DetailsOrRating))
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openProductionDetailsWindow(production);
                }
            });
        else
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openRatingWindow(production,user);
                }
            });
        panel.add(button);
    }

    private void refreshList() {
        panel.removeAll();
        addSortButtons();
        populateList();
        panel.revalidate();
        panel.repaint();
    }

    private void sortProductionsByName() {
        Comparator<Production> byName = Comparator.comparing(Production::getTitle);
        Collections.sort(movieList, byName);
        Collections.sort(seriesList, byName);
    }

    private void sortProductionsByDate() {
        Comparator<Production> byAverageRating = new Comparator<Production>() {
            @Override
            public int compare(Production p1, Production p2) {
                return p2.getAverageRating().compareTo(p1.getAverageRating());
            }
        };
        Collections.sort(movieList, byAverageRating);
        Collections.sort(seriesList, byAverageRating);
    }

    private void openProductionDetailsWindow(Production production) {
        ProductionDetailsWindow detailsWindow = new ProductionDetailsWindow(production);
        detailsWindow.setVisible(true);
    }

    private void openRatingWindow(Production production, User<?> user)
    {
        RatingWindow ratingWindow = new RatingWindow(production,user);
        ratingWindow.setVisible(true);
    }
}
