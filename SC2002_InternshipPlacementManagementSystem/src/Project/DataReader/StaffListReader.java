
package Project.DataReader;

import Project.classes.CareerStaff;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StaffListReader extends BaseListReader {

    public StaffListReader(String folder, String fileName) {
        super(folder, fileName, "StaffID,Name,Role,Department");
    }

    public void insert(CareerStaff careerStaff) {
        try {
            List<String[]> rows = readAll();
            rows.add(new String[]{
                    careerStaff.getId(),
                    careerStaff.getName(),
                    careerStaff.getRole(),
                    careerStaff.getDepartment()
            });
            writeAll(rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(CareerStaff careerStaff) {
        try {
            List<String[]> rows = readAll();
            for (int i = 1; i < rows.size(); i++) {
                if (rows.get(i)[0].equals(careerStaff.getId())) {
                    rows.set(i, new String[]{
                            careerStaff.getId(),
                            careerStaff.getName(),
                            careerStaff.getRole(),
                            careerStaff.getDepartment()
                    });
                    break;
                }
            }
            writeAll(rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CareerStaff findCareerStaff(String col, String value) {
        try {
            List<String[]> rows = readAll();
            if (rows.isEmpty()) return null;

            int colNum = -1;
            String[] header = rows.get(0);
            for (int i = 0; i < header.length; i++) {
                if (header[i].equals(col)) {
                    colNum = i;
                    break;
                }
            }
            if (colNum == -1) return null;

            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                if (row[colNum].equals(value)) {
                    return new CareerStaff(row[0], row[1], row[2], row[3]);
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CareerStaff> getAllCareerStaff() {
        List<CareerStaff> staffList = new ArrayList<>();
        try {
            List<String[]> rows = readAll();
            if (rows.size() <= 1) return staffList;

            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                CareerStaff cs = new CareerStaff(row[0], row[1], row[2], row[3]);
                staffList.add(cs);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    public List<CareerStaff> getCareerStaffBy(String columnName, String value) {
        List<CareerStaff> careerStaffs = new ArrayList<>();
        try {
            List<String[]> rows = readAll();
            if (rows.size() <= 1) return careerStaffs;

            String[] header = rows.get(0);
            int colIndex = -1;
            for (int i = 0; i < header.length; i++) {
                if (header[i].equalsIgnoreCase(columnName)) {
                    colIndex = i;
                    break;
                }
            }
            if (colIndex == -1) return careerStaffs;

            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                if (row.length >= 4 && row[colIndex].equals(value)) {
                    careerStaffs.add(new CareerStaff(row[0], row[1], row[2], row[3]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return careerStaffs;
    }
}