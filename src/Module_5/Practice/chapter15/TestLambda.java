package Module_5.Practice.chapter15;

// @FunctionalInterface tells the compiler that the interface is a functional interface.
// Since T1, T2, and T3 are all functional interfaces, a lambda expression can be used with the methods
// setAction1(T1), setAction2(T2), and getValue(T3).
// The program prints "Action 1! 4.5 7" -- 7 is followed by a newline because of println)
public class TestLambda {
    public static void main(String[] args) {
        // create an instance to call instance merthods on.
        TestLambda test = new TestLambda();

        // setAction1() expects a T1.  T1 has one abstract method void m1().
        // The lambda() -> System.out.print("action1! ") is shorthand for the body of m1().
        // Inside setAction1, t.m1(); is called, which executes the lambda and prints "Action 1!"
        test.setAction1(() -> System.out.print("Action 1! "));

        // setAction2 expects a T2.
        // T2 has void m2(Double d).
        // The lambda e -> System.out.print(e + " ") is the body of m2(Double e).
        // setAction2 calls t.m2(4.5); the primitiave 4.5 is autoboxed to Double.
        // The lambda runs and prints "4.5 ".
        test.setAction2(e -> System.out.print(e + " "));

        // getValue expects a T3
        // T3 has int m3(int d1, int d2)
        // The lambda(e1, e2) -> e1 + e2 is the body of m3
        // getValue calls t.m3(5, 2); so the lambda returns 7.
        // println prints 7 and a newline
        System.out.println(test.getValue((e1, e2) -> e1 + e2));
    }
    // The three "higher-order" methods.
    // Each method accepts behavior (a lambda as a funtional interface) and invokes it.
    public void setAction1(T1 t) {
        t.m1();
    }

    public void setAction2(T2 t) {
        t.m2(4.5);
    }

    public int getValue(T3 t) {
        return t.m3(5, 2);
    }
}

// @FunctionalInterface = exactly one abstract method (a "SAM type")
// that's what makes them valid lambda targets.
// parameter names (e, e1, e2) in lambdas are aribrary; types are inferred from the interface
// for T2, e is inferred as Double.  for T3, e1/e2 are int.
@FunctionalInterface
interface T1 {
    public void m1();
}

@FunctionalInterface
interface T2 {
    public void m2(Double d);
}

@FunctionalInterface
interface T3 {
    public int m3(int d1, int d2);
}