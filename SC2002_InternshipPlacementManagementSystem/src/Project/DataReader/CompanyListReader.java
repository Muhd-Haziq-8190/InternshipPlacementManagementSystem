
package Project.DataReader;

import Project.classes.CompanyRepresentative;
import Project.classes.Internship;

import java.io.IOException;
import java.util.List;

public class CompanyListReader extends BaseListReader {

    public CompanyListReader(String folder, String fileName) {
        super(folder, fileName,
                "CompanyRepID,Company,Name,Department,Position,Status,InternshipIDs,InternshipCount");
    }

    public void insert(CompanyRepresentative companyRep) {
        try {
            List<String[]> rows = readAll();
            rows.add(new String[]{
                    companyRep.getId(),
                    companyRep.getCompanyName(),
                    companyRep.getName(),
                    companyRep.getDepartment(),
                    companyRep.getPosition(),
                    companyRep.getAccountStatus(),
                    companyRep.getInternshipIds(),
                    String.valueOf(companyRep.getInternshipCount())
            });
            writeAll(rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(CompanyRepresentative companyRep) {
        try {
            List<String[]> rows = readAll();
            for (int i = 0; i < rows.size(); i++) {
                if (rows.get(i)[0].equals(companyRep.getId())) {
                    rows.set(i, new String[]{
                            companyRep.getId(),
                            companyRep.getCompanyName(),
                            companyRep.getName(),
                            companyRep.getDepartment(),
                            companyRep.getPosition(),
                            companyRep.getAccountStatus(),
                            companyRep.getInternshipIds(),
                            String.valueOf(companyRep.getInternshipCount())
                    });
                    break;
                }
            }
            writeAll(rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] findCompanyRepRaw(String repId) {
        try {
            List<String[]> rows = readAll();
            for (int i = 1; i < rows.size(); i++) {
                if (rows.get(i)[0].equals(repId)) {
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








