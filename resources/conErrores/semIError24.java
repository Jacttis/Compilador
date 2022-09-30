///[Error:metodo_b|31]

// test de metodos heredados. La clase C "redefine" de manera incorrecta un metodo heredado de la clase A

class A {

    int metodo_en_a() {

    }

    static void main() {

    }

}

class B extends A {

    int metodo_b() {

    }

}

class C extends B {

    int metodo_en_a(int x) {

    }

    void metodo_b() {

    }

}