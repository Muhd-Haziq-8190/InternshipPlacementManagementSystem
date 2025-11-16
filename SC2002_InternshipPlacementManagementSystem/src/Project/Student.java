package Project;

public class Student extends User {
	
	private String major;
	private int yearOfStudy;
	private String email;
	private InternshipApplication[] internAppli;
	private int applicationCount;
	private InternshipApplication acceptedPlacement;
	
	// use on creation of student
	public Student(String userId, String name, int yearOfStudy, String major, String email) {
		super(userId, name);
		this.major = major;
		this.yearOfStudy = yearOfStudy;
		this.email = email;
		this.acceptedPlacement = null;
		this.applicationCount = 0;
		this.internAppli = new InternshipApplication[3];			// Each student can only apply to 3 internships
		this.acceptedPlacement = null;									// default is null, student must accept an internship first
	}
	
	// use when retrieveing from .csv
	public Student(String userId, String name, int yearOfStudy, String major, String email, InternshipApplication[] internAppli, int applicationCount, InternshipApplication acceptedPlacement) {
		super(userId, name);
		this.major = major;
		this.yearOfStudy = yearOfStudy;
		this.email = email;
		this.acceptedPlacement = acceptedPlacement;
		this.applicationCount = applicationCount;
		this.internAppli = internAppli;												// Each student can only apply to 3 internships
		this.acceptedPlacement = acceptedPlacement;									// default is null, student must accept an internship first
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
			System.out.println("Student Class, applyTo() method: applicationCount >= 3");
			return false;
			
		}
		
		// internship is not open or student is not eligible
		if(!internship.isOpenFor(this)) {
//			System.out.println("Student Class, isOpenFor() method: Student not eligible");
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
				System.out.printf("%s, %s\n", internAppli[i].getInternship().getTitle(), internAppli[i].getInternship().getCompanyName());
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
	
	public boolean requestWithdrawal(InternshipApplication application) {
		// ensure that application student wants to withdraw exists
		boolean owns = false;
		for(int i = 0; i < this.applicationCount; i++) {
			if(this.internAppli[i] == application) {
				owns = true;
				break;
			}
		}
		
		// if does not exist, return false
		if(!owns) {
			return false; 
		}
		
		// request a withdrawal for given application
		application.setWithdrawalRequested(true);
		return true;
	}
	
	public String getApplicationIds() {
        
        // final string applicationIDs should append to
        String applicationIds;
        
        // if no internships
        if (internAppli == null || internAppli.length == 0) {
        	applicationIds = "";   
        } else {
        	// if have internships
        	
            // filter out null entries
        	// join existing entries with |  ie. INT001|INT002
            StringBuilder sb = new StringBuilder();
            for (InternshipApplication appli : internAppli) {
                if (appli != null && !appli.getId().trim().isEmpty()) {
                    if (sb.length() > 0) {sb.append("|");}
                    sb.append(appli.getId());
                }
            }
            applicationIds = sb.toString();
        }
        
        return applicationIds;
	}
	
	
	// --------- GETTER & SETTER --------- //
	
	
	public InternshipApplication[] getInternshipApplications() {
        return this.internAppli;
    }
	
	public int getApplicationCount() {
		return this.applicationCount;
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
	
	public void setAcceptedPlacement(InternshipApplication application) {
		this.acceptedPlacement = application;
	}
	
	
	
	
	
}
