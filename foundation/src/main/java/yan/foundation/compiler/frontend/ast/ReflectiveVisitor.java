package yan.foundation.compiler.frontend.ast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ReflectiveVisitor find corresponding visit method using reflection,
 * generally, for an object (typed C), it will search for method like {@code visit(C)},
 * or {@code visit(C's superclass)} or {@code visit(C's last interface)}.
 *
 * <p>Any classes that implement this interface should be public,
 * otherwise {@link IllegalAccessException} might be thrown.</p>
 */
public interface ReflectiveVisitor {

    default void dispatch(Tree tree) {
        Method m = getBestMethodFor(tree);
        try {
            m.invoke(this, tree);
        } catch (IllegalAccessException e) {
            e.printStackTrace(System.err);
            throw new Error("Method " + m + " aborting, bad access: " + e);
        } catch (InvocationTargetException e) {
            // This exception is thrown if the reflectively called method
            // throws anything for any reason
            e.printStackTrace(System.err);
            throw new Error("Method " + m + " aborting: " + e);
        }
    }

    /**
     * Find the closest visit method that handles the supplied object
     */

    default Method getBestMethodFor(Tree o) {
        Class<?> nodeClass = o.getClass();
        Method ans = null;

        // Try the superclasses

        for (Class<?> c = nodeClass;
             c != Tree.class && ans == null;
             c = c.getSuperclass()) {
//            debugMsg("Looking for class match for " + c.getName());
            try {

                // Unlike GoF, all methods are "visit" and are
                // distinguished by their param type

                ans = getClass().getMethod("visit", c);

            } catch (NoSuchMethodException ignored) { }
        }

        // Try the interfaces.  The code below will find the last
        // interface listed for
        // which "this" visitor can handle the type

        Class<?> iClass = nodeClass;
        while (ans == null && iClass != Tree.class) {
//            debugMsg("Looking for interface  match in " + iClass.getName());
            Class<?>[] interfaces = iClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
//                debugMsg("   trying interface " + anInterface);
                try {
                    ans = getClass().getMethod("visit", anInterface);
                } catch (NoSuchMethodException ignored) { }
            }
            iClass = iClass.getSuperclass();
        }

        if (ans == null) {
            try {
//                debugMsg("Giving up");
                ans = getClass().getMethod("defaultVisit",
                                           Tree.class);
            } catch (NoSuchMethodException e) {
                // Just stop, because throwing an exception will cascade upwards
//                debugMsg("Cannot happen -- could not find defaultVisit(Object)");
                e.printStackTrace(System.err);
                System.exit(-1);
            }
        }
//        debugMsg("Best method for " + o + " is " + ans);
        return ans;
    }

}
