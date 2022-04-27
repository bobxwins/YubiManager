package sample;

import java.io.Serializable;


public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

   Group(String nameString, int groupID ) {
        this.name=nameString;
        this.ID =groupID ;

    }
    String name;
      int ID;

    public String getName() {

        return name;
    }

    public int getID() {

        return ID;
    }


    public void setName(String name) {

        this.name = name;
    }

    public void setID(int ID) {

        this.ID = ID;
    }


}

