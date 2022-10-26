///[Error:b|6]
// Error Semantico en linea 6: El atributo de la clase "B" con nombre "b" no puede ser accedido ya que no es publico.
class A {
    public A a;
    int m1(){
        return a.b;
    }
}

class B {
    public A a;
    private int b;
}

class Init{
    static void main()
    { }
}