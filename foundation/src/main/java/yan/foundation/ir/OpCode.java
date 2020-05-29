package yan.foundation.ir;

public enum OpCode {
    // Terminator Instructions

    RET,
    BR,

    // Standard Binary Operators

    ADD,
    FADD,

    SUB,
    FSUB,

    MUL,
    FMUL,

    DIV,
    FDIV,

    REM,   // Integer remainder
    FREM,  // Float remainder

    // Logical Operators

    SHL,   // Shift-Left
    LSHR,  // Logical shift-right
    ASHR,  // Arithmetic, signed, right-shift
    AND,
    OR,
    XOR,

    // Memory Operators

    ALLOCA,
    MALLOC,
    FREE,
    LOAD,
    STORE,
    GET_ELEMENT_PTR,

    // Cast Operators


    FP_TO_INT,
    INT_TO_FP,
//    TRUNC,
//    EXT,
//    FP_TRUNC,
//    FP_EXT,
//    PTR_TO_INT,
//    INT_TO_PTR,

    // Other Operators

    ICMP,
    FCMP,
    CALL,
    ;

    public enum Binary {
        ADD,
        FADD,
        SUB,
        FSUB,
        MUL,
        FMUL,
        DIV,
        FDIV,
        REM,   // Integer remainder
        FREM,  // Float remainder

        SHL,   // Shift-Left
        LSHR,  // Logical shift-right
        ASHR,  // Arithmetic, signed, right-shift
        AND,
        OR,
        XOR,
    }

    public enum Cast {
        FP_TO_INT,
        INT_TO_FP,

        // below inst has not been supported yet
//        TRUNC,
//        EXT,
//        FP_TRUNC,
//        FP_EXT,
//        PTR_TO_INT,
//        INT_TO_PTR,
    }
}
