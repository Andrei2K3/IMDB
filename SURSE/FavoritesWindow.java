import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.SortedSet;
import java.util.TreeSet;

public class FavoritesWindow {

    private SortedSet<ProductionAndActor> favorites;
    private JFrame frame;
    private JList<String> favoritesJList;
    private DefaultListModel<String> listModel;

    public FavoritesWindow(SortedSet<ProductionAndActor> favoritesSet, User<?> user) {
        favorites = favoritesSet;

        frame = new JFrame("Actor/Production favorite");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        listModel = new DefaultListModel<>();
        favoritesJList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(favoritesJList);
        frame.add(scrollPane, BorderLayout.CENTER);

        for (ProductionAndActor item : favorites) {
            if(item instanceof Actor)
            listModel.addElement(((Actor) item).getName());
            else
                listModel.addElement(((Production) item).getTitle());
        }

        JButton addButton = new JButton("Add to favorite");
        JButton deleteButton = new JButton("Remove from favorite");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = JOptionPane.showInputDialog(frame, "Type name of actor/production:");

                IMDB imdb = IMDB.getInstance();
                boolean isInFavorites = imdb.isActorProductionInFavorites(user, selectedItem);
                if (isInFavorites) {
                    JOptionPane.showMessageDialog(frame, "The name is already in favorites.");
                } else {
                    ProductionAndActor paa = imdb.actorProductionSystem(selectedItem);
                    if (paa == null) {
                        JOptionPane.showMessageDialog(frame, "The name was not found in the system.");
                    } else {
                        favorites.add(paa);
                        listModel.addElement(selectedItem);
                    }
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = favoritesJList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String removedItem = listModel.getElementAt(selectedIndex);
                    IMDB imdb = IMDB.getInstance();
                    ProductionAndActor paa = imdb.actorProductionInFavorites(user, removedItem);
                    favorites.remove(paa);
                    listModel.remove(selectedIndex);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a name.");
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
