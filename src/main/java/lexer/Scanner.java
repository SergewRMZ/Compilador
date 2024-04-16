package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.management.RuntimeErrorException;

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

    // CONSTRUCTOR
    public Scanner(String source){
        this.source = source + " ";
        this.tokens = new ArrayList<>();
        this.state = 0;
        this.lexema = "";
        this.index = 0;
    }

    // Limpiar el Token
    private void resetToken () {
        this.state = 0;
        this.lexema = "";
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

                    else if (c == '"') {
                        this.state = 24;
                    }

                    else if (c == '/') {
                        this.state = 26;
                    }

                    else if (Character.isWhitespace(c)) {
                        this.state = 33;
                    }

                    else if (c == '+') {
                        this.state = 35;
                    }

                    else if (c == '-') {
                        this.state = 36;
                    }

                    else if (c == '*') {
                        this.state = 37;
                    }

                    else if (c == ';') {
                        this.state = 38;
                    }

                    else if (c == ',') {
                        this.state = 39;
                    }

                    else if (c == '.') {
                        this.state = 40;
                    }

                    else if (c == '(') {
                        this.state = 41;
                    }

                    else if (c == ')') {
                        this.state = 42;
                    }

                    else if (c == '{') {
                        this.state = 43;
                    }

                    else if (c == '}') {
                        this.state = 44;
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

                case 24:
                    scanStrings (c);
                    break;

                case 26:
                    commentStart(c);
                    break;
                    
                case 27:
                    commentBody(c);
                    break;

                case 28:
                    commentEnd(c);
                    break;

                case 29:
                    commentOnlineComment(c);
                    break;

                case 33:
                    scanDelim (c);
                    break;

                case 35:
                    scanPlus  (c);
                    break;

                case 36:
                    scanMinus(c);
                    break;

                case 37:
                    scanStar(c);
                    break;

                case 38:
                    scanSemiColon(c);
                    break;

                case 39:
                    scanComma(c);
                    break;
                
                case 40:
                    scanDot(c);
                    break;

                case 41:
                    scanLeftParen(c);
                    break;
                
                case 42:
                    scanRightParen(c);
                    break;

                case 43:
                    scanLeftBrace(c);
                    break;

                case 44:
                    scanRightBrace(c);
                    break;
            }
        } 

        return tokens;
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

    // Determinar si es un número exponencial
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

    private void scanStrings (char c) {
        if (c == '"') {
            this.lexema += c;
            Token t = new Token(TipoToken.STRING, lexema);
            tokens.add(t);
            this.resetToken();
        }

        else if (c == '\n') {
            throw new RuntimeErrorException(null, "Se detecto salto de linea");
        }

        else {
            lexema += c;
        }
    }

    /** COMENTARIOS 
     * Después de scanear comentarios, debe de continuar leyendo tokens **/

    private void commentStart (char c) {
        if (c == '*') this.state = 27;
        else if (c == '/') this.state = 30;

        // Crea token de '/'
        else {
            this.lexema += c;
            Token t = new Token(TipoToken.SLASH, lexema);
            tokens.add(t);
            this.resetToken();
            return;
        }

        this.lexema += c;
    }

    private void commentBody (char c) {
        if (c == '*') 
            this.state = 28;

        this.lexema += c;

        // System.out.println("Body");
    }

    private void commentEnd (char c) {
        if (c == '/') this.resetToken();
        else if (c == '*') this.state = 28;
        else this.state = 27;

        this.lexema += c;

        // System.out.println("End");

    }

    private void commentOnlineComment (char c) {
        if (c == '\n') this.resetToken();
        else this.lexema += c;
        // System.out.println("Línea");
    }

    // DELIMITADORES
    private void scanDelim (char c) {
        if (Character.isWhitespace(c)) 
            this.lexema += c;

        else {
            this.index --;
            this.resetToken();
        }
    }

    private void scanPlus (char c) {
        Token t = new Token(TipoToken.PLUS, lexema);
        tokens.add(t);
        this.resetToken();
    }

    private void scanMinus (char c) {
        Token t = new Token(TipoToken.MINUS, lexema);
        tokens.add(t);
        this.resetToken();
    }

    private void scanStar (char c) {
        Token t = new Token(TipoToken.STAR, lexema);
        tokens.add(t);
        this.resetToken();
    }

    private void scanSemiColon (char c) {
        Token t = new Token(TipoToken.SEMICOLON, lexema);
        tokens.add(t);
        this.resetToken();
    }

    private void scanComma (char c) {
        Token t = new Token(TipoToken.COMMA, lexema);
        tokens.add(t);
        this.resetToken();
    }

    private void scanDot (char c) {
        Token t = new Token(TipoToken.DOT, lexema);
        tokens.add(t);
        this.resetToken();
    }

    private void scanLeftParen (char c) {
        Token t = new Token(TipoToken.LEFT_PAREN, lexema);
        tokens.add(t);
        this.resetToken();
    }

    private void scanRightParen (char c) {
        Token t = new Token(TipoToken.RIGHT_PAREN, lexema);
        tokens.add(t);
        this.resetToken();
    }

    private void scanLeftBrace (char c) {
        Token t = new Token(TipoToken.LEFT_BRACE, lexema);
        tokens.add(t);
        this.resetToken();
    }

    private void scanRightBrace (char c) {
        Token t = new Token(TipoToken.LEFT_PAREN, lexema);
        tokens.add(t);
        this.resetToken();
    }
}