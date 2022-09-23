
class A<E,V<C<T>>> extends B<E,V> {
    String  a;
    void metodo1() {
        S.a();
    }

    int metodo2() {
        return null;
    }

    static String metodo3() {
        return this.x();
    }
    String metodo3() {
        return this.x();
    }


}