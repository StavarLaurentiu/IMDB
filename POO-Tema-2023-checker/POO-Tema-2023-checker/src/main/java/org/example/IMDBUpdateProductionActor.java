import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IMDBUpdateProductionActor extends JFrame {

    private JComboBox<String> entryComboBox;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField directorsField;
    private JTextField actorsField;
    private JTextField genresField;
    private JTextField releaseYearField;
    private JTextField durationField;
    private JTextField seasonsField;

    private JButton updateButton;
    private JButton returnButton;

    private User<?> currentUser;

    public IMDBUpdateProductionActor(User<?> user) {
        this.currentUser = user;

        setTitle("Update Production/Actor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(20, 2));

        JLabel entryLabel = new JLabel("Select a Production/Actor:");
        List<String> productionsAndActors = getProductionsAndActors();
        entryComboBox = new JComboBox<>(productionsAndActors.toArray(new String[0]));
        mainPanel.add(entryLabel);
        mainPanel.add(entryComboBox);

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

        updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedEntry = (String) entryComboBox.getSelectedItem();
                String[] selectedEntrySplit = selectedEntry.split(", ");
                String selectedEntryName = selectedEntrySplit[0];
                String selectedEntryType = selectedEntrySplit[1];

                if (selectedEntryType.equals("Movie")) {
                    if (titleField.getText().isEmpty() && descriptionArea.getText().isEmpty()
                            && directorsField.getText().isEmpty() && actorsField.getText().isEmpty()
                            && genresField.getText().isEmpty() && releaseYearField.getText().isEmpty()
                            && durationField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill in at least one field.\n" +
                                "One of this fields cannot be empty for a movie: Title, Description, Directors, Actors, Genres, Release Year, Duration." +
                                "\nOnly non empty fields will be updated.");
                        return;
                    }

                    Production movie = null;
                    for (Production production : IMDB.productionsList) {
                        if (production.title.equals(selectedEntryName)) {
                            movie = production;
                            break;
                        }
                    }

                    if (movie == null) {
                        JOptionPane.showMessageDialog(null, "Movie not found.");
                        return;
                    }

                    if (!titleField.getText().isEmpty()) {
                        movie.title = titleField.getText();
                    }

                    if (!descriptionArea.getText().isEmpty()) {
                        movie.description = descriptionArea.getText();
                    }

                    if (!directorsField.getText().isEmpty()) {
                        movie.directors = Arrays.asList(directorsField.getText().split(","));
                    }

                    if (!actorsField.getText().isEmpty()) {
                        movie.actors = Arrays.asList(actorsField.getText().split(","));
                    }

                    if (!genresField.getText().isEmpty()) {
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

                        movie.genres = genres;
                    }

                    if (!releaseYearField.getText().isEmpty()) {
                        try {
                            ((Movie) movie).releaseYear = Integer.parseInt(releaseYearField.getText());
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Invalid release year: " + releaseYearField.getText());
                            return;
                        }
                    }

                    if (!durationField.getText().isEmpty()) {
                        try {
                            ((Movie) movie).duration = Integer.parseInt(durationField.getText());
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Invalid duration: " + durationField.getText());
                            return;
                        }
                    }

                    JOptionPane.showMessageDialog(null, "Movie updated successfully.");
                } else if (selectedEntryType.equals("Series")) {
                    if (titleField.getText().isEmpty() && descriptionArea.getText().isEmpty()
                            && directorsField.getText().isEmpty() && actorsField.getText().isEmpty()
                            && genresField.getText().isEmpty() && releaseYearField.getText().isEmpty()
                            && seasonsField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill in at least one field.\n" +
                                "One of this fields cannot be empty for a series: Title, Description, Directors, Actors, Genres, Release Year, Number of Seasons." +
                                "\nOnly non empty fields will be updated.");
                        return;
                    }

                    Production series = null;
                    for (Production production : IMDB.productionsList) {
                        if (production.title.equals(selectedEntryName)) {
                            series = production;
                            break;
                        }
                    }

                    if (series == null) {
                        JOptionPane.showMessageDialog(null, "Series not found.");
                        return;
                    }

                    if (!titleField.getText().isEmpty()) {
                        series.title = titleField.getText();
                    }

                    if (!descriptionArea.getText().isEmpty()) {
                        series.description = descriptionArea.getText();
                    }

                    if (!directorsField.getText().isEmpty()) {
                        series.directors = Arrays.asList(directorsField.getText().split(","));
                    }

                    if (!actorsField.getText().isEmpty()) {
                        series.actors = Arrays.asList(actorsField.getText().split(","));
                    }

                    if (!genresField.getText().isEmpty()) {
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

                        series.genres = genres;
                    }

                    if (!releaseYearField.getText().isEmpty()) {
                        try {
                            ((Series) series).releaseYear = Integer.parseInt(releaseYearField.getText());
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Invalid release year: " + releaseYearField.getText());
                            return;
                        }
                    }

                    if (!seasonsField.getText().isEmpty()) {
                        try {
                            ((Series) series).numSeasons = Integer.parseInt(seasonsField.getText());
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Invalid number of seasons: " + seasonsField.getText());
                            return;
                        }
                    }

                    JOptionPane.showMessageDialog(null, "Series updated successfully.");
                } else if (selectedEntryType.equals("Actor")) {
                    if (titleField.getText().isEmpty() && descriptionArea.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill in at least on of the fields.\n" +
                                "One of this fields cannot be empty for an actor: Name, Biography");
                        return;
                    }

                    Actor actor = null;
                    for (Actor a : IMDB.actorsList) {
                        if (a.getName().equals(selectedEntryName)) {
                            actor = a;
                            break;
                        }
                    }

                    if (actor == null) {
                        JOptionPane.showMessageDialog(null, "Actor not found.");
                        return;
                    }

                    if (!titleField.getText().isEmpty()) {
                        actor.setName(titleField.getText());
                    }

                    if (!descriptionArea.getText().isEmpty()) {
                        actor.setBiography(descriptionArea.getText());
                    }

                    JOptionPane.showMessageDialog(null, "Actor updated successfully.");
                }

                clearFields();
            }
        });
        mainPanel.add(updateButton);

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

    private List<String> getProductionsAndActors() {
        List<String> productionsAndActors = new ArrayList<>();

        for (Production production : IMDB.productionsList) {
            if (production instanceof Movie) {
                productionsAndActors.add(production.title + ", Movie");
            } else if (production instanceof Series) {
                productionsAndActors.add(production.title + ", Series");
            }
        }

        for (Actor actor : IMDB.actorsList) {
            productionsAndActors.add(actor.getName() + ", Actor");
        }

        return productionsAndActors;
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
