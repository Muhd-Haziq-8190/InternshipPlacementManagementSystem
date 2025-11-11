package Project;

public class Student extends User {
	protected String major;
	protected int yearOfStudy;
	protected String email;
	protected InternshipApplication[] internAppli;
	protected int applicationCount;
	protected InternshipApplication acceptedPlacement;
	
	public Student(String userId, String name, int yearOfStudy, String major, String email) {
		super(userId, name);
		this.major = major;
		this.yearOfStudy = yearOfStudy;
		this.email = email;
		acceptedPlacement = null;
		applicationCount = 0;
		internAppli = new InternshipApplication[3];			// Each student can only apply to 3 internships
		InternshipApplication acceptedPlacement = null;		// default is null, student must accept an internship first
	}
	
	
	// checks if student has already accepted an internship
	public boolean hasAcceptedPlacement() {
        return this.acceptedPlacement != null;
    }
	
	
	// checks if specified level can be displayed for current student
	public boolean canApplyForLevel(String level) {
        if (yearOfStudy <= 2) {
        	return level.equalsIgnoreCase("Basic"); // if year 1 or 2, only basic internships are allowed, hence true should be returned
        }
        return true;  // else they are year 3, who can apply for all levels
    }

	// applies for internship if condition met
	public boolean applyTo(Internship internship) {
		
		// can only apply for max 3 internships
		// does not apply if at capacity
		if(this.applicationCount >= 3) {
			return false;
		}
		
		// internship is not open or student is not eligible
		if(!internship.isOpenFor(this)) {
			return false;
		}
		
		
		InternshipApplication appli = new InternshipApplication(this, internship);
		internAppli[applicationCount++] = appli; // updates student's application list and count
		internship.addApplication(appli); // updates respective internship with application information
		return true;
		
	}
	
	
	// Display all internship title and companyName of each Internship Application
	public void displayInternshipApplications() {
	
		if(internAppli.length != 0) {
			for(int i = 0; i < internAppli.length; i++) {
				System.out.printf("%s, %s\n", internAppli[i].internship.getTitle(), internAppli[i].internship.getCompanyName());
			}
		}
	
	}
	
	
	// Remove an existing application by student
	public void removeApplication(InternshipApplication appli) {
		
		for (int i = 0; i < applicationCount; i++) {
			if(internAppli[i] == appli) {
				for(int j = i; j < applicationCount - 1; j++) {
					internAppli[j] = internAppli[j+1];
				}
				internAppli[--applicationCount] = null;
				break;
			}
		}
	
	}
	
	
	
	// --------- GETTER & SETTER --------- //
	
	
	public InternshipApplication[] getInternshipApplications() {
        return this.internAppli;
    }
	
	public String getMajor() {
		return this.major;
	}
	
	public int getYearOfStudy() {
		return this.yearOfStudy;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public InternshipApplication getAcceptedPlacement() {
		return this.acceptedPlacement;
	}
	
	void setAcceptedPlacement(InternshipApplication application) {
		this.acceptedPlacement = application;
	}
	
	
	
	
	
}
