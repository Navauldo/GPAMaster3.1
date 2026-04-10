package Backend;

public class User {
    private int id;
    private String name;
    private String password;

    public User(int id, String name, String password)
    {
        this.id=id;
        this.name=name;
        this.password=password;
    }

    public int getId(){return this.id;}
    public String getName(){return this.name;}
    public String getPassword(){return this.password;}

    //setters
    public void setPassword(String passnew){this.password=passnew;}
    public void setId(int newID){this.id=newID;}
    public void setName(String newname){this.name=newname;}
    public boolean login(int id, String Password){return false;}

}
