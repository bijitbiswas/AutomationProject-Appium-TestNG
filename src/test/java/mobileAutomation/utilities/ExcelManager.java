package mobileAutomation.utilities;

import mobileAutomation.Constants;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelManager {

    private XSSFSheet excelWSheet = null;

    public String[][] getMethodData(String methodName) {

        FileInputStream excelFile;
        XSSFWorkbook excelWBook;
        try {
            excelFile = new FileInputStream(Constants.TEST_DATA_EXCEL_PATH);
            excelWBook = new XSSFWorkbook(excelFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        excelWSheet = excelWBook.getSheet(Constants.TEST_DATA_EXCEL_SHEET_NAME);

        /* If the test method name is not found in the first column,
         * testMethodRowNumber will be 0
         * */
        int testMethodRowNumber = getMethodRowNumber(methodName);
        return getTableArray(testMethodRowNumber);
    }

    private int getMethodRowNumber(String testMethodName) {
        int lastRowCount = excelWSheet.getLastRowNum();
        int testCaseRow = 0;
        int i = 1;
        while (i <= lastRowCount) {
            String getMethodCellData = getCellData(i, 0);
            if (getMethodCellData.equalsIgnoreCase(testMethodName)) {
                testCaseRow = i;
                break;
            }
            i++;
        }
        return testCaseRow;
    }

    private String[][] getTableArray(int testMethodRowNumber) {

        int totalCols;
        try {
            totalCols = excelWSheet.getRow(testMethodRowNumber).getLastCellNum() - 1;
        } catch (Exception e) {
            throw new RuntimeException("Please add default data on 1st row of your Testdata.xlsx");
        }
        for (int c = 1 ; c <= totalCols ; c++) {
            String cellCheckData = getCellData(testMethodRowNumber, c);
            if (cellCheckData.isEmpty()) {
                totalCols = c - 1;
                break;
            }
        }

        int ci = 0;
        String[][] tabArray = new String[1][totalCols];
        int j = 1;
        while (j <= totalCols) {
            tabArray[0][ci] = getCellData(testMethodRowNumber, j);
            j++;
            ci++;
        }
        return tabArray;
    }

    private String getCellData(int rowNumber, int columnNumber) {
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
