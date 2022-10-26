///[Error:mB1|7]
// Error Semantico en linea 7: El tipo del argumento actual "a" no coincide con el tipo del parametro formal.
class A {
    public A a;
    public B b;
    int mA1(){
        mA2().mB1(true, a);
    }
    B mA2(){
    }
}

class B {
    public A a;
    int mB1(boolean p1, int p2){
    }
}

class Init{
    static void main()
    { }
}

