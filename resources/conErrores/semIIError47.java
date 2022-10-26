///[Error:c1|9]
// Error Semantico en linea 9: El atributo de la clase "C" con nombre "c1" no puede ser accedido ya que no es publico.
class A {
    public int a;
    public boolean c;
    public B b;

    int m1A(){
        b.mB1().mC2().c1 = true;
    }
    boolean m2A(){}
}
class B {
    public boolean c;
    C mB1(){}
}
class C{
    private boolean c1;
    C mC2(){}
}

class Init{
    static void main()
    { }
}
