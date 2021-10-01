package ast;
import java.util.List;
import Environment.Environment;
import emitter.Emitter;
/**
 * Handles block statments, executing each statement within it
 * one by one
 * 
 * @author Aditi khanna
 * @version April 14 2020
 */
public class Block extends Statement
{
    // instance variables - replace the example below with your own
    private List<Statement> stmts;

    /**
     *Constructs a new block
     *@param statements the list of statements called
     */
    public Block(List<Statement> statements)
    {
        stmts = statements;
    }
    /**
     * Executes each statment within the block
     * @param env the environment that holds the variables
     */
    public void exec(Environment env)
    {
        for(int i = 0; i<= stmts.size()-1; i++)
        {
            stmts.get(i).exec(env);
        }
    }
    /**
     * Uses an emitter to write code for a block
     * @param e the emitter 
     */
    public void compile(Emitter e)
    {
        for(int i = 0; i < stmts.size(); i++)
        {
            stmts.get(i).compile(e);
        }
    }
}
