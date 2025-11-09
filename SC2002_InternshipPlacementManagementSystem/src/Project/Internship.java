package Project;

import java.time.LocalDate;


public class Internship {
	protected int idCounter= 1;
	
	protected String id;
	protected String title;
	protected String description;
	protected String level;			// 1. Basic, 2. Intermediate, 3. Advanced
	protected String preferredMajor;
	protected LocalDate openDate;
	protected LocalDate closeDate;
	protected String status;		// Pending, Approved, Rejected, Failed
	protected String companyName;
	protected CompanyRepresentative companyRep;
	protected int slotsAvailable;
	protected boolean visibility;
	
    private InternshipApplication[] internAppli = new InternshipApplication[30];
    private int applicationCount = 0;
	
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
		this.slotsAvailable = Math.max(0, Math.min(10, slots)); // ensure 0-10
		this.visibility = true;
    }
    
    
    public boolean isOpenFor(Student s) {
        if (!this.visibility) {
        	return false;
        } 
        if (!status.equalsIgnoreCase("Approved")) {
        	return false;
        }
        
        
        LocalDate now = LocalDate.now();
        if (now.isBefore(openDate) || now.isAfter(closeDate)) {
        	return false;
        }
        
        if (!s.canApplyForLevel(level)) {
        	return false;
        }
        
        if (!preferredMajor.equalsIgnoreCase(s.getMajor())) {
        	return false;
        }
        
        return slotsAvailable > 0;
    }
    
	public void addApplication(InternshipApplication appli) {
        if (this.applicationCount < this.internAppli.length) {
            internAppli[this.applicationCount++] = appli;
        }
    }

	public void confirmSlot() {
        if (this.slotsAvailable > 0) {
        	this.slotsAvailable--;
        }
        
        if (this.slotsAvailable == 0) {
        	status = "Filled";
        }
    }
	
    
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

    
    
	

}
