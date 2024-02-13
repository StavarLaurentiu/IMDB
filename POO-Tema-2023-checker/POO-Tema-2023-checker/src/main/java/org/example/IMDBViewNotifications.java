import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class IMDBViewNotifications extends JFrame {
    private User<?> user;
    private JTextArea notificationsArea;

    public IMDBViewNotifications(User<?> user) {
        this.user = user;

        setTitle("View Notifications");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        notificationsArea = new JTextArea();
        notificationsArea.setEditable(false);
        notificationsArea.setLineWrap(true);
        notificationsArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(notificationsArea);

        populateNotifications(user.notifications);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    private void populateNotifications(List<String> notifications) {
        StringBuilder sb = new StringBuilder();
        for (String notification : notifications) {
            sb.append(notification).append("\n\n");
        }
        notificationsArea.setText(sb.toString());
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Return to Home Page");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new IMDBHomeScreen(user);
            }
        });
        buttonPanel.add(backButton);
        return buttonPanel;
    }
}
