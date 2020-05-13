package com.coin.msdict.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName ExcelUtil
 * @Description: TODO
 * @Author kh
 * @Date 2020/1/6 8:59
 * @Version V1.0
 **/
public class ExcelUtil {

    /**
     * @MethodName export
     * @Description 数据导出到excel，并传输到web
     * @param response
     * @param wbName excel文件名称，可为空，为空命名为“数据”
     * @param sheetName excel表名称，可为空，为空命名为“数据”
     * @param head 表标题
     * @param names 字段名称
     * @param data 待导出数据
     * @return boolean 返回成功 true, 失败 false
     * @author kh
     * @date 2020/1/6 11:23
     */
    public static boolean export(HttpServletResponse response,
                     String wbName, String sheetName,
                     String[] head, String[] names, List<Map> data) {
        Workbook wb = new XSSFWorkbook();
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            Sheet sheet = null;
            if(StringUtils.isEmpty(sheetName)) {
                sheet = wb.createSheet("数据");
            } else {
                sheet = wb.createSheet(sheetName);
            }

            // 第一行合并单元格
            CellStyle mainHeadStyle = wb.createCellStyle();
            mainHeadStyle.setAlignment(HorizontalAlignment.CENTER);
            mainHeadStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            mainHeadStyle.setBorderBottom(BorderStyle.THIN);
            mainHeadStyle.setBorderTop(BorderStyle.THIN);
            mainHeadStyle.setBorderRight(BorderStyle.THIN);
            mainHeadStyle.setBorderLeft(BorderStyle.THIN);

            Font mainHeadFont = wb.createFont();
            mainHeadFont.setFontName("黑体");
            mainHeadFont.setBold(true);
            mainHeadStyle.setFont(mainHeadFont);
            Row mainHeadRow = sheet.createRow(0);
            CellRangeAddress region = new CellRangeAddress(0, 0, 0, names.length - 1);
            sheet.addMergedRegion(region);
            Cell mainHeadCell = mainHeadRow.createCell(0);
            mainHeadCell.setCellValue(wbName);
            mainHeadCell.setCellStyle(mainHeadStyle);
            mainHeadRow.setHeightInPoints((short) 20);

            Cell mainHeadCellRight = mainHeadRow.createCell(names.length - 1);
            mainHeadCellRight.setCellStyle(mainHeadStyle);

            // 每列最大宽度
            int[] widthArray = new int[names.length];
            for(int i=0; i<widthArray.length; i++) {
                widthArray[i] = head[i].length() * 2;
            }
            // 数据格式
            CellStyle dataStyle = wb.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.CENTER);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);

            // 数据
            int rowNum = 2;
            for(Map map : data) {
                // 获取每列的数据类型
                Row dataRow = sheet.createRow(rowNum);
                dataRow.setHeightInPoints((short) 15);
                rowNum += 1;
                for(int i=0; i < names.length; i++) {
                    if(Objects.isNull(map.get(names[i]))) {
                        Cell dataCellNone = dataRow.createCell(i, CellType.STRING);
                        dataCellNone.setCellValue("");
                        dataCellNone.setCellStyle(dataStyle);
                    } else {
                        Cell dataCellNum = dataRow.createCell(i, CellType.NUMERIC);
                        dataCellNum.setCellStyle(dataStyle);
                        String str = map.get(names[i]).toString();
                        if(str.length() * 2 > widthArray[i]) {
                            widthArray[i] = str.length() * 2;
                        }
                        if(NumberUtil.isNumeric(str)) {
                            if(map.get(names[i]) instanceof Integer) {
                                dataCellNum.setCellValue((Integer) map.get(names[i]));
                            } else if(map.get(names[i]) instanceof Float) {
                                dataCellNum.setCellValue((Float) map.get(names[i]));
                            } else if(map.get(names[i]) instanceof Double) {
                                dataCellNum.setCellValue((Double) map.get(names[i]));
                            } else {
                                dataCellNum.setCellValue(str);
                            }
                        } else {
                            Cell dataCellStr = dataRow.createCell(i, CellType.STRING);
                            dataCellStr.setCellStyle(dataStyle);
                            dataCellStr.setCellValue(str);
                        }
                    }
                }
            }

            Row headRow = sheet.createRow(1);

            // 表头格式
            CellStyle headStyle = wb.createCellStyle();
            headStyle.setAlignment(HorizontalAlignment.CENTER);
            headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headStyle.setBorderBottom(BorderStyle.THIN);
            headStyle.setBorderTop(BorderStyle.THIN);
            headStyle.setBorderRight(BorderStyle.THIN);
            headStyle.setBorderLeft(BorderStyle.THIN);

            Font headFont = wb.createFont();
            headFont.setFontName("黑体");
            headFont.setBold(true);
            headStyle.setFont(headFont);
            headRow.setHeightInPoints((short) 18);
            // 表头

            for(int i=0; i<head.length; i++) {
                Cell cell = headRow.createCell(i, CellType.STRING);
                cell.setCellStyle(headStyle);
                cell.setCellValue(head[i]);
                if(widthArray[i] >= 30) {
                    sheet.setColumnWidth(i, 256 * 30);
                } else {
                    sheet.setColumnWidth(i, (int)(256 * widthArray[i] * 1.5));
                }
            }

            if(StringUtils.isEmpty(wbName)) {
                wbName = "数据";
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(wbName.getBytes("gbk"),"iso8859-1") + ".xlsx");
            wb.write(os);
            os.flush();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if(null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @Description: 根据第一行的列数读取数据
     * @Param: [fs]
     * @return: java.util.List<java.util.List<java.lang.String>>
     * @Author: kh
     * @Date: 2018/8/10 9:16
     */
    public static List<List<String>> readExcel(InputStream fs, int rowNum) throws Exception {
        List<List<String>> rowList = new ArrayList<List<String>>();
        try {
            Workbook workBook = WorkbookFactory.create(fs);
            for (int i = 0; i < workBook.getNumberOfSheets(); i++) {
                // 创建工作表
                Sheet sheet = workBook.getSheetAt(i);
                int rows = sheet.getPhysicalNumberOfRows(); // 获得行数
                if (rows > 0) {
                    sheet.getMargin(HSSFSheet.TopMargin);
                    //获取第一行的列数
                    int cells = sheet.getRow(rowNum).getLastCellNum();
                    for (int r = 0; r < rows; r++) { // 行循环
                        Row row = sheet.getRow(r);
                        if (row != null && r > rowNum) {// 不取第一行
                            //int cells = row.getLastCellNum();// 获得列数
                            // 定义集合datas用于存Excel中一个行的数据
                            Vector<String> datas = new Vector<String>();
                            boolean flag_emptyRow = true;
                            for (short c = 0; c < cells; c++) { // 列循环
                                Cell cell = row.getCell(c);
                                if (cell != null) {
                                    String value;
                                    value = getValue(cell);
                                    if(!"".equals(value)) {
                                        flag_emptyRow = false;
                                    }
                                    datas.add(value);
                                } else {
                                    datas.add(null);
                                }
                            }
                            if(!flag_emptyRow) {
                                rowList.add(datas);
                            }
                        }
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if(null != fs) {
                fs.close();
            }
        }
        return rowList;
    }

    /**
     * @Description: 从合并单元格excel读取数据
     * @Param: [fs, initRow, endCombineColumn]
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @Author: kh
     * @Date: 2018/9/11 20:39
     */
    public static List<Map<String, Object>> readCombineExcel(InputStream fs, int initRow, int endCombineColumn) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        //按行循环，
        Workbook workBook = WorkbookFactory.create(fs);
        for (int i = 0; i < workBook.getNumberOfSheets(); i++) {
            Sheet sheet = workBook.getSheetAt(i);
            //所有合并单元格
            List<CellRangeAddress> cellRangeAddresses = sheet.getMergedRegions();

            int rows = sheet.getPhysicalNumberOfRows();
            if(rows == 0) {
                continue;
            }
            //标题列数
            int cols = sheet.getRow(initRow).getLastCellNum();
            //要先判断行有没有合并的
            for(int r=0; r<rows; r++) {
                Row row = sheet.getRow(r);
                if (row != null && r > initRow) {
                    //该行是否有合并
                    boolean ismerge = isMergeRegion(sheet, r, 0);
                    Map<String, Object> outMap = new HashMap<>();
                    boolean flag_emptyRow = true;
                    for(int c=0; c<endCombineColumn; c++) {
                        Cell cell = row.getCell(c);
                        putValue(cell, outMap, c);
                    }

                    List<Map<String, Object>> custList = new ArrayList<>();
                    int lastRow = getLastRow(sheet, r);
                    if(ismerge) {
                        for(; r<=lastRow; r++) {
                            Map<String, Object> custMap = new HashMap<>();
                            row = sheet.getRow(r);
                            for(int c=endCombineColumn+1; c<cols; c++) {
                                Cell cell = row.getCell(c);
                                putValue(cell, custMap, c);
                            }
                            custList.add(custMap);
                        }
                        r--;
                    } else {
                        Map<String, Object> custMap = new HashMap<>();
                        for(int c=endCombineColumn+1; c<cols; c++) {
                            Cell cell = row.getCell(c);
                            putValue(cell, custMap, c);
                        }
                        custList.add(custMap);
                    }

                    outMap.put("m", custList);

                    list.add(outMap);
                }
            }
        }
        return list;
    }

    public static List<Map<String, Object>> readExcel(MultipartFile file, int initRow) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        //按行循环，
        Workbook workBook = WorkbookFactory.create(file.getInputStream());
        for (int i = 0; i < workBook.getNumberOfSheets(); i++) {
            Sheet sheet = workBook.getSheetAt(i);

            int rows = sheet.getPhysicalNumberOfRows();
            if(rows == 0) {
                continue;
            }
            //标题列数
            int cols = sheet.getRow(initRow).getLastCellNum();
            //要先判断行有没有合并的
            for(int r=0; r<rows; r++) {
                Row row = sheet.getRow(r);
                if (row != null && r > initRow) {
                    Map<String, Object> outMap = new HashMap<>();
                    for(int c=0; c<cols; c++) {
                        Cell cell = row.getCell(c);
                        putValue(cell, outMap, c);
                    }

                    list.add(outMap);
                }
            }
        }
        return list;
    }

    public static int getLastRow(Sheet sheet, int row) {
        List<CellRangeAddress> cellRangeAddresses = sheet.getMergedRegions();
        for(CellRangeAddress cra : cellRangeAddresses) {
            if(row >= cra.getFirstRow() && row <= cra.getLastRow()
                    && cra.getFirstRow() < cra.getLastRow()) {
                return cra.getLastRow();
            }
        }
        return row;
    }

    /**
     * @Description: 判断是否为合并单元格
     * @Param: [sheet, row, col]
     * @return: boolean
     * @Author: kh
     * @Date: 2018/9/11 17:11
     */
    public static boolean isMergeRegion(Sheet sheet, int row, int col) {
        List<CellRangeAddress> cellRangeAddresses = sheet.getMergedRegions();
        for(CellRangeAddress cra : cellRangeAddresses) {
            if(row >= cra.getFirstRow() && row <= cra.getLastRow()
                    && cra.getFirstRow() < cra.getLastRow()) {
                return true;
            }
            if(col >= cra.getFirstColumn() && col <= cra.getLastColumn()
                    && cra.getFirstColumn() < cra.getLastColumn()) {
                return true;
            }
        }
        return false;
    }

    public static void putValue(Cell cell, Map map, int c) {
        if (cell != null) {
            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                cell.setCellType(CellType.STRING);
            }
            String value;
            value = getValue(cell);

            map.put("c" + c, value);
        } else {
            map.put("c" + c, null);
        }
    }

    public static void putValue(Workbook workbook, Cell cell, List list, int c) {
        if (cell != null) {
            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                if(HSSFDateUtil.isCellDateFormatted(cell)) {
                    CellStyle style=workbook.createCellStyle();
                    DateFormat format = new SimpleDateFormat();
                    short df = workbook.createDataFormat().getFormat("yyyy-MM-dd");
                    style.setDataFormat(df);
                    cell.setCellStyle(style);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String readDateValue = dateFormat.format(cell.getDateCellValue());
                    cell.setCellValue(readDateValue);
                } else {
                    cell.setCellType(CellType.STRING);
                }
            }
            String value;
            value = getValue(cell);

            list.add(value);
        } else {
            list.add(null);
        }
    }

    public static String getValue(Cell cell) {
        String value = "";

        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是date类型则 ，获取该cell的date值
                    //value = HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();
                    //Date date1 = new Date(value);
                    Date date1 = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    value = format.format(date1);
                } else {// 纯数字
                    cell.setCellType(CellType.STRING);
                    value = String.valueOf(cell.getStringCellValue());
                }
                break;
            case STRING:
                /* 此行表示单元格的内容为string类型 */
                value = cell.getStringCellValue();
                break;
            case FORMULA:
                // 读公式计算值
                value = String.valueOf(cell.getNumericCellValue());
                if (value.equals("NaN")) {
                    // 如果获取的数据值为非法值,则转换为获取字符串
                    value = cell.getStringCellValue().toString();
                }
                cell.getCellFormula();
                break;
            case BOOLEAN:
                value = " " + cell.getBooleanCellValue();
                break;
            /* 此行表示该单元格值为空 */
            case BLANK:
                value = "";
                break;
            case ERROR:
                value = "";
                break;
            default:
                value = cell.getStringCellValue().toString();
        }

        return value;
    }

    public static void main(String[] args) throws IOException {
        String[] heads = {"开始", "行进", "结束"};
        String[] names = {"start", "going", "end"};
        List<Map> list = new ArrayList<>();
        HashMap<Object, Object> map = new HashMap<>();
        map.put("start", 1);
        map.put("going", 2.1f);
        map.put("end", "地区");
        list.add(map);
        HashMap<Object, Object> map1 = new HashMap<>();
        map1.put("start", null);
        map1.put("going", 3.2d);
        map1.put("end", "地区");
        list.add(map1);

        HashMap<Object, Object> map2 = new HashMap<>();
        map2.put("start", 100);
        map2.put("going", 5.5);
        map2.put("end", "100%");
        list.add(map2);

        //exportTest("测试", "测试", heads, names, list);
    }
}
