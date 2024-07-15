import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.*;


public class parser {

    private ArrayList<String> data = new ArrayList<String>();
    String path;
    String line = "";


    public parser(String path){
        this.path = path;

        try {
            BufferedReader br =  new BufferedReader(new FileReader(path));
                String text;
                line = br.readLine();
                text = line;
                while(!text.matches("(?m)^\\.*", "a"); 
                //&& (line = br.readLine()) != null)
                {
                    data.add(text);
                }
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

    //print out data file
    public void ShowData()
    {
        for (int i = 0; i< data.size(); i++)
        {
            System.out.println(data.get(i));
        }
    }

}
