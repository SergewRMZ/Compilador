package lexer;

public class Token {

    final TipoToken tipo;
    final String lexema;
    final Object literal;

    public Token(TipoToken tipo, String lexema) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = null;
    }

    public Token(TipoToken tipo, String lexema, Object literal) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
    }

    public TipoToken getTipo () {
        return this.tipo;
    }

    public String getLexema () {
        return this.lexema;
    }

    public Object getLiteral () {
        return this.literal;
    }

    public String toString() {
        return "<" + tipo + " " + lexema + " " + literal + ">";
    }
}
