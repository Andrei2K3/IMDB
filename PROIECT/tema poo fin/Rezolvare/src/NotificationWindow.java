import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NotificationWindow extends JFrame {
    private JList<String> notificationList;

    public NotificationWindow(List<String> notifications) {
        setTitle("Notifications");
        setSize(400, 300);

        DefaultListModel<String> notificationListModel = new DefaultListModel<>();
        for (String notification : notifications) {
            notificationListModel.addElement(notification);
        }

        notificationList = new JList<>(notificationListModel);
        notificationList.setFont(new Font("Arial", Font.PLAIN, 16));
        notificationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(notificationList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(scrollPane);

        setLocationRelativeTo(null);
    }

    public void display() {
        setVisible(true);
    }
}
