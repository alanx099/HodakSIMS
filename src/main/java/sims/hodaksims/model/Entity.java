package sims.hodaksims.model;

import java.util.Random;

public abstract class Entity {
    private long id;

    public Entity(){
        this.id = new Random().nextLong();
    }
    public long getId(){
        return this.id;
    }
    public void setId(long ids){
        this.id =ids;
    }

}
