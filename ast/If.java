package ast;
import Environment.Environment;
import emitter.Emitter;
/**
 * Handles and executes if statements
 * 
 * @author Aditi Khanna
 * @version 14 april 2020
 */
public class If extends Statement
{
    private Condition con;
    private Statement stmt;

    /**
     * Constructs a new if statment with a condition and executing statment
     * @param condit the statment to consider
     * @param state the statment to execute if true
     */
    public If(Condition condit, Statement state)
    {
        con = condit;
        stmt = state;
    }

    /**
     * Executes the if statment
     * @param env the enviornment that holds the variables
     */
    public void exec(Environment env)
    {
        if(con.eval(env))  
            stmt.exec(env);
    }
    /**
     * Uses an emitter to write code for an if statemnet
     * @param e the emitter that writes to the file
     */
    public void compile(Emitter e)
    {
        String label = "endif" + Integer.toString(e.nextLabelID());
        con.compile(e, label);
        stmt.compile(e);
        e.emit(label + ":");
    }
}
