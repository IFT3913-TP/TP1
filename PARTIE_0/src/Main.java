import jls.Jls;

public class Main {

    public static void main(String[] args) {
        if (args.length == 1) {
            Jls jl1 = new Jls(args[0]);
            System.out.print(jl1.getCSVOutput());
        } else {
            System.err.println("Only one argument (the path of the directory to analyze) is expected by this program");
            System.exit(1);
        }
    }
}