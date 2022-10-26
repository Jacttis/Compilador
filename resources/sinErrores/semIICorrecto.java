// Prueba de acceso a atributo

class A {
    public A a;
    public B b;
    int m1a(){
        var a = b.c;
        return a;
    }
}

class B {
    public int c;
}

class Init{
    static void main()
    { }
}



