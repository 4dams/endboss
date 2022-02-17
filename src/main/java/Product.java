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

public Product(String name, String description, float productionCost) {  
        this.setName(name);
        this.setDescription(description);
        this.setProductionCost(productionCost);    
}

}
