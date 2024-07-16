import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class parser {

    private ArrayList<String> data = new ArrayList<String>();
    String path;
    String line = "";


    public parser(String path){
        this.path = path;

        try {
            BufferedReader br =  new BufferedReader(new FileReader(path));
   
            while((line = br.readLine()) != null)
                {
                line = line.replace(" ", "");
                    if (!line.matches("[//].*"))
                    {
                        data.add(line);
                        data.removeAll(Arrays.asList("", null));
                    }
                    }
            br.close();
            }

        catch (FileNotFoundException e)
        {
        e.printStackTrace();
        }
        catch (IOException e)
        {
        e.printStackTrace();
     }
    }

    public void save_data()
    {
        for (int i = 0; i < data.size(); i++)
        {
            if (data.get(i).matches("[@].*"))
            {
                String num = (data.get(i));
                String numSubString = num.substring(1, num.length());
                int number = Integer.parseInt(numSubString);
                String binary_number = Integer.toBinaryString(number);
                data.set(i, binary_number);
                System.out.println(data.get(i));
            }
        }  
    }
   

    //print out data file
    public void ShowData()
    {
        for (int i = 0; i< data.size(); i++)
        {
            System.out.println(data.get(i));
        }
    }

}
