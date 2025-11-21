package Project;

import java.io.*;
import java.util.*;

/** attention!!!!!!
 * this will cover all user's password to default
 */


public class UserListCreate {

    public static void main(String[] args) {
        new UserListCreate().run();
    }

    public void run() {
        String dataFolder = "data";

        try {
            List<String[]> users = new ArrayList<>();
            users.add(new String[]{"id", "username", "password", "type", "index"});

            syncUsers(users, dataFolder + "/student_list.csv", "student");
            syncUsers(users, dataFolder + "/staff_list.csv", "staff");
            syncUsers(users, dataFolder + "/company_list.csv", "company");
            writeUserList(dataFolder + "/userlist.csv", users);

        } catch (Exception e) {
            System.err.println("同步失败: " + e.getMessage());
        }
    }

    private void syncUsers(List<String[]> users, String filePath, String userType) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("文件不存在: " + filePath);
                return;
            }

            List<String[]> rows = readCSV(file);
            if (rows.size() <= 1) {
                System.out.println(userType + " 列表为空");
                return;
            }

            int count = 0;
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                if (row.length > 0 && !row[0].trim().isEmpty()) {
                    String id = row[0].trim();
                    users.add(new String[]{id, id, "password", userType, id});
                    count++;
                }
            }

            System.out.println("已同步 " + count + " 个" + userType);

        } catch (Exception e) {
            System.err.println("同步 " + userType + " 时出错: " + e.getMessage());
        }
    }

    private List<String[]> readCSV(File file) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line.split(","));
            }
        }
        return rows;
    }

    private void writeUserList(String filePath, List<String[]> users) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (String[] user : users) {
                writer.println(String.join(",", user));
            }
        }
    }
}