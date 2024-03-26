package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scanner {
    private List<Token> tokens;
    private int state;
    private String lexema;
    private int index;

    private static HashMap<String,TipoToken> palabrasReservadas;
    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and", TipoToken.AND);
        palabrasReservadas.put("else", TipoToken.ELSE);
        palabrasReservadas.put("false", TipoToken.FALSE);
        palabrasReservadas.put("fun", TipoToken.FUN);
        palabrasReservadas.put("for", TipoToken.FOR);
        palabrasReservadas.put("if", TipoToken.IF);
        palabrasReservadas.put("null", TipoToken.NULL);
        palabrasReservadas.put("or", TipoToken.OR);
        palabrasReservadas.put("print", TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("true", TipoToken.TRUE);
        palabrasReservadas.put("var", TipoToken.VAR);
        palabrasReservadas.put("while", TipoToken.WHILE);
    }

    private String source;

    public Scanner(String source){
        this.source = source + " ";
        this.tokens = new ArrayList<>();
        this.state = 0;
        this.lexema = "";
        this.index = 0;
    }

    private void resetToken () {
        this.state = 0;
        this.lexema = "";
    }

    private void scanOperadorMayor (char c) {
        if (c == '=') {
            this.lexema += c;
            Token t = new Token(TipoToken.GREATER_EQUAL, lexema);
            this.tokens.add(t);
        }
        
        else {
            Token t = new Token(TipoToken.GREATER, lexema);
            this.tokens.add(t);
            this.index--;
        }

        resetToken();
    }
    
    private void scanOperadorMenor (char c) {
        if (c == '=' ) {
            lexema += c;
            Token t = new Token(TipoToken.LESS_EQUAL, lexema);
            tokens.add(t);
        }
        
        else {
            Token t = new Token (TipoToken.LESS, lexema);
            tokens.add(t);
            this.index--;
        }

        resetToken();
    }

    private void scanOperadorIgualdad (char c) {
        if (c == '=') {
            lexema += c;
            Token t = new Token(TipoToken.EQUAL_EQUAL, lexema);
            tokens.add(t);
        }
        
        else {
            Token t = new Token (TipoToken.EQUAL, lexema);
            tokens.add(t);
            this.index--; 
        }

        resetToken();
    }

    public void scanOperadorBang (char c) {
        if (c == '=') {
            lexema += c;
            Token t = new Token(TipoToken.BANG_EQUAL, lexema);
            tokens.add(t);
        }
        
        else {
            Token t = new Token (TipoToken.BANG, lexema);
            tokens.add(t);
            this.state = 0;
        }

        resetToken();
    }

    private void scanIdentificador (char c) {
        if (Character.isLetterOrDigit(c)) {
            this.lexema += c;
        } 
        
        else {
            TipoToken tipo = palabrasReservadas.get(lexema);
            Token t = new Token(tipo == null? TipoToken.IDENTIFIER : tipo, lexema);   
            tokens.add(t);
            this.index--;
            resetToken();
        }
    }

    private void scanUnsignedNumber (char c) {
        if (Character.isDigit(c)) {
            this.lexema += c;
        }
        
        else if (c == '.') {
            this.lexema += c;
            this.state = 16;
            // Token t = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema));
        }
        
        else if (c == 'E') {
            this.lexema += c;
            this.state = 18;
        }

        else {
            Token t = new Token(TipoToken.NUMBER, lexema);
            tokens.add(t);
            resetToken();
            this.index--;
        }        
    }

    private void scanUnsignedNumberDouble (char c) {
        if (Character.isDigit(c)) {
            lexema += c;
            this.state = 17;
        }

        else {
            System.err.println("Number not valid in " + lexema);
        }
    }

    private void scanNumberOrExp (char c) {
        if (Character.isDigit(c)) {
            this.lexema += c;
        }
        
        else if (c == 'E') {
            this.lexema += c;
            this.state = 18;
        }
        
        else {
            Token t = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema));
            tokens.add(t);
            this.resetToken();
            this.index--;
        }
    }

    private void scanSignedExponent (char c) {
        if (c == '+' || c == '-') {
            this.lexema += c;
            this.state = 19;
        }
        
        else if (Character.isDigit(c)) {
            this.lexema += c;
            this.state = 20;
        }

        else {
            System.err.println("Exponent not specified");
        }
    }

    private void scanValueExponent (char c) {
        System.out.println("Estado 19");
        if (Character.isDigit(c)) {
            lexema += c;
            this.state = 20;
        }
    }

    private void scanValueExponentTwo (char c) {
        if (Character.isDigit(c)) {
            this.lexema += c;
        }
        
        else {
            Token t = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema));
            tokens.add(t);
            this.resetToken();
            this.index--;
        }
    }

    public List<Token> scan(){        
        for (this.index = 0; this.index < source.length(); this.index++) {
            char c = source.charAt(this.index);
            
            switch (this.state) {
                case 0:
                    if (c == '>') {
                        this.state = 1;
                    }
                    
                    else if (c == '<') {
                        this.state = 4;
                    }
                    
                    else if (c == '=') {
                        this.state = 7;
                    }
                    
                    else if (c == '!') {
                        this.state = 10;
                    }
                    
                    else if (Character.isLetter(c)) {
                        this.state = 13;
                    }
                    
                    else if (Character.isDigit(c)) {
                        this.state = 15;
                    }
                    
                    this.lexema += c;
                    break;

                case 1:
                    scanOperadorMayor(c);
                    break;

                case 4: 
                    scanOperadorMenor(c);
                    break;
                
                case 7:
                    scanOperadorIgualdad(c);
                    break;
                
                case 10: 
                    scanOperadorBang(c);
                    break;

                case 13:
                    scanIdentificador(c);
                    break;

                case 15:
                    scanUnsignedNumber(c);
                    break;
                
                case 16:
                    scanUnsignedNumberDouble(c);
                    break;

                case 17:
                    scanNumberOrExp(c);
                    break;

                case 18:
                    scanSignedExponent(c);
                    break;

                case 19:
                    scanValueExponent(c);
                    break;

                case 20:
                    scanValueExponentTwo(c);
                    break;
            }
        } 

        return tokens;
    }
}
