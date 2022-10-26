///[Error:==|7]
// Error Semantico en linea 7: La subexpresion del lado izquierdo "int" no coincide con el tipo esperado por el lado derecho "boolean"
class A {
    public A a;
    public B b;
    boolean mA1() {
        return ((4+ 10 + 33) > 12) || ((99*15) == true) && (97<=12) || !true && ((4/2) != 2);
    }
}

class B {
    public A a;
    static C mB1(){
    }
    static int mB2(){

    }
}

class C{
    static C mC1(){}
}

class Init{
    static void main()
    { }
}





