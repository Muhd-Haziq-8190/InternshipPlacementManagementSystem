package Project;

import java.time.LocalDate;


public class Internship {
	protected int idCounter= 1;		// counter for unique Internship IDs
	
	protected String id;
	protected String title;
	protected String description;
	protected String level;			// 1. Basic, 2. Intermediate, 3. Advanced
	protected String preferredMajor;
	protected LocalDate openDate;
	protected LocalDate closeDate;
	protected String status;		// Pending, Approved, Rejected, Fiiled
	protected String companyName;
	protected CompanyRepresentative companyRep;
	protected int slotsAvailable; 	// can only accept a max of 10 students
	protected boolean visibility;
	
    private InternshipApplication[] internAppli = new InternshipApplication[30];		// only allowing a max of 30 students to apply, 30 being an arbitrary number
    private int applicationCount = 0;													// checks how many students have currently applied
	
    public Internship(String title, String description, String level,
            String preferredMajor, LocalDate openDate,
            LocalDate closeDate, String companyName,
            CompanyRepresentative companyRep, int slots) {

		this.id = "INT" + (idCounter++);  
		this.title = title;
		this.description = description;
		this.level = level;
		this.preferredMajor = preferredMajor;
		this.openDate = openDate;
		this.closeDate = closeDate;
		this.companyName = companyName;
		this.companyRep = companyRep;
//		this.slotsAvailable = Math.max(0, Math.min(10, slots)); // ensure 0-10
		this.slotsAvailable = 10;	// defaults to 10 slots available
		this.visibility = true; 	// visible by default
		this.status = "Pending";	// pending by default when created
    }
    
    
    // checks if current internship is available for display for specified student
    public boolean isOpenFor(Student s) {
    	// if internship visibility is not enabled at all
    	if (!this.visibility) {
        	return false;
        }
    	
    	// if status of internship status is still Pending, Rejected, or Filled
        if (!status.equalsIgnoreCase("Approved")) {
        	return false;
        }
        
        // if the current date is within duration that internship is open
        LocalDate now = LocalDate.now();
        if (now.isBefore(openDate) || now.isAfter(closeDate)) {
        	return false;
        }
        
        // if current student is eligible based on their year
        if (!s.canApplyForLevel(level)) {
        	return false;
        }
        
        // if current student matches the preferred major of the internship requirements
        if (!preferredMajor.equalsIgnoreCase(s.getMajor())) {
        	return false;
        }
        
        // returns true if meets above conditions and there is enough slots available
        return slotsAvailable > 0;
    }
    
    
	public void addApplication(InternshipApplication appli) {
		// if amount of applications don't exceed max amount of applications available
		// add a new application
        if (this.applicationCount < this.internAppli.length) {
            internAppli[this.applicationCount++] = appli;
        }
        
        
    }
	
	public void confirmSlot() {
		// once slot is confirmed, reduce current available slots
        if (this.slotsAvailable > 0) {
        	this.slotsAvailable--;
        }
        
        // change status to Filled if reduced to 0
        if (this.slotsAvailable == 0) {
        	status = "Filled";
        }
    }
	
	// --------- GETTER & SETTER --------- //
    
	public String getId() {
		return this.id;
	}
	
	public String getDescription() {
		return this.description;
	}
    
	public String getTitle() {
		return this.title;
	}
	
	public String getCompanyName() {
		return this.companyName;
	}

    public String getStatus() {
    	return this.status;
    }
    
    public void setStatus(String s) {
    	status = s;
    }
    
    public int getSlotsAvailable() {
    	return this.slotsAvailable;
    }
    
    public String getLevel() {
    	return this.level;
    }
    
    public int getApplicationCount() {
    	return this.applicationCount;
    }
    
    public void toggleVisibility() {
    	this.visibility= !this.visibility; 
    }
    
    public InternshipApplication[] getInternshipApplications() {
    	return this.internAppli;
    }

    
    
	

}
