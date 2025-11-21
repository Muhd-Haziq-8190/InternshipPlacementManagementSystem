
package Project.DataReader;

import java.io.IOException;
import java.util.List;

public class UserListReader extends BaseListReader {

    public UserListReader(String folder, String fileName) {
        super(folder, fileName, "id,username,password,type,index");
    }

    public void insert(String id, String username, String password, String type, String index) {
        try {
            List<String[]> rows = readAll();
            rows.add(new String[]{id, username, password, type, index});
            writeAll(rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(String id, String username, String password, String type, String index) {
        try {
            List<String[]> rows = readAll();
            for (int i = 1; i < rows.size(); i++) {
                if (rows.get(i)[0].equals(id)) {
                    rows.set(i, new String[]{id, username, password, type, index});
                    break;
                }
            }
            writeAll(rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}