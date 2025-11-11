package Project;

public class CareerStaff extends User{
	protected String department;
	
	public CareerStaff(String id, String name, String department) {
		super(id, name);
		this.department = department;
	}
	
	// approve or reject respective internship based on input value approve
	public boolean approveInternship(Internship internship, boolean approve) {
		internship.setStatus(approve ? "Approved" : "Rejected");
		return true;
	}
	
	
	// allow the creation of Company Representative account so they can login
	public boolean authorizeCompanyRepresentativeCreation(CompanyRepresentative companyRep, boolean approve) {
		companyRep.setAccountStatus(approve ? "Approved" : "Rejected");
		return true;
	}
}
