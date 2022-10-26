//[Error:==|8]
// Error Semantico en linea 8: La subexpresion del lado izquierdo "int" no coincide con el tipo esperado por el lado derecho "void"

class A{
    public int a;
    public int b;
    void m1A() { 
        var a = m2A() == m3A();
    }
    int m2A() {
    }
    void m3A(){}
}

class B extends A{
    static void main(){}
}