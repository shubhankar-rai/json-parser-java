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
                case '[':
                    tokens.add(new Token(TokenType.LBRACKET, null));
                    pos++;
                    break;
                case ']':
                    tokens.add(new Token(TokenType.RBRACKET, null));
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
                case 't':
                    tokens.add(new Token(TokenType.TRUE, readLiteral("true")));
                    break;
                case 'f':
                    tokens.add(new Token(TokenType.FALSE, readLiteral("false")));
                    break;
                case 'n':
                    tokens.add(new Token(TokenType.NULL, readLiteral("null")));
                    break;
                default:
                    if(Character.isDigit(ch) || ch == '-') {
                        tokens.add(new Token(TokenType.NUMBER, readNumber()));
                    } else {
                        throw new RuntimeException("Unexpected character: " + ch);
                    }
            }
        }

        tokens.add(new Token(TokenType.EOF, null));
        return tokens;
    }

    private String readNumber() {
        int start = pos;

        if(input.charAt(pos) == '-') pos++;

        while(pos < input.length() && Character.isDigit(input.charAt(pos))) {
            pos++;
        }

        // Fractional Part
        if(pos < input.length() && input.charAt(pos) == '.') {
            pos++;
            while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
                pos++;
            }
        }

        // Exponent Part
        if(pos < input.length() && (input.charAt(pos) == 'e' || input.charAt(pos) == 'E')) {
            pos++;
            if(pos < input.length() && (input.charAt(pos) == '+' || input.charAt(pos) == '-')) {
                pos++;
            }
            while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
                pos++;
            }
        }

        return input.substring(start, pos);
    }

    private String readLiteral(String expected) {
        if (input.startsWith(expected, pos)) {
            pos += expected.length();
        } else {
            throw new RuntimeException("Unexpected character: " + input.charAt(pos));
        }
        return expected;
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
        parseValue();
    }

    private void parseValue() {
        if (match(TokenType.STRING)) return;
        if (match(TokenType.NUMBER)) return;
        if (match(TokenType.TRUE)) return;
        if (match(TokenType.FALSE)) return;
        if (match(TokenType.NULL)) return;
        if (check(TokenType.LBRACE)) { parseObject(); return; }
        if (check(TokenType.LBRACKET)) { parseArray(); return; }
        throw new RuntimeException("Unexpected token in value: " + peek().type);
    }

    private void parseArray() {
        expect(TokenType.LBRACKET);

        if(!check(TokenType.RBRACKET)) {
            parseValue();
            while (match(TokenType.COMMA)) {
                parseValue();
            }
        }

        expect(TokenType.RBRACKET);
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
