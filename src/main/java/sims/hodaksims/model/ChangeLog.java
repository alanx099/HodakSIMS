package sims.hodaksims.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.interfaces.Logable;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChangeLog implements Serializable {
    private UserRoles role;
    private String promjena;
    private LocalDateTime dateLog;
    private static final Logger log = LoggerFactory.getLogger(ChangeLog.class);


    public ChangeLog(UserRoles role, String promjena, LocalDateTime dateLog) {
        this.role = role;
        this.promjena = promjena;
        this.dateLog = dateLog;
    }

    public UserRoles getRoleNew() {
        return role;
    }

    public void setRoleNew(UserRoles role) {
        this.role = role;
    }

    public String getPromjena() {
        return promjena;
    }

    public void setPromjena(String promjena) {
        this.promjena = promjena;
    }

    public LocalDateTime getDateLog() {
        return dateLog;
    }


    public void setDateLog(LocalDateTime dateLog) {
        this.dateLog = dateLog;
    }
    /**
     * Metoda za spremanje promjene od korisnika
     * @throws IOException
     */
    public void saveChangeLog() throws IOException {
        List<ChangeLog> changes = loadChangeLog();

        try(FileOutputStream fileOut = new FileOutputStream("changes.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut)){
            changes.add(this);
            out.writeObject(changes);
        }
    }

    public static List<ChangeLog> loadChangeLog(){
        List<ChangeLog> changes = new ArrayList<>();
        try(FileInputStream filIn = new FileInputStream("changes.ser");
            ObjectInputStream in = new ObjectInputStream(filIn)){
            changes = (List<ChangeLog>) in.readObject();
        }catch (ClassNotFoundException | IOException e){
            log.error(e.getMessage());
        }
        return changes;
    }
    public<T extends Entity & Logable> void updateEntry(T oldObject, T newObject, String desc){
        try{
            String[] oldValue = oldObject.changesToString();
            String[] printNew = newObject.changesToString();
            Long  diffrence = Arrays.stream(printNew).count() - Arrays.stream(oldValue).count();
            this.setPromjena("Promjene nad objektom "+ desc+ " ID= " +oldObject.getId()+"\n");

            for(int i = 0; i< Arrays.stream(oldValue).count() ; i++){
                if( i< Arrays.stream(printNew).count()){
                    if(oldValue[i].compareTo(printNew[i]) != 0)  this.setPromjena( this.getPromjena() + oldValue[i]+"->"+printNew[i]+","+"\n" );
                }
                else{
                    this.setPromjena( this.getPromjena() + oldValue[i]+"-> deleted"+"\n" );
                }
            }
            if(diffrence.intValue() > 0){
            for(int i = Math.toIntExact(Arrays.stream(printNew).count())-1; i > diffrence.intValue()+1 ; i--){
                this.setPromjena( this.getPromjena() + printNew[i]+"-> added"+"\n" );
            }}


            this.saveChangeLog();
        }catch(IOException e){
            log.error(e.getMessage());
        }
    }

    public void newEntry(String desc){
            this.setPromjena("Stvoren novi "+desc+" objekt:"+this.getPromjena());
            try{
                this.saveChangeLog();
            }catch(IOException e){
                log.error(e.getMessage());
            }
    }
    public void delEntry(String desc){
        this.setPromjena("Obrisan "+desc+" objekt:"+this.getPromjena());
        try{
            this.saveChangeLog();
        }catch(IOException e){
            log.error(e.getMessage());
        }
    }

}
