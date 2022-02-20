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

    public Task(String name, String description, int billedHours) {
        this.setName(name);
        this.setDescription(description);
        this.setBilledHours(billedHours);
    }

    public String toString() {
        return String.format("TASK     %s, Arbeitsaufwand: %sh", this.getName(), this.getBilledHours());
    }
}
