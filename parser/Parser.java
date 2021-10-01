package parser;
import scanner.Scanner;
import scanner.ScanErrorException;
import java.util.*; 
import java.io.Reader;
import java.io.*;
import java.io.IOException;
import ast.Statement;
import ast.Expression;
import ast.Writeln;
import ast.BinOp;
import ast.Block;
import ast.Assignment;
import ast.Number;
import ast.Variable;
import Environment.Environment;
import ast.Condition;
import ast.If;
import ast.While;
import ast.Program;
import ast.ProcedureDeclaration;
import ast.ProcedureCall;
import emitter.Emitter;
/**
 * 
 * Implements a simple pascal parser that takes in tokenized input from scanner
 * and executes statements, including those withing blocks, and can parse
 * if as well as while statements
 * 
 * @author Aditi Khanna
 * @version March 22, 2020
 */
public class Parser
{
    Scanner scan; 
    String current; //curent token
    /**
     * Constructs a new parser
     * @param sca the scanner that tokenizes input
     */
    public Parser(Scanner sca) throws ScanErrorException
    {
        scan = sca;
        current = scan.nextToken();

    }

    /**
     * Eats the current token and sets current to the next one
     * @param exp the expression to check and eat
     * @throws ScanErrorException if current is not equal to num
     */
    public void eat(String exp) throws ScanErrorException
    {
        if(exp.equals(current))
        {
            current = scan.nextToken();  
        }
        else
        {
            throw new IllegalArgumentException(exp+ " was expected" + current + "was found");
        }
    }

    /**
     * Parses a number
     * @precondition current token in an integer
     * @postcondition number token has been eaten
     * @return the integer form of the token
     * @throws ScanErrorException if current is invalid
     */
    private int parseNumber() throws ScanErrorException
    {
        int num = 0;
        if(current.equals("-"))
        {
            eat("-");
            num = Integer.parseInt(current)*-1;
        }
        else
        {
            num = Integer.parseInt(current);
        }
        eat(current);
        return num;
    }

    /**
     * Parses a statement
     * @precondition current points to a statement
     * @postcondition all statements have been parsed, END is eaten
     * @throws ScanErrorException if current is invalid
     * @return a new Statement
     */
    public Statement parseStatement() throws ScanErrorException
    {
        if(current.equals("WRITELN"))
        {
            eat("WRITELN");
            eat("(");
            Expression exp = parseExpr();
            eat(")");
            eat(";");
            return new Writeln(exp);
        }
        else if(current.equals("BEGIN"))
        {
            eat("BEGIN");
            return parseWhileBegin();
        }
        else if(current.equals("IF"))
        {
            eat("IF");
            Condition con = parseCondition();
            eat("THEN");
            return new If(con, parseStatement());
        }
        else if(current.equals("WHILE"))
        {
            eat("WHILE");
            Condition con = parseCondition();
            eat("DO");
            return new While(con, parseStatement());
        }
        else
        {
            String key = current;
            eat(current);
            eat(":=");
            Expression expr = parseExpr();
            if(current.equals(";"))
                eat(";");
            Assignment var = new Assignment(key, expr);
            return var;

        }
    }

    /**
     * Parses the condtion within the if and while loop
     * @return a new condition 
     */
    public Condition parseCondition() throws ScanErrorException
    {
        Expression expr = parseExpr();
        if(current.equals("="))
        {
            eat("=");
            return new Condition("=", expr, parseExpr());
        }
        if(current.equals(">"))
        {
            eat(">");
            return new Condition(">", expr, parseExpr());
        }
        if(current.equals("<"))
        {
            eat("<");
            return new Condition("<", expr, parseExpr());
        }
        if(current.equals("!="))
        {
            eat("!=");
            return new Condition("!=", expr, parseExpr());
        }
        if(current.equals("<="))
        {
            eat("<=");
            return new Condition("<=", expr, parseExpr());
        }
        else
        {
            eat(">=");
            return new Condition(">=", expr, parseExpr());
        }
    }

    /**
     * Handles statements within blocks
     * @precondition current points to the statement after "BEGIN"
     * @postcondition parsing ends, END is eaten
     * @throws ScanErrorException if current is invalid
     * @return a new block with the list of statements
     */
    public Block parseWhileBegin() throws ScanErrorException
    {
        List<Statement> statemts = new ArrayList<Statement>();
        while(!current.equals("END"))
        {
            Statement states = parseStatement();
            statemts.add(states);
        }
        eat("END");
        eat(";");
        return new Block(statemts);
    }

    /**
     * Parses the program including Procedures and statements
     * @return a Program 
     * @throws  ScanErrorException if current is invalid
     */
    public Program parseProgram() throws ScanErrorException
    {
        ArrayList<String> vars = new ArrayList<String>();
        while(current.equals("VAR"))
        {
            eat("VAR");
            while(!current.equals(";"))
            {
                vars.add(current);
                eat(current);
                if(current.equals(","))
                {
                    eat(",");
                }
            }
            eat(";");
        }
        if(current.equals("PROCEDURE"))
        {
            eat("PROCEDURE");
            String name = current;
            eat(current);
            eat("(");
            List<String> paramList = maybeParms();
            eat(")");
            eat(";");
            ProcedureDeclaration method = new ProcedureDeclaration(parseStatement(), name, 
                                                                   paramList);
            //eat(";");
            return new Program(method, parseProgram(), vars);
        }
        else
        {
            return new Program(parseStatement());
        }

    }

    /**
     * Parses the formal parameters 
     * @return an array list of parameters strings
     * @throws ScanErrorException if current is invalid
     */
    public List<String> maybeParms() throws ScanErrorException
    {
        if(!current.equals(")"))
        {
            List<String> parm = new ArrayList<String>();
            parm.add(current);
            eat(current);
            while(current.equals(","))
            {
                eat(",");
                parm.add(current);
                eat(current);
            }
            return parm;
        }
        return new ArrayList<String>();
    }

    /**
     * Parses the potential actual parametres
     * @return a list of expressions of parameters
     * @throws ScanErrorException if current is invalid
     */
    public List<Expression> maybeActualParams() throws ScanErrorException
    {
        if(!current.equals(")"))
        {
            List<Expression> parm = new ArrayList<Expression>();
            parm.add(parseExpr());
            while(current.equals(","))
            {
                eat(",");
                parm.add(parseExpr());
            }
            return parm;
        }
        return new ArrayList<Expression>();
    }

    /**
     * Parses the factor 
     * @precondition current is a factor
     * @return the parsed factor
     * @throws ScanErrorException if current is invalid
     */
    private Expression parseFactor() throws ScanErrorException
    {
        if(current.equals("("))
        {
            eat("("); 
            Expression retValue = parseExpr();
            eat(")");
            return retValue;
        }
        else if(current.equals("-"))
        {
            eat("-"); 
            Number num = new Number(-1);
            return new BinOp("*", parseFactor(), num);
        }
        else if(scan.isLetter(current.charAt(0)))
        {
            String methodName = current;
            eat(current);

            if(current.equals("("))
            {
                eat("(");
                ProcedureCall proc = new ProcedureCall(methodName, maybeActualParams());
                eat(")");
                if(current.equals(";"))
                    eat(";");
                return proc;
            }
            else
            {
                Variable var = new Variable(methodName);
                return var;
            }
        }
        else
        {
            Number num = new Number(parseNumber());
            return num;
        }
    }

    /**
     * Parses the term
     * @precondition current is a term
     * @postcondition the term has been multiplied/divided as necessary
     * @return the result 
     * @throws ScanErrorException if current is invalid
     */
    private Expression parseTerm() throws ScanErrorException
    {
        Expression res = parseFactor();
        while (current.equals("*") || current.equals("/") )
        {
            if (current.equals("*"))
            {
                eat("*");
                res = new BinOp("*", res, parseFactor());
            }
            else if (current.equals("/"))
            {
                eat("/");
                res = new BinOp("/", res, parseFactor());
            }
        }
        return res;
    }

    /**
     * Pareses the expression
     * @precondition current is an expression
     * @postcondition the expression has been done out
     * @throws ScanErrorException if current is invalid
     */
    private Expression parseExpr() throws ScanErrorException
    {
        Expression res = parseTerm();
        while(current.equals("-") || current.equals("+"))
        {
            if (current.equals("-"))
            {
                eat("-");
                res = new BinOp("-", res, parseTerm());
            }
            else if(current.equals("+"))
            {
                eat("+");
                res = new BinOp("+", res, parseTerm());
            } 
        }
        return res;
    }

    /**
     * Runs the parser, parses a file
     * @param args the arguments 
     */
    public static void main(String[] args) throws Exception
    {
        try
        {
            FileInputStream inStream = new FileInputStream(
                    new File("/Users/aditikhanna/Desktop/Harker/Compilers/" + 
                        "parserTest7.txt"));
            Scanner scan = new Scanner(inStream);
            Parser par = new Parser(scan);
            Environment env = new Environment();
            Emitter em = new Emitter("target.asm");
            while(scan.hasNext())
            {
                Program prog = par.parseProgram();
                prog.compile(em);
                // Statement st = par.parseStatement();
                //st.exec(env);
            }
        }
        catch (Exception e)
        {
            throw e;
            //System.out.println(e);
        }

    }
}
