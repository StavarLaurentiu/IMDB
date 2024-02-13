import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public abstract class Staff<T extends Comparable<T>> extends User implements StaffInterface {
    List<Request> requestsToHandle = new ArrayList<>();
    private SortedSet<T> contributions;

    public void setContributions(SortedSet<T> contributions) {
        this.contributions = contributions;
    }
    public SortedSet<T> getContributions() {
        return contributions;
    }
    public void addProductionSystem(Production p) {
        contributions.add((T) p);
        IMDB.productionsList.add(p);
        p.setContributor(this);

        this.updateExperience(new AddProduction());
    }
    public void addActorSystem(Actor a) {
        contributions.add((T) a);
        IMDB.actorsList.add(a);
        a.setContributor(this);

        this.updateExperience(new AddProduction());
    }
    public void removeProductionSystem(Production p) {
        IMDB.productionsList.remove(p);
        contributions.remove(p);
    }
    public void removeActorSystem(Actor a) {
        IMDB.actorsList.remove(a);
        contributions.remove(a);
    }
    public void updateProduction(Production p) {
        for (Production production : IMDB.productionsList) {
            if (production.title.equals(p.title)) {
                production = p;
                break;
            }
        }
    }
    public void updateActor(Actor a) {
        for (Actor actor : IMDB.actorsList) {
            if (actor.getName().equals(a.getName())) {
                actor = a;
                break;
            }
        }
    }
    @Override
    public void displayInfo() {
        super.displayInfo();

        System.out.println("Contributions: ");
        for (Object contribution : contributions) {
            if (contribution instanceof Production) {
                Production contributionCasted = (Production) contribution;

                if (contributionCasted instanceof Movie) {
                    System.out.println("    Type: Movie");
                } else if (contributionCasted instanceof Series) {
                    System.out.println("    Type: Series");
                }
                System.out.println("    Title: " + contributionCasted.title);
                System.out.println("    Description: " + contributionCasted.description);
                System.out.println("    Average rating: " + contributionCasted.averageRating);
                System.out.println();
            } else if (contribution instanceof Actor) {
                Actor contributionCasted = (Actor) contribution;

                System.out.println("    Type: Actor");
                System.out.println("    Name: " + contributionCasted.getName());
                System.out.println("    Biography: " + contributionCasted.getBiography());
                System.out.println();
            }
        }
    }
}
