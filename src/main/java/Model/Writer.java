package Model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Writer {

    private final String start="library IEEE;\n" +
            "use IEEE.STD_LOGIC_1164.ALL;\n" +
            "use IEEE.STD_LOGIC_UNSIGNED.ALL;\n" +
            "\n" +
            "entity mem_instr is\n" +
            "    Generic (DIM_MI : INTEGER := 256);          -- dimensiunea memoriei de instructiuni (cuvinte)\n" +
            "    Port ( Clk      : in  STD_LOGIC;\n" +
            "           Rst      : in  STD_LOGIC;\n" +
            "           Adr      : in  STD_LOGIC_VECTOR (7 downto 0);\n" +
            "           Data     : out STD_LOGIC_VECTOR (31 downto 0));\n" +
            "end mem_instr;\n" +
            "\n" +
            "architecture Behavioral of mem_instr is\n" +
            "    type MI_TYPE is array (0 to DIM_MI-1) of STD_LOGIC_VECTOR (31 downto 0);\n" +
            "    signal MI : MI_TYPE := (\n";

    private final String finish=" \n" +
            "\n" +
            "    begin\n" +
            "        process (Clk)\n" +
            "        begin\n" +
            "            if RISING_EDGE (Clk) then\n" +
            "                if (Rst = '1') then\n" +
            "                    Data <= (others => '0');\n" +
            "                else\n" +
            "                    Data <= MI (CONV_INTEGER (Adr));\n" +
            "                end if;\n" +
            "            end if;\n" +
            "        end process;\n" +
            "    end Behavioral;";

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
