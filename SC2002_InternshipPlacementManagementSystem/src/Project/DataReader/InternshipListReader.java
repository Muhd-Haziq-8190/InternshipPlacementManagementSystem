
package Project.DataReader;

import Project.classes.Internship;
import java.io.IOException;
import java.util.List;

public class InternshipListReader extends BaseListReader {

    public InternshipListReader(String folder, String fileName) {
        super(folder, fileName,
                "InternshipID,Title,Description,Level,PreferredMajor,OpenDate,CloseDate,Status,CompanyName,CompanyRepresentativeID,AvailableSlots,isVisible,InternshipApplicationID");
    }

    public void insert(Internship internship) {
        try {
            List<String[]> rows = readAll();
            rows.add(new String[]{
                    internship.getId(),
                    internship.getTitle(),
                    internship.getDescription(),
                    internship.getLevel(),
                    internship.getPreferredMajor(),
                    String.valueOf(internship.getOpenDate()),
                    String.valueOf(internship.getCloseDate()),
                    internship.getStatus(),
                    internship.getCompanyName(),
                    internship.getCompanyRepresentativeId(),
                    String.valueOf(internship.getSlotsAvailable()),
                    String.valueOf(internship.isVisible()),
                    internship.getApplicationIds()
            });
            writeAll(rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(Internship internship) {
        try {
            List<String[]> rows = readAll();
            for (int i = 1; i < rows.size(); i++) {
                if (rows.get(i)[0].equals(internship.getId())) {
                    rows.set(i, new String[]{
                            internship.getId(),
                            internship.getTitle(),
                            internship.getDescription(),
                            internship.getLevel(),
                            internship.getPreferredMajor(),
                            String.valueOf(internship.getOpenDate()),
                            String.valueOf(internship.getCloseDate()),
                            internship.getStatus(),
                            internship.getCompanyName(),
                            internship.getCompanyRepresentativeId(),
                            String.valueOf(internship.getSlotsAvailable()),
                            String.valueOf(internship.isVisible()),
                            internship.getApplicationIds()
                    });
                    break;
                }
            }
            writeAll(rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] findInternshipRaw(String internshipId) {
        try {
            List<String[]> rows = readAll();
            for (int i = 1; i < rows.size(); i++) {
                if (rows.get(i)[0].equals(internshipId)) {
                    return rows.get(i);
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}