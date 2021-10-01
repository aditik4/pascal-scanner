package ast;
import Environment.Environment;
import emitter.Emitter;
/**
 * Variable holds the name of the variable and is able to obtain
 * information on its value
 * 
 * @author Aditi Khanna
 * @version 14 April 2020
 */
public class Variable extends Expression
{
    // instance variables - replace the example below with your own
    private String name;

    /**
     * Constructor a new Variable with particular name
     * @param vname the name of the new variable
     */
    public Variable(String vname)
    {
        name = vname;
    }

    /**
     * Evaluates the variable, obtains information about its value
     * @param env the environment to obtain information from
     * @return the value of the particular variable
     */
    public int eval(Environment env)
    {
        return env.getVariable(name);
    }
    /**
     * Uses an emitter to write code for a variable
     * @param e the emitter to put code on the file
     */
    public void compile(Emitter e)
    {
        e.emit("la $t0 var" + name);
        e.emit("lw $v0 ($t0)");
    }
}
