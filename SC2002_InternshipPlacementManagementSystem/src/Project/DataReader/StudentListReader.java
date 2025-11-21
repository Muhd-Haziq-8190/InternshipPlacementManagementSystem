
package Project.DataReader;

import Project.classes.Student;
import java.io.IOException;
import java.util.List;

public class StudentListReader extends BaseListReader {

    public StudentListReader(String folder, String fileName) {
        super(folder, fileName,
                "StudentID,Name,Major,Year,Email,InternshipApplicationIDs,ApplicationCount,AcceptedInternshipApplicationID");
    }

    public void insert(Student student) {
        try {
            List<String[]> rows = readAll();
            rows.add(new String[]{
                    student.getId(),
                    student.getName(),
                    student.getMajor(),
                    String.valueOf(student.getYearOfStudy()),
                    student.getEmail(),
                    student.getApplicationIds(),
                    String.valueOf(student.getApplicationCount()),
                    student.getAcceptedPlacement() != null ? student.getAcceptedPlacement().getId() : ""
            });
            writeAll(rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(Student student) {
        try {
            List<String[]> rows = readAll();
            for (int i = 1; i < rows.size(); i++) {
                if (rows.get(i)[0].equals(student.getId())) {
                    rows.set(i, new String[]{
                            student.getId(),
                            student.getName(),
                            student.getMajor(),
                            String.valueOf(student.getYearOfStudy()),
                            student.getEmail(),
                            student.getApplicationIds(),
                            String.valueOf(student.getApplicationCount()),
                            student.getAcceptedPlacement() != null ? student.getAcceptedPlacement().getId() : ""
                    });
                    break;
                }
            }
            writeAll(rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] findStudentRaw(String studentId) {
        try {
            List<String[]> rows = readAll();
            for (int i = 1; i < rows.size(); i++) {
                if (rows.get(i)[0].equals(studentId)) {
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