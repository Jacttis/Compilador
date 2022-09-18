
class A<E,V<C<T>>> extends B<E,V> {

    void metodo1() {
        return 10;
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