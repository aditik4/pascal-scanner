package ast;
import Environment.Environment;
import emitter.Emitter;
/**
 * An abstract class that handles expressions 
 * 
 * @author Aditi Khanna
 * @version 16 April 2020
 */
public abstract class Expression 
{
    /**
     * Evaluates the expression
     * @param env the environment that stores the varaibles
     * @return the value of the expression after evaluating
     */
    public abstract int eval(Environment env);
    /**
     * Uses emitter to write code for an expression
     * @param e the emitter that emits to the file
     * 
     */
    public void compile(Emitter e)
    {
        throw new RuntimeException("Implement me!!!!!");
    }
}
