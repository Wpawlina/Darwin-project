package darwin.model.animal;

public class AnimalCrazy extends AbstractAnimal {

    public AnimalCrazy(AnimalProperties properties) {
        super(properties);
    }

    @Override
    public AbstractAnimal createChildren(AbstractAnimal partner,int energyCost,int min ,int max){
        AnimalProperties childProperties=this.createAnimalProperties(partner,energyCost);
        AbstractAnimal child=new AnimalCrazy(childProperties);
        child.getProperties().getParents().add(this);
        child.getProperties().getParents().add(partner);
        child.mutate(min,max);
        this.addChildren(child);
        partner.addChildren(child);
        return child;

    };

    @Override
    protected int newIndexAfterMove(int index) {
        if(Math.random()<0.8)
        {
            return (int)(Math.random()*this.getProperties().getGenome().length);
        }
        else {
            return (index+1)%this.getProperties().getGenome().length;
        }
    }



}
