package ast;
import Environment.Environment;
import emitter.Emitter;
/**
 * Writeln handles Writeln expressions
 * 
 * @author Aditi Khanna
 * @version April 14 2020
 */
public class Writeln extends Statement
{
    // instance variables - replace the example below with your own
    private Expression exp;

    /**
     * Constructs a new Writeln object
     * @param exp the expression to print out
     */
    public Writeln(Expression exp)
    {
        this.exp = exp;
    }

    /**
     * Executes the Writeln statment by evaluating the expression 
     * and printing it out
     * @param env the environment where the variables are stored
     */
    public void exec(Environment env)
    {
        System.out.println(exp.eval(env));
    }
    /**
     * Uses an emitter to write code from a writeln
     * @param e the emitter
     */
    public void compile(Emitter e)
    {
        exp.compile(e);
        e.emit("move $a0 $v0");
        e.emit("li $v0 1");
        e.emit("syscall");
        e.emit("la $a0 nl");
        e.emit("li $v0 4");
        e.emit("syscall");
    }
}
