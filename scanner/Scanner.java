package scanner;

import java.io.IOException;
import java.io.Reader;
import java.io.*;

/**
 * Scanner class  reads the input string from input file,
   determes the individual lexemes according to a given set of rules, and produces a string of
   tokens that are printed.
 * 
 * @author Aditi Khanna 
 * @version 25 Feb 2020
 */

public class Scanner
{
    private Reader in; //file reader
    private char currentChar; //current character to be looked at
    private boolean eof; //whether or not we have reached the end of the file
    /**
     *  
     * Scanner constructor for constructing a scanner that 
     * scans a given input string.  It sets the end-of-file flag an then reads
     * the first character of the input string into the instance field currentChar.
     * Usage: Scanner lex = new Scanner(input_string);
     * @param inString the string to scan
     *
     */
    public Scanner(String inString)
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }

    /**
     * Scanner constructor for construction of a scanner that 
     * uses an InputStream object for input.  
     * Usage: 
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * Scanner lex = new Scanner(inStream);
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
    }

    /**
     * The getNextChar method attempts to get the next character from the input
     * stream.  It sets the endOfFile flag true if the end of file is reached on
     * the input stream.  Otherwise, it reads the next character from the stream
     * and converts it to a Java String object.
     * @postcondition: The input stream is advanced one character if it is not at
     * end of file and the currentChar instance field is set to the String 
     * representation of the character read from the input stream.  The flag
     * endOfFile is set true if the input stream is exhausted.
     */
    private void getNextChar()
    {
        try
        {
            int input = in.read();
            if(input == -1) 
                eof = true;
            else 
                currentChar =  (char) input;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Determines whether the input stream has another character
     * @return true if there is a another character; otherwise,
     *         false.
     *        
     */
    public boolean hasNext()
    {
        return(!eof);

    }

    /**
     * Takes in a String object and then compares it to the currentChar instance field
    @param curr the string to be eaten
    @throws ScanErrorException if char is missing
    @postcondition currentChar is set to the next character in the stream

     */
    private void eat(char curr) throws ScanErrorException
    {
        if(currentChar == (curr))
        {
            getNextChar();
        }
        else
        {
            throw new ScanErrorException("Expected" + currentChar + "but instead found " + curr);
        }
    }

    /**
     * Determines if the string is a digit
     * @param curr the string to examine
     * @return true if the character is a digit; otherwise, false.
     */
    private boolean isDigit(char curr)
    {
        return(curr >= '0' && curr<='9');
    }

    /**
     * Determines if the paramater string is a digit
     * @param curr the char to examine
     * @return true if it is a digit; otherwise,
     *         false
     */
    public boolean isLetter(char curr)
    {
        if((curr >= 'a' && curr <= 'z') ||( curr >= 'A' && curr<= 'Z'))
        {
            return true;
        }
        return false;
    }

    /**
     * Determines whether or not the character is a special char
     * @param curr the character to examine
     * @return true if it is a special char; otherwise,
     *         false.
     */
    private boolean isSpecial(char curr)
    {
        if(curr == '-' || curr == '\'')
        {
            return true;
        }
        return false;
    }

    /**
     * Determines if the char is a phase terminator
     * @param curr the character to examine
     * @return true if the character is a terminator; otherwise,
     *         false.
     */
    private boolean isPhraseTerminator(char curr)
    {
        if((curr == (',')) || (curr == (':')) ||( curr ==(';')))
        {
            return true;
        }
        return false;
    }

    /**
     * Determines if the char is a sentence terminator
     * @param curr the character to examine
     * @return true if it is a sent terminator; otherwise,
     *          false
     *
     */
    private boolean isSentTerminator(char curr)
    {
        if(curr == ('.') || curr == ('!') || curr == ('?'))
        {
            return true;
        }
        return false;
    }

    /**
     * Determines whether or not the character is a white space
     * @param curr the character to examine
     * @return true if the char is a white space; otherwise, 
     *          false.
     */
    private boolean isWhite(char curr)
    {
        return (curr == ' ' || curr == '\n' || curr == '\t' || curr == '\r');
    }

    /**
     * Determines whether or not the charcter is an operator
     * @param curr the character to examine
     * @return true if the character is an operator; otherwise,
     *         false
     * 
     */
    private boolean isOperator(char curr)
    {
        return (curr == ('-') || curr == ('/') || curr == ('+') || curr == ('*') ||
            curr == '=' || isSentTerminator(curr) 
            || isPhraseTerminator(curr) || isSpecial(curr) || 
            curr == '(' || curr == ')' || curr == '>' || curr == '<');
    }

    /**
     * Scans the input for a digit or group of digits
     * @return the lexeme
     * @throws ScanErrorException if lexeme is not recognized
     * @postcondition the currentCharacter is set to the char after the set of digits
     */
    private String scanNumber() throws ScanErrorException
    {
        String lexeme = "";
        try 
        {
            while(hasNext() && !isWhite(currentChar))
            {
                if(isDigit(currentChar))
                {
                    lexeme += currentChar;
                    eat(currentChar);
                }
                else
                {
                    break;
                }
            }
        }
        catch (Exception e)
        {
            throw new ScanErrorException("no lexeme recognized");
        }
        return lexeme;
    }

    /**
     * Scans the input for an operand token
     * @return the lexeme
     * @throws ScanErrorException if no lexeme recognized
     * @postcondition currentChar is set to the character after the operand
     */
    private String scanOperand() throws ScanErrorException
    {
        String lexeme = "";
        try
        {
            if(isOperator(currentChar))

            {
                lexeme = currentChar + "";

                if(currentChar == '>' || currentChar == '<' || currentChar == ':')
                {
                    eat(currentChar);
                    if(currentChar == '=')
                    {
                        lexeme += currentChar +"";
                    }
                }
                eat(currentChar);
                return lexeme;
            }
        }

        catch( Exception e)
        {
            throw new ScanErrorException("no lexeme recognized");
        }
        return lexeme;
    }

    /**
     * Scans the input for an identifier token
     * @return the lexeme
     * @throws ScanErrorException if lexeme is not regognizeed.
     * 
     * @postcondition currentChar is set to the char after the identifier/ set of identifiers
     */
    private String scanIdentifier() throws ScanErrorException
    {
        String lexeme = "";
        try
        {
            while(hasNext() &&  !isWhite(currentChar))
            {
                if(isLetter(currentChar))
                {
                    lexeme += currentChar;
                    eat(currentChar);
                }
                else
                {
                    break;
                }
            }

        }
        catch (Exception e)
        {
            throw new ScanErrorException("no lexeme recognized");
        }
        return lexeme;
    }

    /**
     * Scans next token in input stream
     * @return a string representing the type of token found: number, identifiyer, operand
     * 
     */
    public String nextToken() throws ScanErrorException
    {
        try
        {
            while (isWhite(currentChar) && !eof)
            {
                eat(currentChar);
            }

            if(currentChar == '/')
            {
                eat(currentChar);
                if(currentChar == '/')
                {
                    while((currentChar != ('\n')))
                    {
                        eat(currentChar);
                    }
                    while(isWhite(currentChar))
                    {
                        eat(currentChar);
                    }
                }
                else
                {
                    return "/";
                }
            }

            if(eof || currentChar == '.')
            {
                eof = true; 
                return "END";
            }
            else if(isDigit(currentChar))
            {
                return scanNumber();
            }
            else if(isLetter(currentChar))
            {
                return scanIdentifier();
            }
            else if(isOperator(currentChar))
            {
                return scanOperand();
            }
            else
            {
                throw new ScanErrorException("no lexeme recognized");
            }

        }
        catch (Exception e)
        {
            String s = e.getMessage() + " " + currentChar;
            eat(currentChar);
            return s;
        }

    }

    /**
     * Runs the scanner, creates tokens from file
     * @param args the args of method
     * @throws ScanErrorException if no input found
     * @prostcondition the file have been tokenized until a period or end
     * of file has been reached
     */
    public static void main (String[] args) throws ScanErrorException
    {
        try
        {
            FileInputStream inStream = new FileInputStream(
                    new File("/Users/aditikhanna/Desktop/Harker/Compilers/" + 
                        "scannerTestAdvanced.txt"));
            Scanner scan = new Scanner(inStream);

            while(scan.hasNext()) 
            {
                System.out.println(scan.nextToken());
            }
            // System.out.println(scan.nextToken());
        }
        catch (Exception e)
        {
            System.out.println(e);
            throw new ScanErrorException("no input found");
        }
    }
}
