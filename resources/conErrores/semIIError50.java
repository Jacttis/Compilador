///[Error:c1|9]
// Error Semantico en linea 9: El elemento del lado izquierdo debe ser una llamada.
class A {
    public int a;
    public boolean c;
    public B b;

    int m1A(){
        b.mB1().mC2().c1;
    }
    void m2A(){
    }
}
class B {
    public boolean c;
    C mB1(){}
}
class C{
    public boolean c1;
    C mC2(){}
}

class Init{
    static void main()
    { }
}
