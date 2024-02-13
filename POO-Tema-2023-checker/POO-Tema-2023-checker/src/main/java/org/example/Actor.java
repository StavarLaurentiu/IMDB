import java.io.File;
import java.util.*;
public class Actor implements Comparable {
    class NameTypePair {
        private String name;
        private ProductionType type;

        public NameTypePair(String name, ProductionType type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public ProductionType getType() {
            return type;
        }

        @Override
        public String toString() {
            return name + " => " + type;
        }
    }

    private String name;
    private String photoPath = "/Users/laur/Desktop/Tema Poo/assets/actors/noImageFound.png";
    private List<NameTypePair> performances;
    private String biography;
    private String contributor;

    public Actor(String name, String biography) {
        this.name = name;
        this.performances = new ArrayList<>();
        this.biography = biography;
        contributor = "STAFF";

        String possiblePhoto = "/Users/laur/Desktop/Tema Poo/assets/actors/" + name + ".jpg";
        File file = new File(possiblePhoto);
        if (file.exists()) {
            this.photoPath = possiblePhoto;
        }
    }

    public Actor(String name, List<NameTypePair> performances, String biography) {
        this.name = name;
        this.performances = performances;
        this.biography = biography;

        String possiblePhoto = "/Users/laur/Desktop/Tema Poo/assets/" + name + ".png";
        File file = new File(possiblePhoto);
        if (file.exists()) {
            this.photoPath = possiblePhoto;
        }
    }

    public void addProduction(String name, ProductionType type) {
        performances.add(new NameTypePair(name, type));
    }

    public void removeProduction(String name, ProductionType type) {
        performances.remove(new NameTypePair(name, type));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void printProductions() {
        for (NameTypePair ntp : performances) {
            System.out.println("    " + ntp.getName() + ", " + ntp.getType());
        }
    }

    public void setContributor(User contributor) {
        this.contributor = contributor.username;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    public String getContributor() {
        return contributor;
    }

    public List<NameTypePair> getPerformances() {
        return performances;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void displayInfo() {
        if (name != null) System.out.println("Actor Name: " + name);
        if (performances != null) {
            System.out.println("Performances list: ");
            printProductions();
        }
        if (biography != null) System.out.println("Biography: " + biography);
        if (contributor != null) System.out.println("Contributor: " + contributor);
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Actor) {
            Actor a = (Actor) o;
            return name.compareTo(a.name);
        }

        return 0;
    }
}
