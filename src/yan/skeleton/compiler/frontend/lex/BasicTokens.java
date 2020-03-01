package yan.skeleton.compiler.frontend.lex;

import java.util.HashMap;
import java.util.Map;

public interface BasicTokens {

    static public int UNKNOWN = -1;
    static public int EOF = 0;

    static public int IDENTIFIER = 1;

    // 数字
    static public int INT_CONST = 50;

    // 标点符号 Punctuator
    static public int PLUS = 10;
    static public int MINUS = 11;
    static public int MULTI = 12;
    static public int DIV = 13;

    static public int ASSIGN = 14;

//    protected static Map<Integer, String> typeDescription = new HashMap<>();
//
//    static public String stringfy(int type) {
//        if (typeDescription.containsKey(type))
//            return typeDescription.get(type);
//        else return String.valueOf(type);
//    }
}
