import java.util.Arrays;

/**
 * @author Juri Adams (@4dams)
 * @author Felix Rein
 * 
 * @version 1.0.0-Snapshot
 */
public class Project extends ProjectComponent {
    @Override
    public void setHourlyRate(float hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public float getHourlyRate() {
        return this.hourlyRate;
    }

    @Override
    public ProjectComponent[] addComponent(ProjectComponent component) {
        this.components = Arrays.copyOf(this.components, this.components.length + 1);
        this.components[this.components.length - 1] = component;

        return this.components;
    }

    /**
     * Create a new Project
     * 
     * @param name        Name of the Project
     * @param description Description of the Project
     * @param hourRate    Hour Rate of the Project
     */
    public Project(String name, String description, float hourRate) {
        this.setName(name);
        this.setDescription(description);
        this.setHourlyRate(hourRate);
    }

    @Override
    public float berechneKosten() {
        float total = 0;

        // Iterate through the components of this project
        for (ProjectComponent component : this.components) {
            if (component instanceof Project) {
                // Run this method again for the subproject
                total += component.berechneKosten();
            } else if (component instanceof Task) {
                // Add all billed hours to the total
                total += component.getBilledHours() * this.hourlyRate;
            } else if (component instanceof Product) {
                // Add the production cost to the total
                total += component.getProductionCost();
            }
        }

        return total;
    }

    @Override
    public boolean hasChildren() {
        if (this.components != null && this.components.length > 0) {
            return true;
        }

        return false;
    }

    public String toString() {
        return String.format("PROJECT  %s, Stundensatz: %s €, Gesamtkosten: %s", this.getName(), this.getHourlyRate(),
                this.berechneKosten());
    }
}
