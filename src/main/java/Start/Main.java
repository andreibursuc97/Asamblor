package Start;

import Model.Reading;
import Model.Transform;
import Model.Writer;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static int nr;

    public static void main(String[] args)
    {
        try {

            String filename = "D:\\FACULTA\\AN 3\\Semestrul 2\\SSC\\Asamblor\\src\\main\\resources\\cod.txt";
            Reading reading = new Reading();
            Transform transform = new Transform();
            List<String> lines = reading.readFile(filename);
            transform.setLabels(reading.getLabels());
            List<String> codes = new ArrayList<>();
            nr=1;
            for (String line : lines) {
                codes.add(transform.transformLine(line,nr));
                nr++;
            }

            Writer writer=new Writer(codes);
            writer.write();
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage()+" at line "+nr);
        }
    }
}
