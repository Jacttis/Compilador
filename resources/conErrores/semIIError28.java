///[Error:mB1|7]
// Error Semantico en linea 7: La cantidad de argumentos actuales del metodo "mB1" no coincide con la cantidad de argumentos formales.
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
    int mB1(boolean p1, int p2){
    }
}

class Init{
    static void main()
    { }
}
