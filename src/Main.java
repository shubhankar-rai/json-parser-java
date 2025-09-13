import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {

        if(args.length == 0) {
            System.err.println("Usage: java Main <path_to_json_file>");
            System.exit(1);
        }

        String path = args[0];

        try {
            String content = Files.readString(Paths.get(path)).trim();
            Parser parser = new Parser(content);

            if(parser.parse()) {
                System.out.println("Valid JSON");
                System.exit(0);
            } else {
                System.out.println("Invalid JSON");
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("Invalid JSON");
            System.exit(1);
        }
    }
}