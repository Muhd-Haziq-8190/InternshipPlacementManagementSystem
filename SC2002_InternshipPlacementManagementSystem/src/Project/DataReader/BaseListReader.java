
package Project.DataReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Base class for all list readers to reduce code duplication
 */
public abstract class BaseListReader {
    protected final String filePath;
    protected final String header;

    public BaseListReader(String folder, String fileName, String header) {
        // Ensure directory exists
        File dir = new File(folder);
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("Data directory created: " + dir.getAbsolutePath());
        }

        this.filePath = folder + File.separator + fileName;
        this.header = header;

        // Create file with header if it doesn't exist
        File file = new File(filePath);
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(header + "\n");
                System.out.println("Data file created: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Common read/write operations
    public List<String[]> readAll() throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line.split(","));
            }
        }
        return rows;
    }

    public List<String[]> readWithoutHeader() throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                rows.add(line.split(","));
            }
        }
        return rows;
    }

    protected void writeAll(List<String[]> rows) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (String[] row : rows) {
                writer.write(String.join(",", row) + "\n");
            }
        }
    }

    public void printAll() {
        try {
            List<String[]> rows = readAll();
            for (String[] row : rows) {
                System.out.println(String.join("\t", row));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(String id) {
        try {
            List<String[]> rows = readAll();
            rows.removeIf(row -> !row[0].equals("id") && row[0].equals(id));
            writeAll(rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> find(String col, String value) {
        try {
            List<String[]> rows = readAll();
            if (rows.isEmpty()) return Collections.emptyList();

            // Find column index
            int colNum = -1;
            String[] header = rows.get(0);
            for (int i = 0; i < header.length; i++) {
                if (header[i].equals(col)) {
                    colNum = i;
                    break;
                }
            }
            if (colNum == -1) return Collections.emptyList();

            // Find value
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                if (row[colNum].equals(value)) {
                    return Arrays.asList(row);
                }
            }
            return Collections.emptyList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}