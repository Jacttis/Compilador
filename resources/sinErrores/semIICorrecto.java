///[Error:m2|11]
// Tipos incompatibles en el pasaje de parametros: B no conforma con String - ln: 10
class A {
    public int a1;
    public int v1;
    public D v2;


    void m1(int p1)
    {
        m2(p1+(v1-10),"hola!",new B());
    }

    void m2(int p1, String x, B p2){
        m3(v2);
    }

    void m3(C inter){

    }
}

class B{
}

interface C{}

interface D extends C{}




class Init{
    static void main()
    { }
}


