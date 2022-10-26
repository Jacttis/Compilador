///[Error:mB1|8]
// Error Semantico en linea 8: El metodo invocado "mB1" no esta declarado en la clase "A"
class A {
    public A a;
    public B b;
    int mA1(){
        mA2();
        mB1();
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
