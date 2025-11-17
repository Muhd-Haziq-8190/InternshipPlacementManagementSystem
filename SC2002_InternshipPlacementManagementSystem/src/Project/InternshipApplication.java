package Project;

public class InternshipApplication  {
	private static int idCounter = 1;	 // counter for unique Internship Application IDs
	
	private String id;
	private Student student;
	private Internship internship;
	private String status;		// Pending, Successful, Unsuccessful
	private boolean accepted; 	// checks if current internship application has been accepted by student
	private boolean withdrawalRequested;
	
	// foreign keys
	private String studentId;
	private String internshipId;
	
	public InternshipApplication(Student student, Internship internship) {
		this.id = "APP" + (idCounter++);
		this.student = student;
		this.internship = internship;
		this.accepted = false;			// application is defaulted to not being accepted yet
		this.status = "Pending";		// defaults to "Pending"
		this.withdrawalRequested = false;
	}
	
	public InternshipApplication(String id, Student student, Internship internship, String status, boolean accepted, boolean withdrawalRequested) {
		this.id = id;
		this.student = student;
		this.internship = internship;
		this.accepted = accepted;			
		this.status = status;		
		this.withdrawalRequested = withdrawalRequested;
		
		// --- UPDATE ID COUNTER SO NEW IDS DON'T CLASH ---
	    int numeric = Integer.parseInt(id.replace("APP", ""));
	    if (numeric >= idCounter) {
	        idCounter = numeric + 1;
	    }
	}
	
	public InternshipApplication(String id, String studentId, String internshipId, String status, boolean accepted, boolean withdrawalRequested) {
		this.id = id;
		this.studentId = studentId;
		this.internshipId = internshipId;
		this.accepted = accepted;			
		this.status = status;		
		this.withdrawalRequested = withdrawalRequested;
		
		// --- UPDATE ID COUNTER SO NEW IDS DON'T CLASH ---
	    int numeric = Integer.parseInt(id.replace("APP", ""));
	    if (numeric >= idCounter) {
	        idCounter = numeric + 1;
	    }
	}
	
	public InternshipApplication(String id, String studentId, Internship internship, String status, boolean accepted, boolean withdrawalRequested) {
		this.id = id;
		this.student = null;
		this.studentId = studentId;
		this.internship = internship;
		this.accepted = accepted;			
		this.status = status;		
		this.withdrawalRequested = withdrawalRequested;
		
		// --- UPDATE ID COUNTER SO NEW IDS DON'T CLASH ---
	    int numeric = Integer.parseInt(id.replace("APP", ""));
	    if (numeric >= idCounter) {
	        idCounter = numeric + 1;
	    }
	}
	
	public boolean acceptPlacement() {
		
		// checks if application is Successful AND not been accepted yet by student
		if(status.equalsIgnoreCase("Successful") && !accepted) {
			
			// update to accept it
			this.accepted = true;
			
			// once accepted, update on the internship side to reduce available slots
			internship.confirmSlot();
			
			// retrieve all application submitted by current student
			InternshipApplication[] applications = student.getInternshipApplications();
			
			// loop through all current student's applications
			for(InternshipApplication app : applications) {
				if (app == null) continue; 
				// each student can only accept one internship
				// set status of all applications aside from the accepted one to "Unsuccessful"
				if(!app.getId().equals(this.id)) {
					app.setStatus("Unsuccessful");
				}
				
			}
			
			// returns true when placement is accepted
			return true;
			
		}
		
		// returns false when application is not successful
		return false;
	}
	
	public void setWithdrawalRequested(boolean isWithdraw) {
		this.withdrawalRequested = isWithdraw;
	}
	
	
	// --------- GETTER & SETTER --------- //
	
	public String getId() {
		return this.id;
	}

	public Student getStudent() {
		return this.student;
	}
	
	public Internship getInternship() {
		return this.internship;
	}
	
	
	public String getStatus() {
		return this.status;
	}
	
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setStudent(Student student) {
		this.student = student;
	}
	
	public void setInternship(Internship internship) {
		this.internship = internship;
	}
	
	public boolean isAccepted() {
		return this.accepted;
	}
	
	public boolean isWithdrawalRequested() {
		return this.withdrawalRequested;
	}

	
}
