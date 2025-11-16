package Project.DataReader;


import java.io.*;
import java.util.*;

public abstract class ListReader {

    protected final String filePath;

    public ListReader(String folder, String fileName) {
        File dir = new File(folder);
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("已创建数据目录：" + dir.getAbsolutePath());
        }

        this.filePath = folder + File.separator + fileName;
        ensureFileExists();

        List<String[]> data = readCsv();
        loadToDB(data);
    }

    // -------------------------
    // 子类必须实现的方法
    // -------------------------
    protected abstract void loadToDB(List<String[]> data);

    // -------------------------
    // 通用：确保文件存在
    // -------------------------
    public void ensureFileExists() {
        File f = new File(filePath);
        if (!f.exists()) {
            try {
                f.createNewFile();
                System.out.println("已创建数据文件：" + f.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException("无法创建文件: " + filePath);
            }
        }
    }

    // -------------------------
    // 通用：读取 CSV
    // -------------------------
    protected List<String[]> readCsv() {
        List<String[]> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            while ((line = br.readLine()) != null) {
                list.add(line.split(","));
            }

        } catch (IOException e) {
            System.err.println("CSV读取失败: " + filePath);
        }

        return list;
    }
}
