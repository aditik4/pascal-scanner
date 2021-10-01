package ast;
import Environment.Environment;
import java.util.List;
/**
 * Procedure Declaration class handles declaring a new procedure
 * 
 * @author Aditi khanna 
 * @version May 1 2020
 */
public class ProcedureDeclaration extends Statement
{
    Statement statement;
    String procName;
    List<String> paramNames;
    /**
     * Constructs a new procedureDeclaration 
     * @param st the statement that comprises the method body
     * @param name the name of the method
     * @param params the parameter list for the method
     */
    public ProcedureDeclaration(Statement st, String name, List<String> params)
    {
        this.statement = st;
        procName = name;
        paramNames = params;
    } 
    /**
     * Executes proc declaration by setting the procedure in the environment
     * @param env the environment that stores the vars
     * 
     */
    public void exec(Environment env) 
    {
       
        env.setProcedure(procName, this);
    }
    /**
     * Reeturns the procedure body statement
     * @return the statement 
     */
    public Statement getProcDec()
    {
        return this.statement;
    }
    /**
     * Returns the formal parameter list
     * @return the list of paramaters
     */
    public List<String> getParams()
    {
        return paramNames;
    }
}
