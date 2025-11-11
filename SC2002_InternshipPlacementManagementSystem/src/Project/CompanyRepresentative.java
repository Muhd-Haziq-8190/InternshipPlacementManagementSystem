package Project;

import java.time.LocalDate;

public class CompanyRepresentative extends User{
	protected String companyName;
	protected String department;
	protected String position;
	protected String accountStatus;
	protected Internship[] internships;

	public CompanyRepresentative(String userId, String name, String companyName, String department, String position) {
		super(userId, name);
		this.companyName = companyName;
		this.department = department;
		this.position= position;
		this.accountStatus = "Pending";
		this.internships = new Internship[5];

	}
	
	
	public Internship createInternship(String title, String description, String level, String preferredMajor, LocalDate openDate, LocalDate closeDate, int slotsAvailable) {
		
		// If company representative already created 5 internships, don't allow any more creation
		// On GUI side, if return type is null, display error message, else proceed
		if(this.internships.length == 5) {
			return null;
		}	
		
		Internship internship = new Internship(title, description, level, preferredMajor, openDate, closeDate, this.companyName, this, slotsAvailable);
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
	

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	
}
