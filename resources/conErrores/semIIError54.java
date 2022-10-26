//[Error:return|8]
// Error Semantico en linea 8: El metodo es void, por lo tanto no debe retornar nada.

class A{
    public int a;
    public int b;
    void m1A() {
        return m1A();
    }
    void m2A() {
    }
    int m3A(){}
}

class B extends A{
    static void main(){}
}
