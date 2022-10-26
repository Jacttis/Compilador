///[Error:=|8]
// Error Semantico en linea 8: El elemento del lado izquierdo de la asignacion debe ser una variable.
class A {
    public int a;
    public B b;

    int m1A(){
        m2A() = true;
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
