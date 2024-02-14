import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RequestsWindow extends JFrame {
    private JList<Request> requestList;
    private JButton resolveButton;
    private JButton rejectButton;
    private DefaultListModel<Request> listModel;

    Staff<?> staff;
    IMDB imdb = IMDB.getInstance();

    public RequestsWindow(List<Request> requests, Staff<?> staff) {
        this.staff = staff;

        setTitle("Received Requests");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        listModel = new DefaultListModel<>();
        requestList = new JList<>(listModel);
        requestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        requestList.addListSelectionListener(e -> updateButtons());

        for (Request request : requests) {
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
                    staff.getListOfRequestsFromUsers().remove(selectedRequest);
                    User<?> userRequest = imdb.searchRegularOrContributorWithName(selectedRequest.getUserUsername());
                    if (userRequest instanceof Regular<?>)
                        ((Regular<?>) userRequest).getRequestsToUsers().remove(selectedRequest);
                    else
                        ((Contributor<?>) userRequest).getRequestsToUsers().remove(selectedRequest);
                    imdb.getRequests().remove(selectedRequest);
                    //
                    String notification = staff.getUsername() + " resolved your request " + selectedRequest.getType() + " wih description " + selectedRequest.getDescription();
                    staff.notifyObserver(notification, userRequest);

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
                    staff.getListOfRequestsFromUsers().remove(selectedRequest);
                    User<?> userRequest = imdb.searchRegularOrContributorWithName(selectedRequest.getUserUsername());
                    if (userRequest instanceof Regular<?>)
                        ((Regular<?>) userRequest).getRequestsToUsers().remove(selectedRequest);
                    else
                        ((Contributor<?>) userRequest).getRequestsToUsers().remove(selectedRequest);
                    imdb.getRequests().remove(selectedRequest);

                    String notification = staff.getUsername() + " rejected your request " + selectedRequest.getType() + " wih description " + selectedRequest.getDescription();
                    staff.notifyObserver(notification, userRequest);
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

        for (int i = 0; i < staff.getListOfRequestsFromUsers().size(); i++) {
            listModel.addElement(staff.getListOfRequestsFromUsers().get(i));
        }
        requestList.setModel(listModel);
    }
}
