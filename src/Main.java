import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Parqueadero a1=new Parqueadero();
        int op;
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("*********Ingrese una opcion*********");
            System.out.println("1. Ingresar un carro al parqueadero.");
            System.out.println("2. Dar salida a un carro del parqueadero.");
            System.out.println("3. Informar los ingresos del parqueadero.");
            System.out.println("4. Consultar la cantidad de puestos disponibles.");
            System.out.println("5. Avanzar el reloj del parqueadero.");
            System.out.println("6. Cambiar la tarifa del parqueadero.");
            System.out.println("7. Tiempo promedio");
            System.out.println("8. Lista de carros con mas de 8 horas");
            System.out.println("9. Lista de carros con mas de 3 horas");
            System.out.println("10. Carros con la misma placa");
            System.out.println("11. Carros que comienzan con PB");
            System.out.println("12. Carros con mas de 24h");
            System.out.println("13. Cantidad de carros con placa PB con mas de 24h");
            System.out.println("14. Desocupar el parqueadero");
            System.out.println("15. Salir");
            op = Integer.parseInt(sc.next());
            switch (op) {
                case 1: {
                    System.out.print("Ingrese la placa del carro: ");
                    String placa = sc.next();
                    int resultado = a1.entrarCarro(placa);
                    if (resultado == Parqueadero.NO_HAY_PUESTO) {
                        System.out.println("El parqueadero está lleno");
                    } else if (resultado == Parqueadero.PARQUEADERO_CERRADO) {
                        System.out.println("El parqueadero está cerrado");
                    } else if (resultado == Parqueadero.CARRO_YA_EXISTE) {
                        System.out.println("Ya hay un carro con esa placa en el parqueadero");
                    } else {
                        System.out.println("Carro ingresado correctamente. Puesto asignado: " + resultado);
                    }

                }
                break;
                case 2: {
                    System.out.print("Ingrese la placa del carro a sacar: ");
                    String placaSacar = sc.next();
                    int valorPagar = a1.sacarCarro(placaSacar);
                    if (valorPagar == Parqueadero.CARRO_NO_EXISTE) {
                        System.out.println("El carro no se encuentra en el parqueadero");
                    } else if (valorPagar == Parqueadero.PARQUEADERO_CERRADO) {
                        System.out.println("El parqueadero está cerrado");
                    } else {
                        System.out.println("Carro sacado del parqueadero. Valor a pagar: " + valorPagar);
                    }

                }
                break;
                case 3:{
                    int montoCaja = a1.darMontoCaja();
                    System.out.println("Ingresos del parqueadero: " + montoCaja);
                }
                break;
                case 4:{
                    int puestosLibres = a1.calcularPuestosLibres();
                    System.out.println("Puestos disponibles: " + puestosLibres);
                }
                break;
                case 5: {
                    a1.avanzarHora();
                    System.out.println("Reloj avanzado");
                }
                break;
                case 6: {
                    System.out.print("Ingrese la nueva tarifa: ");
                    int nuevaTarifa = sc.nextInt();
                    a1.cambiarTarifa(nuevaTarifa);
                    System.out.println("Tarifa cambiada");

                }
                break;
                case 7:{
                    float tiempoPromedio=a1.darTiempoPromedio();
                    System.out.println("El tiempo promedio es: "+tiempoPromedio);
                }
                break;
                case 8:{
                    ArrayList<Carro> carros8H = a1.darCarrosMasDe8HorasParqueados();
                    System.out.println("Carros con mas de 8h: "+carros8H);
                }
                break;
                case 9:{
                    ArrayList<Carro> carros3H = a1.darCarrosMasDeTresHorasParqueados();
                    System.out.println("Carros con mas de 3h: "+carros3H);
                }
                break;

                case 11:{
                    int carrosPB = a1.contarCarrosQueComienzanConPlacaPB();
                    System.out.println("Carros que comienzan con placa PB:"+carrosPB);
                }
                break;
                case 12:{
                    boolean carros24H = a1.hayCarroCon24Horas();
                    System.out.println(carros24H);
                }
                break;
                case 13:{
                    System.out.println(a1.metodo1());
                }
                break;
                case 14:{
                    int desocuparParqueadero = a1.desocuparParqueadero();
                    System.out.println("Cantidad de carros desocupados: "+desocuparParqueadero);
                }
            }
            }while (op != 15) ;


    }
}