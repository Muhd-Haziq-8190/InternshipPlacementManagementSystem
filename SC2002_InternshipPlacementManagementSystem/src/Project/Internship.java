package Project;

import java.time.LocalDate;


public class Internship {
	private static int idCounter= 1;		// counter for unique Internship IDs
	
	private String id;
	private String title;
	private String description;
	private String level;			// 1. Basic, 2. Intermediate, 3. Advanced
	private String preferredMajor;
	private LocalDate openDate;
	private LocalDate closeDate;
	private String status;		// Pending, Approved, Rejected, Fiiled
	private String companyName;
	private CompanyRepresentative companyRep;
	private int slotsAvailable; 	// can only accept a max of 10 students
	private boolean visibility;
	
    private InternshipApplication[] internAppli = new InternshipApplication[30];		// only allowing a max of 30 students to apply, 30 being an arbitrary number
    private int applicationCount = 0;													// checks how many students have currently applied
	
    // Use when creating new internship
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
    
    // Use when retrieved from csv
    public Internship(String id, String title, String description, String level,
            String preferredMajor, LocalDate openDate,
            LocalDate closeDate, String companyName,
            CompanyRepresentative companyRep, String status, boolean visibility, InternshipApplication[] internAppli, int slots) {

		this.id = id;
		this.title = title;
		this.description = description;
		this.level = level;
		this.preferredMajor = preferredMajor;
		this.openDate = openDate;
		this.closeDate = closeDate;
		this.companyName = companyName;
		this.companyRep = companyRep;
		this.slotsAvailable = Math.max(1, Math.min(10, slots)); // allows max of 10 slots
		this.visibility = visibility; 	// visible by default
		this.status = status;	// pending by default when created
		this.internAppli = internAppli;
		
		// --- UPDATE ID COUNTER SO NEW IDS DON'T CLASH ---
	    int numeric = Integer.parseInt(id.replace("INT", ""));
	    if (numeric >= idCounter) {
	        idCounter = numeric + 1;
	    }
    }
    
    
    // checks if current internship is available for display for specified student
    public boolean isOpenFor(Student s) {
    	// if internship visibility is not enabled at all
    	if (!this.visibility) {
    		System.out.println("Internship Class, isOpenFor() method: visibility False");
        	return false;
        }
    	
    	// if status of internship status is still Pending, Rejected, or Filled
        if (!status.equalsIgnoreCase("Approved")) {
        	System.out.println("Internship Class, isOpenFor() method: status field not \"Approved\"");
        	return false;
        }
        
        // if the current date is within duration that internship is open
        LocalDate now = LocalDate.now();
        if (now.isBefore(openDate) || now.isAfter(closeDate)) {
        	System.out.println("Internship Class, isOpenFor() method: Date outside open-close duration");
        	return false;
        }
        
        // if current student is eligible based on their year
        if (!s.canApplyForLevel(level)) {
        	System.out.println("Internship Class, isOpenFor() method: Student is not at appropriate Level");
        	return false;
        }
        
        // if current student matches the preferred major of the internship requirements
        if (!preferredMajor.equalsIgnoreCase(s.getMajor())) {
        	System.out.println("Internship Class, isOpenFor() method: Student is not in Preferred Major");
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
	
	public void removeApplication(InternshipApplication application) {
		
		//loop through all applications to find target application
	    for (int i = 0; i < this.applicationCount; i++) {
	        if (this.internAppli[i] == application) {
	        	
	            // shift remaining applications left to "replace and remove" target application 
	            for (int j = i; j < this.applicationCount - 1; j++) {
	            	this.internAppli[j] = this.internAppli[j + 1];
	            }
	            
	            // decrement application count
	            // clear last ref
	            this.internAppli[--this.applicationCount] = null;
	            break;
	        }
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
	
	// add back the additional slot if application is withdrawn
	public void releaseSlot() {
		if(this.getSlotsAvailable() < 10) {
			this.slotsAvailable++;
			if(this.getStatus().equals("Filled")) {
				this.setStatus("Approved");
			}
		}
	}
	
	
	 public String getApplicationIds() {
	        
        // final string applicationIDs should append to
        String internshipIds;
        
        // if no internships
        if (internAppli == null || internAppli.length == 0) {
            internshipIds = "";   
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
            internshipIds = sb.toString();
        }
        
        return internshipIds;
	}
	
	// --------- GETTER & SETTER --------- //
	
	public String getCompanyRepresentativeId() {
    	return this.companyRep.getId();
    }
	
	public CompanyRepresentative getCompanyRepresentative() {
    	return this.companyRep;
    }
	
	public String getPreferredMajor() {
		return this.preferredMajor;
	}
	
	public LocalDate getOpenDate() {
		return this.openDate;
	}
	
	public LocalDate getCloseDate() {
		return this.closeDate;
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
    
    public void setStatus(String status) {
    	this.status = status;
    }
    
    public int getSlotsAvailable() {
    	return this.slotsAvailable;
    }
    
    public void setSlotsAvailable(int slotsAvailable) {
    	this.slotsAvailable = slotsAvailable;
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
    
    public boolean isVisible() {
    	return this.visibility;
    }
    
    
    public InternshipApplication[] getInternshipApplications() {
    	return this.internAppli;
    }
    
   

}
