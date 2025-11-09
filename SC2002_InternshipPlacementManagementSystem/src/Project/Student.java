package Project;

public class Student extends User {
	protected String major;
	protected int yearOfStudy;
	protected String email;
	protected InternshipApplication[] internAppli;
	protected int applicationCount;
	protected InternshipApplication acceptedPlacement = null;
	
	public Student(String userId, String name, int yearOfStudy, String major, String email) {
		super(userId, name);
		this.major = major;
		this.yearOfStudy = yearOfStudy;
		this.email = email;
		acceptedPlacement = null;
		applicationCount = 0;
		internAppli = new InternshipApplication[3];
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
	
	public boolean hasAcceptedPlacement() {
        return acceptedPlacement != null;
    }
	
	public InternshipApplication[] getInternshipApplications() {
		InternshipApplication[] applications = new InternshipApplication[applicationCount];
        for (int i = 0; i < this.applicationCount; i++) applications[i] = this.internAppli[i];
        return applications;
    }
	
	public boolean canApplyForLevel(String level) {
        if (yearOfStudy <= 2) {
        	return level.equalsIgnoreCase("Basic");
        }
        return true;
    }

	
	public boolean applyTo(Internship internship) {
		if(this.applicationCount >= 3) {
			return false;
		}
		if(!internship.isOpenFor(this)) {
			return false;
		}
		
		InternshipApplication appli = new InternshipApplication(this, internship);
		internAppli[applicationCount++] = appli;
		internship.addApplication(appli);
		return true;
		
	}
	
	public void displayInternshipApplications() {
		if(internAppli.length != 0) {
			for(int i = 0; i < internAppli.length; i++) {
				System.out.printf("%s, %s\n", internAppli[i].internship.getTitle(), internAppli[i].internship.getCompanyName());
			}
		}
	}
	
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
	
	
	
	
	
	
	
	
	
	
	
}
