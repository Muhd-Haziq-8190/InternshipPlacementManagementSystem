package Project;

public class InternshipApplication  {
	private static int idCounter = 1;
	
	protected String id;
	protected Student student;
	protected Internship internship;
	protected String status;
	protected boolean accepted;
	
	public InternshipApplication(Student student, Internship internship) {
		this.id = "APP" + (idCounter++);
		this.student = student;
		this.internship = internship;
		accepted = false;
		status = "Pending...";
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public boolean acceptPlacement() {
		if(status.equalsIgnoreCase("Successful") && !accepted) {
			this.accepted = true;
			internship.confirmSlot();
			InternshipApplication[] applications = student.getInternshipApplications();
			
			for(InternshipApplication app : applications) {
				if(!app.getId().equals(this.id)) {
					app.setStatus("Unsuccessful");
				}
				
			}
			return true;
			
		}
		return false;
	}
	

	
	
}
