import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.util.SortedSet;

public class MenuWindow extends JFrame {
    public MenuWindow(Regular<?> user) {
        String title = "Menu " + user.getUsername();
        setTitle(title);
        setSize(400, 600);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        buttonPanel.add(createButton("View productions details", user));
        buttonPanel.add(createButton("View actors details", user));
        buttonPanel.add(createButton("View notifications", user));
        buttonPanel.add(createButton("Search for actor/movie/series", user));
        buttonPanel.add(createButton("Add/Delete actor/movie/series to/from favorites", user));
        buttonPanel.add(createButton("Create/Remove a request", user));
        buttonPanel.add(createButton("Add/Delete a review for a production", user));
        buttonPanel.add(createButton("Logout", user));
        buttonPanel.add(createButton("My profile", user));

        add(buttonPanel);

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new LoginInterface().setVisible(true);
            }
        });
    }

    public MenuWindow(Contributor<?> user) {
        setTitle("Menu");
        setSize(400, 600);
        setLayout(new FlowLayout());

        add(createButton("View productions details", user));
        add(createButton("View actors details", user));
        add(createButton("View notifications", user));
        add(createButton("Search for actor/movie/series", user));
        add(createButton("Add/Delete actor/movie/series to/from favorites", user));
        add(createButton("Create/Remove a request", user));
        add(createButton("Add/Delete actor/movie/series to/from system", user));
        add(createButton("View/Solve received requests", user));
        add(createButton("Update Movie Details", user));
        add(createButton("Update Actor Details", user));
        add(createButton("Logout", user));
        add(createButton("My profile", user));

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new LoginInterface().setVisible(true);
            }
        });
    }

    public MenuWindow(Admin<?> user) {
        setTitle("Menu");
        setSize(400, 600);
        setLayout(new FlowLayout());

        add(createButton("View productions details", user));
        add(createButton("View actors details", user));
        add(createButton("View your notifications", user));
        add(createButton("Search for actor/movie/series", user));
        add(createButton("Add/Delete actor/movie/series to/from favorites", user));
        add(createButton("See the notifications of the entire admins team", user));
        add(createButton("Add/Delete actor/movie/series to/from system", user));
        add(createButton("View/Solve received my own requests", user));
        add(createButton("Update Movie Details", user));
        add(createButton("Update Actor Details", user));
        add(createButton("Add/Delete/View a user", user));
        add(createButton("Logout", user));
        add(createButton("My profile", user));
        add(createButton("View/Solved received our own requests", user));

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new LoginInterface().setVisible(true);
            }
        });
    }

    private JButton createButton(String buttonText, User<?> user) {
        JButton button = new JButton(buttonText);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleButtonClick(buttonText, user);
            }
        });
        return button;
    }

    private void handleButtonClick(String buttonText, User<?> user) {
        if ("View productions details".equals(buttonText)) {
            IMDB imdb = IMDB.getInstance();
            List<Movie> movieList = imdb.getMovies();
            List<Series> seriesList = imdb.getSeries();
            ProductionsListWindow productionsListWindow = new ProductionsListWindow(user,movieList, seriesList,"Details");
            productionsListWindow.setVisible(true);
        } else if ("View actors details".equals(buttonText)) {
            IMDB imdb = IMDB.getInstance();
            List<Actor> actorList = imdb.getActors();
            ActorListWindow actorListWindow = new ActorListWindow(actorList);
            actorListWindow.setVisible(true);
        } else if ("View your notifications".equals(buttonText) || "View notifications".equals(buttonText)) {
            List<String> userNotifications = user.getNotifications();
            if (userNotifications.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No notifications");
                return;
            }
            NotificationWindow notificationWindow = new NotificationWindow(userNotifications);
            notificationWindow.setVisible(true);
        } else if ("Search for actor/movie/series".equals(buttonText)) {
            SearchWindow searchWindow = new SearchWindow();
            searchWindow.setVisible(true);
        } else if ("Add/Delete actor/movie/series to/from favorites".equals(buttonText)) {
            FavoritesWindow favoritesWindow = new FavoritesWindow((SortedSet<ProductionAndActor>) user.getFavorites(), user);
        } else if("Create/Remove a request".equals(buttonText))
        {
            RequestWindow requestWindow = new RequestWindow(user);
        } else if("Add/Delete a review for a production".equals(buttonText))
        {
            IMDB imdb = IMDB.getInstance();
            List<Movie> movieList = imdb.getMovies();
            List<Series> seriesList = imdb.getSeries();
            ProductionsListWindow productionsListWindow = new ProductionsListWindow(user,movieList, seriesList,"Ratings");
            productionsListWindow.setVisible(true);
        }
        else if("Logout".equals(buttonText))
        {
            new LoginInterface().setVisible(true);
            dispose();
        }
        else if("Add/Delete actor/movie/series to/from system".equals(buttonText))
        {
            AddOrDeleteActorOrProductionSystemWindow addOrDeleteActorOrProductionSystemWindow = new AddOrDeleteActorOrProductionSystemWindow(user);
        }
        else if("View/Solve received requests".equals(buttonText) || "View/Solve received my own requests".equals(buttonText))
        {
            RequestsWindow requestsWindow = new RequestsWindow(((Staff<?>)user).getListOfRequestsFromUsers(),(Staff<?>) user);
            requestsWindow.setVisible(true);
        }
        else if("See the notifications of the entire admins team".equals(buttonText))
        {
            IMDB imdb = IMDB.getInstance();
            List<String> userNotifications = imdb.getAdmins().get(0).getNotifications();
            if (userNotifications.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No notifications");
                return;
            }
            NotificationWindow notificationWindow = new NotificationWindow(userNotifications);
            notificationWindow.setVisible(true);
        }
        else if("View/Solved received our own requests".equals(buttonText))
        {
            IMDB imdb = IMDB.getInstance();
            RequestsHolderWindow requestsHolderWindow = new RequestsHolderWindow(imdb.getAdmins().get(0),user);
            requestsHolderWindow.setVisible(true);
        }
        else if("Update Movie Details".equals(buttonText) || "Update Actor Details".equals(buttonText) || "Add/Delete/View a user".equals(buttonText))
        {
            JOptionPane.showMessageDialog(MenuWindow.this,
                    "This functionality is yet to be implemented!", "Functionality under development", JOptionPane.INFORMATION_MESSAGE);
        }
        else if("My profile".equals(buttonText))
        {
            UserProfileWindow userProfileWindow = new UserProfileWindow(user);
            userProfileWindow.setVisible(true);
        }
    }

}
