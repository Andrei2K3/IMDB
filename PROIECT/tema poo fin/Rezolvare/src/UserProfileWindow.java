import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserProfileWindow extends JFrame {
    private User<?> user;
    public UserProfileWindow(User<?> user) {
        this.user = user;
        setTitle("User Profile");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("Name:");
        JLabel usernameLabel = new JLabel("Username:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel accountType = new JLabel("Account Type:");
        JLabel experience = new JLabel("Experience:");

        JLabel nameValueLabel = new JLabel(user.getInfo().getName());
        JLabel usernameValueLabel = new JLabel(user.getUsername());
        JLabel emailValueLabel = new JLabel(user.getInfo().getCredentials().getEmail());
        JLabel accountTypeValueLabel = new JLabel(user.getAccountType().toString());
        JLabel experienceValueLabel = new JLabel(String.valueOf(user.getExperience()));

        panel.add(nameLabel);
        panel.add(nameValueLabel);
        panel.add(usernameLabel);
        panel.add(usernameValueLabel);
        panel.add(emailLabel);
        panel.add(emailValueLabel);
        panel.add(accountType);
        panel.add(accountTypeValueLabel);
        panel.add(experience);
        panel.add(experienceValueLabel);

        getContentPane().add(panel, BorderLayout.CENTER);


        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        getContentPane().add(closeButton, BorderLayout.SOUTH);
    }
}
