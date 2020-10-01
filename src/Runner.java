import java.io.BufferedWriter;
import java.io.FileWriter;

public class Runner {
    public static void main(String[] args) throws Exception{
        for (int i = 1; i < 7; i++) {
            BufferedWriter bw = new BufferedWriter(new FileWriter(i+".csv"));
            bw.write(Main.main(new String[]{i+".pdf"}));
            bw.flush();
            bw.close();
        }
    }
}
