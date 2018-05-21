package Model;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Transform {

    private static final String detectNumbers;
    private static final String detectRegisters;
    private static final String detectOperations;
    private static final String detectHexa;
    private static final String detectComment;
    private static final String detectLabels;
    private static final String commentAfterOp;
    private HashMap<String,Integer> labels=new HashMap<>();

    static {
        detectNumbers="[, ]+-?\\d+";
        detectRegisters="[, ]*[rR]\\d+";
        detectOperations="^[ \t]*[a-zA-Z]+[0-9,]*[ \n\r]*";
        detectHexa="0x[0-9a-fA-F]{4}";

        detectComment="^[ \t]*;";
        detectLabels="[ \t,]+[a-zA-Z][a-zA-Z0-9]*";
        commentAfterOp="[a-zA-Z0-9 \t,]+;";
    }

    public void setLabels(HashMap<String, Integer> labels) {
        this.labels = labels;
    }

    public String transformLine(String line,int nr) throws IllegalArgumentException
    {

            Pattern comm=Pattern.compile(detectComment);
            Pattern op=Pattern.compile(detectOperations);
            Pattern p = Pattern.compile(detectNumbers);
            Pattern r = Pattern.compile(detectRegisters);
            Pattern h = Pattern.compile(detectHexa);
            Matcher com=comm.matcher(line);
            if(com.find())
            {
                return new String("comment");
            }

            Pattern commA=Pattern.compile(commentAfterOp);
            Matcher comA=commA.matcher(line);
            if(comA.find())
            {
                //System.out.println("aici");
                line=comA.group();
                //System.out.println(line);
            }

            Matcher m = op.matcher(line);
            Operation operation=new Operation("0",0);
            //System.out.println(line);

            if(m.find())
            {
                //System.out.println(m.group());
                String operationString = m.group().replaceAll(" |,", "");

                OperationType operationType=new OperationType(operationString);
                operation=operationType.returnCode();
            }
            else
            {
                throw new IllegalArgumentException("Nu exista aceasta operatie!");
            }

            StringBuilder code=new StringBuilder(operation.getCode()+"_");
            //System.out.println(operation.getCode());


            int registersCheck=0;
            //System.out.println("Registers:");
            m = r.matcher(line);
            while (m.find()) {
                //System.out.println(m.group());
                if(m.group().equals("\n"))
                    break;
                String number = m.group().replaceAll("R|r|,| ", "");
                int num = Integer.parseInt(number);
                if(num<0 || num>16)
                    throw new IllegalArgumentException("Format gresit pentru un registru");
                String binary = Integer.toString(num, 2);
                binary=adjustLength(binary,4,false);
                code.append(binary+"_");
                registersCheck++;
            }

            boolean negativ=false;

            //System.out.println(registersCheck);

            if((operation.getType()==7|| operation.getType()==0) && registersCheck!=0)
                throw new IllegalArgumentException("Format gresit operatie");

            if((operation.getType()==1 || operation.getType()==3 || operation.getType()==4) && registersCheck!=2)
                throw new IllegalArgumentException("Format gresit operatie");

            if((operation.getType()==5 || operation.getType()==6) && registersCheck!=1)
                throw new IllegalArgumentException("Format gresit operatie");



            if(operation.getType()==2 && registersCheck!=3)
                throw new IllegalArgumentException("Format gresit operatie");


            m = p.matcher(line);
            int numbersCheck = 0;
            String numberFianl="";
            while (m.find()) {
                String number = m.group().replaceAll(" |,", "");
                //System.out.println(number);
                String binary = Integer.toString(Integer.parseInt(number), 2);
                numbersCheck++;
                if(Integer.parseInt(number)>=0)
                    numberFianl=binary;
                else
                {
                    numberFianl=reverse(binary);
                    negativ=true;
                }
            }
            //System.out.println(numbersCheck);
            if((operation.getType()==3 || operation.getType()==4) && numbersCheck!=1)
                throw new IllegalArgumentException("Format gresit operatie");

        if(operation.getType()==7 && numbersCheck!=1)
        {
            Pattern lb=Pattern.compile(detectLabels);
            Matcher lbm=lb.matcher(line);
            if(lbm.find())
            {
                String label=lbm.group().replaceAll("[ \t,]","");
                if(labels.containsKey(label)) {
                    if(labels.get(label) - nr>=0)
                    numberFianl = Integer.toString(Integer.parseInt(Integer.toString(labels.get(label) - nr)), 2);
                    else
                    {
                        numberFianl = Integer.toString(Integer.parseInt(Integer.toString(labels.get(label) - nr)), 2);
                        //System.out.println(numberFianl);
                        numberFianl=reverse(numberFianl);
                        negativ=true;
                    }
                }
                else
                    throw new IllegalArgumentException("Format gresit operatie");
            }
        }

            if(operation.getType()==6 && numbersCheck!=1)
            {
                Pattern lb=Pattern.compile(detectLabels);
                Matcher lbm=lb.matcher(line);
                lbm.find();
                lbm.find();
                //String first=lbm.group();
                if(lbm.find())
                {
                    String label=lbm.group().replaceAll("[ \t,]","");
                    if(labels.containsKey(label)) {
                        //System.out.println(label+" "+labels.get(label)+" "+nr);
                        if(labels.get(label) - nr>=0)
                            numberFianl = Integer.toString(Integer.parseInt(Integer.toString(labels.get(label) - nr)), 2);
                        else
                        {
                            numberFianl = Integer.toString(Integer.parseInt(Integer.toString(labels.get(label) - nr)), 2);
                            numberFianl=reverse(numberFianl);
                            negativ=true;
                        }
                    }
                    else
                        throw new IllegalArgumentException("Format gresit operatie");
                }
            }

            if((registersCheck==3  || operation.getType()==1 || operation.getType()==0) && numbersCheck!=0)
                throw new IllegalArgumentException("Format gresit operatie");

            if(operation.getType()==5 && numbersCheck!=0)
            {
                throw new IllegalArgumentException("Format gresit operatie");
            }

            if(operation.getType()==4)
            {
                m = h.matcher(line);
                int hexaCheck = 0;
                while (m.find()) {
                    hexaCheck++;
                    String number = m.group().replaceAll(" |,", "");
                    number= number.replaceAll("0x","");
                    //System.out.println(number);
                    if(number.length()>4)
                        throw new IllegalArgumentException("Format gresit numar hexa");
                    String binary = Integer.toString(Integer.parseInt(number,16), 2);
                    numberFianl=binary;
                    //System.out.println(binary);
                }
                //System.out.println(hexaCheck);
                if(hexaCheck!=1 && numbersCheck!=1)
                    throw new IllegalArgumentException("Format gresit operatie");
            }

            if (operation.getType()==0)
            {
                code.append(adjustLength("",24,negativ));
            }

            if( operation.getType()==5)
                code.append(adjustLength("",20,negativ));

            if(operation.getType()==2)
                code.append((adjustLength("",12,negativ)));

            if(operation.getType()==1 ||operation.getType()==3 || operation.getType()==4)
                code.append(adjustLength(numberFianl,16,negativ));

            if(operation.getType()==6)
            {
                code.append(adjustLength("",4,negativ));
                code.append(adjustLength(numberFianl,16,negativ));
            }

            if(operation.getType()==7)
            {
                code.append(adjustLength("",8,negativ));
                code.append(adjustLength(numberFianl,16,negativ));
            }



            //System.out.println(code.toString());

            return code.toString();


    }

    private String adjustLength(String code,int length,boolean negativ)
    {
        StringBuffer stringBuffer=new StringBuffer(code);
        if(!negativ) {
            while (code.length() < length)
                code = '0' + code;
        }
        else
        {
            while (code.length() < length)
                code = '1' + code;
        }
        return code;
    }

    private String reverse(String numberFianl)
    {
        System.out.println(numberFianl);
        numberFianl=numberFianl.replaceAll("-","");
        int length=numberFianl.length();
        StringBuilder numar=new StringBuilder();
        for(int i=0;i<numberFianl.length();i++)
        {
            if(numberFianl.charAt(i)=='0')
                numar.append("1");
            else
                numar.append("0");
        }
        int local=Integer.parseInt(numar.toString(),2)+1;
        numberFianl=Integer.toString(Integer.parseInt(Integer.toString(local)), 2);
        numberFianl=adjustLength(numberFianl,length,false);
        return numberFianl;
    }


}
