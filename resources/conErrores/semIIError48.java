///[Error:a|16]
// Error Semantico en linea 13: La variable "b" ya fue declarada en un bloque que contiene al bloque que contiene la declaracion.
class A {
    public int a;
    public boolean c;
    public B b;

    int m1A(int p1, boolean p2){
        var b = 3;
        {
            var a = 4;
            {
                var c = 5;
            }
            {
                var a = true;
            }
        }
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