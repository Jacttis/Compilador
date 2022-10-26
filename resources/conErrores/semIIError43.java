///[Error:return|7]
// Error Semantico en linea 7: Retorno vacio en metodo que no es void
class A {
    public A a;
    public B b;
    String mA1(){
        return ;
    }
}
class B {
    static C mB2(){
    }
}
class C{
    boolean mC2(){}
}

class Init{
    static void main()
    { }
}
