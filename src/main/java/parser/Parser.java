package parser;
import lexer.TipoToken;
import lexer.Token;
import java.util.List;

import exception.ParserException;

public class Parser {
    private final List<Token> tokens;
    private int i = 0; 
    private Token preanalisis;


    public Parser (List<Token> tokens) {
        this.tokens = tokens;
        this.preanalisis = tokens.get(i);
    }

    public void parse () {
        program();

        if (preanalisis.getTipo() != TipoToken.EOF) {
            String message = "Se encontró un error en el programa";
            throw new ParserException(message);
        }
    }

    private void match (TipoToken tt) throws ParserException {
        if (preanalisis.getTipo() == tt) {
            i++;
            preanalisis = tokens.get(i);
        }

        else {
            String message = "Error. Se esperaba " + preanalisis.getTipo() + " pero se encontró " + tt;
            throw new ParserException(message);
        }
    }

    private Token previous () {
        return this.tokens.get(i - 1);
    }

}