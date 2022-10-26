///[Error:mA2|7]
// Error Semantico en linea 7: Se invoco un metodo dinamico desde un metodo estatico.
class A {
    public A a;
    public B b;
    static int mA1(){
        mA2();
    }
    int mA2(){

    }
}

class B {
    public A a;
    int mB1(){

    }
}

class Init{
    static void main()
    { }
}
