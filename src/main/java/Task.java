/**
 * @author Juri Adams (@4dams)
 * @author Felix Rein
 * 
 * @version 1.0.0-Snapshot
 */
public class Task extends ProjectComponent {
    @Override
    public void setBilledHours(int billedHours) {
        if (billedHours <= 0)
            throw new IllegalArgumentException("`billedHours` must be a positive integer");

        this.billedHours = billedHours;
    }

    @Override
    public int getBilledHours() {
        return this.billedHours;
    }

    /**
     * Constructor for Task Object
     * 
     * @param name        name
     * @param description description
     * @param billedHours Money per hour
     * 
     */
    public Task(String name, String description, int billedHours) {
        this.setName(name);
        this.setDescription(description);
        this.setBilledHours(billedHours);
    }

    public String toString() {
        return String.format("TASK     %s, Arbeitsaufwand: %sh", this.getName(), this.getBilledHours());
    }
}
