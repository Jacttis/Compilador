///[Error:return|7]
// El tipo de retorno de la expresion no coincide con el tipo de retorno del metodo.
class A {
    public A a;
    public B b;
    int m1(){
        return a.b;
    }
}

class B {
    public A a;
    public int c;
}

class Init{
    static void main()
    { }
}