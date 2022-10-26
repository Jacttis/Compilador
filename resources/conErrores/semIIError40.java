///[Error:this|13]
// Error Semantico en linea 13: No es posible utilizar el operador this desde un metodo estatico.
class A {
    public A a;
    public B b;
}

class B {
    public A a;
    static C mB1(){
    }
    static int mB2(){
        this.a;
    }
}

class C{
    static C mC1(){}
}

class Init{
    static void main()
    { }
}






