package parser;
import scanner.Scanner;
import scanner.ScanErrorException;
import java.util.*; 
import java.io.Reader;
import java.io.*;
import java.io.IOException;
/**
 * Implements a simple pascal parser that takes in tokenized input from scanner
 * and executes statements, including those withing blocks.
 * 
 * @author Aditi Khanna
 * @version March 22, 2020
 */
public class ParserOld
{

    private Map<String, Integer> varMap; //stores variables
    Scanner scan; 
    String current; //curent token
    /**
     * Constructs a new parser
     * @param sca the scanner that tokenizes input
     */
    public ParserOld(Scanner sca) throws ScanErrorException
    {
        scan = sca;
        current = scan.nextToken();
        varMap = new HashMap<String, Integer>();
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
     */
    public void parseStatement() throws ScanErrorException
    {
        if(current.equals("WRITELN"))
        {
            eat("WRITELN");
            eat("(");
            int retValue = parseExpr();
            eat(")");
            eat(";");
            System.out.println(retValue);
        }
        else if(current.equals("BEGIN"))
        {
            eat("BEGIN");
            parseWhileBegin();
        }
        else
        {
            String key = current;
            eat(current);
            eat(":=");
            int n = parseExpr();
            varMap.put(key,n);
            eat(";");
        }
    }

    /**
     * Handles statements within blocks
     * @precondition current points to the statement after "BEGIN"
     * @postcondition parsing ends, END is eaten
     * @throws ScanErrorException if current is invalid
     */
    public void parseWhileBegin() throws ScanErrorException
    {
        if(current.equals("END"))
        {
            eat("END");
            eat(";");
        }
        else
        {
            parseStatement();
            parseWhileBegin();
        }
    }

    /**
     * Parses the factor 
     * @precondition current is a factor
     * @return the parsed factor
     * @throws ScanErrorException if current is invalid
     */
    private int parseFactor() throws ScanErrorException
    {
        if(current.equals("("))
        {
            eat("("); 
            int retValue = parseExpr();
            eat(")");
            return retValue;

        }
        if(current.equals("-"))
        {
            eat("-");
            return -1* parseFactor();
        }
        else if(varMap.containsKey(current))
        {
            int vnum = varMap.get(current);
            eat(current);
            return vnum;
        }
        else
        {
            int curr = parseNumber();
            return curr;
        }

    }

    /**
     * Parses the term
     * @precondition current is a term
     * @postcondition the term has been multiplied/divided as necessary
     * @return the result 
     * @throws ScanErrorException if current is invalid
     */
    private int parseTerm() throws ScanErrorException
    {
        int res = parseFactor();
        while (current.equals("*") || current.equals("/") )
        {
            if (current.equals("*"))
            {
                eat("*");
                res = res * parseFactor();
            }
            else if (current.equals("/"))
            {
                eat("/");
                res = res / parseFactor();
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
    private int parseExpr() throws ScanErrorException
    {
        int res = parseTerm();
        while(current.equals("-") || current.equals("+"))
        {
            if (current.equals("-"))
            {
                eat("-");
                res = res - parseTerm();
            }
            else if(current.equals("+"))
            {
                eat("+");
                res = res + parseTerm();
            } 
        }
        return res;
    }

    /**
     * Runs the parser, parses a file
     * @param args the arguments 
     */
    public static void main(String[] args)
    {
        try
        {
            FileInputStream inStream = new FileInputStream(
                    new File("/Users/aditikhanna/Desktop/Compilers/" + 
                        "parserTest0.txt"));
            Scanner scan = new Scanner(inStream);
            ParserOld par = new ParserOld(scan);
            
            while(scan.hasNext())
            {
                par.parseStatement();
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }
}
