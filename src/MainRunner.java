import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Scanner;

public class MainRunner {
    public static String subject;
    public static boolean hs;
    public static boolean pwd;
    public static boolean female;
    public static String category;


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter details to filter:");
        System.out.print("Enter subject- one word is enough(ex. civil for civil engineering): ");
        subject = sc.nextLine().toLowerCase();
        System.out.print("Enter gender- f for female, anything else for gender-neutral: ");
        female = sc.nextLine().toLowerCase().charAt(0) == 'f';
        System.out.print("Do you belong to PwD category(y/n): ");
        pwd = sc.nextLine().toLowerCase().charAt(0) == 'y';
        System.out.print("Do want home state cutoffs(y/n): ");
        hs = sc.nextLine().toLowerCase().charAt(0) == 'y';
        System.out.print("Enter your category(OBC-NCL/GEN-EWS/SC/ST/OPEN): ");
        category = sc.nextLine().toLowerCase();

        for (int i = 1; i < 7; i++) {
//            BufferedWriter bw = new BufferedWriter(new FileWriter(i+".csv"));
//            bw.write(parse(new String[]{Integer.toString(i)}));
//            bw.flush();
//            bw.close();
            try {
                parse(new String[]{Integer.toString(i)});
            } catch (Exception e) {
                System.out.println("Error processing " + i);
            }
        }
    }

    public static void parse(String[] args) throws Exception{
//        StringBuilder ret = new StringBuilder();
        System.out.println("Processing " + args[0]);
        PDDocument doc = PDDocument.load(args[0] + ".pdf");
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(doc);

        //Create blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet
        XSSFSheet spreadsheet = workbook.createSheet("round-" + args[0]);

        //Create row object
        XSSFRow row;

        int rowid = 0;

        Scanner scanner = new Scanner(text);
        String line = null;
        out: while (scanner.hasNextLine()) {
            line = scanner.nextLine().trim();
            if (!Character.isDigit(line.charAt(0))) continue;
            while (!Character.isDigit(line.charAt(line.length()-1))) {
                if (!scanner.hasNextLine()) break out;
                line += scanner.nextLine().trim();
            }
            if (!line.toLowerCase().contains(subject)) continue; //subject
            if (hs ^ line.contains("HS")) continue; //region
            if (!line.toLowerCase().contains(category)) continue; // category
            if (pwd ^ line.contains("PwD")) continue; // pwd or no
            if ((!female) ^ line.contains("Gender-Neutral")) continue; // gender

            int column = 0;
            row = spreadsheet.createRow(rowid++);

            row.createCell(column++).setCellValue(line.replace(",", "").replace("  ", " "));

            System.out.println(line.replace(",", "").replace("  ", " "));
            String[] arr = line.replace(",", "").replace("  ", " ").split(" ");
            row.createCell(column++).setCellValue(arr[arr.length-2]);
            row.createCell(column++).setCellValue(arr[arr.length-1]);
//            ret.append(out);
//            ret.append('\n');
        }

        //Write the workbook in file system
        FileOutputStream out = new FileOutputStream(
                new File(args[0] + ".xlsx"));

        workbook.write(out);
        out.close();
        System.out.println(args[0] + ".xlsx written successfully");

        doc.close();
//        return ret.toString();
    }
}
