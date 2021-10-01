package ast;
import Environment.Environment;
import emitter.Emitter;
/**
 * This class handles the execution of while statements
 * 
 * @author Aditi Khanna
 * @version 14 April 2020
 */
public class While extends Statement
{
    private Condition con;
    private Statement stmt;

    /**
     * Constructs a new while statement
     * @param condit the condition of the statment
     * @param state the statement to execute
     */
    public While(Condition condit, Statement state)
    {
        con = condit;
        stmt = state;
    }

    /**
     * Evaluates the while loop
     * @param env the environment that holds the variables
     */
    public void exec(Environment env)
    {
        while(con.eval(env))
        {
            stmt.exec(env);
        }
    }
    /**
     * USes an emitter to write code for a while statement
     * @param e the emitter that emits to the file
     */
    public void compile(Emitter e)
    {
        String label = "endWhile" + Integer.toString(e.nextLabelID());
        String start = "while" + Integer.toString(e.nextLabelID());
        e.emit(start);
        con.compile(e, label);
        stmt.compile(e);
        e.emit("j" + start);
        e.emit(label + ":");
    }
}
