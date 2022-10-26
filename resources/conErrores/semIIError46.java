///[Error:=|9]
// Error Semantico en linea 9: El ultimo elemento del encadenado del lado izquierdo de la asignacion debe ser una variable.
class A {
    public int a;
    public boolean c;
    public B b;

    int m1A(){
        b.mB1() = true;
    }
    boolean m2A(){}
}
class B {
    public boolean c;
    boolean mB1(){}
}
class C{
    boolean mC2(){}
}

class Init{
    static void main()
    { }
}
