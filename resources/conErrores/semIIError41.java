///[Error:c|14]
// Error Semantico en linea 14: La variable "c" no fue declarada o no es accesible.
class A {
    public A a;
    public B b;
}

class B {
    public A a;
    public int c;
    static C mB1(){
    }
    static int mB2(){
        return c * 2;
    }
}

class C{
    static C mC1(){}
}

class Init{
    static void main()
    { }
}






