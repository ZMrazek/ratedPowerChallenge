package bruteForce;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelImporter {

    public static List<Task> importTasksFromExcel(String filePath, int numRows, int sheetIndex) {
        List<Task> tasks = new ArrayList<>();
        Map<String, Task> taskMap = new HashMap<>();

        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(sheetIndex);

            // Iterate through rows up to the specified number of rows (numRows)
            int rowCount = 0;
            for (Row row : sheet) {
                if (rowCount >= numRows) {
                    break;
                }

                // Check if there are at least two cells in the row
                if (row.getPhysicalNumberOfCells() < 2) {
                    // Log a warning or handle the case where there are not enough cells
                    continue;
                }

                // Process first and second columns
                String taskName = row.getCell(0).getStringCellValue();
                Integer delayedCost = (int) row.getCell(1).getNumericCellValue();

                // Create a task with name and delay cost
                Task task = new Task(taskName, delayedCost);

                tasks.add(task);
                taskMap.put(taskName, task);

                rowCount++;
            }

            // Process dependencies for the first numRows rows
            rowCount = 0;
            for (Row row : sheet) {
                if (rowCount >= numRows) {
                    break;
                }

                // Check if there is a third cell in the row
                if (row.getPhysicalNumberOfCells() >= 3) {
                    Cell dependenciesCell = row.getCell(2);
                    if (dependenciesCell != null) {
                        String[] dependencyNames = dependenciesCell.getStringCellValue().split(",");
                        String taskName = row.getCell(0).getStringCellValue();
                        Task task = taskMap.get(taskName);

                        if (task != null) {
                            for (String dependencyName : dependencyNames) {
                                Task dependency = taskMap.get(dependencyName.trim());
                                if (dependency != null) {
                                    task.addDependency(dependency);
                                }
                            }
                        }
                    }
                }

                rowCount++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return tasks;
    }
}
