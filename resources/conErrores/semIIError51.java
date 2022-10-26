///[Error:a|10]
// Error Semantico en linea 10: La variable "a" ya fue declarada.
class A {
    public int a;
    public boolean c;
    public B b;

    int m1A(int p1, boolean p2){
        var a = 3;
        var a = 4;
    }
    void m2A(){
    }
}
class B {
    public boolean c;
    C mB1(){}
}
class C{
    public boolean c1;
    C mC2(){}
}

class Init{
    static void main()
    { }
}
