// Prueba una expresion con operadores binarios y unarios

interface Base1 { void m1(); }
interface Base2 extends Base1{ void m1(); }
class Conc implements Base2{ void m1(){} }
class Prueba {
    public Conc p;

    Base2 dar(Base1 x, Base2 y) {
        x = p;
        return p;
        dar(null,null);
        m2();
    }
    static void m2(){

    }

}


class Init{
    static void main()
    { }
}
