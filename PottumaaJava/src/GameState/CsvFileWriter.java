package GameState;

import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class CsvFileWriter {

    private String outputFileName;

    public CsvFileWriter(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public String convertArrayListIntoCommaSeparatedString(ArrayList<String> list) {
        final String separator = ";";
        var stringBuffer = new StringBuffer();
        var counter = 1;
        var listSize = list.size();
        for (String s : list) {
            stringBuffer.append(s);
            //do not append separator after last element
            if(counter < listSize) {
                stringBuffer.append(separator);
            }
            counter++;
        }
        String str = stringBuffer.toString();
        return str;
    }

    public void writeCsv(ArrayList<String> list) throws IOException {
        File file = new File(outputFileName+ ".csv");
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        for(int i = 0; i < list.size(); i++)
        {
            bw.write(list.get(i));
            bw.newLine();
        }
        bw.close();
        fw.close();
    }
}
