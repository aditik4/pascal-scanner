package ast;
//import ast.ProcedureDeclaration;
import Environment.Environment;
import emitter.Emitter;
import java.util.List;
import java.util.ArrayList;
/**
 * Program class handles executing the entire program, including procedures
 * and statements
 * 
 * @author Aditi Khanna
 * @version May 1
 */
public class Program
{
    ProcedureDeclaration proc;
    Statement statement;
    Program prog;
    private List<String> variables;

    /**
     * Constructs a new Program
     * @param proc the procedure declaration in the program
     * @param p the possible program to follow 
     */
    public Program(ProcedureDeclaration proc, Program p, List<String> vars)
    {
        // initialise instance variables
        variables = vars;
        this.proc = proc;
        this.prog = p;
    }

    /**
     * Constructs a program
     * @param statement the statemtn in the program
     * 
     */
    public Program(Statement statement) 
    {
        this.statement = statement;
    }

    /**
     * Executes the program
     * @param env the environment in which the variables are held
     */
    public void exec(Environment env)
    {
        if(proc == null || prog == null)
        {
            statement.exec(env);
        }
        else
        {
            proc.exec(env);
            prog.exec(env);
        }

    }
    /**
     * Takes in an output file name and uses an Emitter to 
     * write the following code to that file.
     * @param e the emitter to write code into the file
     */
    public void compile (Emitter e)
    {
        e.emit(".data");
        e.emit("\t nl: .asciiz \"\\n\"");
        for( int i = 0; i< variables.size(); i++)
        {
            e.emit("var" + variables.get(i) + ": .word 0");
        }
        e.emit(".text");
        e.emit(".globl main");
        e.emit("main:");
        statement.compile(e);
        e.emit("li $v0 10");
        e.emit("syscall");
    }
}
