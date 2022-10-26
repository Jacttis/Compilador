//[Error:aPrivateA|14]

class A{
    private int aPrivateA;
    public int aPublicA;

    static void main(){}
}

class B extends A{
    private int aPrivateB;
    public int aPublicB;
    void m1(){
        this.aPublicA = this.aPrivateA;
    }
}