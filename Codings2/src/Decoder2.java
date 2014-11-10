import java.io.IOException;
import java.io.File;
import java.io.RandomAccessFile;

public class Decoder2
{
	public static void main(String [] args)
	{
		File fin = new File(args[0]);
		File fout = new File(args[1]);
		long pos = Long.parseLong(args[2]);
		try
		{
		    RandomAccessFile rafin = new RandomAccessFile(fin, "r");
			RandomAccessFile rafout = new RandomAccessFile(fout, "rw");
			try
			{
				byte b = new Byte("0");
				
				for (int i = 0; i < 6; i++)
				{
					rafin.seek(pos);
					b = rafin.readByte();
					//  1000 0000 - 1011 1111
					if (b >= -127 && b <= -64)
					    pos += 1;
					else break;
				}
	
				rafin.seek(pos);
				for (long i = pos; i < rafin.length(); i++)
				    rafout.write(rafin.readByte());
			}
			finally
			{
				rafin.close();
				rafout.close();
			}
		}
		catch (IOException ex)
		{ ex.printStackTrace(); }
		catch (Exception ex)
		{ ex.printStackTrace(); }
	}
}
