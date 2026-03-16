import java.util.Scanner;

public class SistemaGestionEmpleados {

    public static void main(String[] args) {
        Empleados empleados = new Empleados(Mensajes.CAPACIDAD_INICIAL);

        // Dar de alta los tres empleados usando constantes de nombres, cargos y salarios
        empleados.altaEmpleado(new Empleado(Mensajes.NOMBRE_JUAN, Mensajes.CARGOS[0], Mensajes.SALARIO_JUAN));
        empleados.altaEmpleado(new Empleado(Mensajes.NOMBRE_MARIA, Mensajes.CARGOS[1], Mensajes.SALARIO_MARIA));
        empleados.altaEmpleado(new Empleado(Mensajes.NOMBRE_PEDRO, Mensajes.CARGOS[2], Mensajes.SALARIO_PEDRO));

        Scanner scanner = new Scanner(System.in);
        System.out.print(Mensajes.PETICION_PORCENTAJE);
        if (!scanner.hasNextDouble()) {
            System.out.println(Mensajes.ENTRADA_INVALIDA);
            scanner.close();
            return;
        }
        double porcentaje = scanner.nextDouble();

        empleados.aumentarSalario(porcentaje);

        empleados.mostrarListado();

        scanner.close();
    }
}

class Empleados {
    // Renombrado a 'lista' y hecho público según requisitos
    public Empleado[] lista;

    public Empleados(int capacidad) {
        this.lista = new Empleado[capacidad];
    }

    // Añade un empleado en la primera posición libre; devuelve true si se añadió
    public boolean altaEmpleado(Empleado empleado) {
        if (empleado == null) return false;
        for (int i = 0; i < lista.length; i++) {
            if (lista[i] == null) {
                lista[i] = empleado;
                return true;
            }
        }
        return false; // lista llena
    }

    // Aumenta el salario de todos los empleados registrados
    public void aumentarSalario(double porcentaje) {
        for (Empleado empleado : lista) {
            if (empleado != null) {
                double nuevoSalario = empleado.getSalario() * (1 + porcentaje / 100);
                empleado.setSalario(nuevoSalario);
            }
        }
    }

    // Muestra el listado de empleados (misma salida que antes)
    public void mostrarListado() {
        System.out.println(Mensajes.LISTA_EMPLEADOS);
        for (Empleado empleado : lista) {
            if (empleado != null) {
                System.out.println(empleado);
            }
        }
    }

    // Accesor opcional (sigue disponible)
    public Empleado[] getLista() {
        return lista;
    }
}

class Empleado {
    private String nombre;
    private String puesto;
    private double salario;

    public Empleado(String nombre, String puesto, double salario) {
        this.nombre = nombre;
        this.puesto = puesto;
        this.salario = salario;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getPuesto() {
        return puesto;
    }

    public double getSalario() {
        return salario;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    @Override
    public String toString() {
        return String.format(Mensajes.FORMATO_EMPLEADO, nombre, puesto, salario);
    }
}

/**
 * Clase para gestionar la lista de cargos profesionales válidos.
 * Permite obtener la lista, mostrarla y normalizar / validar un cargo dado.
 */
class Cargos {

    // Devuelve una copia de la lista de cargos
    public String[] getLista() {
        return Mensajes.CARGOS.clone();
    }

    // Muestra los cargos disponibles por pantalla
    public void mostrarCargos() {
        System.out.println(Mensajes.CARGOS_DISPONIBLES);
        for (String c : Mensajes.CARGOS) {
            System.out.println(Mensajes.BULLET + c);
        }
    }

    // Comprueba si un nombre de cargo existe (ignora mayúsculas y espacios alrededor)
    public boolean existeCargo(String nombre) {
        return normalizar(nombre) != null;
    }

    // Devuelve la forma canónica del cargo si existe, o null si no
    public String normalizar(String nombre) {
        if (nombre == null) return null;
        String n = nombre.trim().toLowerCase();
        for (String c : Mensajes.CARGOS) {
            if (c.toLowerCase().equals(n)) {
                return c; // forma canónica
            }
        }
        return null;
    }
}

/**
 * Clase con constantes estáticas (textos y números fijos) para evitar magic strings/numbers.
 */
final class Mensajes {
    // Números / capacidades
    public static final int CAPACIDAD_INICIAL = 3;

    // Nombres por defecto (evitar magic strings en main)
    public static final String NOMBRE_JUAN = "Juan";
    public static final String NOMBRE_MARIA = "María";
    public static final String NOMBRE_PEDRO = "Pedro";

    // Salarios por defecto (evitar magic numbers en main)
    public static final double SALARIO_JUAN = 50000.0;
    public static final double SALARIO_MARIA = 45000.0;
    public static final double SALARIO_PEDRO = 60000.0;

    // Mensajes de interacción
    public static final String PETICION_PORCENTAJE = "Introduzca el porcentaje de aumento de salario: ";
    public static final String ENTRADA_INVALIDA = "Entrada inválida.";
    public static final String LISTA_EMPLEADOS = "Lista de Empleados:";
    public static final String CARGOS_DISPONIBLES = "Cargos disponibles:";
    public static final String BULLET = "- ";

    // Formatos
    public static final String FORMATO_EMPLEADO = "%s - %s - %.2f";

    // Lista canónica de cargos (puede reutilizarse desde Cargos y main)
    public static final String[] CARGOS = {
        "Desarrollador",
        "Diseñadora",
        "Gerente",
        "Analista",
        "Administrativo",
        "Soporte",
        "Recursos Humanos"
    };

    // Evitar instanciación
    private Mensajes() {}
}