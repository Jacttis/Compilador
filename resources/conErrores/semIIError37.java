///[Error:z|7]
// Error Semantico en linea 7: La clase "B" no posee un atributo con el nombre "z"
class A {
    public A a;
    public B b;
    int mA1() {
        var b2 = new B().z;
    }
}

class B {
    public A a;
    static int mB1(){
    }
}

interface C{

}

class Init{
    static void main()
    { }
}


