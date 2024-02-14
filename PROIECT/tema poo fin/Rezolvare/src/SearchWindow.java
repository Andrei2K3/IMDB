import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchWindow extends JFrame {
    private JTextField searchTextField;
    private JButton searchButton;

    public SearchWindow() {
        setTitle("Search:");
        setSize(400, 100);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        searchTextField = new JTextField(20);
        searchButton = new JButton("Cauta");

        panel.add(searchTextField);
        panel.add(searchButton);

        add(panel);

        setLocationRelativeTo(null);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchTextField.getText();
                performSearch(searchText);
            }
        });
    }

    private void performSearch(String searchText) {
        IMDB imdb = IMDB.getInstance();
        ProductionAndActor searchPAA = imdb.isProductionOrActorInSystemWithName(searchText);

        if (searchPAA == null) {
            JOptionPane.showMessageDialog(this, "Name not found in system: " + searchText);
        } else {
            if(searchPAA instanceof Actor)
            {
                ActorDetailsWindow actorDetailsWindow = new ActorDetailsWindow((Actor) searchPAA);
                actorDetailsWindow.setVisible(true);
            }
            else
            {
                ProductionDetailsWindow productionDetailsWindow = new ProductionDetailsWindow((Production) searchPAA);
                productionDetailsWindow.setVisible(true);
            }
            dispose();
        }
    }
}
