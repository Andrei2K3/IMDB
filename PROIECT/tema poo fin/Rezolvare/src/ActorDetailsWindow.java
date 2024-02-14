import javax.swing.*;
import java.awt.*;

public class ActorDetailsWindow extends JFrame {
    public ActorDetailsWindow(Actor selectedActor) {
        setTitle("Actor Details");
        setSize(300, 400);
        setLayout(new BorderLayout());

        JLabel nameLabel = new JLabel("Name: " + selectedActor.getName());
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(nameLabel, BorderLayout.NORTH);

        JTextArea biographyArea = new JTextArea("Biography: " + selectedActor.getBiography());
        biographyArea.setWrapStyleWord(true);
        biographyArea.setLineWrap(true);
        biographyArea.setEditable(false);
        biographyArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane bioScrollPane = new JScrollPane(biographyArea);
        add(bioScrollPane, BorderLayout.CENTER);

        String[] filmAppearances = selectedActor.getFilmography().stream()
                .map(filmAppearance -> filmAppearance.getName() + " (" + filmAppearance.getType() + ")")
                .toArray(String[]::new);
        JList<String> filmList = new JList<>(filmAppearances);
        filmList.setBorder(BorderFactory.createTitledBorder("Film Appearances"));
        JScrollPane filmScrollPane = new JScrollPane(filmList);
        add(filmScrollPane, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }
}
