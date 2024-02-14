import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RequestsHolderWindow extends JFrame {
    private JList<Request> requestList;
    private JButton resolveButton;
    private JButton rejectButton;
    private DefaultListModel<Request> listModel;

    Admin<?> adminBoss;
    IMDB imdb = IMDB.getInstance();

    public RequestsHolderWindow(Admin<?> adminBoss, User<?> user) {
        this.adminBoss = adminBoss;

        setTitle("Received Requests");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        listModel = new DefaultListModel<>();
        requestList = new JList<>(listModel);
        requestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        requestList.addListSelectionListener(e -> updateButtons());

        for (Request request : adminBoss.getListOfRequestsHolderFromUsers()) {
            listModel.addElement(request);
        }

        JScrollPane scrollPane = new JScrollPane(requestList);

        resolveButton = new JButton("Resolve");
        rejectButton = new JButton("Reject");

        resolveButton.setEnabled(false);
        rejectButton.setEnabled(false);

        resolveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Request selectedRequest = requestList.getSelectedValue();
                if (selectedRequest != null) {
                    adminBoss.getListOfRequestsHolderFromUsers().remove(selectedRequest);
                    User<?> userRequest = imdb.searchRegularOrContributorWithName(selectedRequest.getUserUsername());
                    if (userRequest instanceof Regular<?>)
                        ((Regular<?>) userRequest).getRequestsToUsers().remove(selectedRequest);
                    else
                        ((Contributor<?>) userRequest).getRequestsToUsers().remove(selectedRequest);
                    imdb.getRequests().remove(selectedRequest);
                    //
                    String notification = adminBoss.getUsername() + " resolved your request " + selectedRequest.getType() + " wih description " + selectedRequest.getDescription();
                    adminBoss.notifyObserver(notification, userRequest);
                    //
                    String notificationToAdmins = user.getUsername() + " resolved request " + selectedRequest.getType() + " with description " + selectedRequest.getDescription();
                    for (Admin<?> a : imdb.getAdmins())
                        if (a != (Admin<?>)user)
                            adminBoss.notifyObserver(notificationToAdmins, a);

                    userRequest.setExperienceStrategy(new RequestAccepted());
                    userRequest.updateExperience();
                }
                updateRequestList();
            }
        });

        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Request selectedRequest = requestList.getSelectedValue();
                if (selectedRequest != null) {
                    adminBoss.getListOfRequestsHolderFromUsers().remove(selectedRequest);
                    User<?> userRequest = imdb.searchRegularOrContributorWithName(selectedRequest.getUserUsername());
                    if (userRequest instanceof Regular<?>)
                        ((Regular<?>) userRequest).getRequestsToUsers().remove(selectedRequest);
                    else
                        ((Contributor<?>) userRequest).getRequestsToUsers().remove(selectedRequest);
                    imdb.getRequests().remove(selectedRequest);

                    String notification = adminBoss.getUsername() + " rejected your request " + selectedRequest.getType() + " wih description " + selectedRequest.getDescription();
                    adminBoss.notifyObserver(notification, userRequest);
                    //
                    String notificationToAdmins = user.getUsername() + " rejected request " + selectedRequest.getType() + " with description " + selectedRequest.getDescription();
                    for (Admin<?> a : imdb.getAdmins())
                        if (a != (Admin<?>)user)
                            adminBoss.notifyObserver(notificationToAdmins, a);
                }
                updateRequestList();
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(resolveButton);
        buttonPanel.add(rejectButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateButtons() {
        boolean isSelection = !requestList.isSelectionEmpty();
        resolveButton.setEnabled(isSelection);
        rejectButton.setEnabled(isSelection);
    }

    private void updateRequestList() {
        listModel.clear();

        for (int i = 0; i < adminBoss.getListOfRequestsHolderFromUsers().size(); i++) {
            listModel.addElement(adminBoss.getListOfRequestsHolderFromUsers().get(i));
        }
        requestList.setModel(listModel);
    }
}
