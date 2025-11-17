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

public class InternshipListReader {
        private final String filePath;
        
        // constructor, create internship_list.csv if doesn't exist
        public InternshipListReader(String folder, String fileName) {
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
                    writer.write("InternshipID,Title,Description,Level,PreferredMajor,OpenDate,CloseDate,Status,CompanyName,CompanyRepresentativeID,AvailableSlots,isVisible,InternshipApplicationID\n");

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
        
        public void insert(Internship internship) {
            try {
                List<String[]> rows = readAll();
                
//                rows.add(new String[]{companyRep.getId(),companyRep.getCompanyName(), companyRep.getName(), companyRep.getDepartment(), companyRep.getPosition(), companyRep.getAccountStatus(), companyRep.getInternshipIds(), String.valueOf(companyRep.getInternshipCount())});
                rows.add(new String[]{
                		internship.getId(),
                		internship.getTitle(),
                		internship.getDescription(),
                		internship.getLevel(),
                		internship.getPreferredMajor(),
                		String.valueOf(internship.getOpenDate()),
                		String.valueOf(internship.getCloseDate()),
                		internship.getStatus(),
                		internship.getCompanyName(),
                		internship.getCompanyRepresentativeId(),
                		String.valueOf(internship.getSlotsAvailable()),
                		String.valueOf(internship.isVisible()),
                		internship.getApplicationIds()});
                writeAll(rows);
                //System.out.println("插入成功！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void update(Internship internship) {
            try {
            	List<String[]> rows = readAll();
                
                for (int i = 1; i < rows.size(); i++) {
                    if (rows.get(i)[0].equals(internship.getId())) {
                        rows.set(i, new String[]{
                        		internship.getId(),
                        		internship.getTitle(),
                        		internship.getDescription(),
                        		internship.getLevel(),
                        		internship.getPreferredMajor(),
                        		String.valueOf(internship.getOpenDate()),
                        		String.valueOf(internship.getCloseDate()),
                        		internship.getStatus(),
                        		internship.getCompanyName(),
                        		internship.getCompanyRepresentativeId(),
                        		String.valueOf(internship.getSlotsAvailable()),
                        		String.valueOf(internship.isVisible()),
                        		internship.getApplicationIds()
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
        
        // find by header, return Internship obj
        public Internship findInternship(String col, String value) {
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
                
                CompanyListReader companyRepDb = new CompanyListReader("data", "company_list.csv");
                InternshipApplicationListReader internshipAppDb = new InternshipApplicationListReader("data", "internshipApplication_list.csv");

                /** find value */
                for (int i = 1; i < rows.size(); i++) {
                    String[] row = rows.get(i);
                    if (row[colNum].equals(value)) {
                    	String appIds = (row.length > 12) ? row[12] : "";
                    	Internship internship = new Internship(
                    			row[0], 	// InternshipID
                    			row[1], 	// Title
                    			row[2], 	// Description
                    			row[3], 	// Level
                    			row[4], 	// PreferredMajor
                    			LocalDate.parse(row[5]), 	// OpenDate
                    			LocalDate.parse(row[6]), 	// CloseDate
                    			row[8], 	// CompanyName
                    			companyRepDb.findCompanyRepresentative("CompanyRepID", row[9]), 	// CompanyRepresentativeID convert to CompanyRepresentative obj
                    			row[7], 	// Status
                    			Boolean.parseBoolean(row[11]), 	// isVisible
                    			internshipAppDb.getApplicationsFromIds(appIds), 	// InternshipApplicationIDs convert to InternshipApplication[]
                    			Integer.parseInt(row[10]) 	// AvailableSlots
                    	);
                    	return internship;
                    	
                    }
                }
                
                return null;		// Not found
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        
        // retrieve multiple internships from a string like "INT1|INT2|INT3"
        public Internship[] getInternshipsFromIds(String internshipIdsString) {

            if (internshipIdsString == null || internshipIdsString.isEmpty()) {
                return new Internship[0];
            }

            // Split IDs by "|"
            String[] ids = internshipIdsString.split("\\|");

            List<Internship> result = new ArrayList<>();

            for (String id : ids) {
                id = id.trim();
                if (id.isEmpty()) continue;

                Internship internship = findInternship("InternshipID", id);

                if (internship != null) {
                    result.add(internship);
                }
            }

            return result.toArray(new Internship[0]);
        }

        
        
        public String[] findInternshipRaw(String internshipId) {
            try {
                List<String[]> rows = readAll();
                for (int i = 1; i < rows.size(); i++) {
                    if (rows.get(i)[0].equals(internshipId)) {
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

            // ------------------------------------
            // Setup InternshipListReader
            // ------------------------------------
            InternshipListReader db = new InternshipListReader("data", "internship_list.csv");

            System.out.println("=== Initial Data ===");
            db.printAll();


            // ------------------------------------
            // Create a company representative
            // ------------------------------------
            CompanyRepresentative rep = new CompanyRepresentative(
                    "alice@google.com",
                    "Alice",
                    "Google",
                    "Tech",
                    "Lead Engineer"
            );
            rep.setAccountStatus("Approved");


            // ------------------------------------
            // Create Students
            // ------------------------------------
            Student s1 = new Student("U1234567A", "John", 2, "Computer Science", "soup@fish.com");
            Student s2 = new Student("U2345678B", "Mary", 3, "Computer Engineering", "soup2@fish2.com2");
            Student s3 = new Student("U3456789C", "Alex", 3, "Computer Science", "soup3@fish3.com");


            // ------------------------------------
            // Create Internship via CompanyRepresentative
            // ------------------------------------
            Internship intern = rep.createInternship(
                    "Software Engineering Intern",
                    "Develop backend APIs",
                    "Intermediate",
                    "Computer Science",
                    LocalDate.now().minusDays(2),
                    LocalDate.now().plusDays(30),
                    5
            );

            // ------------------------------------
            // Add applications
            // ------------------------------------
            InternshipApplication app1 = new InternshipApplication(s1, intern);
            InternshipApplication app2 = new InternshipApplication(s2, intern);
            InternshipApplication app3 = new InternshipApplication(s3, intern);

            intern.addApplication(app1);
            intern.addApplication(app2);
            intern.addApplication(app3);

            System.out.println("\nApplication IDs: " + intern.getApplicationIds());


            // ------------------------------------
            // INSERT TEST
            // ------------------------------------
            System.out.println("\n=== INSERT TEST ===");
//            db.insert(intern);

            System.out.println("After inserting:");
            db.printAll();


            // ------------------------------------
            // UPDATE TEST
            // Modify something, then update()
            // ------------------------------------
            System.out.println("\n=== UPDATE TEST ===");

            intern.setStatus("Closed");
            intern.setSlotsAvailable(2);   // reduces available slots
            intern.removeApplication(app2); // remove one app

            System.out.println("Updated Application IDs: " + intern.getApplicationIds());

            db.update(intern);

            System.out.println("\nAfter updating:");
            db.printAll();

        }





}


