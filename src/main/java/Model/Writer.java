package Model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Writer {

    private final String start="library IEEE;\n" +
            "use IEEE.STD_LOGIC_1164.ALL;\n" +
            "use IEEE.STD_LOGIC_ARITH.ALL;\n" +
            "use IEEE.STD_LOGIC_UNSIGNED.ALL;\n" +
            "entity UnitateIF is\n" +
            "    Port ( clk : in STD_LOGIC;\n" +
            "           RESET : in STD_LOGIC;\n" +
            "           WE : in STD_LOGIC;\n" +
            "           brench_address: in STD_LOGIC_VECTOR (31 downto 0);\n" +
            "           jump_address: in STD_LOGIC_VECTOR(31 downto 0);\n" +
            "           PC_src: in STD_LOGIC;\n" +
            "           jump: in STD_LOGIC;\n" +
            "           PC_incrementat: out STD_LOGIC_VECTOR(31 downto 0);\n" +
            "           instruction: out STD_LOGIC_VECTOR (31 downto 0)\n" +
            "           );\n" +
            "end UnitateIF;\n" +
            "\n" +
            "architecture Behavioral of UnitateIF is\n" +
            "signal PC_int : STD_LOGIC_VECTOR (31 downto 0);\n" +
            "signal PC_out: STD_LOGIC_VECTOR (31 downto 0);\n" +
            "signal sum_out : STD_LOGIC_VECTOR (31 downto 0);\n" +
            "signal mux_out1 : STD_LOGIC_VECTOR (31 downto 0);\n" +
            "\n" +
            "type rom_type  is array (0 to 48 ) of STD_LOGIC_VECTOR (31 downto 0);\n" +
            "signal ROM:rom_type:=(\n";

    private List<String> codes;

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public Writer(List<String> codes) {
        this.codes = codes;
    }

    public void write()
    {
        try(FileWriter writer=new FileWriter("memory.txt")){

            writer.write(start);
            for(String code:codes)
            {
                writer.write("                      B\"");
                writer.write(code+"\",\n");
            }

        }catch (IOException e){
            e.printStackTrace();
        }

    }
}