///[Error:mB1|7]
// Error Semantico en linea 7: El tipo "B" no posee un metodo llamado "mB1"
class A {
    public A a;
    public B b;
    int mA1(){
        mA2().mB1(true);
    }
    B mA2(){
    }
}

class B {
    public A a;
}

class Init{
    static void main()
    { }
}
