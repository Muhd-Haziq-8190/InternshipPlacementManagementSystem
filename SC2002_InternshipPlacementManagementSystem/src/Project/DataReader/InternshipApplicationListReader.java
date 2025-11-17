package Project.DataReader;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import Project.CareerStaff;
import Project.CompanyRepresentative;
import Project.Internship;
import Project.InternshipApplication;
import Project.Student;

public class InternshipApplicationListReader {
        private final String filePath;
        
        // constructor, create internshipApplication_list.csv if doesn't exist
        public InternshipApplicationListReader(String folder, String fileName) {
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
                    writer.write("InternshipApplicationID,StudentID,InternshipID,Status,isAccepted,withdrawalRequested\n");

//                    writer.write("CompanyRepID,Company,Name,Department,Position,Status,InternshipIDs,InternshipCount\n");
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
        
        public void insert(InternshipApplication application) {
            try {
                List<String[]> rows = readAll();
                
//                rows.add(new String[]{companyRep.getId(),companyRep.getCompanyName(), companyRep.getName(), companyRep.getDepartment(), companyRep.getPosition(), companyRep.getAccountStatus(), companyRep.getInternshipIds(), String.valueOf(companyRep.getInternshipCount())});
                rows.add(new String[]{
                		application.getId(),
                		application.getStudent().getId(),
                		application.getInternship().getId(),
                		application.getStatus(),
                		String.valueOf(application.isAccepted()),
                		String.valueOf(application.isWithdrawalRequested())
                });
                writeAll(rows);
                //System.out.println("插入成功！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void update(InternshipApplication application) {
            try {
            	List<String[]> rows = readAll();
                
                for (int i = 1; i < rows.size(); i++) {
                    if (rows.get(i)[0].equals(application.getId())) {
                        rows.set(i, new String[]{
                        		application.getId(),
                        		application.getStudent().getId(),
                        		application.getInternship().getId(),
                        		application.getStatus(),
                        		String.valueOf(application.isAccepted()),
                        		String.valueOf(application.isWithdrawalRequested())
                        });
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
        
        // find by header, return InternshipApplication obj
        public InternshipApplication findInternshipApplication(String col, String value) {
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
                
                InternshipListReader internshipDb = new InternshipListReader("data", "internship_list.csv");
                StudentListReader studentDb = new StudentListReader("data", "student_list.csv");

                /** find value */
                for (int i = 1; i < rows.size(); i++) {
                    String[] row = rows.get(i);
                    if (row[colNum].equals(value)) {
                    	InternshipApplication application = new InternshipApplication(
                    			row[0], 	// InternshipApplicationID
                    			studentDb.findStudent("StudentID", row[1]), 	// StudentID convert to Student obj
                    			internshipDb.findInternship("InternshipID", row[2]), 	// InternshipID convert to Internship obj
                    			row[3], 		// Status
                    			Boolean.parseBoolean(row[4]), 		// isAccepted
                    			Boolean.parseBoolean(row[5]) 		// withdrawalRequested
                    	);
                    	return application;
                    	
                    }
                }
                
                return null;		// Not found
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        
        // retrieve multiple internships from a string like "APP1|APP2|APP3"
        public InternshipApplication[] getApplicationsFromIds(String applicationIdsString) {

            if (applicationIdsString == null || applicationIdsString.isEmpty()) {
                return new InternshipApplication[0];
            }

            // Split IDs by "|"
            String[] ids = applicationIdsString.split("\\|");

            List<InternshipApplication> result = new ArrayList<>();

            for (String id : ids) {
                id = id.trim();
                if (id.isEmpty()) continue;

                InternshipApplication application = findInternshipApplication("InternshipApplicationID", id);

                if (application != null) {
                    result.add(application);
                }
            }

            return result.toArray(new InternshipApplication[0]);
        }
        
        public String[] findApplicationRaw(String applicationId) {
            try {
                List<String[]> rows = readAll();
                for (int i = 1; i < rows.size(); i++) {
                    if (rows.get(i)[0].equals(applicationId)) {
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

            InternshipApplicationListReader db =
                    new InternshipApplicationListReader("data", "internshipApplication_list.csv");

            System.out.println("=== Initial Data ===");
            db.printAll();


            // ---------------------------------------------------
            // Create Students
            // ---------------------------------------------------
            Student s1 = new Student("U1234567A", "John", 2, "CS", "soup@1.com");
            Student s2 = new Student("U2345678B", "Mary", 3, "IS", "fish@2.net");


            // ---------------------------------------------------
            // Create Company Representative
            // ---------------------------------------------------
            CompanyRepresentative rep = new CompanyRepresentative(
                    "alice@google.com",
                    "Alice",
                    "Google",
                    "Tech",
                    "Lead Engineer"
            );
            rep.setAccountStatus("Approved");


            // ---------------------------------------------------
            // Create Internship using createInternship()
            // ---------------------------------------------------
            Internship internship = rep.createInternship(
                    "Software Engineering Intern",
                    "Develop backend services",
                    "Intermediate",
                    "Computer Science",
                    LocalDate.now().minusDays(2),
                    LocalDate.now().plusDays(40),
                    10
            );


            // ---------------------------------------------------
            // Create Internship Applications (using your constructor)
            // ---------------------------------------------------
            InternshipApplication app1 = new InternshipApplication(s1, internship);
            InternshipApplication app2 = new InternshipApplication(s2, internship);

            // connect applications to internship
            internship.addApplication(app1);
            internship.addApplication(app2);


            // ---------------------------------------------------
            // INSERT TEST
            // ---------------------------------------------------
            System.out.println("\n=== INSERT TEST ===");

//            db.insert(app1);
//            db.insert(app2);

            System.out.println("After inserting:");
            db.printAll();


            // ---------------------------------------------------
            // UPDATE TEST
            // Let's modify app2
            // ---------------------------------------------------
            System.out.println("\n=== UPDATE TEST (modify Mary’s application) ===");

            app2.setStatus("Successful");
            app2.setWithdrawalRequested(true);

            // mark as accepted
            // (only works if status is Successful)
            app2.acceptPlacement();  // optional but valid for testing

//            db.update(app2);

            System.out.println("After updating app2:");
            db.printAll();


            // ---------------------------------------------------
            // DELETE TEST (Optional)
            // ---------------------------------------------------
//             db.delete(app1.getId());
//             System.out.println("\nAfter deleting app1:");
//             db.printAll();
        }






}


