///[Error:b|8]
// Error Semantico en linea 8: El tipo de la variable encadenada no puede ser primitivo.
class A {
    public A a;
    public int b;
    int m1(){
        while (3 > 1){
            var z = a.b.c;
        }
    }
}

class B {
    public A a;
}

class Init{
    static void main()
    { }
}
