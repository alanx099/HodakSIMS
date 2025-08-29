package sims.hodaksims.model;

import java.util.Random;

public abstract class Entity {
    /**
     *Klasa koju će nasljeđivati svi entiteti
     *
     * <p>
     *     Ovu klasu će svi entiteti unutar aplikacije nasljeđivati kako bi imali id sustav za sve entitete
     *  </p>
     * @param id
     *
     */
    private Long id;

    protected Entity(){
        this.id = new Random().nextLong();
    }
    public Long getId(){
        return this.id;
    }
    public void setId(Long ids){
        this.id =ids;
    }

}
