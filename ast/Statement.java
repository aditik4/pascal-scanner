package ast;
import Environment.Environment;
import emitter.Emitter;
/**
 * An abstract class that handles Statements
 * 
 * @author Aditi Khanna
 * @version April 16, 2020
 */
public abstract class Statement
{
    /**
     * Executes the statement 
     * @param env the environment that holds the varialbles
     */
    public abstract void exec(Environment env);
    /**
     * Uses an emitter to write code for a statement
     * @param e the emitter that emits to the file
     */
   
    public void compile(Emitter e)
    {
        throw new RuntimeException("Implement me!!!!!");
    }
}
