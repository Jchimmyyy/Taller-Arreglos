import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.Collections;
/**
 * Esta clase representa un parqueadero con TAMANO puestos.
 */
public class Parqueadero {
// -----------------------------------------------------------------
    // Constantes
    // -----------------------------------------------------------------

    /**
     * Indica el n�mero de puestos en el parqueadero.
     */
    public static final int TAMANO = 40;

    /**
     * Es el c�digo de error para cuando el parqueadero est� lleno.
     */
    public static final int NO_HAY_PUESTO = -1;

    /**
     * Es el c�digo de error para cuando el parqueadero est� cerrado.
     */
    public static final int PARQUEADERO_CERRADO = -2;

    /**
     * Es el c�digo de error para cuando el carro que se busca no est� dentro del parqueadero.
     */
    public static final int CARRO_NO_EXISTE = -3;

    /**
     * Es el c�digo de error para cuando ya hay un carro en el parqueadero con la placa de un nuevo carro que va a entrar.
     */
    public static final int CARRO_YA_EXISTE = -4;

    /**
     * Es la hora a la que se abre el parqueadero.
     */
    public static final int HORA_INICIAL = 6;

    /**
     * Es la hora a la que se cierra el parqueadero.
     */
    public static final int HORA_CIERRE = 20;

    /**
     * Es la tarifa inicial del parqueadero.
     */
    public static final int TARIFA_INICIAL = 1200;

    // -----------------------------------------------------------------
    // Atributos
    // -----------------------------------------------------------------

    /**
     * Contenedora de tama�o fijo de puestos.
     */
    private Puesto puestos[];
    private int cantidadCarrosSacados;

    /**
     * Tarifa por hora en el parqueadero.
     */
    private int tarifa;

    /**
     * Valor recibido en la caja del parqueadero.
     */
    private int caja;

    /**
     * Hora actual en el parqueadero.
     */
    private int horaActual;

    /**
     * Indica si el parqueadero esta abierto.
     */
    private boolean abierto;

    // -----------------------------------------------------------------
    // Constructores
    // -----------------------------------------------------------------

    /**
     * Crea un parqueadero con su informaci�n b�sica. <br>
     * <b>post: </b> Se cre� un parqueadero abierto con la tarifa establecida y el arreglo de puestos est� creado.
     */
    public Parqueadero( )
    {
        horaActual = HORA_INICIAL;
        abierto = true;
        tarifa = TARIFA_INICIAL;
        caja = 0;
        // Crea el arreglo de puestos e inicializa cada uno de ellos
        puestos = new Puesto[TAMANO];
        for( int i = 0; i < TAMANO; i++ )
            puestos[ i ] = new Puesto( i );

    }

    // -----------------------------------------------------------------
    // M�todos
    // -----------------------------------------------------------------

    /**
     * Retorna un mensaje con la placa del carro que se encuentra en la posici�n indicada.
     * @param pPosicion Posici�n del carro.
     * @return Mensaje con la placa. Si no hay un carro en dicha posici�n retorna un mensaje indicando que no hay un carro en esa posici�n.
     */
    public String darPlacaCarro( int pPosicion )
    {
        String respuesta = "";
        if( estaOcupado( pPosicion ) )
        {
            respuesta = "Placa: " + puestos[ pPosicion ].darCarro( ).darPlaca( );
        }
        else
        {
            respuesta = "No hay un carro en esta posici�n";
        }

        return respuesta;
    }

    /**
     * Ingresa un un carro al parqueadero. <br>
     * <b>pre: </b> El arreglo de puestos no est� vac�o. <br>
     * <b>post: </b>El carro qued� parqueado en el puesto indicado.
     * @param pPlaca Placa del carro que ingresa. pPlaca != null.
     * @return Puesto en el que debe parquear. <br>
     *         Si el parqueadero est� lleno retorna el valor NO_HAY_PUESTO. <br>
     *         Si el parqueadero est� cerrado retorna el valor PARQUEADERO_CERRADO.
     */
    public int entrarCarro( String pPlaca )
    {
        int resultado = 0;
        if( !abierto )
        {
            resultado = PARQUEADERO_CERRADO;
        }
        else
        {
            // Buscar en el parqueadero un carro con la placa indicada
            int numPuestoCarro = buscarPuestoCarro( pPlaca.toUpperCase() );
            if( numPuestoCarro != CARRO_NO_EXISTE )
            {
                resultado = CARRO_YA_EXISTE;
            }

            // Buscar un puesto libre para el carro y agregarlo
            resultado = buscarPuestoLibre( );
            if( resultado != NO_HAY_PUESTO )
            {
                Carro carroEntrando = new Carro( pPlaca, horaActual );
                puestos[ resultado ].parquearCarro( carroEntrando );
            }
        }

        return resultado;
    }

    /**
     * Sirve para sacar un carro del parqueadero y saber la cantidad de dinero que debe pagar. <br>
     * <b>pre: </b> El arreglo de puestos no est� vac�o. <br>
     * <b>post: </b> El carro sali� del parqueadero y el puesto que ocupaba, ahora est� libre.
     * @param pPlaca Placa del carro que va a salir. pPlaca != null.
     * @return Retorna el valor a pagar. Si el carro no se encontraba dentro del parqueadero entonces retorna CARRO_NO_EXISTE. <br>
     *         Si el parqueadero ya estaba cerrado retorna PARQUEADERO_CERRADO.
     */
    public int sacarCarro( String pPlaca )
    {
        int resultado = 0;
        if( !abierto )
        {
            resultado = PARQUEADERO_CERRADO;
        }
        else
        {
            int numPuesto = buscarPuestoCarro( pPlaca.toUpperCase( ) );
            if( numPuesto == CARRO_NO_EXISTE )
            {
                resultado = CARRO_NO_EXISTE;
            }
            else
            {
                Carro carro = puestos[ numPuesto ].darCarro( );
                int nHoras = carro.darTiempoEnParqueadero( horaActual );
                int porPagar = nHoras * tarifa;
                caja = caja + porPagar;
                puestos[ numPuesto ].sacarCarro( );
                resultado = porPagar;
                cantidadCarrosSacados++;
            }
        }

        return resultado;
    }

    /**
     * Indica la cantidad de dinero que hay en la caja.
     * @return Los ingresos totales en la caja.
     */
    public int darMontoCaja( )
    {
        return caja;
    }

    /**
     * Indica la cantidad de puestos libres que hay.
     * @return El n�mero de espacios vac�os en el parqueadero.
     */
    public int calcularPuestosLibres( )
    {
        int puestosLibres = 0;
        for( Puesto puesto : puestos )
        {
            if( !puesto.estaOcupado( ) )
            {
                puestosLibres = puestosLibres + 1;
            }
       }
        return puestosLibres;
    }

    /**
     * Cambia la tarifa actual del parqueadero.
     * @param pTarifa Tarifa nueva del parqueadero.
     */
    public void cambiarTarifa( int pTarifa )
    {
        tarifa = pTarifa;
    }

    /**
     * Busca un puesto libre en el parqueadero y lo retorna. Si no encuentra retorna el valor NO_HAY_PUESTO.
     * @return N�mero del puesto libre encontrado.
     */
    private int buscarPuestoLibre( )
    {
        int puesto = NO_HAY_PUESTO;
        for( int i = 0; i < TAMANO && puesto == NO_HAY_PUESTO; i++ )
        {
            if( !puestos[ i ].estaOcupado( ) )
            {
                puesto = i;
            }
        }
        return puesto;
    }

    /**
     * Indica el n�mero de puesto en el que se encuentra el carro con una placa dada. Si no lo encuentra retorna el valor CARRO_NO_EXISTE.
     * @param pPlaca Placa del carro que se busca. pPlaca != null.
     * @return N�mero del puesto en el que se encuentra el carro.
     */
    private int buscarPuestoCarro( String pPlaca )
    {
        int puesto = CARRO_NO_EXISTE;
        for( int i = 0; i < TAMANO && puesto == CARRO_NO_EXISTE; i++ )
        {
            if( puestos[ i ].tieneCarroConPlaca( pPlaca ) )
            {
                puesto = i;
            }
        }
        return puesto;
    }

    /**
     * Avanza una hora en el parqueadero. Si la hora actual es igual a la hora de cierre, el parqueadero se cierra.
     */
    public void avanzarHora( )
    {
        if( horaActual <= HORA_CIERRE )
        {
            horaActual = ( horaActual + 1 );
        }
        if( horaActual == HORA_CIERRE )
        {
            abierto = false;
        }
    }

    /**
     * Retorna la hora actual.
     * @return La hora actual en el parqueadero.
     */
    public int darHoraActual( )
    {
        return horaActual;
    }

    /**
     * Indica si el parqueadero est� abierto.
     * @return Retorna true si el parqueadero est� abierto. False en caso contrario.
     */
    public boolean estaAbierto( )
    {
        return abierto;
    }

    /**
     * Retorna la tarifa por hora del parqueadero.
     * @return La tarifa que se est� aplicando en el parqueadero.
     */
    public int darTarifa( )
    {
        return tarifa;
    }

    /**
     * Indica si un puesto est� ocupado.
     * @param pPuesto El puesto que se quiere saber si est� ocupado. pPuesto >= 0 && pPuesto < puestos.length.
     * @return Retorna true si el puesto est� ocupado. False en caso contrario.
     */
    public boolean estaOcupado( int pPuesto )
    {
        boolean ocupado = puestos[ pPuesto ].estaOcupado( );
        return ocupado;
    }

    // -----------------------------------------------------------------
    // Puntos de Extensi�n
    // -----------------------------------------------------------------

    /**
     * M�todo de extensi�n 1.
     * @return Respuesta 1.
     */
    public String metodo1()
    {
        int cantidadCarrosPB = contarCarrosQueComienzanConPlacaPB();
        boolean hayCarro24HorasOMas = hayCarroCon24Horas();

        String mensaje = "Cantidad de carros con placa PB: " + cantidadCarrosPB + " - Hay carro parqueado por 24 o más horas: ";

        if (hayCarro24HorasOMas) {
            mensaje += "Sí.";
        } else {
            mensaje += "No.";
        }
        return mensaje;
    }

    /**
     * M�todo de extensi�n 2.
     * @return Respuesta 2.
     */
    public String metodo2( )
    {
        String mensaje = "Cantidad de carros sacados: " + cantidadCarrosSacados + ".";
        return mensaje;
    }

    public float darTiempoPromedio(){// tiempo promedio de los carros en el parqueadero
        double totalTiempos=0;
        int cantidadCarros =0;
        for( Puesto puesto : puestos )
        {
            if( puesto.estaOcupado( ) )

            {

                Carro carro = puesto.darCarro();
                int nHoras = carro.darTiempoEnParqueadero( horaActual );
                totalTiempos +=nHoras;
                cantidadCarros++;
            }
    }
        double tiempoPromedio=totalTiempos/cantidadCarros;
        return (float) tiempoPromedio;
    }

    public boolean hayCarrosMasDeOchoHoras(){
        for(Puesto puesto:puestos){
            if(puesto.estaOcupado()){
                Carro carro = puesto.darCarro();
                int horasEnParqueadero = carro.darTiempoEnParqueadero(horaActual);
                if(horasEnParqueadero>8){
                    return true;
                }
            }
        }
        return false;
    }
    public ArrayList<Carro>darCarrosMasDe8HorasParqueados(){
        ArrayList<Carro> carrosMasDe8Horas = new ArrayList<>();
        for(Puesto puesto:puestos){
            if(puesto.estaOcupado()){
                Carro carro = puesto.darCarro();
                int horasEnParqueadero = carro.darTiempoEnParqueadero(horaActual);
                if(horasEnParqueadero>8){
                    carrosMasDe8Horas.add(carro);
                }
            }
        }
        return carrosMasDe8Horas;
    }
    public ArrayList<Carro>darCarrosMasDeTresHorasParqueados(){
    ArrayList<Carro> carrosMasDeTresHoras = new ArrayList<>();
    for(Puesto puesto:puestos){
        if(puesto.estaOcupado()){
            Carro carro = puesto.darCarro();
            int horasEnParqueadero = carro.darTiempoEnParqueadero(horaActual);
            if(horasEnParqueadero>3){
                carrosMasDeTresHoras.add(carro);
            }
        }
    }
    return carrosMasDeTresHoras;
    }
    public boolean hayCarrosPlacaIgual(int pPosicion) {
        if (estaOcupado(pPosicion)) {
            String placaActual = puestos[pPosicion].darCarro().darPlaca();

            for (int i = 0; i < puestos.length; i++) {
                if (i != pPosicion && estaOcupado(i)) {
                    String placaComparar = puestos[i].darCarro().darPlaca();

                    if (placaActual.equals(placaComparar)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public int contarCarrosQueComienzanConPlacaPB(){
        int contador = 0;

        for (Puesto puesto : puestos) {
            if (puesto.estaOcupado()) {
                Carro carro = puesto.darCarro();
                String placa = carro.darPlaca();

                if (placa.toLowerCase().startsWith("pb")) {
                    contador++;
                }
            }
        }

        return contador;
    }
    public boolean hayCarroCon24Horas(){
        long ahora= System.currentTimeMillis();
        long limite= ahora -(24*60*60*1000);

        for (int i=0; i<TAMANO; i++){
            if (puestos[i].estaOcupado()){
                long horaIngreso= puestos[i].darCarro().darHoraLlegada();
                if (horaIngreso < limite) {
                    return true;
                }
            }
        }
        return false;
    }
    public int desocuparParqueadero(){
        int carrosSacados = 0;

        //RECORRE LOS PUESTOS DEL PARQUEADERO
        for (Puesto puesto : puestos){
            //VERIFICA SI EL CARRO ESTA OCUPADO
            if (puesto.estaOcupado()){
                //SACA EL CARRO DEL PUESTO
                puesto.sacarCarro();
                carrosSacados++;
            }
        }
        return carrosSacados;
    }

}
