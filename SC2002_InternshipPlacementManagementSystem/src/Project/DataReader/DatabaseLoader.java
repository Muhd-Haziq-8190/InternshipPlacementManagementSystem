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
	
    public DatabaseLoader() {
    	
    }

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

    public List<InternshipApplication> getApplicationWithdrawal(boolean withdrawalRequest) {
        List<InternshipApplication> filteredApplications = new ArrayList<>();
        for (InternshipApplication app : applications.values()) {
            // 只显示已请求撤回但尚未被同意的申请
            if (app.isWithdrawalRequested() == withdrawalRequest && !app.isWithdrawAccepted()) {
                filteredApplications.add(app);
            }
        }

        if(filteredApplications.size() <= 0) {
            System.out.println("No withdrawal requests found.");
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

    public List<Internship> getInternshipsByCompanyRep(String repId) {
        List<Internship> filteredInternships = new ArrayList<>();
        for (Internship i : internships.values()) {
            if (i.getCompanyRepresentativeId().equals(repId)) {
                filteredInternships.add(i);
            }
        }

        if(filteredInternships.size() <= 0) {
            System.out.println("No internships found for this company representative.");
        }

        return filteredInternships;
    }

    // --------- MAIN LOADING METHOD ---------
    public void loadAllData() throws IOException {

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
            int internshipCount = 0;
            if (row.length > 7 && !row[7].isEmpty()) {
                internshipCount = Integer.parseInt(row[7]);
            }
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
            boolean accepted = "TRUE".equalsIgnoreCase(row[4]);
            boolean withdrawalRequested = "TRUE".equalsIgnoreCase(row[5]);

            // 处理 withdrawAccepted 字段，确保向后兼容
            boolean withdrawAccepted = false;
            if (row.length > 6) {
                withdrawAccepted = "TRUE".equalsIgnoreCase(row[6]);
            }

            Student student = students.get(studentId);
            Internship internship = internships.get(internshipId);

            if (student != null && internship != null) {
                // 使用新的构造方法，包含 withdrawAccepted 参数
                InternshipApplication application = new InternshipApplication(id, student, internship, status, accepted, withdrawalRequested, withdrawAccepted);

                // Add to maps
                applications.put(id, application);

                // Link student - 修复这里的逻辑
                boolean linkedToStudent = false;
                for (int i = 0; i < student.getInternshipApplications().length; i++) {
                    if (student.getInternshipApplications()[i] == null) {
                        student.getInternshipApplications()[i] = application;
                        linkedToStudent = true;
                        break;
                    }
                }

                // 如果申请被接受，设置 acceptedPlacement
                if (accepted) {
                    student.setAcceptedPlacement(application);
                }

                // Link internship
                internship.addApplication(application);
                updateStudentApplicationCount(student);
            }
        }
    }

    private void updateStudentApplicationCount(Student student) {
        int count = 0;
        for (InternshipApplication app : student.getInternshipApplications()) {
            if (app != null) count++;
        }

        // 使用反射设置 applicationCount，因为它是一个私有字段
        try {
            java.lang.reflect.Field field = Student.class.getDeclaredField("applicationCount");
            field.setAccessible(true);
            field.set(student, count);
        } catch (Exception e) {
            System.out.println("Error setting application count for student: " + e.getMessage());
        }
    }


    
}
