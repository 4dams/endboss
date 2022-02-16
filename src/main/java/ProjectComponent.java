import exceptions.NotImplementedException;

public abstract class ProjectComponent {
    /**
     * Name of the Component
     */
    public String name;

    /**
     * Description of the Component
     */
    public String description;

    /**
     * Billed hours for a Task
     * ⚠️ Only available inside Tasks
     */
    public int billedHours;

    /**
     * Production cost for a Product
     * ⚠️ Only available inside Products
     */
    public float productionCost;

    /**
     * The hourly rate for Tasks inside a Project
     * ⚠️ Only available inside Projects
     */
    public float hourlyRate;

    /**
     * Array of components of a Project
     * ⚠️ Only available inside Projects
     */
    public ProjectComponent[] components = {};

    public ProjectComponent[] addComponent(ProjectComponent component) {
        throw new NotImplementedException("method not implemented");
    };

    /**
     * Set the name of the Component
     * 
     * @param name String containing the new name of the Component
     */
    public void setName(String name) {
        if (name.trim().isEmpty())
            throw new IllegalArgumentException("`name` must not be empty");

        this.name = name;
    };

    /**
     * Set the description of the Component
     * 
     * @param description String containing the description of the Component
     */
    public void setDescription(String description) {
        if (description.trim().isEmpty())
            throw new IllegalArgumentException("`description` must not be empty");

        this.description = description;
    };

    /**
     * Set the billed hours of a Task
     * ⚠️ Only available inside Tasks
     * 
     * @param billedHours Int containing the billed hours of a task
     */
    public void setBilledHours(int billedHours) {
        throw new NotImplementedException("method not implemented");
    };

    /**
     * Set the production cost of a Product
     * ⚠️ Only available inside Products
     * 
     * @param productionCost Float containing the production cost of the Product
     */
    public void setProductionCost(float productionCost) {
        throw new NotImplementedException("method not implemented");
    };

    /**
     * Set the hourly rate for Tasks inside a Project
     * ⚠️ Only available inside Projects
     * 
     * @param hourlyRate Float containing the hourly rate for Tasks inside a Project
     */
    public void setHourlyRate(float hourlyRate) {
        throw new NotImplementedException("method not implemented");
    };

    /**
     * Get the name of the Component
     * 
     * @return String containing the name of the Component
     */
    public String getName() {
        return this.name;
    };

    /**
     * Get the description of the Component
     * 
     * @return String containing the description of the Component
     */
    public String getDescription() {
        return this.description;
    };

    /**
     * Get the billed hours of a Task
     * ⚠️ Only available inside Tasks
     * 
     * @return Int containing the billed hours of a task
     */
    public int getBilledHours() {
        throw new NotImplementedException("method not implemented");
    };

    /**
     * Get the production cost of a Product
     * ⚠️ Only available inside Products
     * 
     * @return Float representing the production cost of the Product
     */
    public float getProductionCost() {
        throw new NotImplementedException("method not implemented");
    };

    /**
     * Get the hourly rate for Tasks inside a Project
     * ⚠️ Only available inside Projects
     * 
     * @return Float representing the hourly rate for Tasks inside a Project
     */
    public float getHourlyRate() {
        throw new NotImplementedException("method not implemented");
    };

    /**
     * Calculate the cost of the Component and its Subcomponents
     * 
     * @return float representing the cost of this Component and its Subcomponents
     */
    public float berechneKosten() {
        throw new NotImplementedException("method not implemented");
    };
}
