package Project.classes;

import java.time.LocalDate;

public class CompanyRepresentative extends User{
	private String companyName;
	private String department;
	private String position;
	private String accountStatus;
	private Internship[] internships;
	private int internshipCount;

	public CompanyRepresentative(String userId, String name, String companyName, String department, String position) {
		super(userId, name);
		// user id is company email
		this.companyName = companyName;
		this.department = department;
		this.position= position;
		this.accountStatus = "Pending";
		this.internships = new Internship[5];
		this.internshipCount = 0;
	}
	
//	public CompanyRepresentative(String userId, String name, String companyName, String department, String position, String status, String internshipIds, Internship[] internships, int internshipCount) {
//		super(userId, name);
//		// user id is company email
//		this.companyName = companyName;
//		this.department = department;
//		this.position= position;
//		this.accountStatus = status;
//		this.internships = internships;
//		this.internshipCount = internshipCount;
//	}
//	
	public CompanyRepresentative(String userId, String name, String companyName, String department, String position, String status, Internship[] internships, int internshipCount) {
		super(userId, name);
		// user id is company email
		this.companyName = companyName;
		this.department = department;
		this.position= position;
		this.accountStatus = status;
		this.internships = internships;
		this.internshipCount = internshipCount;
	}
	
	
	public Internship createInternship(String title, String description, String level, String preferredMajor, LocalDate openDate, LocalDate closeDate, int slotsAvailable) {
	   
	    // If company representative already created 5 internships, don't allow any more creation
	 	// On GUI side, if return type is null, display error message, else proceed
	 	// Count current internships
		if(this.internshipCount >= 5) {
			return null;
		}	
		
		Internship internship = new Internship(title, description, level, preferredMajor, openDate, closeDate, this.companyName, this, slotsAvailable);
		
		// add new internship to the first available spot in internships[]
		for (int i = 0; i < this.internships.length; i++) {
	        if (this.internships[i] == null) {
	            this.internships[i] = internship;
	            this.internshipCount++;  // increment count after adding
	            break;
	        }
	    }
		return internship;
	}
	
	
	public boolean approveApplication(String applicationId) {
		
		// Loop through all internships created by Company Representative
		for(int i = 0; i < this.internships.length; i++ ) {
			Internship internship = internships[i];
			InternshipApplication[] applications = internship.getInternshipApplications();
			
			// For each internship, loop through each applicant 
			for(int j = 0; j < internship.getApplicationCount(); j++) {
				
				// Match the applicant ids and update their status to be successful
				if(applications[j].getId().equals(applicationId)) {
					applications[j].setStatus("Successful");
					return true;
				}
			}
		}
		
		return false;
		
	}
	
	public String getInternshipIds() {
        
        // final string internshipIDs should append to
        String internshipIds;
        
        // if no internships
        if (internships == null || internships.length == 0) {
            internshipIds = "";   
        } else {
        	// if have internships
        	
            // filter out null entries
        	// join existing entries with |  ie. INT001|INT002
            StringBuilder sb = new StringBuilder();
            for (Internship internship : internships) {
                if (internship != null && !internship.getId().trim().isEmpty()) {
                    if (sb.length() > 0) {sb.append("|");}
                    sb.append(internship.getId());
                }
            }
            internshipIds = sb.toString();
        }
        
        return internshipIds;
	}
	
	// --------- GETTER & SETTER --------- //
	
	
	public String getCompanyName() {
		return this.companyName;
	}
	
	public String getDepartment() {
		return this.department;
	}
	
	public String getPosition() {
		return this.position;
	}
	
	public String getAccountStatus() {
		return this.accountStatus;
	}
	
	public Internship[] getInternships() {
		return this.internships;
	}
	
	public int getInternshipCount() {
		return this.internshipCount;
	}
	

	public void setDepartment(String department) {
		this.department = department;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}
	
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	


	
}
