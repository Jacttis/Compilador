
class A<E,VCT> {
    String  a,b,c;

    A(){

    }
    A(int i){

    }
    void metodo1(String s) {
        S.a();
    }

    int metodo1() {
        return null;
    }

    static E metodo3() {
        return this.x();
    }
    VCT metodo3(int i) {
        return this.x();
    }
}

class B<E,VCT> extends A<Object,System> {
    String  a,b,c;
    void metodo1(String s) {
        S.a();
    }

    int metodo1() {
        return null;
    }

    static String metodo3() {
        return this.x();
    }
    String metodo3(int i) {
        return this.x();
    }
}






