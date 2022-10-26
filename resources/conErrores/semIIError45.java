///[Error:+=|10]
// Error Semantico en linea 9: En las sentencias de asignacion compuestas (incremento y decremento) tanto el lado izquierdo como el lado derecho
// de la asignacion deben ser de tipo int.
class A {
    public int a;
    public B b;
    public boolean c;

    int m1A(){
        c += true;
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
