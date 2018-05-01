package Model;

public class Operation {
    private String code;
    private int type;

    /*
    type 0: nop;halt
    type 1: R1,R4   //2 registre
    type 2: R2,R1,R4
    type 3: R2,R0,4
    type 4: R8,R2,0x8000(ANDI, ORI)
    type 5: R3 (JMPR)
    type 6: R3,8
    type 7: 10 (JMP)
     */

    public Operation(String code, int type) {
        this.code = code;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
