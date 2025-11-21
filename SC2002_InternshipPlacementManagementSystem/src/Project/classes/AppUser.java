package Project.classes;

import java.util.List;
import Project.DataReader.*;

public class AppUser {
    private String id;
    private  String username;
    private  String password;
    private  String type;
    private  String index;

    public AppUser(List<String> row) {
        this.id = row.get(0);
        this.username = row.get(1);
        this.password = row.get(2);
        this.type = row.get(3);
        this.index = row.get(4);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void changePassword(String password) {
        this.password = password;
        UserListReader userListReader=new UserListReader("data", "userlist.csv");
        userListReader.update(id,username,password,type,index);
    }


}
