/**
 * @author Juri Adams (@4dams)
 * @author Felix Rein
 * 
 * @version 1.0.0-Snapshot
 * 
 *          Class Product
 *          extends ProjectComponent
 */
public class Product extends ProjectComponent {

    @Override
    public void setProductionCost(float productionCost) {
        if (productionCost <= 0)
            throw new IllegalArgumentException("`productionCost` must be a positive float");

        this.productionCost = productionCost;
    }

    @Override
    public float getProductionCost() {
        return this.productionCost;
    }

/**
 * Constructor for Project Object
 * 
 * @param name name 
 * @param description description
 * @param productionCost Production Cost
 */
    public Product(String name, String description, float productionCost) {
        this.setName(name);
        this.setDescription(description);
        this.setProductionCost(productionCost);
    }

    public String toString() {
        return String.format("PRODUCT  %s, Produktionskosten: %s €", this.getName(), this.getProductionCost());
    }
}
