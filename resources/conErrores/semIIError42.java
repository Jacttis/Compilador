///[Error:mB1|14]
// Error Semantico en linea 14: Se invoco un metodo dinamico desde un metodo estatico.
class A {
    public A a;
    public B b;
}

class B {
    public A a;
    public C c;
    int mB1(){
    }
    static int mB2(){
        return mB1();
    }
}

class C{
    int mC1(){}
}

class Init{
    static void main()
    { }
}
