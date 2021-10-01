package ast;
import Environment.Environment;
import emitter.Emitter;
/**
 * BinOp handles Binary Operations (multiply, divide,
 * subtract, add) between two expressions; 
 * 
 * @author Aditi Khanna
 * @version 14 April 2020
 */
public class BinOp extends Expression
{
    // instance variables - replace the example below with your own
    private String op;
    private Expression exp1;
    private Expression exp2;

    /**
     * constructs a new Binary operation
     * @param oper the operation to perform in string form
     * @param e1 the first expression
     * @param e2 the second expression
     */
    public BinOp(String oper, Expression e1, Expression e2)
    {
        op = oper;
        exp1 = e1;
        exp2 = e2;
    }

    /**
     * Evaluate the binary operation
     * @param env the environment to obtain variable values with
     * @return the result of the operation
     */
    public int eval(Environment env)
    {
        if(op.equals("/"))
        {
            return (exp1.eval(env))/ (exp2.eval(env));
        }
        if(op.equals("+"))
        {
            return (exp1.eval(env))+ (exp2.eval(env));
        }
        if(op.equals("-"))
        {
            return (exp1.eval(env)) - (exp2.eval(env));
        }
        else
        {
            return (exp1.eval(env)) *(exp2.eval(env));
        }

    }
    /**
     * Uses an emitter to write code for a binary operation
     * @param e the emitter that writes to the file
     */
    public void compile(Emitter e)
    {
        exp1.compile(e);
        e.emitPush("$v0");
        exp2.compile(e);
        e.emitPop("$t0");
        if(op.equals("+"))
        {
            e.emit("addu $v0 $t0 $v0");
        }
        else if(op.equals("-"))
        {
            e.emit("subu $v0 $t0 $v0");
        }
        else if(op.equals("*"))
        {
            e.emit("mult $v0 $t0");
            e.emit("mflo $v0");
        }
        else
        {
            e.emit("div $t0 $v0");
            e.emit("mflo $v0");
        }
    }
}
