import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scanner {
    private List<Token> tokens = new ArrayList<>();
    
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
    }

    public List<Token> scan(){
        List<Token> tokens = new ArrayList<>();
        int estado = 0;
        String lexema = "";
        
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            
            switch (estado) {
                case 0:
                    if (c == '>') {
                        estado = 1;
                    }
                    
                    else if (c == '<') {
                        estado = 4;
                    }
                    
                    else if (c == '=') {
                        estado = 7;
                    }
                    
                    else if (c == '!') {
                        estado = 10;
                    }
                    
                    else if (Character.isLetter(c)) {
                        estado = 13;
                    }
                    
                    else if (Character.isDigit(c)) {
                        estado = 15;
                    }
                    
                    lexema += c;
                    break;
                   
                case 1:
                    if (c == '=') {
                        lexema += c;
                        Token t = new Token(TipoToken.GREATER_EQUAL, lexema);
                        tokens.add(t);
                        
                        estado = 0; 
                        lexema = "";
                    }
                    
                    else {
                        Token t = new Token(TipoToken.GREATER, lexema);
                        tokens.add(t);
                        
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                
                case 4:
                    if (c == '=' ) {
                        lexema += c;
                        Token t = new Token(TipoToken.LESS_EQUAL, lexema);
                        tokens.add(t);
                        
                        estado = 0; 
                        lexema = "";
                    }
                    
                    else {
                        Token t = new Token (TipoToken.LESS, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    
                    break;
                
                case 7: 
                    if (c == '=') {
                        lexema += c;
                        Token t = new Token(TipoToken.EQUAL_EQUAL, lexema);
                        tokens.add(t);
                        
                        estado = 0;
                        lexema = "";
                    }
                    
                    else {
                        Token t = new Token (TipoToken.EQUAL, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--; 
                    }
                    
                    break;
                    
                case 10:
                    if (c == '=') {
                        lexema += c;
                        Token t = new Token(TipoToken.BANG_EQUAL, lexema);
                        tokens.add(t);
                        
                        estado = 0;
                        lexema = "";
                    }
                    
                    else {
                        Token t = new Token (TipoToken.BANG, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    
                    break;
                    
                case 13:
                    if (Character.isLetterOrDigit(c)) {
                        lexema += c;
                    } 
                    
                    else {
                        TipoToken tipo = palabrasReservadas.get(lexema);
                        Token t = new Token(tipo == null? TipoToken.IDENTIFIER : tipo, lexema);   
                        tokens.add(t);
                        
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    
                    break;
                    
                case 15:
                    if (Character.isDigit(c)) {
                        lexema += c;
                    }
                    
                    else if (c == '.') {
                        lexema += c;
                        estado = 16;
                        Token t = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema));
                    }
                    
                    else if (c == 'E') {
                        lexema += c;
                        estado = 18;
                    }

                    else {
                        Token t = new Token(TipoToken.NUMBER, lexema);
                        tokens.add(t);
                        lexema = "";
                        estado = 0;
                        i--;
                    }                    
                    
                    break;
                    
                case 16:
                    if (Character.isDigit(c)) {
                        lexema += c;
                        estado = 17;
                    }
                    
                    else {
                        System.err.println("No seas menso");
                    }
                    
                    break;
                    
                case 17:
                    if (Character.isDigit(c)) {
                        lexema += c;
                    }
                    
                    else if (c == 'E') {
                        lexema += c;
                        estado = 18;
                    }
                    
                    else {
                        Token t = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema));
                        tokens.add(t);
                        lexema = "";
                        estado = 0;
                        i--;
                    }
                    
                    break;
                    
                case 18: 
                    if (c == '+' || c == '-') {
                        lexema += c;
                        estado = 19;
                    }
                    
                    else if (Character.isDigit(c)) {
                        lexema += c;
                        estado = 20;
                    }
                    
                    break;
                    
                case 19:
                    if (Character.isDigit(c)) {
                        lexema += c;
                        estado = 20;
                    }
                    
                    break;
                
                case 20:
                    if (Character.isDigit(c)) {
                        lexema += c;
                    }
                    
                    else {
                        Token t = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    
                    break;
            }
        } 

        return tokens;
    }

}
