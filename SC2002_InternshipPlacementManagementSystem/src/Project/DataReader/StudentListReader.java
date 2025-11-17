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

public class StudentListReader {
        private final String filePath;
        public StudentListReader(String folder, String fileName) {
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
                    writer.write("StudentID,Name,Major,Year,Email,InternshipApplicationIDs,ApplicationCount,AcceptedInternshipApplicationID\n");
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

        public void insert(Student student) {
            try {
                List<String[]> rows = readAll();
                rows.add(new String[]{
                		student.getId(),
                		student.getName(),
                		student.getMajor(),
                		String.valueOf(student.getYearOfStudy()),
                		student.getEmail(),
                		student.getApplicationIds(),
                		String.valueOf(student.getApplicationCount()),
                		student.getAcceptedPlacement() != null ? student.getAcceptedPlacement().getId() : ""
                });
                writeAll(rows);
                //System.out.println("插入成功！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void update(Student student) {
            try {
                List<String[]> rows = readAll();
                for (int i = 1; i < rows.size(); i++) {
                    if (rows.get(i)[0].equals(student.getId())) {
                        rows.set(i, new String[]{
                        		student.getId(),
                        		student.getName(),
                        		student.getMajor(),
                        		String.valueOf(student.getYearOfStudy()),
                        		student.getEmail(),
                        		student.getApplicationIds(),
                        		String.valueOf(student.getApplicationCount()),
                        		student.getAcceptedPlacement() != null ? student.getAcceptedPlacement().getId() : ""
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
                rows.removeIf(row -> !row[0].equals("StudentID") && row[0].equals(id));
                writeAll(rows);
                //System.out.println("删除成功！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        public List<String> find(String col, String value) {
//            try {
//                List<String[]>rows = readAll();
//                if(rows.isEmpty()) return Collections.emptyList();
//                /** find col */
//                int colNum = -1;
//                String[] header = rows.get(0);
//                for (int i = 1; i < header.length; i++) {
//                    if(header[i].equals(col)) {colNum = i;}
//                }
//                if(colNum == -1) return Collections.emptyList();
//                /** find value */
//                for (int i = 1; i < rows.size(); i++) {
//                    String[] row = rows.get(i);
//                    if (row[colNum].equals(value)) {return Arrays.asList(row);}
//                }
//                return Collections.emptyList();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return Collections.emptyList();
//        }
//        
        
        public List<String[]> find(String col, String value) {
            try {
                List<String[]> rows = readAll();
                if (rows.isEmpty()) return Collections.emptyList();

                int colNum = -1;
                String[] header = rows.get(0);

                for (int i = 0; i < header.length; i++) {
                    if (header[i].equals(col)) {
                        colNum = i;
                        break;
                    }
                }

                if (colNum == -1) return Collections.emptyList();

                // collect ALL matching rows
                List<String[]> result = new ArrayList<>();
                for (int i = 1; i < rows.size(); i++) {
                    if (rows.get(i)[colNum].equals(value)) {
                        result.add(rows.get(i));
                    }
                }

                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return Collections.emptyList();
        }

        
        // find by header, return Student obj
        public Student findStudent(String col, String value) {
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
                
                InternshipApplicationListReader internshipAppDb = new InternshipApplicationListReader("data", "internshipApplication_list.csv");
                
                /** find value */
                for (int i = 1; i < rows.size(); i++) {
                    String[] row = rows.get(i);
                    if (row[colNum].equals(value)) {
                    	String appPlacement = (row.length > 7) ? row[7] : "";
                    	Student student = new Student(
                    			row[0], 	// StudentID
                    			row[1], 	// Name
                    			Integer.parseInt(row[3]), 		// Year
                    			row[2], 	// Major
                    			row[4], 	// Email
                    			internshipAppDb.getApplicationsFromIds(row[5]), 	// InternshipApplicationIDs convert to InternshipApplication[]
                    			Integer.parseInt(row[6]), 	// ApplicationCount
                    			internshipAppDb.findInternshipApplication("InternshipApplicationID", appPlacement) 	// acceptedPlacement
                    	);
                    	return student;
                    	
                    }
                }
                
                return null;		// Not found
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        
        public String[] findStudentRaw(String studentId) {
            try {
                List<String[]> rows = readAll();
                for (int i = 1; i < rows.size(); i++) {
                    if (rows.get(i)[0].equals(studentId)) {
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
            StudentListReader db = new StudentListReader("data", "student_list.csv");

            System.out.println("=== Initial Data ===");
            db.printAll();

            // -------------------------------
            // Create Students
            // -------------------------------
            Student s1 = new Student("U1234567A", "John Tan", 2, "Computer Science", "john@ntu.edu.sg");
            Student s2 = new Student("U2345678B", "Mary Lim", 3, "Information Systems", "mary@ntu.edu.sg");
            
            CompanyRepresentative rep = new CompanyRepresentative("alice@google.com", "Alice", "Google", "Tech", "Lead Engineer");
            rep.setAccountStatus("Approved");
            // -------------------------------
            // Create Internships
            // -------------------------------
            Internship i1 = new Internship(
                    "Software Engineering Intern",
                    "Develop backend services",
                    "Basic",
                    "Computer Science",
                    LocalDate.now().minusDays(5),
                    LocalDate.now().plusDays(30),
                    "Google",
                    rep,
                    5
            );

            Internship i2 = new Internship(
                    "IT Support Intern",
                    "Maintain IT systems",
                    "Basic",
                    "Information Systems",
                    LocalDate.now().minusDays(2),
                    LocalDate.now().plusDays(25),
                    "Amazon",
                    rep,
                    5
            );
            
            i1.setStatus("Approved");
            i2.setStatus("Approved");


            // Add applications to students and internships
            s1.applyTo(i1); 
            s2.applyTo(i2);
            
            InternshipApplication app1 = s1.getInternshipApplications()[0];
            InternshipApplication app2 = s2.getInternshipApplications()[0];

            // Accept placements for testing
            app1.setStatus("Successful");
            app1.acceptPlacement(); // sets accepted placement
            app2.setStatus("Successful");
            app2.acceptPlacement();

            // -------------------------------
            // INSERT STUDENTS
            // -------------------------------
            System.out.println("\n=== INSERT TEST ===");
//            db.insert(s1);
//            db.insert(s2);
            db.printAll();

            // -------------------------------
            // UPDATE TEST
            // -------------------------------
            System.out.println("\n=== UPDATE TEST ===");

            // Change s1's accepted placement to a new internship
            Internship i3 = new Internship(
                    "Shopee SWE Intern",
                    "Backend development",
                    "Intermediate",
                    "Computer Science",
                    LocalDate.now(),
                    LocalDate.now().plusDays(20),
                    "Shopee",
                    null,
                    3
            );
            
            Internship i4 = new Internship(
                    "Shopee SWE asddqwd",
                    "Backend qwd1qwd ",
                    "Basic",
                    "Computer Science",
                    LocalDate.now(),
                    LocalDate.now().plusDays(20),
                    "Lazada",
                    null,
                    3
            );
            
            i4.setStatus("Approved");
            
            s1.applyTo(i4);
            s1.setAcceptedPlacement(s1.getInternshipApplications()[0]);

//            db.update(s1);
//            db.update(s2);

            System.out.println("\nAfter update:");
            db.printAll();
            
            // Delete test
//            db.delete(s1.getId());
            System.out.println("\nAfter deleting s1:");
            db.printAll();
        }
        

}


