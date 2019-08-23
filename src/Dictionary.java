import java.util.*;
import java.io.*;

public class Dictionary {

    public static void main(String[] args) throws FileNotFoundException{
        Scanner sc = new Scanner(System.in);

        FileReader code = new FileReader("sowpods.txt");
        Scanner scan = new Scanner(code);
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("dictionary.txt"));
            String str;

            while (scan.hasNextLine()) {
                str = scan.nextLine();
                if (str.length() <= 10) {
                    out.write(str + "\n");
                }
            }
            out.close();
        }
        catch (IOException e) {

        };
    }
}
