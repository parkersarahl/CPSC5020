import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
                    }
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
