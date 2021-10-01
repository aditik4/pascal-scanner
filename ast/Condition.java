package ast;
import emitter.Emitter;
import Environment.Environment;
/**
 * Condition class handles conditions within if and while statements
 * 
 * @author Aditi khanna 
 * @version april 14 2020
 */
public class Condition
{
    // instance variables - replace the example below with your own
    private String op;
    private Expression exp1;
    private Expression exp2;

    /**
     * Constructs a condition that will evaluate a quantity relationship
     * @param oper the operation to evaluate with
     * @param e1 the first expression
     * @param e2 the second expression
     */
    public Condition(String oper, Expression e1, Expression e2)
    {
        op = oper;
        exp1 = e1;
        exp2 = e2;
    }

    /**
     * Evalutates the boolean expression
     * @param env the environment that holds the variables
     * @return true if the statement is true; otherwise,
     *              false
     */
    public boolean eval(Environment env)
    {
        if(op.equals(">"))
        {
            return (exp1.eval(env)) > (exp2.eval(env));
        }
        if(op.equals("="))
        {
            return (exp1.eval(env)) == (exp2.eval(env));
        }
        if(op.equals("<"))
        {
            return (exp1.eval(env)) < (exp2.eval(env));
        }
        if(op.equals("<>"))
        {
            return (exp1.eval(env)) !=(exp2.eval(env));
        }
        if(op.equals("<="))
        {
            return (exp1.eval(env)) <= (exp2.eval(env));
        }
        if(op.equals(">="))
        {
            return (exp1.eval(env)) >= (exp2.eval(env));
        }
        return false;
    }
    /**
     * Uses an emitter to write code for conditionals
     * @param e the emitter that writes to the file
     */
    public void compile(Emitter e, String label)
    {
        exp1.compile(e);
        e.emitPush("$v0");
        exp2.compile(e);
        e.emitPop("$t0");
        if(op.equals(">"))
        {
            e.emit("ble $t0 $v0" + label);
        }
        else if(op.equals("<"))
        {
            e.emit("bge $t0, $v0" + label);
        }
        else if(op.equals("="))
        {
            e.emit("bne $t0 $v0" + label);
        }
        else if (op.equals("<>"))
        {
            e.emit("beq $t0 $v0" + label);
        }
        else if(op.equals(">="))
        {
            e.emit("blt $t0 $v0" + label);
        }
        else
        {
            e.emit("bgt $t0 $v0" + label);
        }
    }
}
