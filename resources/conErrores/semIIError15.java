///[Error:b|7]
// Error Semantico en linea 7: El tipo de la variable encadenada no puede ser primitivo.
class A {
    public A a;
    public int b;
    C m1(){
        return b.c;
    }
}

class B {
    public A a;
    public C c;
}

interface C {

}

class Init{
    static void main()
    { }
}