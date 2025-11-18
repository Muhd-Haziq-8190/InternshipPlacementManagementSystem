package Project;

import Project.DataReader.*;
import Project.classes.CareerStaff;
import Project.classes.CompanyRepresentative;
import Project.classes.Internship;
import Project.classes.Student;

import java.time.LocalDate;

public class DB_Testing {
	CompanyListReader companyDb = new CompanyListReader("data", "company_list.csv");
    InternshipListReader internshipDb = new InternshipListReader("data", "internship_list.csv");
    InternshipApplicationListReader appDb = new InternshipApplicationListReader("data", "internshipApplication_list.csv");
    StaffListReader staffDb = new StaffListReader("data", "staff_list.csv");
    StudentListReader studentDb = new StudentListReader("data", "student_list.csv");

    public static void main(String[] args) {

        // ----------------------------
        // Initialize all readers
        // ----------------------------
        CompanyListReader companyDb = new CompanyListReader("data", "company_list.csv");
        InternshipListReader internshipDb = new InternshipListReader("data", "internship_list.csv");
        InternshipApplicationListReader appDb = new InternshipApplicationListReader("data", "internshipApplication_list.csv");
        StaffListReader staffDb = new StaffListReader("data", "staff_list.csv");
        StudentListReader studentDb = new StudentListReader("data", "student_list.csv");

        // ----------------------------
        // Create Staff
        // ----------------------------
        CareerStaff staff1 = new CareerStaff("STF001", "Alice Tan", "Manager", "Career Office");
        CareerStaff staff2 = new CareerStaff("STF002", "Bob Lee", "Advisor", "Student Support");
        staffDb.insert(staff1);
        staffDb.insert(staff2);

        // ----------------------------
        // Create Company Representatives
        // ----------------------------
        CompanyRepresentative rep1 = new CompanyRepresentative("alice@google.com", "Alice", "Google", "Tech", "Lead Engineer");
        rep1.setAccountStatus("Approved");
        CompanyRepresentative rep2 = new CompanyRepresentative("bob@amazon.com", "Bob", "Amazon", "Tech", "HR Manager");
        rep2.setAccountStatus("Approved");
        companyDb.insert(rep1);
        companyDb.insert(rep2);

        // ----------------------------
        // Create Internships
        // ----------------------------
        Internship i1 = rep1.createInternship(
                "Software Engineering Intern",
                "Backend API Development",
                "Intermediate",
                "Computer Science",
                LocalDate.now().minusDays(5),
                LocalDate.now().plusDays(30),
                5
        );

        Internship i2 = rep2.createInternship(
                "IT Support Intern",
                "Maintain IT systems",
                "Basic",
                "Information Systems",
                LocalDate.now().minusDays(3),
                LocalDate.now().plusDays(25),
                3
        );
        
        Internship i3 = rep2.createInternship(
                "IT FISHING ESCAPADE",
                "Maintain AI Fishin Software",
                "Intermediate",
                "Information Systems",
                LocalDate.now().minusDays(3),
                LocalDate.now().plusDays(25),
                3
        );
        
        i1.setStatus("Approved");
        i2.setStatus("Approved");
        i3.setStatus("Approved");

        internshipDb.insert(i1);
        internshipDb.insert(i2);
        internshipDb.insert(i3);
        
        companyDb.update(rep1);
        companyDb.update(rep2);

        // ----------------------------
        // Create Students
        // ----------------------------
        Student s1 = new Student("U1234567A", "John Tan", 3, "Computer Science", "john@ntu.edu.sg");
        Student s2 = new Student("U2345678B", "Mary Lim", 3, "Information Systems", "mary@ntu.edu.sg");

        // ----------------------------
        // Students apply to internships
        // ----------------------------
        s1.applyTo(i1); // creates InternshipApplication internally
        s2.applyTo(i2);
        appDb.insert(s1.getInternshipApplications()[0]);
        appDb.insert(s2.getInternshipApplications()[0]);

        // Update students and internships in CSV after applications
        studentDb.insert(s1);
        studentDb.insert(s2);
        internshipDb.update(i1);
        internshipDb.update(i2);

    }

}
