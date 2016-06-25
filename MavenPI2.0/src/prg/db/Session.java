package prg.db;

import java.io.Serializable;
import java.util.Hashtable;

public class Session implements Serializable {

    private static final long serialVersionUID = 752647198969828L;
    private int id;
    private String userId = null;
    public boolean validUser = false;
    private String fName = null;
    private String mName = null;
    private String lName = null;
    private String uType = null;
    private Hashtable clsObjects = new Hashtable();

    /**
     * Creates a new instance of Session
     */
    public Session() {
    }

    public int getId() {
        return id;
    }

    public void setId(int Id) {
        this.id = Id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Object getObject(String clsName) {
        Object object = null;
        object = clsObjects.get(clsName);
        return object;
    }

    public void setObject(Object object) {
        java.lang.Class cls = object.getClass();
        String clsName = cls.getName();
        clsObjects.put(clsName, object);

    }

    public boolean isValidUser() {
        return validUser;
    }

    public void setFirstName(String fname) {
        this.fName = fname;
    }

    public String getFirstName() {
        return this.fName;
    }

    public void setMiddleName(String mname) {
        this.mName = mname;
    }

    public String getMiddleName() {
        return this.mName;
    }

    public void setLastName(String lname) {
        this.fName = lname;
    }

    public String getLastName() {
        return this.lName;
    }

    public void setUserType(String utype) {
        this.uType = utype;
    }

    public String getUserType() {
        return this.uType;
    }
}
