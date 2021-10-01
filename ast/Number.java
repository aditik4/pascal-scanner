package ast;
import Environment.Environment;
import emitter.Emitter;
/**
 * This class handles creating and returning a number
 * 
 * @author Aditi Khanna
 * @version April 14 2020
 */
public class Number extends Expression
{
    private int value;
    /**
     * Constructs a new number wich holds a particular value
     * @param val the value of the number
     */
    public Number(int val)
    {
        value = val;
    }

    /**
     * Evaluates the number by returning its value
     * @param env the environment where variables are stored
     * @return the value of the number
     */
    public int eval(Environment env)
    {
        return value;
    }
    /**
     * Emits the number 
     * @param e the emitter to emit codee
     */
    public void compile(Emitter e)
    {
        e.emit("li $v0" + value);
    }
}
