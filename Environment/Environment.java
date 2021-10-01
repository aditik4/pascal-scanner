package Environment;
import java.util.*; 
import ast.Statement;
import ast.ProcedureDeclaration;

/**
 * The environment class holds all the variables and their information. You 
 * can also set and get the variables.
 * 
 * 
 * @author Aditi Khanna
 * @version May 2, 2020
 */
public class Environment
{
    private HashMap<String,Integer> varMap;
    private HashMap<String, ProcedureDeclaration> procMap;
    Environment parent;
    /**
     * Constructs a new environment with null parent
     * 
     */
    public Environment()
    {
        varMap = new HashMap<String, Integer>();
        procMap = new HashMap<String, ProcedureDeclaration>();
        parent = null;
    }

    /**
     * Constructs an environment with a parent
     * @param env the parent enviornment
     * 
     */
    public Environment(Environment env)
    {
        varMap = new HashMap<String, Integer>();
        procMap = new HashMap<String, ProcedureDeclaration>();
        parent = env;
    }

    /**
     * sets the value of the variable in the global environment if the
     *  variable is found in the global environment otherwise, creates in
     *  current env
     * @param variable the name of the variable
     * @param value the value to set it to 
     */
    public void setVariable(String variable, int value)
    {
        if(parent != null)
        {
            if(!varMap.containsKey(variable))
            {
                if(parent.varMap.containsKey(variable))
                {
                    varMap.put(variable, value);
                }
            }
            varMap.put(variable, value);
        }
        varMap.put(variable, value);
    }

    /**
     * Obtains the variable's value from current environment or the global
     * if not found.
     * @param variable the name of the variable
     * @return the value of the variable
     */
    public int getVariable(String variable)
    {
        if(parent != null)
        {
            if(varMap.containsKey(variable))
            {
                return varMap.get(variable);
            }
            else
            {
                return parent.getVariable(variable);
            }
        }
        return varMap.get(variable);
    }

    /**
     * Retrieves the procedure from the parent environment
     * @return the procedure declartion from the parent
     * @param procName the name of the preoceure
     */
    public ProcedureDeclaration getProcedure(String procName)
    {
        if(parent == null)
        {   
            return procMap.get(procName);
        }
        else
        {
            return parent.getProcedure(procName);
        }
    }

    /**
     * Sets the procedure into the env
     * @param procName the name of the proc
     * @param pd the procedure declaration to put in the map
     */
    public void setProcedure(String procName, ProcedureDeclaration pd)
    {
        procMap.put(procName, pd);
    }

    /**
     * Declares a variable in the current environment
     * @param variable the name variable
     * @param value the value of the variable
     */
    public void declareVariable(String variable, int value)
    {
        varMap.put(variable, value);
    }

    /**
     * Retrives the variable map in current env
     * @return the variable map
     */
    public HashMap<String, Integer> getVarMap()
    {
        return varMap;
    }
}
