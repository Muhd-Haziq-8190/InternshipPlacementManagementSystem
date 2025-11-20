package Project.DataReader;

import java.io.*;
import java.util.*;

public class UserListReader {
        private final String filePath;
        public UserListReader(String folder, String fileName) {
            // make sure file exist
            File dir = new File(folder);
            if (!dir.exists()) {
                dir.mkdirs(); // 创建文件夹
                System.out.println("已创建数据目录：" + dir.getAbsolutePath());
            }

            this.filePath = folder + File.separator + fileName;

            // if file not exist, create one
            File file = new File(filePath);
            if (!file.exists()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("id,username,password,type,index\n");
                    System.out.println("已创建数据文件：" + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        public List<String[]> readAll() throws IOException {
            List<String[]> rows = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    rows.add(line.split(","));
                }
            }
            return rows;
        }


        private void writeAll(List<String[]> rows) throws IOException {
            try (FileWriter writer = new FileWriter(filePath)) {
                for (String[] row : rows) {
                    writer.write(String.join(",", row) + "\n");
                }
            }
        }


        public void printAll() {
            try {
                List<String[]> rows = readAll();
                for (String[] row : rows) {
                    System.out.println(String.join("\t", row));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void insert(String id,String username, String password, String type , String index) {
            try {
                List<String[]> rows = readAll();
                rows.add(new String[]{id,username, password , type, index});
                writeAll(rows);
                //System.out.println("插入成功！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void update(String id,String username, String password, String type , String index) {
            try {
                List<String[]> rows = readAll();
                for (int i = 1; i < rows.size(); i++) {
                    if (rows.get(i)[0].equals(id)) {
                        rows.set(i, new String[]{id,username, password, type, index});
                        break;
                    }
                }
                writeAll(rows);
                //System.out.println("更新成功！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void delete(String id) {
            try {
                List<String[]> rows = readAll();
                rows.removeIf(row -> !row[0].equals("id") && row[0].equals(id));
                writeAll(rows);
                //System.out.println("删除成功！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public  List<String> find(String col, String value) {
            try {
                List<String[]>rows = readAll();
                if(rows.isEmpty()) return Collections.emptyList();
                /** find col */
                int colNum = -1;
                String[] header = rows.get(0);
                for (int i = 1; i < header.length; i++) {
                    if(header[i].equals(col)) {colNum = i;}
                }
                if(colNum == -1) return Collections.emptyList();
                /** find value */
                for (int i = 1; i < rows.size(); i++) {
                    String[] row = rows.get(i);
                    if (row[colNum].equals(value)) {return Arrays.asList(row);}
                }
                return Collections.emptyList();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Collections.emptyList();
        }


        /** test */
        
        public static void main(String[] args) {
            UserListReader db = new UserListReader("data", "userlist.csv");

            System.out.println("当前数据：");
            db.printAll();

            System.out.println("\n 插入：");
            db.insert("3","C", "123", "student", "1");
            //db.delete("1");
            System.out.println("\n 插入后：");
            db.printAll();
        }
}


