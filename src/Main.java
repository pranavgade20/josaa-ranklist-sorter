import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

import java.util.Scanner;

public class Main {
    public static String main(String[] args) throws Exception{
        StringBuilder ret = new StringBuilder();
        System.out.println(args[0]);
        PDDocument doc = PDDocument.load(args[0]);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(doc);

        Scanner scanner = new Scanner(text);
        String line = null;
        out: while (scanner.hasNextLine()) {
            line = scanner.nextLine().trim();
            if (!Character.isDigit(line.charAt(0))) continue;
            while (!Character.isDigit(line.charAt(line.length()-1))) {
                if (!scanner.hasNextLine()) break out;
                line += scanner.nextLine().trim();
            }
            if (!line.toLowerCase().contains("computer")) continue;
            if (!(line.contains("AI") || line.contains("OS"))) continue;
            if (!line.contains("OPEN")) continue;
            if (line.contains("PwD")) continue;
            if (!line.contains("Gender-Neutral")) continue;
            String out = line.replace(",", "").replace("  ", " ");

            String[] arr = out.split(" ");
            out += "," + arr[arr.length-2];
            out += "," + arr[arr.length-1];
            System.out.println(out);
            ret.append(out);
            ret.append('\n');
        }
        doc.close();
        return ret.toString();
    }
}
