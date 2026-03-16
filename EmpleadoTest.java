import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmpleadoTest {

    private Empleados empleados;
    private static final double CONVENIO = Mensajes.CONVENIO;

    @BeforeEach
    void setUp() {
        empleados = new Empleados(Mensajes.CAPACIDAD_INICIAL);
    }

    // Helper: busca empleado por nombre (trim antes de comparar)
    private Empleado findByName(String nombre) {
        if (nombre == null) return null;
        Empleado[] lista = empleados.getLista();
        if (lista == null) return null;
        for (Empleado e : lista) {
            if (e == null) continue;
            String n = e.getNombre();
            if (n == null) continue;
            if (n.trim().equals(nombre.trim())) return e;
        }
        return null;
    }

    // ---------- Casos válidos ----------

    @Test
    void V1_CasoTipicoValido() {
        boolean ok = empleados.altaEmpleado(new Empleado("Juan Pérez", Mensajes.CARGOS[0], 50_000.00));
        assertTrue(ok, "Alta válida debe devolver true");
        assertNotNull(findByName("Juan Pérez"), "Empleado añadido y encontrado en la lista");
    }

    @Test
    void V2_CargoConDistintoCaseYEspacios() {
        boolean ok = empleados.altaEmpleado(new Empleado("María López", "  desarrollador  ", 45_000.00));
        assertTrue(ok, "Alta válida con cargo con espacios/case debe devolver true");
        assertNotNull(findByName("María López"));
    }

    @Test
    void V3_NombreConAcentosYMuchosEspacios() {
        String nombre = "  Ana   María Torres  ";
        boolean ok = empleados.altaEmpleado(new Empleado(nombre, "Analista", 30_000.00));
        assertTrue(ok, "Alta válida con nombre con espacios/acento debe devolver true");
        assertNotNull(findByName(nombre));
    }

    @Test
    void V4_NombreLimiteMinimoPalabras() {
        boolean ok = empleados.altaEmpleado(new Empleado("A B", "Soporte", CONVENIO));
        assertTrue(ok, "Alta válida en límite mínimo de palabras debe devolver true");
        assertNotNull(findByName("A B"));
    }

    @Test
    void V5_SalarioDecimalPequenoPeroMayorOIgualConvenio() {
        boolean ok = empleados.altaEmpleado(new Empleado("Luis Ruiz", "Administrativo", 20_500.50));
        assertTrue(ok, "Alta válida con salario decimal debe devolver true");
        Empleado e = findByName("Luis Ruiz");
        assertNotNull(e);
        assertEquals(20_500.50, e.getSalario(), 0.0001);
    }

    // ---------- Casos no válidos ----------

    @Test
    void N1_NombreVacio() {
        boolean ok = empleados.altaEmpleado(new Empleado("   ", Mensajes.CARGOS[0], 50_000.00));
        assertFalse(ok, "Alta con nombre vacío debe rechazarse");
        assertNull(findByName("   "));
    }

    @Test
    void N2_NombreUnaSolaPalabra() {
        boolean ok = empleados.altaEmpleado(new Empleado("Juan", Mensajes.CARGOS[0], 50_000.00));
        assertFalse(ok, "Alta con nombre de una sola palabra debe rechazarse");
        assertNull(findByName("Juan"));
    }

    @Test
    void N3_NombreNulo() {
        boolean ok = empleados.altaEmpleado(new Empleado(null, "Gerente", 60_000.00));
        assertFalse(ok, "Alta con nombre nulo debe rechazarse");
        assertNull(findByName(null));
    }

    @Test
    void N4_CargoNoListado() {
        boolean ok = empleados.altaEmpleado(new Empleado("María López", "DevOps", 45_000.00));
        assertFalse(ok, "Alta con cargo no listado debe rechazarse");
        assertNull(findByName("María López"));
    }

    @Test
    void N5_CargoVacio() {
        boolean ok = empleados.altaEmpleado(new Empleado("María López", "", 45_000.00));
        assertFalse(ok, "Alta con cargo vacío debe rechazarse");
        assertNull(findByName("María López"));
    }

    @Test
    void N6_SalarioNaN() {
        boolean ok = empleados.altaEmpleado(new Empleado("Pedro Gómez", "Gerente", Double.NaN));
        assertFalse(ok, "Alta con salario NaN debe rechazarse");
        assertNull(findByName("Pedro Gómez"));
    }

    @Test
    void N7_SalarioNegativo() {
        boolean ok = empleados.altaEmpleado(new Empleado("Pedro Gómez", "Gerente", -100.00));
        assertFalse(ok, "Alta con salario negativo debe rechazarse");
        assertNull(findByName("Pedro Gómez"));
    }

    @Test
    void N8_SalarioPorDebajoConvenio() {
        boolean ok = empleados.altaEmpleado(new Empleado("Pedro Gómez", "Gerente", CONVENIO - 0.01));
        assertFalse(ok, "Alta con salario por debajo de convenio debe rechazarse");
        assertNull(findByName("Pedro Gómez"));
    }

    @Test
    void N9_MultiplesErroresCombinados() {
        boolean ok = empleados.altaEmpleado(new Empleado("Solo", "DevOps", -10.0));
        assertFalse(ok, "Alta con múltiples errores debe rechazarse");
        assertNull(findByName("Solo"));
    }

    @Test
    void N10_SalarioCeroSiConvenioMayorQueCero() {
        boolean ok = empleados.altaEmpleado(new Empleado("Ana Torres", "Analista", 0.0));
        assertFalse(ok, "Alta con salario 0 cuando convenio>0 debe rechazarse");
        assertNull(findByName("Ana Torres"));
    }
}