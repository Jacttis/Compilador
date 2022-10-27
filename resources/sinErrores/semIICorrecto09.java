class A {
    public A a;
    public B b;
    public int c;
    int mA1() {
        var a2 = new B().a;
        B.mB1().mC1(5*2,3 > 2);
        b.mB2().mC2().aC1 = true;
        c = b.c.c2;
        c += mA2();
    }
    int mA2(){
        var v1 = true || !false;
        var v2 = this.b;
        var v3 = mA3();
        return (((3*2) + 4) / 2) * c;
    }
    String mA3(){
        var b2 = new B().a.mA1();
        return ((("hola")));
    }
}

class B {
    public A a;
    public C c;
    static C mB1(){
    }
    C mB2(){
        c.mC1(2+3,true);
    }
}

class C{
    public boolean aC1;
    public int c2;
    int mC1(int p1, boolean p2){
        var b = "hola";
        {
            var a = 4;
            {
                b = "";
            }
        }
    }
    C mC2(){
        var i = 10;
        while (i > 0){
            if (i == 5) {
                return new A().b.mB2();
            }
            i -= 1;
        }
    }
}

class Init{
    static void main()
    { }
}


