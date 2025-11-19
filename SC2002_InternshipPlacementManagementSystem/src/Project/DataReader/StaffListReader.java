package Project.DataReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import Project.classes.CareerStaff;

public class StaffListReader {
        private final String filePath;
        public StaffListReader(String folder, String fileName) {
            // 确保文件夹存在
            File dir = new File(folder);
            if (!dir.exists()) {
                dir.mkdirs(); // 创建文件夹
                System.out.println("已创建数据目录：" + dir.getAbsolutePath());
            }

            this.filePath = folder + File.separator + fileName;

            // 如果文件不存在，则创建并写入表头
            File file = new File(filePath);
            if (!file.exists()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("StaffID,Name,Role,Department\n");
//                    System.out.println("已创建数据文件：" + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /** 读取整个表格 */
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
        
        public List<String[]> readWithoutHeader() throws IOException {
            List<String[]> rows = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                boolean isFirstLine = true;
                while ((line = br.readLine()) != null) {
                    if (isFirstLine) { 
                        isFirstLine = false; 
                        continue; // skip header
                    }
                    rows.add(line.split(","));
                }
            }
            return rows;
        }

        /** 写回文件 */
        private void writeAll(List<String[]> rows) throws IOException {
            try (FileWriter writer = new FileWriter(filePath)) {
                for (String[] row : rows) {
                    writer.write(String.join(",", row) + "\n");
                }
            }
        }

        /** 查询所有 */
        public void selectAll() {
            try {
                List<String[]> rows = readAll();
                for (String[] row : rows) {
                    System.out.println(String.join("\t", row));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /** 插入记录 */
        public void insert(CareerStaff careerStaff) {
            try {
                List<String[]> rows = readAll();

                rows.add(new String[]{
                		careerStaff.getId(),
                		careerStaff.getName(),
                		careerStaff.getRole(),
                		careerStaff.getDepartment()                		
                });

                writeAll(rows);
//                System.out.println("插入成功！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        

        /** 更新记录 */
        public void update(CareerStaff careerStaff) {
            try {
                List<String[]> rows = readAll();
                for (int i = 1; i < rows.size(); i++) {
                    if (rows.get(i)[0].equals(careerStaff.getId())) {
                        rows.set(i, new String[]{
                        		careerStaff.getId(),
                        		careerStaff.getName(),
                        		careerStaff.getRole(),
                        		careerStaff.getDepartment()	
                        });
                        break;
                    }
                }
                writeAll(rows);
//                System.out.println("更新成功！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /** 删除记录 */
        public void delete(String id) {
            try {
                List<String[]> rows = readAll();
                rows.removeIf(row -> !row[0].equals("id") && row[0].equals(id));
                writeAll(rows);
                System.out.println("删除成功！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        
        // find header, return String
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
        
        // find by header, return CareerStaff obj
        public CareerStaff findCareerStaff(String col, String value) {
            try {
                List<String[]>rows = readAll();
                if(rows.isEmpty()) return null;
                /** find col */
                int colNum = -1;
                String[] header = rows.get(0);
                for (int i = 0; i < header.length; i++) {
                    if(header[i].equals(col)) {colNum = i;}
                }
                if(colNum == -1) {
                	return null;
                };
                
                /** find value */
                for (int i = 1; i < rows.size(); i++) {
                    String[] row = rows.get(i);
                    if (row[colNum].equals(value)) {
                    	CareerStaff careerStaff = new CareerStaff(
                    			row[0], 	// StaffID
                    			row[1], 	// Name
                    			row[2], 	// Role
                    			row[3] 		// Department
                    	);
                    	return careerStaff;
                    	
                    }
                }
                
                return null;		// Not found
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        
        // return all staff records as List<CareerStaff>
        public List<CareerStaff> getAllCareerStaff() {
            List<CareerStaff> staffList = new ArrayList<>();

            try {
                List<String[]> rows = readAll();
                if (rows.size() <= 1) return staffList; 

                for (int i = 1; i < rows.size(); i++) {
                    String[] row = rows.get(i);

//                    if (row.length < 4) continue;

                    CareerStaff cs = new CareerStaff(
                            row[0],  // StaffID
                            row[1],  // Name
                            row[2],  // Role
                            row[3]   // Department
                    );

                    staffList.add(cs);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return staffList;
        }
        
        // find all based on column value (eg. all those in Department of Science)
        public List<CareerStaff> getCareerStaffBy(String columnName, String value) {

            List<CareerStaff> careerStaffs = new ArrayList<>();

            try {
                List<String[]> rows = readAll();
                if (rows.size() <= 1) return careerStaffs;

                String[] header = rows.get(0);

                // Find matching column index
                int colIndex = -1;
                for (int i = 0; i < header.length; i++) {
                    if (header[i].equalsIgnoreCase(columnName)) {
                        colIndex = i;
                        break;
                    }
                }
                if (colIndex == -1) return careerStaffs;  // column not found

                // Search rows
                for (int i = 1; i < rows.size(); i++) {
                    String[] row = rows.get(i);

                    if (row.length < 4) continue; // skip bad rows

                    if (row[colIndex].equals(value)) {
                    	careerStaffs.add(new CareerStaff(
                                row[0],  // StaffID
                                row[1],  // Name
                                row[2],  // Role
                                row[3]   // Department
                        ));
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return careerStaffs;
        }


        /** test */

        public static void main(String[] args) {

            StaffListReader db = new StaffListReader("data", "staff_list.csv");

            System.out.println("=== INITIAL CONTENT ===");
            db.selectAll();
            
            
            // ----------------------------------------
            // Create staff objects
            // ----------------------------------------
//            CareerStaff s1 = new CareerStaff("STF001", "Alice Tan", "Manager", "Career Office");
//            CareerStaff s2 = new CareerStaff("STF002", "Bob Lee", "Advisor", "Student Support");
//
//            // ----------------------------------------
//            // INSERT TEST
//            // ----------------------------------------
//            System.out.println("\n=== INSERT TEST ===");
//            db.insert(s1);
//            db.insert(s2);
//
//            System.out.println("After Insert:");
//            db.selectAll();

            // ----------------------------------------
            // UPDATE TEST
            // ----------------------------------------
//            System.out.println("\n=== UPDATE TEST ===");
//            s1.setRole("Senior Manager");   // modify something
//            s1.setDepartment("Student Career Office");
//
//            db.update(s1);
//
//            System.out.println("After Update:");
//            db.selectAll();

            // ----------------------------------------
            // FIND TEST
            // ----------------------------------------
//            System.out.println("\n=== FIND TEST (find by Role: 'Advisor') ===");
//            
//            List<String> result = db.find("Role", "Advisor");
//            if (result.isEmpty()) {
//                System.out.println("No staff found with that role.");
//            } else {
//                System.out.println(String.join(" | ", result));
//            }

            // ----------------------------------------
            // DELETE TEST
            // ----------------------------------------
//            System.out.println("\n=== DELETE TEST (delete STF002) ===");
//            db.delete("STF002");
//
//            System.out.println("After Delete:");
//            db.selectAll();
            
            // Search test
            System.out.println(db.findCareerStaff("StaffID", "STF001").getId());
//            
//            //getAll test
//            List<CareerStaff> staffList = db.getAllCareerStaff();
//            
//            for(int i = 0; i < staffList.size(); i++) {
//            	 System.out.println(staffList.get(i).getId() + staffList.get(i).getName() + staffList.get(i).getRole() + staffList.get(i).getDepartment());
//            }

            // retrieve all based on header
            List<CareerStaff> deptStaff = db.getCareerStaffBy("Department", "Student Career Office");

            for (CareerStaff cs : deptStaff) {
                System.out.println(cs.getId() + " | " + cs.getName());
            }
           
        }




}


