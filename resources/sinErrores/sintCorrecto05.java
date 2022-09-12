class Prueba extends Object {

    public String text;
    public AA hola ;
    private int x;
    private ClaseA a;

    Prueba hh(int f,char j){
        x = this.hola + 4;
        Prueba.metodoPrueba(true);

    }

    static int metodoPrueba(boolean a){
        var f=0;
        if(a) {
            f = 1;
        }
        else {f=-1;}
        var contador=10;
        return f;
    }

}