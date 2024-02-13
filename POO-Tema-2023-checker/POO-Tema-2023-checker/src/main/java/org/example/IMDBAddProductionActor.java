import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IMDBAddProductionActor extends JFrame {

    private JComboBox<String> entryTypeComboBox;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField directorsField;
    private JTextField actorsField;
    private JTextField genresField;
    private JTextField releaseYearField;
    private JTextField durationField;
    private JTextField seasonsField;

    private JButton submitButton;
    private JButton returnButton;

    private User<?> currentUser;

    public IMDBAddProductionActor(User<?> user) {
        this.currentUser = user;

        setTitle("Add Production/Actor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(20, 2));

        JLabel entryTypeLabel = new JLabel("Entry Type:");
        entryTypeComboBox = new JComboBox<>(new String[]{"Movie", "Series", "Actor"});
        mainPanel.add(entryTypeLabel);
        mainPanel.add(entryTypeComboBox);

        JLabel titleLabel = new JLabel("Title/Name:");
        titleField = new JTextField();
        mainPanel.add(titleLabel);
        mainPanel.add(titleField);

        JLabel descriptionLabel = new JLabel("Description/Biography:");
        descriptionArea = new JTextArea();
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        mainPanel.add(descriptionLabel);
        mainPanel.add(descriptionScrollPane);

        JLabel directorsLabel = new JLabel("Directors:");
        directorsField = new JTextField();
        mainPanel.add(directorsLabel);
        mainPanel.add(directorsField);

        JLabel actorsLabel = new JLabel("Actors:");
        actorsField = new JTextField();
        mainPanel.add(actorsLabel);
        mainPanel.add(actorsField);

        JLabel genresLabel = new JLabel("Genres:");
        genresField = new JTextField();
        mainPanel.add(genresLabel);
        mainPanel.add(genresField);

        JLabel releaseYearLabel = new JLabel("Release Year:");
        releaseYearField = new JTextField();
        mainPanel.add(releaseYearLabel);
        mainPanel.add(releaseYearField);

        JLabel durationLabel = new JLabel("Duration:");
        durationField = new JTextField();
        mainPanel.add(durationLabel);
        mainPanel.add(durationField);

        JLabel seasonsLabel = new JLabel("Number of Seasons:");
        seasonsField = new JTextField();
        mainPanel.add(seasonsLabel);
        mainPanel.add(seasonsField);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) entryTypeComboBox.getSelectedItem();
                if (selectedType.equals("Movie")) {
                    if (titleField.getText().isEmpty() || descriptionArea.getText().isEmpty()
                            || directorsField.getText().isEmpty() || actorsField.getText().isEmpty()
                            || genresField.getText().isEmpty() || releaseYearField.getText().isEmpty()
                            || durationField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill in all the fields.\n" +
                                "Fields that cannot be empty for a movie: Title, Description, Directors, Actors, Genres, Release Year, Duration.");
                        return;
                    }

                    String title = titleField.getText();
                    String description = descriptionArea.getText();
                    String[] directors = directorsField.getText().split(",");
                    String[] actors = actorsField.getText().split(",");

                    String[] genresString = genresField.getText().split(", ");
                    List<Genre> genres = new ArrayList<>();
                    for (String genre : genresString) {
                        try {
                            genres.add(Genre.valueOf(genre.toUpperCase()));
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Invalid genre: " + genre);
                            return;
                        }
                    }

                    int releaseYear;
                    try {
                        releaseYear = Integer.parseInt(releaseYearField.getText());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Invalid release year: " + releaseYearField.getText());
                        return;
                    }

                    int duration;
                    try {
                        duration = Integer.parseInt(durationField.getText());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Invalid duration: " + durationField.getText());
                        return;
                    }

                    // Create Movie object based on the input data
                    Movie newMovie = new Movie(title, Arrays.stream(directors).toList(), Arrays.stream(actors).toList(), genres, new ArrayList<>(), description, 0.0, releaseYear, duration);
                    ((Staff <?>) currentUser).addProductionSystem(newMovie);
                    JOptionPane.showMessageDialog(null, "Movie added successfully!");
                } else if (selectedType.equals("Series")) {
                    if (titleField.getText().isEmpty() || descriptionArea.getText().isEmpty()
                            || directorsField.getText().isEmpty() || actorsField.getText().isEmpty()
                            || genresField.getText().isEmpty() || releaseYearField.getText().isEmpty()
                            || seasonsField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill in all the fields.\n" +
                                "Fields that cannot be empty for a series: Title, Description, Directors, Actors, Genres, Release Year, Number of Seasons.");
                        return;
                    }

                    String title = titleField.getText();
                    String description = descriptionArea.getText();
                    String[] directors = directorsField.getText().split(",");
                    String[] actors = actorsField.getText().split(",");

                    String[] genresString = genresField.getText().split(", ");
                    List<Genre> genres = new ArrayList<>();
                    for (String genre : genresString) {
                        try {
                            genres.add(Genre.valueOf(genre.toUpperCase()));
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Invalid genre: " + genre);
                            return;
                        }
                    }

                    int releaseYear;
                    try {
                        releaseYear = Integer.parseInt(releaseYearField.getText());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Invalid release year: " + releaseYearField.getText());
                        return;
                    }

                    int seasons;
                    try {
                        seasons = Integer.parseInt(seasonsField.getText());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Invalid number of seasons: " + seasonsField.getText());
                        return;
                    }

                    // Create Series object based on the input data
                    Series newSeries = new Series(title, Arrays.stream(directors).toList(), Arrays.stream(actors).toList(), genres, new ArrayList<>(), description, 0.0, releaseYear, seasons);
                    ((Staff <?>) currentUser).addProductionSystem(newSeries);
                    JOptionPane.showMessageDialog(null, "Series added successfully!");
                } else if (selectedType.equals("Actor")) {
                    if (titleField.getText().isEmpty() || descriptionArea.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill in all the fields.\n" +
                                "Fields that cannot be empty for an actor: Name, Biography");
                        return;
                    }

                    String name = titleField.getText();
                    String biography = descriptionArea.getText();

                    // Create Actor object based on the input data
                    Actor newActor = new Actor(name, biography);
                    ((Staff <?>) currentUser).addActorSystem(newActor);
                    JOptionPane.showMessageDialog(null, "Actor added successfully!");
                }

                // Clear input fields after submitting the entry
                clearFields();
            }
        });
        mainPanel.add(submitButton);

        returnButton = new JButton("Return to Home Screen");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new IMDBHomeScreen(currentUser);
            }
        });
        mainPanel.add(returnButton);

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    private void clearFields() {
        titleField.setText("");
        descriptionArea.setText("");
        directorsField.setText("");
        actorsField.setText("");
        genresField.setText("");
        releaseYearField.setText("");
        durationField.setText("");
        seasonsField.setText("");
    }
}