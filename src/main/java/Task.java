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

}
