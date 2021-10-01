package ast;
import emitter.Emitter;
import Environment.Environment;
/**
 * Assigns a new value to a new variable
 * 
 * @author Aditi Khanna
 * @version 14 April 2020
 */
public class Assignment extends Statement
{
    private String var;
    private Expression ex;
    /**
     * Creates a new Assignment object that holds the name of the
     * variable, and the expression it holds.
     * @param variable the variable name 
     * @param expr the expression to set as the value
     */
    public Assignment(String variable, Expression expr)
    {
        var = variable;
        ex = expr;
    }

    /**
     * Sets a new variable with the value of the evaluated expression's result
     * @param env the environment to store variables
     */
    public void exec(Environment env)
    {
        env.setVariable(var, ex.eval(env));
    }
    /**
     * Uses an emitter to write code for an assignment
     * @param e the emitter to write code to thee file
     */
    public void compile(Emitter e)
    {
        ex.compile(e);
        e.emit("la $t0 var" + var);
        e.emit("sw $v0 ($t0)");
    }
}
