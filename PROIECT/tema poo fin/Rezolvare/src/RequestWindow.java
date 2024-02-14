import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RequestWindow {
    private List<Request> requests;
    private JFrame frame;
    private DefaultTableModel tableModel;
    private JTable requestTable;

    public RequestWindow(User<?> user) {
        if (user instanceof Regular<?>)
            requests = ((Regular<?>) user).getRequestsToUsers();
        else if (user instanceof Contributor<?>)
            requests = ((Contributor<?>) user).getRequestsToUsers();

        frame = new JFrame("Request Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Type");
        tableModel.addColumn("Date");
        tableModel.addColumn("Title/Name");
        tableModel.addColumn("Description");
        tableModel.addColumn("User");
        tableModel.addColumn("Resolver");

        requestTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(requestTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        JButton createButton = new JButton("Create Request");
        JButton deleteButton = new JButton("Delete Request");

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createRequest(user);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = requestTable.getSelectedRow();
                if (selectedRow != -1) {
                    Request request = requests.get(selectedRow);
                    String toUsername = request.getResolverUsername();
                    IMDB imdb = IMDB.getInstance();
                    if (toUsername.equals("ADMIN")) { // E cerere de tipul 1 sau 4
                        imdb.getAdmins().get(0).removeReceiveRequestHolder(request);
                        String notification = user.getUsername() + " removed request " + request.getType() + " with description: " + request.getDescription();
                        user.notifyObserver(notification, imdb.getAdmins().get(0));
                    } else {
                        User<?> to = imdb.isAccountWithUsername(toUsername);
                        if (to instanceof Contributor<?>)
                            ((Contributor<?>) to).removeReceivedRequest(request);
                        else if (to instanceof Admin<?>)
                            ((Admin<?>) to).removeReceivedRequest(request);
                        String notification = user.getUsername() + " removed request " + request.getType() + " with description: " + request.getDescription();
                        user.notifyObserver(notification, to);
                    }
                    if (user instanceof Regular<?>)
                        ((Regular<?>) user).removeRequest(request);
                    else if (user instanceof Contributor<?>)
                        ((Contributor<?>) user).removeRequest(request);
                    requests.remove(selectedRow);
                    tableModel.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a request to delete.");
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createButton);
        buttonPanel.add(deleteButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        for (Request request : requests) {
            addRequest(request);
        }

        frame.setVisible(true);
    }

    private void createRequest(User<?> user) {

        JDialog dialog = new JDialog(frame, "Create Request", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 200);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));

        String[] requestTypes = {"Delete_Account", "Title_Issue", "Movie_Issue", "Others"};
        JComboBox<String> typeComboBox = new JComboBox<>(requestTypes);

        JTextField titleField = new JTextField();
        titleField.setEnabled(false);

        JTextField descriptionField = new JTextField();

        typeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) typeComboBox.getSelectedItem();
                titleField.setEnabled(!selectedType.equals("Delete_Account") && !selectedType.equals("Others"));
            }
        });

        JButton createButton = new JButton("Create");
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = (String) typeComboBox.getSelectedItem();
                String titleOrName = titleField.getText();
                String description = descriptionField.getText();
                String userUsername = user.getUsername();

                if (type.isEmpty() || (type.equals("Title_Issue") || type.equals("Movie_Issue")) && titleOrName.isEmpty() || description.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill in all fields.");
                } else {
                    Request request = null;
                    IMDB imdb = IMDB.getInstance();

                    if (type.equals("Delete_Account") || type.equals("Others")) {
                        String resolverUsername = "ADMIN";
                        if (type.equals("Delete_Account"))
                            request = new Request(RequestType.DELETE_ACCOUNT, LocalDateTime.now(), null, description, userUsername, resolverUsername);
                        else
                            request = new Request(RequestType.OTHERS, LocalDateTime.now(), null, description, userUsername, resolverUsername);
                        requests.add(request);

                        ((RequestsManager) user).createRequest(request);
                        imdb.getAdmins().get(0).receivedRequestHolder(request);
                        user.addObserver(imdb.getAdmins().get(0));
                        String notification;
                        if (type.equals("Delete_Account"))
                            notification = user.getUsername() + " made request " + RequestType.DELETE_ACCOUNT + " with description: " + description;
                        else
                            notification = user.getUsername() + " made request " + RequestType.OTHERS + " with description: " + description;
                        user.notifyObserver(notification, imdb.getAdmins().get(0));
                    } else {
                        if (type.equals("Title_Issue")) {
                            Contributor<?> cc = null;
                            Admin<?> aa = null;
                            for (Contributor<?> c : imdb.getContributors())
                                if (c.isActorContributedWithName(titleOrName)) {
                                    cc = c;
                                    break;
                                }
                            if (cc == null)
                                for (Admin<?> a : imdb.getAdmins())
                                    if (a.isActorContributedWithName(titleOrName)) {
                                        aa = a;
                                        break;
                                    }
                            if (cc == null && aa == null) {
                                JOptionPane.showMessageDialog(dialog, "Invalid Actor");

                            } else {
                                if (user == cc)
                                    JOptionPane.showMessageDialog(dialog, "Request impossible because you added the actor");
                                else {

                                    if (cc == null)
                                        request = new Request(RequestType.ACTOR_ISSUE, LocalDateTime.now(), titleOrName, description, user.getUsername(), aa.getUsername());
                                    else
                                        request = new Request(RequestType.ACTOR_ISSUE, LocalDateTime.now(), titleOrName, description, user.getUsername(), cc.getUsername());
                                    requests.add(request);
                                    ((RequestsManager) user).createRequest(request);
                                    //
                                    if (cc == null) {
                                        aa.receivedRequest(request);
                                        user.addObserver(aa);
                                        String notification = user.getUsername() + " made request " + RequestType.ACTOR_ISSUE + " - " + titleOrName + " with description: " + description;
                                        user.notifyObserver(notification, aa);
                                    } else {
                                        cc.receivedRequest(request);
                                        user.addObserver(cc);
                                        String notification = user.getUsername() + " made request " + RequestType.ACTOR_ISSUE + " - " + titleOrName + " with description: " + description;
                                        user.notifyObserver(notification, cc);
                                    }
                                }
                            }
                        } else {
                            Contributor<?> cc = null;
                            Admin<?> aa = null;
                            for (Contributor<?> c : imdb.getContributors())
                                if (c.isProdContributedWithName(titleOrName)) {
                                    cc = c;
                                    break;
                                }
                            if (cc == null)
                                for (Admin<?> a : imdb.getAdmins())
                                    if (a.isProdContributedWithName(titleOrName)) {
                                        aa = a;
                                        break;
                                    }
                            if (cc == null && aa == null) {
                                JOptionPane.showMessageDialog(dialog, "Invalid Movie name");
                            } else {
                                if (user == cc)
                                    JOptionPane.showMessageDialog(dialog, "Request impossible because you added the production");
                                else {

                                }
                                if (cc == null)
                                    request = new Request(RequestType.MOVIE_ISSUE, LocalDateTime.now(), titleOrName, description, user.getUsername(), aa.getUsername());
                                else
                                    request = new Request(RequestType.MOVIE_ISSUE, LocalDateTime.now(), titleOrName, description, user.getUsername(), cc.getUsername());
                                requests.add(request);
                                ((RequestsManager) user).createRequest(request);
                                //
                                if (cc == null) {
                                    aa.receivedRequest(request);
                                    user.addObserver(aa);
                                    String notification = user.getUsername() + " made request " + RequestType.MOVIE_ISSUE + " - " + titleOrName + " with description: " + description;
                                    user.notifyObserver(notification, aa);
                                } else {
                                    cc.receivedRequest(request);
                                    user.addObserver(cc);
                                    String notification = user.getUsername() + " made request " + RequestType.MOVIE_ISSUE + " - " + titleOrName + " with description: " + description;
                                    user.notifyObserver(notification, cc);
                                }
                            }
                        }
                    }
                    if (request != null)
                        addRequest(request);
                    dialog.dispose();
                }
            }
        });

        inputPanel.add(new JLabel("Type:"));
        inputPanel.add(typeComboBox);
        inputPanel.add(new JLabel("Title/Name:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(createButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }


    public void addRequest(Request request) {
        //requests.add(request);//?
        tableModel.addRow(new Object[]{
                request.getType().toString(),
                request.getFormattedCreationDate(),
                request.getTitleOrName(),
                request.getDescription(),
                request.getUserUsername(),
                request.getResolverUsername()
        });
    }

}
