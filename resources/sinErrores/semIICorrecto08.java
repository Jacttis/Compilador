class A {
    public A a;
    public B b;
    void m1a(){
        a = b;
    }
}

class B extends A{
    public int c;
    void m1b(){
        Y t;
        X c=t;
    }
}

class D implements X{}

interface X{}

interface Y extends X{}



class Init{
    static void main()
    { }
}



