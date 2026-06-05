package mobileAutomation.utilities;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelManager {

    public static String[][] getMethodData(String methodName) {

        try (FileInputStream excelFile = new FileInputStream(Constants.TEST_DATA_EXCEL_PATH);
             XSSFWorkbook excelWBook = new XSSFWorkbook(excelFile)) {

            XSSFSheet excelWSheet = excelWBook.getSheet(Constants.TEST_DATA_EXCEL_SHEET_NAME);
            if (excelWSheet == null) {
                throw new RuntimeException("Sheet not found: " + Constants.TEST_DATA_EXCEL_SHEET_NAME);
            }

            /* If the test method name is not found in the first column,
             * testMethodRowNumber will be 0
             * */
            int testMethodRowNumber = getMethodRowNumber(excelWSheet, methodName);
            return getTableArray(excelWSheet, testMethodRowNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getMethodRowNumber(XSSFSheet excelWSheet, String testMethodName) {
        int lastRowCount = excelWSheet.getLastRowNum();
        int testCaseRow = 0;
        int i = 1;
        while (i <= lastRowCount) {
            String getMethodCellData = getCellData(excelWSheet, i, 0);
            if (getMethodCellData.equalsIgnoreCase(testMethodName)) {
                testCaseRow = i;
                break;
            }
            i++;
        }
        return testCaseRow;
    }

    private static String[][] getTableArray(XSSFSheet excelWSheet, int testMethodRowNumber) {

        int totalCols;
        try {
            totalCols = excelWSheet.getRow(testMethodRowNumber).getLastCellNum() - 1;
        } catch (Exception e) {
            throw new RuntimeException("Please add default data on 1st row of your Testdata.xlsx");
        }
        for (int c = 1 ; c <= totalCols ; c++) {
            String cellCheckData = getCellData(excelWSheet, testMethodRowNumber, c);
            if (cellCheckData.isEmpty()) {
                totalCols = c - 1;
                break;
            }
        }

        int ci = 0;
        String[][] tabArray = new String[1][totalCols];
        int j = 1;
        while (j <= totalCols) {
            tabArray[0][ci] = getCellData(excelWSheet, testMethodRowNumber, j);
            j++;
            ci++;
        }
        return tabArray;
    }

    private static String getCellData(XSSFSheet excelWSheet, int rowNumber, int columnNumber) {
        XSSFCell cell = excelWSheet.getRow(rowNumber).getCell(columnNumber);

        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };


    }

}
