// Prueba encabezados de metodos redefinicion valida e implementacion de interface
class A implements C<System>{
    void m3(A p1, B p2)
    {}  
}
class B extends A {
    void m3(A p1, B p2)
    {}  
}

interface C<D> extends B1{}

interface B1<D>{}

class Init{
    static void main()
    { }
}




