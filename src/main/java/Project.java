import java.util.Arrays;

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

    public Project(String name, String description, float hourRate) {
        this.setName(name);
        this.setDescription(description);
        this.setHourlyRate(hourRate);
    }

    /**
     * Calculate the cost of this Project
     */
    @Override
    public float berechneKosten() {
        float total = 0;

        // Iterate through the components of this project
        for (ProjectComponent component : this.components) {
            if (component instanceof Project) {
                System.out.println("DEBUG  | component is of type `Project`");

                // Run this method again for the subproject
                total += component.berechneKosten();
            } else if (component instanceof Task) {
                System.out.println("DEBUG  | component is of type `Task`");

                // Add all billed hours to the total
                total += component.getBilledHours() * this.hourlyRate;
            } else if (component instanceof Product) {
                System.out.println("DEBUG  | component is of type `Product`");

                // Add the production cost to the total
                total += component.getProductionCost();
            }
        }

        System.out.println(String.format("INFO   | calculated total: %s", total));

        return total;
    }

}
