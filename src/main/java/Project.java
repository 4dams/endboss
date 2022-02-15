public class Project extends ProjectComponent {
    private ProjectComponent[] components;

    @Override
    public void setHourlyRate(float hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public float getHourlyRate() {
        return this.hourlyRate;
    }

    /**
     * Calculate the cost of this Project
     */
    public float berechneKosten() {
        float total = 0;

        // Iterate through the components of this project
        for (ProjectComponent component : this.components) {
            if (component instanceof Project) {
                System.out.println("DEBUG | component is of type `Project`");

                // Run this method again for the subproject
                total += component.berechneKosten();
            } else if (component instanceof Task) {
                System.out.println("DEBUG | component is of type `Task`");

                // Add all billed hours to the total
                total += component.getBilledHours() * this.hourlyRate;
            } else if (component instanceof Product) {
                System.out.println("DEBUG | component is of type `Product`");

                // Add the production cost to the total
                total += component.getProductionCost();
            }
        }

        System.out.println(String.format("INFO | calculated total: %s", total));

        return total;
    }

}
