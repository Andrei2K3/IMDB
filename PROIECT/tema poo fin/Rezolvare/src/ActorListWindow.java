import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActorListWindow extends JFrame {
    private JList<String> actorJList;
    private DefaultListModel<String> actorListModel;

    List<Actor> actorList;
    public ActorListWindow(List<Actor> actorList) {
        this.actorList = actorList;

        setTitle("List of Actors");
        setSize(400, 300);
        actorListModel = new DefaultListModel<>();
        actorJList = new JList<>(actorListModel);
        updateActorListModel(actorList);

        JScrollPane scrollPane = new JScrollPane(actorJList);
        scrollPane.setPreferredSize(new Dimension(380, 250));
        add(scrollPane, BorderLayout.CENTER);

        addSortButtons();
        addSelectButton();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void addSortButtons() {
        JPanel sortPanel = new JPanel();
        sortPanel.setLayout(new FlowLayout());

        JButton sortByNameButton = createStyledButton("Sort by Name");
        sortByNameButton.addActionListener(e -> sortActors(Comparator.comparing(Actor::getName)));
        sortPanel.add(sortByNameButton);

        JButton sortByFilmCountButton = createStyledButton("Sort by Film Count");
        sortByFilmCountButton.addActionListener(e -> sortActors(Comparator.comparing(actor -> actor.getFilmography().size())));
        sortPanel.add(sortByFilmCountButton);

        add(sortPanel, BorderLayout.NORTH);
    }

    private void addSelectButton() {
        JButton selectButton = createStyledButton("Select Actor");
        selectButton.addActionListener(e -> {
            String selectedActorName = actorJList.getSelectedValue();
            if (selectedActorName != null) {
                showActorDetails(selectedActorName);
            } else {
                JOptionPane.showMessageDialog(this, "Select an actor from the list.");
            }
        });
        add(selectButton, BorderLayout.SOUTH);
    }


    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(new Color(100, 149, 237));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        button.setOpaque(true);
        button.setBorderPainted(true);
        return button;
    }

    private void sortActors(Comparator<Actor> comparator) {
        Collections.sort(actorList, comparator);
        updateActorListModel(actorList);
    }

    private void updateActorListModel(List<Actor> actors) {
        actorListModel.clear();
        actors.forEach(actor -> actorListModel.addElement(actor.getName()));
    }

    private void showActorDetails(String actorName) {
        Actor selectedActor = actorList.stream()
                .filter(actor -> actor.getName().equals(actorName))
                .findFirst()
                .orElse(null);

        if (selectedActor != null) {
            ActorDetailsWindow detailsWindow = new ActorDetailsWindow(selectedActor);
            detailsWindow.setVisible(true);
        }
    }
}
