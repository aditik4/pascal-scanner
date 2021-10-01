package ast;
import java.util.*; 
import Environment.Environment;

/**
 * Handles calling the procedure 
 * 
 * @author Aditi Khanna
 * @version May 1 2020
 */
public class ProcedureCall extends Expression
{
    String name;
    List<Expression> expressions;

    /**
     * Constructs a new Procedure call 
     * @param name the name of the procedure
     * @param paramExpressions the parameters of the procedure
     */
    public ProcedureCall(String name, List<Expression> paramExpressions )
    {
        this.name = name;
        expressions = paramExpressions;

    }  

    /**
     * Evaluates the statement within the procedure
     * @param env the environemnt that holds variables
     * @return the result of the method call
     */
    public int eval(Environment env)
    {
        Environment child = new Environment(env);
        child.declareVariable(name, 0);
        ProcedureDeclaration pd = env.getProcedure(name);
        List<String> params = pd.getParams();
        for (int i = 0; i < expressions.size() && i < params.size(); i++) 
        {
            env.declareVariable(params.get(i), expressions.get(i).eval(env));
        }
        Statement st = pd.getProcDec();
        st.exec(child);
        return child.getVariable(name);
    }

}    
