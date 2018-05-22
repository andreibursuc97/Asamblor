package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reading {

    private static final String detectLabels="^[ \t]*[a-zA-Z][a-zA-Z0-9]*[ \t]*:[ \t]*";
    private static final String detectComment="^[ \\t]*;";
    private HashMap<String,Integer> labels=new HashMap<>();

    public HashMap<String, Integer> getLabels() {
        return labels;
    }

    public List<String> readFile(String filename)
    {
        List<String> records = new ArrayList<String>();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            int nr=0;
            while ((line = reader.readLine()) != null)
            {
                Pattern op = Pattern.compile(detectLabels);
                Matcher m = op.matcher(line);
                if(m.find())
                {
                    String operationString = m.group().replaceAll("[ \t;:]", "");
                    labels.put(operationString,nr+1);
                    records.add(";");
                }
                else {
                    records.add(line);
                    Pattern op2 = Pattern.compile(detectComment);
                    Matcher m2 = op2.matcher(line);
                    if(!m2.find())
                    {nr++;}
                }

            }

            for (HashMap.Entry<String, Integer> entry : labels.entrySet())
            {
                System.out.println(entry.getKey() + "/" + entry.getValue());
            }

            reader.close();
            return records;
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
            return null;
        }
    }

}
