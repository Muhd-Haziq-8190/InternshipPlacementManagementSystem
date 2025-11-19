package Project.DataReader;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import Project.classes.CareerStaff;
import Project.classes.CompanyRepresentative;
import Project.classes.Internship;

public class CompanyListReader {
        private final String filePath;
        
        
        // constructor, create company_list.csv if doesn't exist
        public CompanyListReader(String folder, String fileName) {
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
                    writer.write("CompanyRepID,Company,Name,Department,Position,Status,InternshipIDs,InternshipCount\n");
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

        public void insert(CompanyRepresentative companyRep) {
            try {
                List<String[]> rows = readAll();
                
                rows.add(new String[]{companyRep.getId(),companyRep.getCompanyName(), companyRep.getName(), companyRep.getDepartment(), companyRep.getPosition(), companyRep.getAccountStatus(), companyRep.getInternshipIds(), String.valueOf(companyRep.getInternshipCount())});
                writeAll(rows);
                //System.out.println("插入成功！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void update(CompanyRepresentative companyRep) {
            try {
            	List<String[]> rows = readAll();
                
                for (int i = 1; i < rows.size(); i++) {
                    if (rows.get(i)[0].equals(companyRep.getId())) {
                        rows.set(i, new String[]{companyRep.getId(),companyRep.getCompanyName(), companyRep.getName(), companyRep.getDepartment(), companyRep.getPosition(), companyRep.getAccountStatus(), companyRep.getInternshipIds(), String.valueOf(companyRep.getInternshipCount())});
                        break;
                    }
                }
                writeAll(rows);
                //System.out.println("更新成功！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
//        public void update(String id,String company, String name, String department, String position, String status) {
//            try {
//                List<String[]> rows = readAll();
//                for (int i = 1; i < rows.size(); i++) {
//                    if (rows.get(i)[0].equals(id)) {
//                        rows.set(i, new String[]{id,company, name, department , position, status});
//                        break;
//                    }
//                }
//                writeAll(rows);
//                //System.out.println("更新成功！");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }


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
        
        
        public String[] findCompanyRepRaw(String repId) {
            try {
                List<String[]> rows = readAll();
                for (int i = 1; i < rows.size(); i++) {
                    if (rows.get(i)[0].equals(repId)) {
                        return rows.get(i);
                    }
                }
                return null;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        


        /** test */

        public static void main(String[] args) {

            CompanyListReader db = new CompanyListReader("data", "company_list.csv");

            System.out.println("=== Initial Data ===");
            db.printAll();


            // ----------------------------------------------------
            // CREATE SAMPLE REPRESENTATIVES
            // ----------------------------------------------------
            CompanyRepresentative rep1 =
                    new CompanyRepresentative("alice@google.com", "Alice", "Google", "Tech", "Lead Engineer");

            CompanyRepresentative rep2 =
                    new CompanyRepresentative("bob@microsoft.com", "Bob", "Microsoft", "Sales", "Manager");

            rep1.setAccountStatus("Approved");
            rep2.setAccountStatus("Pending");


            // ----------------------------------------------------
            // CREATE INTERNSHIPS using createInternship()
            // ----------------------------------------------------
            Internship i1 = rep1.createInternship(
                    "Software Intern", "Backend API development",
                    "Intermediate", "Computer Science",
                    LocalDate.now().minusDays(1),  // opened yesterday
                    LocalDate.now().plusDays(30),  // closes in 30 days
                    10
            );

            Internship i2 = rep1.createInternship(
                    "Data Science Intern", "ML pipeline work",
                    "Advanced", "Computer Science",
                    LocalDate.now().minusDays(2),
                    LocalDate.now().plusDays(25),
                    10
            );

            Internship i3 = rep2.createInternship(
                    "Marketing Intern", "Brand and outreach",
                    "Basic", "Marketing",
                    LocalDate.now().minusDays(3),
                    LocalDate.now().plusDays(20),
                    10
            );

            // ----------------------------------------------------
            // TEST getInternshipIds()
            // ----------------------------------------------------
            System.out.println("rep1 internship IDs = " + rep1.getInternshipIds());
            System.out.println("rep2 internship IDs = " + rep2.getInternshipIds());


            // ----------------------------------------------------
            // INSERT INTO CSV
            // ----------------------------------------------------
//            db.insert(rep1);
//            db.insert(rep2);

            System.out.println("\n=== After Insert ===");
            db.printAll();


            // ----------------------------------------------------
            // UPDATE TEST
            // ----------------------------------------------------
            System.out.println("\n=== UPDATE TEST ===");

            // Change Bob
            rep2.setAccountStatus("Approved");
            rep2.setDepartment("Marketing");
            rep2.setPosition("Senior Manager");

            // Add another internship for Bob using createInternship
            Internship i4 = rep2.createInternship(
                    "Product Intern", "Assist PM team",
                    "Intermediate", "Marketing",
                    LocalDate.now().minusDays(1),
                    LocalDate.now().plusDays(10),
                    10
            );

            System.out.println("Updated Bob internship IDs = " + rep2.getInternshipIds());

//            db.update(rep2);

            System.out.println("\n=== After Updating Bob ===");
            db.printAll();
            
            db.delete("alice@google.com");
        }




}


