import options.OptionsGame;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("******************************************************");
        System.out.println("Bienvenido a Piedra, papel, o tijera! \nTe reto a ganarle a la máquina!");
        System.out.println("------------------------------------------------------");

        int contPlayer = 0, contMachine = 0, round = 0;

        central(scanner, contPlayer, contMachine, round);
    }

    // muestra estadisticas del juego, obtiene opcion del jugador, tambien hace validacion de opcion ingresada
    public static void central(Scanner scanner, int contPlayer, int contMachine, int round) {
        round++;

        // opcion del jugador
        String option = getPlayerOption(scanner);

        // validacion opcion del jugador
        if (validOption(option)) {
            int[] results = playRound(scanner, option, contPlayer, contMachine, round);
            // se asignan valores actualizados para cada uno
            contPlayer = results[0];
            contMachine = results[1];
        } else {
            System.out.println("Opción inválida, punto para la maquina");
            contMachine++;
            if (contMachine == 5) {
                System.out.println("Excediste el límite máximo de intentos fallidos!");
                retry(scanner, contPlayer, contMachine, round);
            }
        }
        retry(scanner, contPlayer, contMachine, round);
    }

    // obtiene opciones del jugador y maquina, también controla el resultado de la ronda
    public static int[] playRound(Scanner scanner, String option, int contPlayer, int contMachine, int round) {
        // se asigna la opcion del jugador (luego de verificar que la opcion si existe)
        OptionsGame optionPlayer = assign(option);
        // genera una opcion para la maquina
        OptionsGame optionMachine = getRandomOption();

        // evalua la logica y determina un resultado que posteriormente se muestra
        String result = evaluateGame(optionPlayer, optionMachine);
        System.out.println(result);

        // operaciones necesarias dependiendo del resultado
        if (result.equals("Ganaste!")) {
            contPlayer++;
        } else if (result.equals("Perdiste!")) {
            contMachine++;
            if (contMachine == 5) {
                System.out.println("Excediste el límite máximo de intentos fallidos!");
                retry(scanner, contPlayer, contMachine, round);
            }
        }
        // devuelve un array de enteros que contenga los valores actualizados de contPlayer y contMachine
        return new int[]{contPlayer, contMachine};
    }

    // Se obtiene la opcion del jugador
    public static String getPlayerOption(Scanner scanner) {
        System.out.println("Ingrese PIEDRA, PAPEL o TIJERA");
        return scanner.nextLine();
    }

    // se genera opcion random para la maquina
    public static OptionsGame getRandomOption() {
        OptionsGame[] allOptions = OptionsGame.values();

        Random random = new Random();
        int indiceAleatorio = random.nextInt(allOptions.length);

        return allOptions[indiceAleatorio];
    }

    // se muestran las estadisticas del juego tales como ronda, puntos del jugador y de la maquina
    public static String summary (int round, int contPlayer, int contMachine){
        return "*********************************\n" +
                "Ronda " + round + ".\n" +
                "Marcador:\nJugador:" + contPlayer + "\nMaquina: " + contMachine;
    }

    // se controla si se quiere seguir jugando
    public static void retry(Scanner scanner, int contPlayer, int contMachine, int round) {
        // estadisticas de juego
        System.out.println(summary(round, contPlayer, contMachine));

        System.out.println("Quieres seguir jugando? y/n");
        String response = scanner.nextLine();

        if (response.equalsIgnoreCase("y")) {
            // si quiere seguir jugando pero excedió el límite de fallos, se reinician las estadísticas
            if (contMachine == 5){
                contPlayer = 0;
                contMachine = 0;
                round = 0;
            }
            // empieza de nuevo el proceso
            central(scanner, contPlayer, contMachine, round);
            // en caso de que no quiera seguir jugando, se cierra scanner y posteriormente la ejecucion
        } else if (response.equalsIgnoreCase("n")) {
            scanner.close();
            System.exit(0);
        } else {
            System.out.printf("La opción ingresada no es válida, intenta de nuevo\n");
            retry(scanner,contPlayer,contMachine,round);
        }
    }

    // evalua si la opcion es valida
    public static boolean validOption (String option){
        try {
            OptionsGame.valueOf(option.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // se asigna el valor del jugador
    public static OptionsGame assign (String option){
        OptionsGame optionPlayer = OptionsGame.valueOf(option.toUpperCase());
        return optionPlayer;
    }

    // logica piedra papel o tijera y se genera respuesta de la maquina
    public static String evaluateGame (OptionsGame playerOption, OptionsGame computerOption){

        if (playerOption.equals(computerOption)) {
            return "Empate";
        }else if (playerOption.equals(OptionsGame.PIEDRA) && computerOption.equals(OptionsGame.TIJERA) ||
                playerOption.equals(OptionsGame.PAPEL) && computerOption.equals(OptionsGame.PIEDRA) ||
                playerOption.equals(OptionsGame.TIJERA) && computerOption.equals(OptionsGame.PAPEL)) {
            return "Ganaste!";
        } else {
            return "Perdiste!";
        }
    }
}