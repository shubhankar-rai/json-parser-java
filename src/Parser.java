import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final String input;
    private int pos = 0;
    private List<Token> tokens;
    private int current = 0;

    public Parser(String input) {
        this.input = input;
        this.tokens = tokenize();
    }

    private List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while(pos < input.length()) {
            char ch = input.charAt(pos);

            if (Character.isWhitespace(ch)) {
                pos++;
                continue;
            }

            switch (ch) {
                case '{':
                    tokens.add(new Token(TokenType.LBRACE, null));
                    pos++;
                    break;
                case '}':
                    tokens.add(new Token(TokenType.RBRACE, null));
                    pos++;
                    break;
                case ':':
                    tokens.add(new Token(TokenType.COLON, null));
                    pos++;
                    break;
                case ',':
                    tokens.add(new Token(TokenType.COMMA, null));
                    pos++;
                    break;
                case '"':
                    tokens.add(new Token(TokenType.STRING, readString()));
                    break;
                default:
                    throw new RuntimeException("Unexpected character: " + ch);
            }
        }

        tokens.add(new Token(TokenType.EOF, null));
        return tokens;
    }

    private String readString() {
        pos++; // skipping "
        StringBuilder sb = new StringBuilder();
        while(pos < input.length()) {
            char ch = input.charAt(pos);
            if(ch == '"') {
                pos++; // skipping "
                return sb.toString();
            } else {
                sb.append(ch);
                pos++;
            }
        }
        throw new RuntimeException("Unterminated string");
    }

    public boolean parse() {
        try {
            parseObject();
            expect(TokenType.EOF);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private void parseObject() {
        expect(TokenType.LBRACE);

        if(check(TokenType.STRING)) {
            parsePair();
            while(match(TokenType.COMMA)) {
                parsePair();
            }
        }

        expect(TokenType.RBRACE);
    }

    private void parsePair() {
        expect(TokenType.STRING);
        expect(TokenType.COLON);
        expect(TokenType.STRING);
    }

    private Token advance() {
        return tokens.get(current++);
    }

    private Token peek() {
        return tokens.get(current);
    }

    private boolean match(TokenType type) {
        if(check(type)) {
            advance();
            return true;
        }
        return false;
    }

    private void expect(TokenType type) {
        if(!check(type)) {
            throw new RuntimeException("Expected" + type + "but found" + peek().type);
        }
        advance();
    }

    private boolean check(TokenType type) {
        return peek().type == type;
    }
}
