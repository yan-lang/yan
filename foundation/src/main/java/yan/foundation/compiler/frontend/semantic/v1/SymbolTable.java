package yan.foundation.compiler.frontend.semantic.v1;

/**
 * A symbol table is responsible for tracking which declaration is in effect
 * when a reference to the symbol is encountered.
 *
 * <p>The api design of this class referenced Crafting a compiler,
 * Chapter 8 Symbol Tables and Declaration Process (p312). </p>
 */
public interface SymbolTable {
    /**
     * Opens a new scope in the symbol table. New symbols are entered in the resulting scope.
     */
    void openScope(Scope scope);

    /**
     * Closes the most recently opened scope in the symbol table.
     * Symbol references subsequently revert to outer scopes.
     */
    void closeScope();


    /**
     * Get the current scope.
     *
     * @return current scope, null if there is no scope in the symbol table for now.
     */
    Scope currentScope();

    /**
     * Enters a symbol in the symbol table’s current scope.
     *
     * @param symbol The symbol to be entered.
     */
    void enter(Symbol symbol);


    /**
     * Returns the symbol table’s currently valid declaration for name.
     * If no declaration for name is currently in effect, then a null pointer is returned.
     *
     * @param name Symbol name.
     * @return The symbol whose name equals the name given. Null if not found.
     */
    Symbol retrieve(String name);


    /**
     * The same as {@link SymbolTable#retrieve(String)}, but only searches current scope.
     *
     * @param name Symbol name.
     * @return The symbol whose name equals the name given. Null if not found.
     */
    Symbol retrieveLocally(String name);


    /**
     * Tests whether name is present in the symbol table’s current (innermost) scope.
     *
     * @param name Symbol name.
     * @return True if it is, False if name is in an outer scope, or is not in the symbol table at all.
     */
    boolean declaredLocally(String name);

}
