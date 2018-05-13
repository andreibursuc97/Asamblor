package Model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Transform {

    private static final String detectNumbers;
    private static final String detectRegisters;
    private static final String detectOperations;
    private static final String detectHexa;
    private static final String type0;

    static {
        detectNumbers="[, ]+\\d+";
        detectRegisters="[, ]*[rR]\\d+";
        detectOperations="[a-zA-Z]+[0-9,]*[ \n\r]*";
        detectHexa="0x[0-9a-fA-F]{4}";
        type0="[a-zA-Z][ ]*";
    }

    public String transformLine(String line) throws IllegalArgumentException
    {


            Pattern op=Pattern.compile(detectOperations);
            Pattern p = Pattern.compile(detectNumbers);
            Pattern r = Pattern.compile(detectRegisters);
            Pattern h = Pattern.compile(detectHexa);

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
                binary=adjustLength(binary,4);
                code.append(binary+"_");
                registersCheck++;
            }


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
                numberFianl=binary;
            }
            //System.out.println(numbersCheck);
            if((operation.getType()==7 || operation.getType()==6 || operation.getType()==3 || operation.getType()==4) && numbersCheck!=1)
                throw new IllegalArgumentException("Format gresit operatie");

            if((registersCheck==3  || operation.getType()==1 || operation.getType()==0) && numbersCheck!=0)
                throw new IllegalArgumentException("Format gresit operatie");

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
                if(hexaCheck!=1)
                    throw new IllegalArgumentException("Format gresit operatie");
            }

            if (operation.getType()==0)
            {
                code.append(adjustLength("",24));
            }

            if(operation.getType()==1 || operation.getType()==5)
                code.append(adjustLength("",20));

            if(operation.getType()==2)
                code.append((adjustLength("",12)));

            if(operation.getType()==3 || operation.getType()==4)
                code.append(adjustLength(numberFianl,16));

            if(operation.getType()==6)
            {
                code.append(adjustLength("",4));
                code.append(adjustLength(numberFianl,16));
            }

            if(operation.getType()==7)
            {
                code.append(adjustLength("",8));
                code.append(adjustLength(numberFianl,16));
            }



            System.out.println(code.toString());

            return code.toString();


    }

    private String adjustLength(String code,int length)
    {
        StringBuffer stringBuffer=new StringBuffer(code);
        while(code.length()<length)
            code='0'+code;
        return code;
    }



}
