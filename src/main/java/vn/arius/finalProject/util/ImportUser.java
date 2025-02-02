package vn.arius.finalProject.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.arius.finalProject.entity.User;

@Service
public class ImportUser {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static boolean isValidExcelFile(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<User> getUserDataFrom(InputStream inputStream) {
        List<User> users = new ArrayList<>();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("Users");
            int rowIndex = 0;

            if (sheet == null) {
                throw new IllegalArgumentException("Sheet 'user' not found in the Excel file");
            }

            for (Row row : sheet) {
                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                User user = new User();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cellIndex) {
                        case 0: {
                            break;
                        }
                        case 1: {
                            if (cell.getCellType() == CellType.STRING) {
                                user.setEmail(cell.getStringCellValue());
                            }
                            break;
                        }
                        case 2: {

                            if (cell.getCellType() == CellType.STRING) {
                                user.setName(cell.getStringCellValue());
                            }
                            break;
                        }
                        case 3: {

                            if (cell.getCellType() == CellType.STRING) {
                                user.setAddress(cell.getStringCellValue());
                            }
                            break;
                        }
                        case 4: {
                            if (cell.getCellType() == CellType.STRING) {
                                user.setPhone(cell.getStringCellValue());
                            }
                            break;
                        }
                        case 5: {
                            if (cell.getCellType() == CellType.STRING) {
                                user.setGender(cell.getStringCellValue());
                            }
                            break;
                        }
                        case 6: {
                            if (cell.getCellType() == CellType.NUMERIC) {
                                user.setAge((int) cell.getNumericCellValue());
                            }
                            break;
                        }
                        case 7: {
                            if (cell.getCellType() == CellType.STRING) {
                                user.setPassword(cell.getStringCellValue());
                            }
                            break;
                        }
                        default:
                            break;
                    }
                    cellIndex++;
                }
                users.add(user);
            }
        } catch (IOException e) {
            System.out.println(">>>>>> Error: " + e.getMessage());
        }
        return users;
    }
}
