package yan.foundation.ir;

public enum OpCode {
    // Terminator Instructions

    RET,
    BR,
    INDIRECT_BR,
    INVOKE,
    UNREACHABLE,

    // Standard Binary Operators

    ADD,
    FADD,
    SUB,
    FSUB,
    MUL,
    FMUL,

    // Logical Operators

    SHL,   // Shift-Left
    LSHR,  // Logical shift-right
    ASHR,  // Arithmetic, signed, right-shift
    AND,
    OR,
    XOR,

    // Memory Operators

    ALLOCA,
    LOAD,
    STORE,
    GET_ELEMENT_PTR,

    // Cast Operators

    TRUNC,
    S_EXT,
    FP_TO_SI,
    SI_TO_FP,
    FP_TRUNC,
    FP_EXT,
    PTR_TO_INT,
    INT_TO_PTR,

    // Other Operators

    ICMP,
    FCMP,
    PHI,
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

        SHL,   // Shift-Left
        LSHR,  // Logical shift-right
        ASHR,  // Arithmetic, signed, right-shift
        AND,
        OR,
        XOR,
    }

    public enum Cast {
        TRUNC,
        S_EXT,
        FP_TO_SI,
        SI_TO_FP,
        FP_TRUNC,
        FP_EXT,
        PTR_TO_INT,
        INT_TO_PTR,
    }
}
