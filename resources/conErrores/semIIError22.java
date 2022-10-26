///[Error:mA2|7]
// Error Semantico en linea 7: El tipo del argumento actual "2" no coincide con el tipo del parametro formal.
class A {
    public A a;
    public B b;
    int mA1(){
        mA2();
    }
    int mA2(boolean p1){
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
