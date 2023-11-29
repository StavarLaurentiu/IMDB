import java.util.*;
public class Actor {
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
    }

    private String name;
    private List<NameTypePair> performances;
    private String biography;

    public Actor(String name, String biography) {
        this.name = name;
        this.performances = new ArrayList<>();
        this.biography = biography;
    }

    public Actor(String name, List<NameTypePair> performances, String biography) {
        this.name = name;
        this.performances = performances;
        this.biography = biography;
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

    public String getBiography() {
        return biography;
    }

    public void printProductions() {
        for (NameTypePair ntp : performances) {
            System.out.println("    " + ntp.getName() + ", " + ntp.getType());
        }
    }

    public void displayInfo() {
        if (name != null) System.out.println("Actor Name: " + name);
        if (performances != null) {
            System.out.println("Performances list: ");
            printProductions();
        }
        if (biography != null) System.out.println("Biography: " + biography);
    }

}
