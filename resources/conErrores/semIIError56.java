//[Error:a|8]
// Error Semantico en linea 8: La expresion no debe ser de tipo void

class A{
    public int a;
    public int b;
    void m1A() { 
        var a = m2A();
    }
    void m2A() {
    }
    int m3A(){}
}

class B extends A{
    static void main(){}
}