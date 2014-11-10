import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Decoder {
	
	public static void main(String [] args)
	{

		try
		{
			File input = new File(args[0]);
		    File output = new File(args[2]);
		    
			InputStream is = new FileInputStream(input);
			OutputStream os = new FileOutputStream(output);
			
			try
			{
				BufferedReader bReader = new BufferedReader(
			            new InputStreamReader(is, args[1])
			    );

				String textString = "", inputLine;
			    while ((inputLine = bReader.readLine()) != null) {
			    	textString += inputLine;
			    }
			    bReader.close();

			    
				BufferedWriter bWriter = new BufferedWriter(
				            new OutputStreamWriter(os, args[3])
				);

				bWriter.write(textString);
				bWriter.close();
			}
			finally
			{
				is.close();
				os.close();
			}
		}
		catch (IOException ex)
		{ ex.printStackTrace(); }
		catch (Exception ex)
		{ ex.printStackTrace(); }
	}

}
