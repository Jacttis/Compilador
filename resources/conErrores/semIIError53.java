///[Error:=|13]
// Error Semantico en linea 13: El tipo de la expresion boolean no coincide con el tipo de la variable asignada B
class A {
    public int a;
    public boolean c;
    public B b;

    int m1A(int p1, boolean p2){
        var b = "hola";
        {
            var a = 4;
            {
                b = true;
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
