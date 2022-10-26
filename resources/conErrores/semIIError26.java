///[Error:mA2|7]
// Error Semantico en linea 7: El tipo de retorno del metodo encadenado no puede ser primitivo o void.
class A {
    public A a;
    public B b;
    int mA1(){
        mA2().mB1(true);
    }
    int mA2(){
    }
}

class B {
    public A a;
    int mB1(boolean p1){

    }
}

class Init{
    static void main()
    { }
}
