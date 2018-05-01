package Model;

public class OperationType {

    private String input;

    public OperationType(String input)
    {
        this.input=input;
    }

    public Operation returnCode()
    {
        this.input=input.toLowerCase();
        switch (this.input)
        {
            case "nop": return new Operation("00000000",0);
            case "mova": return new Operation("01000000",1);
            case "add": return new Operation("00000010",2);
            case "sub": return new Operation("00000101",2);
            case "and": return new Operation("00001000",2);
            case "or": return new Operation("00001001",2);
            case "xor": return new Operation("00001010",2);
            case "not": return new Operation("00001011",1);
            case "addi": return new Operation("00100010",3);
            case "subi": return new Operation("00100101",3);
            case "andi": return new Operation("00101000",4);
            case "ori": return new Operation("00101001",4);
            case "xori": return new Operation("00101010",3);
            case "addu": return new Operation("01000010",3);
            case "subu": return new Operation("01000101",3);
            case "movb": return new Operation("00001100",1);
            case "shr": return new Operation("00001101",3);
            case "shl": return new Operation("00001110",3);
            case "ld": return new Operation("00010000",1);
            case "st": return new Operation("00100000",1);
            case "jmpr": return new Operation("01110000",5);
            case "sgte": return new Operation("01110101",2);
            case "slt": return new Operation("01100101",2);
            case "bz": return new Operation("01100101",6);
            case "bnz": return new Operation("01010000",6);
            case "jmp": return new Operation("01101000",7);
            case "jmpl": return new Operation("00110000",6);
            case "halt": return new Operation("01101001",0);
            default: throw new IllegalArgumentException("Nu exista aceasta operatie!");
        }
    }
}
