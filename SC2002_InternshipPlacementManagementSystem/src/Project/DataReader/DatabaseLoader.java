package Project.DataReader;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import Project.classes.CareerStaff;
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
    private Map<String, CareerStaff> careerStaffs = new HashMap<>();
    
    private StudentListReader studentDb = new StudentListReader("data", "student_list.csv");
    private CompanyListReader companyRepDb = new CompanyListReader("data", "company_list.csv");
    private InternshipListReader internshipDb = new InternshipListReader("data", "internship_list.csv");
    private InternshipApplicationListReader applicationDb= new InternshipApplicationListReader("data", "internshipApplication_list.csv");
    private StaffListReader careerStaffDb = new StaffListReader("data", "staff_list.csv");
	

//    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    
    // ===== CRUD Operations =====
    
    // Student
    public void insertStudent(Student student) throws IOException {
    	studentDb.insert(student);
    	loadAllData();
    }
    
    public void updateStudent(Student student) throws IOException {
    	studentDb.update(student);
    	loadAllData();
    }
    
    public void deleteStudent(String id) throws IOException {
    	studentDb.delete(id);
    	loadAllData();
    }
    
    // CompanyRepresentative
    public void insertCompanyRep(CompanyRepresentative companyRep) throws IOException {
    	companyRepDb.insert(companyRep);
    	loadAllData();
    }
    
    public void updateCompanyRep(CompanyRepresentative companyRep) throws IOException {
    	companyRepDb.update(companyRep);
    	loadAllData();
    }
    
    public void deleteCompanyRep(String id) throws IOException {
    	companyRepDb.delete(id);
    	loadAllData();
    }
    
    // CareerStaff
    public void insertCareerStaff(CareerStaff careerStaff) throws IOException {
    	careerStaffDb.insert(careerStaff);
    	loadAllData();
    }
    
    public void updateCareerStaff(CareerStaff careerStaff) throws IOException {
    	careerStaffDb.update(careerStaff);
    	loadAllData();
    }
    
    public void deleteCareerStaff(String id) throws IOException {
    	careerStaffDb.delete(id);
    	loadAllData();
    }
    
    // Internship
    public void insertInternship(Internship internship) throws IOException {
    	internshipDb.insert(internship);
    	loadAllData();
    }
    
    public void updateInternship(Internship internship) throws IOException {
    	internshipDb.update(internship);
    	loadAllData();
    }
    
    public void deleteInternship(String id) throws IOException {
    	internshipDb.delete(id);
    	loadAllData();
    }
    
    // InternshipApplication
    public void insertInternshipApplication(InternshipApplication internshipApplication) throws IOException {
    	applicationDb.insert(internshipApplication);
    	loadAllData();
    }
    
    public void updateInternshipApplication(InternshipApplication internshipApplication) throws IOException {
    	applicationDb.update(internshipApplication);
    	loadAllData();
    }
    
    public void deleteInternshipApplication(String id) throws IOException {
    	applicationDb.delete(id);
    	loadAllData();
    }
    
    // ===== getUserById =====
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
    
    public CareerStaff getCareerStaffById(String id) {
        return careerStaffs.get(id);
    }

    // ==== Methods to retrieve full arraylist of all =====
    // - students
    // - companyreps
    // - internships
    // - applications
    // - careerStaff

    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    public List<CompanyRepresentative> getAllCompanyReps() {
        return new ArrayList<>(companyReps.values());
    }

    public List<Internship> getAllInternships() {
        return new ArrayList<>(internships.values());
    }

    public List<InternshipApplication> getAllApplications() {
        return new ArrayList<>(applications.values());
    }
    
    public List<CareerStaff> getAllCareerStaff() {
        return new ArrayList<>(careerStaffs.values());
    }


    // ========= Filtering Functions ========= 
    // By fields
    
    // ---- Students ----
    public List<Student> getStudentsByMajor(String major) {
        List<Student> filteredStudents = new ArrayList<>();
        for (Student s : students.values()) {
            if (s.getMajor().equalsIgnoreCase(major)) {
            	filteredStudents.add(s);
            }
        }
        
        if(filteredStudents.size() <= 0) {
        	System.out.println("No students found by this major.");
        }
        
        return filteredStudents;
    }

    public List<Student> getStudentsByYear(int year) {
        List<Student> filteredStudents = new ArrayList<>();
        for (Student s : students.values()) {
            if (s.getYearOfStudy() == year) {
            	filteredStudents.add(s);
            }
        }
        
        if(filteredStudents.size() <= 0) {
        	System.out.println("No students found by this year of study.");
        }
        
        return filteredStudents;
    }

    // ---- Company Representatives ----
    public List<CompanyRepresentative> getCompanyRepsByCompany(String company) {
        List<CompanyRepresentative> filteredCompanyRep = new ArrayList<>();
        for (CompanyRepresentative cr : companyReps.values()) {
            if (cr.getCompanyName().equalsIgnoreCase(company)) {
            	filteredCompanyRep.add(cr);
            }
        }
        
        if(filteredCompanyRep.size() <= 0) {
        	System.out.println("No company reps found by this company.");
        }
        
        return filteredCompanyRep;
    }

    public List<CompanyRepresentative> getCompanyRepsByStatus(String status) {
        List<CompanyRepresentative> filteredCompanyRep = new ArrayList<>();
        for (CompanyRepresentative cr : companyReps.values()) {
            if (cr.getAccountStatus().equalsIgnoreCase(status)) {
            	filteredCompanyRep.add(cr);
            }
        }
        
        if(filteredCompanyRep.size() <= 0) {
        	System.out.println("No company reps found by this status.");
        }
        
        return filteredCompanyRep;
    }

    // ---- Internships ----
    public List<Internship> getInternshipsByMajor(String major) {
        List<Internship> filteredInternships = new ArrayList<>();
        for (Internship i : internships.values()) {
            if (i.getPreferredMajor().equalsIgnoreCase(major)) {
            	filteredInternships.add(i);
            }
        }
        
        if(filteredInternships.size() <= 0) {
        	System.out.println("No internships by this preferred major.");
        }
        return filteredInternships;
    }

    public List<Internship> getInternshipsByStatus(String status) {
        List<Internship> filteredInternships = new ArrayList<>();
        for (Internship i : internships.values()) {
            if (i.getStatus().equalsIgnoreCase(status)) {
            	filteredInternships.add(i);
            }
        }
        
        if(filteredInternships.size() <= 0) {
        	System.out.println("None found by this status.");
        }
        
        return filteredInternships;
    }

    public List<Internship> getInternshipsByCompany(String company) {
        List<Internship> filteredInternships = new ArrayList<>();
        for (Internship i : internships.values()) {
            if (i.getCompanyName().equalsIgnoreCase(company)) {
            	filteredInternships.add(i);
            }
        }
        
        if(filteredInternships.size() <= 0) {
        	System.out.println("No internships found by this company.");
        }
        
        return filteredInternships;
    }

    // ---- Applications ----
    public List<InternshipApplication> getApplicationsByStatus(String status) {
        List<InternshipApplication> filteredApplications = new ArrayList<>();
        for (InternshipApplication app : applications.values()) {
            if (app.getStatus().equalsIgnoreCase(status)) {
            	filteredApplications.add(app);
            }
        }
        
        if(filteredApplications.size() <= 0) {
        	System.out.println("No applications found by this status.");
        }
        
        return filteredApplications;
    }

    public List<InternshipApplication> getApplicationsByStudent(String studentId) {
        List<InternshipApplication> filteredApplications = new ArrayList<>();
        for (InternshipApplication app : applications.values()) {
            if (app.getStudent().getId().equals(studentId)) {
            	filteredApplications.add(app);
            }
        }
        
        if(filteredApplications.size() <= 0) {
        	System.out.println("No applications found by this student.");
        }
        
        
        return filteredApplications;
    }

    public List<InternshipApplication> getApplicationsByInternship(String internshipId) {
        List<InternshipApplication> filteredApplications = new ArrayList<>();
        for (InternshipApplication app : applications.values()) {
            if (app.getInternship().getId().equals(internshipId)) {
            	filteredApplications.add(app);
            }
        }
        
        if(filteredApplications.size() <= 0) {
        	System.out.println("No applications found by this internship.");
        }
        
        return filteredApplications;
    }
    
    // ---- CareerStaff ----
    public List<CareerStaff> getCareerStaffByRole(String role) {
        List<CareerStaff> filteredCareerStaff = new ArrayList<>();
        for (CareerStaff careerStaff : careerStaffs.values()) {
            if (careerStaff.getRole().equalsIgnoreCase(role)) {
            	filteredCareerStaff.add(careerStaff);
            }
        }
        
        if(filteredCareerStaff.size() <= 0) {
        	System.out.println("No career staffs found by this role.");
        }
        
        return filteredCareerStaff;
    }
    
    public List<CareerStaff> getCareerStaffByDepartment(String department) {
        List<CareerStaff> filteredCareerStaff = new ArrayList<>();
        for (CareerStaff careerStaff : careerStaffs.values()) {
            if (careerStaff.getDepartment().equalsIgnoreCase(department)) {
            	filteredCareerStaff.add(careerStaff);
            }
        }
        
        if(filteredCareerStaff.size() <= 0) {
        	System.out.println("No career staffs found by this department.");
        }
        
        return filteredCareerStaff;
    }


    // --------- MAIN LOADING METHOD ---------
    public void loadAllData() throws IOException {
    	 
    	
//    	List<String[]> studentData = new StudentListReader("data", "student_list.csv").readWithoutHeader();
//        List<String[]> companyRepData = new CompanyListReader("data", "company_list.csv").readWithoutHeader();
//        List<String[]> internshipData = new InternshipListReader("data", "internship_list.csv").readWithoutHeader();
//        List<String[]> applicationData = new InternshipApplicationListReader("data", "internshipApplication_list.csv").readWithoutHeader();
//        List<String[]> careerStaffData = new StaffListReader("data", "staff_list.csv").readWithoutHeader();
    	
        List<String[]> studentData = studentDb.readWithoutHeader();
        List<String[]> companyRepData = companyRepDb.readWithoutHeader();
        List<String[]> internshipData = internshipDb.readWithoutHeader();
        List<String[]> applicationData = applicationDb.readWithoutHeader();
        List<String[]> careerStaffData = careerStaffDb.readWithoutHeader();
    	
        // clear existing data so no duplicates are available
        students.clear();
        companyReps.clear();
        internships.clear();
        applications.clear();
        careerStaffs.clear();
        
        // reconstruct CareerStaff obj
        for (String[] row : careerStaffData) {
            String id = row[0];
            String name = row[1];
            String role = row[2];
            String department = row[3];
            careerStaffs.put(id, new CareerStaff(id, name, role, department));
        }

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
        loader.loadAllData();
        
        // creating new student
//        loader.insertStudent(new Student("U2423F", "John", 1, "Computer Engineering", "test123@hotmail.com"));
        
        // retrieving student by id
        Student a = loader.getStudentById("U2423123F");
//        System.out.println(a.getId());
//        System.out.println(a.getName());
//        System.out.println(a.getEmail());
        
        // retrieve internship
        // apply for internship
        // -> new entry
        // -> Internship .csv entry must be updated to add new InternshipApplication linked to that Internship
        // -> Student .csv entry must be updated to add new InternshipApplication to that Student
        // -> Reload data
        Internship internship = loader.getInternshipById("INT1");
//        loader.insertInternshipApplication(a.applyTo(internship));
//        loader.updateInternship(internship);
//        loader.updateStudent(a);
        loader.loadAllData();
        
        CompanyRepresentative companyRep = loader.getCompanyRepById("alice@google.com");
        InternshipApplication application = loader.getApplicationById("APP3");
        System.out.println(companyRep.getCompanyName());
        
        
        loader.updateInternshipApplication(companyRep.approveApplication(application.getId()));
        
        loader.updateInternshipApplication(a.acceptInternship(application));
        loader.updateStudent(a);
        loader.updateInternship(application.getInternship());
        
        
        // load all data, create respective objects
//        loader.loadAllData(studentsCsv, repsCsv, internshipsCsv, applicationsCsv, staffCsv);
        
        // Test output
//        Student s = loader.getStudentById("STU001");
//        Student s = loader.getStudentById("U2345678B");
//        System.out.println(s.getApplicationIds());
//
//        CompanyRepresentative rep = loader.getCompanyRepById("bob@amazon.com");
//        System.out.println(rep.getInternshipIds());
//        System.out.println(rep.getInternships()[1].getTitle());
//        
//        Internship inter = loader.getInternshipById("INT1");
//        System.out.println(inter.getInternshipApplications()[0].getStudent().getEmail());
//        System.out.println(inter.getCompanyRepresentative().getInternshipIds());
//        
//        
//        List<Internship> internships = loader.getInternshipsByCompany("Amazon");
//        System.out.println("\n\n"+ internships.size());
//        
//        for(Internship i : internships) {
//        	System.out.println(i.getId());
//        	System.out.println(i.getCompanyName());
//        	System.out.println(i.getCompanyRepresentative().getPassword());
//        	
//        }
//        
//        List<CareerStaff> careerStaffs = loader.getAllCareerStaff();
//        for(CareerStaff cs : careerStaffs) {
//        	System.out.println(cs);
//        	System.out.println(cs.getId());
//        	System.out.println(cs.getName());
//        	System.out.println(cs.getRole());
//        	System.out.println(cs.getDepartment());
//        }
//        
//        List<CareerStaff> careerStaffs1 = loader.getCareerStaffByRole("Advisor");
//        for(CareerStaff cs : careerStaffs1) {
//        	System.out.println(cs);
//        	System.out.println(cs.getId());
//        	System.out.println(cs.getName());
//        	System.out.println(cs.getRole());
//        	System.out.println(cs.getDepartment());
//        }
        
        

    }

    
    
}
