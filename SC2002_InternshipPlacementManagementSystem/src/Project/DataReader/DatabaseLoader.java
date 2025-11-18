package Project.DataReader;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import Project.classes.CompanyRepresentative;
import Project.classes.Internship;
import Project.classes.InternshipApplication;
import Project.classes.Student;

public class DatabaseLoader {
	
    // maps to hold objects by their IDs for quick lookup
    private Map<String, Student> students = new HashMap<>();
    private Map<String, CompanyRepresentative> companyReps = new HashMap<>();
    private Map<String, Internship> internships = new HashMap<>();
    private Map<String, InternshipApplication> applications = new HashMap<>();

//    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    
    // return specific student, companyrep, internship, application
    
    public Student getStudentById(String id) {
        return students.get(id);
    }
    
    public CompanyRepresentative getCompanyRepById(String id) {
        return companyReps.get(id);
    }

    public Internship getInternshipById(String id) {
        return internships.get(id);
    }

    public InternshipApplication getApplicationById(String id) {
        return applications.get(id);
    }
    


    // --------- MAIN LOADING METHOD ---------
    public void loadAllData(
        List<String[]> studentData,
        List<String[]> companyRepData,
        List<String[]> internshipData,
        List<String[]> applicationData
    ) {
    	
        // clear existing data so no duplicates are available
        students.clear();
        companyReps.clear();
        internships.clear();
        applications.clear();
        
        // reconstruct Students obj
        for (String[] row : studentData) {
            String id = row[0];
            String name = row[1];
//            System.out.println(row[3]);
            int year = Integer.parseInt(row[3]);
            String major = row[2];
            String email = row[4];
            students.put(id, new Student(id, name, year, major, email));
        }

        // reconstruct CompanyRepresentatives obj
        for (String[] row : companyRepData) {
            String id = row[0];
            String company = row[1];
            String name = row[2];
            String dept = row[3];
            String position = row[4];
            String status = row[5];
            int internshipCount = Integer.parseInt(row[7]);
//            companyReps.put(id, new CompanyRepresentative(id, name, company, dept, position, status, new Internship[5], 0));
            companyReps.put(id, new CompanyRepresentative(id, name, company, dept, position, status, new Internship[5], internshipCount));

        }

        // reconstruct Internships obj (without linking applications yet)
        for (String[] row : internshipData) {
//        	for(int i = 0; i < row.length; i++) {
//        		System.out.println(row[i]);
//        	}
            String id = row[0];
            String title = row[1];
            String desc = row[2];
            String level = row[3];
            String preferredMajor = row[4];
            LocalDate open = LocalDate.parse(row[5]);
            LocalDate close = LocalDate.parse(row[6]);
            String status = row[7];
            String companyName = row[8];
            String repId = row[9];
            int slots = Integer.parseInt(row[10]);
            boolean visible = Boolean.parseBoolean(row[11]);

            CompanyRepresentative rep = companyReps.get(repId);
            Internship internship = new Internship(id, title, desc, level, preferredMajor, open, close, companyName, rep, status, visible, new InternshipApplication[30], slots);

            internships.put(id, internship);

            // Add internship to company's array
            for (int i = 0; i < rep.getInternships().length; i++) {
                if (rep.getInternships()[i] == null) {
                    rep.getInternships()[i] = internship;
                    rep.setAccountStatus(rep.getAccountStatus()); // maintain state
                    break;
                }
            }
        }

        // reconstruct InternshipApplications, link both internship and application
        for (String[] row : applicationData) {
            String id = row[0];
            String studentId = row[1];
            String internshipId = row[2];
            String status = row[3];
            boolean accepted = Boolean.parseBoolean(row[4]);
            boolean withdrawalRequested = Boolean.parseBoolean(row[5]);

            Student student = students.get(studentId);
            Internship internship = internships.get(internshipId);

            if (student != null && internship != null) {
                InternshipApplication application = new InternshipApplication(id, student, internship, status, accepted, withdrawalRequested);

                // Add to maps
                applications.put(id, application);

                // Link student
                if (student.getInternshipApplications()[0] == null) {
                    student.getInternshipApplications()[0] = application;
                    student.setAcceptedPlacement(accepted ? application : student.getAcceptedPlacement());
                } else {
                    for (int i = 0; i < student.getInternshipApplications().length; i++) {
                        if (student.getInternshipApplications()[i] == null) {
                            student.getInternshipApplications()[i] = application;
                            break;
                        }
                    }
                }

                // Link internship
                internship.addApplication(application);
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        DatabaseLoader loader = new DatabaseLoader();
        
    	CompanyListReader companyDb = new CompanyListReader("data", "company_list.csv");
        InternshipListReader internshipDb = new InternshipListReader("data", "internship_list.csv");
        InternshipApplicationListReader appDb = new InternshipApplicationListReader("data", "internshipApplication_list.csv");
        StaffListReader staffDb = new StaffListReader("data", "staff_list.csv");
        StudentListReader studentDb = new StudentListReader("data", "student_list.csv");


        // retrieve all data in db as string
        List<String[]> studentsCsv = studentDb.readWithoutHeader();
        List<String[]> repsCsv = companyDb.readWithoutHeader();
        List<String[]> internshipsCsv = internshipDb.readWithoutHeader();
        List<String[]> applicationsCsv = appDb.readWithoutHeader();
        
        // load all data, create respective objects
        loader.loadAllData(studentsCsv, repsCsv, internshipsCsv, applicationsCsv);

        // Test output
//        Student s = loader.getStudentById("STU001");
        Student s = loader.getStudentById("U2345678B");
        System.out.println(s.getApplicationIds());

        CompanyRepresentative rep = loader.getCompanyRepById("bob@amazon.com");
        System.out.println(rep.getInternshipIds());
        System.out.println(rep.getInternships()[1].getTitle());
        
        Internship inter = loader.getInternshipById("INT1");
        System.out.println(inter.getInternshipApplications()[0].getStudent().getEmail());
        System.out.println(inter.getCompanyRepresentative().getInternshipIds());
        
    }

    
    
}
