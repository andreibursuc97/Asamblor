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

    private final String finish=" \n" +
            "begin      \n" +
            "\n" +
            "process(clk, reset)\n" +
            "begin\n" +
            "    if(reset = '1') then\n" +
            "        PC_int <= x\"00000000\";\n" +
            "    else\n" +
            "        if(rising_edge(clk)) then\n" +
            "            if(we = '1') then\n" +
            "                PC_int <= PC_out;\n" +
            "            end if;\n" +
            "        end if;\n" +
            "    end if;\n" +
            "end process;\n" +
            "    \n" +
            "instruction <= ROM(conv_integer(PC_int(5 downto 0)));\n" +
            "\n" +
            "sum_out <= PC_int + 1;\n" +
            "\n" +
            "process(sum_out, brench_address, PC_src)\n" +
            "begin\n" +
            "    if(PC_src = '0') then\n" +
            "        mux_out1 <= sum_out;\n" +
            "    else\n" +
            "        mux_out1 <= brench_address;\n" +
            "    end if;\n" +
            "end process;\n" +
            "\n" +
            "process(mux_out1, jump_address, jump)\n" +
            "begin\n" +
            "    if(jump = '1') then\n" +
            "        PC_out <= jump_address;\n" +
            "    else\n" +
            "        PC_out <= mux_out1;\n" +
            "    end if;\n" +
            "end process;\n" +
            "\n" +
            "PC_incrementat <= sum_out;\n" +
            "\n" +
            "end Behavioral;";

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
            int nr=1;
            for(String code:codes)
            {
                writer.write("                      B\"");
                if(nr<codes.size())
                {writer.write(code+"\",\n");}
                else
                {
                    writer.write(code+"\"\n                     );\n");
                }
                nr++;
            }

            writer.write(finish);

        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
