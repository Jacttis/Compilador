///[Error:while|7]
// Error Semantico en linea 7: La condicion del while es de tipo "A" y debe ser de tipo boolean.
class A {
    public A a;
    public int b;
    int m1(){
        while (a){
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
