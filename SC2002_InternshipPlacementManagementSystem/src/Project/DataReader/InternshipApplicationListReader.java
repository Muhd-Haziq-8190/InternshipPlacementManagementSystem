
package Project.DataReader;

import Project.classes.InternshipApplication;
import java.io.IOException;
import java.util.List;

public class InternshipApplicationListReader extends BaseListReader {

    public InternshipApplicationListReader(String folder, String fileName) {
        super(folder, fileName,
                "InternshipApplicationID,StudentID,InternshipID,Status,isAccepted,withdrawalRequested,withdrawAccepted");  // 添加 withdrawAccepted 列
    }

    public void insert(InternshipApplication application) {
        try {
            List<String[]> rows = readAll();

            rows.add(new String[]{
                    application.getId(),
                    application.getStudent().getId(),
                    application.getInternship().getId(),
                    application.getStatus(),
                    String.valueOf(application.isAccepted()).toUpperCase(),  // 改为大写
                    String.valueOf(application.isWithdrawalRequested()).toUpperCase(),  // 改为大写
                    String.valueOf(application.isWithdrawAccepted()).toUpperCase()  // 新增：withdrawAccepted 列
            });
            writeAll(rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(InternshipApplication application) {
        try {
            List<String[]> rows = readAll();

            for (int i = 1; i < rows.size(); i++) {
                if (rows.get(i)[0].equals(application.getId())) {
                    rows.set(i, new String[]{
                            application.getId(),
                            application.getStudent().getId(),
                            application.getInternship().getId(),
                            application.getStatus(),
                            String.valueOf(application.isAccepted()).toUpperCase(),  // 改为大写
                            String.valueOf(application.isWithdrawalRequested()).toUpperCase(),  // 改为大写
                            String.valueOf(application.isWithdrawAccepted()).toUpperCase()  // 新增：withdrawAccepted 列
                    });
                    break;
                }
            }
            writeAll(rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] findApplicationRaw(String applicationId) {
        try {
            List<String[]> rows = readAll();
            for (int i = 1; i < rows.size(); i++) {
                if (rows.get(i)[0].equals(applicationId)) {
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