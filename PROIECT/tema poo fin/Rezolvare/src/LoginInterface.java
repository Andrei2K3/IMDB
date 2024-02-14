import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginInterface extends JFrame implements ActionListener {
    JLabel emailLabel;
    JTextField emailTextField;
    JLabel passLabel;
    JPasswordField passField;
    JButton loginButton;

    IMDB imdb;

    public LoginInterface() {
        imdb = IMDB.getInstance();
        imdb.loadJsonToData();

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        emailLabel = new JLabel("Email:");
        add(emailLabel, gbc);

        gbc.gridy++;
        emailTextField = new JTextField(20);
        add(emailTextField, gbc);

        gbc.gridy++;
        passLabel = new JLabel("Password:");
        add(passLabel, gbc);

        gbc.gridy++;
        passField = new JPasswordField(20);
        add(passField, gbc);

        gbc.gridy++;
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        add(loginButton, gbc);

        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String email = emailTextField.getText();
        char[] password = passField.getPassword();

        User<?> user = imdb.getUser(email, new String(password));
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Authentication passed");

            Regular<?> regular;
            Contributor<?> contributor;
            Admin<?> admin;
            if (user instanceof Regular<?>) {
                regular = (Regular<?>) user;
                EventQueue.invokeLater(() -> {
                    MenuWindow menu = new MenuWindow(regular);
                    menu.setVisible(true);
                });
            }
            else if(user instanceof Contributor<?>)
            {
                contributor = (Contributor<?>) user;
                EventQueue.invokeLater(() -> {
                    MenuWindow menu = new MenuWindow(contributor);
                    menu.setVisible(true);
                });
            }
            else {
                admin = (Admin<?>) user;
                EventQueue.invokeLater(() -> {
                    MenuWindow menu = new MenuWindow(admin);
                    menu.setVisible(true);
                });
            }

            this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, "Authentication failed. Try again.");
        }
    }


    public static void main(String[] args) {
        new LoginInterface();
    }

}
