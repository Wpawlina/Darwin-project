package darwin.model.animal;

public class Animal extends AbstractAnimal {

    public Animal(AnimalProperties properties) {
        super(properties);
    }

    @Override
    public AbstractAnimal createChildren(AbstractAnimal partner,int energyCost,int min ,int max){
        AnimalProperties childProperties=this.createAnimalProperties(partner,energyCost);
        AbstractAnimal child=new Animal(childProperties);
        child.getProperties().getParents().add(this);
        child.getProperties().getParents().add(partner);
        child.mutate(min,max);
        this.addChildren(child);
        partner.addChildren(child);
        return child;

    };

    @Override
    protected int newIndexAfterMove(int index) {
        return (index+1)%8;
    }







}
