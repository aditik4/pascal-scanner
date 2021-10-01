package emitter;
import ast.*;
import java.io.*;

public class Emitter
{
	private PrintWriter out;
    private int countLabel = 0;
	//creates an emitter for writing to a new file with given name
	public Emitter(String outputFileName)
	{
		try
		{
			out = new PrintWriter(new FileWriter(outputFileName), true);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	//prints one line of code to file (with non-labels indented)
	public void emit(String code)
	{
		if (!code.endsWith(":"))
			code = "\t" + code;
		out.println(code);
	}

	//closes the file.  should be called after all calls to emit.
	public void close()
	{
		out.close();
	}
	
	public void emitPush(String reg)
	{
	    this.emit("subu $sp $sp 4");
	    this.emit("sw" +reg + "($sp)");
	   }
	   
	public void emitPop(String reg)
	 {
	   emit("lw + $t1 +0($sp)");
       emit("addu $sp, $sp, 4");
 
	   }
	public int nextLabelID()
	{
	    return countLabel++;
	    
	   }
}