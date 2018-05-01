package Start;

import Model.Reading;
import Model.Transform;

import java.util.List;

public class Main {

    public static void main(String[] args)
    {
        String filename="D:\\FACULTA\\AN 3\\Semestrul 2\\SSC\\Asamblor\\src\\main\\resources\\cod.txt";
        Reading reading=new Reading();
        Transform transform=new Transform();
        List<String> lines= reading.readFile(filename);

        for(String line:lines)
        {
            transform.transformLine(line);
        }
    }
}
