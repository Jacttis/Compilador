//[Error:==|8]
// Error Semantico en linea 8: El lado izquierdo de la expresion no puede ser void.

class A{
    public int a;
    public int b;
    void m1A() { 
        var a = m3A() == m1A();
    }
    int m2A() {
    }
    void m3A(){}
}

class B extends A{
    static void main(){}
}