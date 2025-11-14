package Project;

public class CareerStaff extends User{
	private String department;
	
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
	
	public void approveWithdrawal(Student student, InternshipApplication application) {
		Internship internship = application.getInternship();
		
		// if student already accepted this internship, undo acceptance
		if(student.getAcceptedPlacement() == application) {
			student.setAcceptedPlacement(null);
			internship.releaseSlot();
		}
		
		// withdrawal approved, remove it from student's application list
		student.removeApplication(application);
		internship.removeApplication(application);
		application.setWithdrawalRequested(false);
	}
	
	public void rejectWithdrawal(InternshipApplication application) {
		application.setWithdrawalRequested(false);
	}
	
	// --------- GETTER & SETTER --------- //

	public String getDepartment() {
		return this.department;
	}
}
