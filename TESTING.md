# TESTING — Análisis de caja negra para clase Empleado

Objetivo  
Verificar por caja negra que la creación / alta y uso de Empleado sólo acepte datos válidos según los criterios definidos para cada atributo (nombre, puesto, salario).

## Criterios de aceptación
- Nombre
  - No nulo y no vacío tras trim().
  - Debe contener al menos dos palabras. Criterio formal (regex): ^\s*\S+(\s+\S+)+\s*$

- Puesto
  - Debe corresponder a un cargo válido listado en `Mensajes.CARGOS`. Comparación case‑insensitive y con trim.

- Salario
  - Número finito mayor que 0.00.
  - Rechazar NaN/Infinity.
  - (Opcional) Tope superior razonable, p. ej. <= 1_000_000.00 — decidir si aplica.

---

## Tabla 1 — Análisis de parámetros

| Atributo | Dominio | Criterio de aceptación | Clases de equivalencia | Valores límite / frontera |
|---|---:|---|---|---|
| nombre | Cadenas Unicode | No nulo, trim()!= "", >= 2 palabras (regex) | Válido: 2+ palabras; Inválido: nulo, vacío, 1 palabra, solo espacios, solo no imprimibles | Límite aceptado: "A B" ; Límite rechazado: "Solo" |
| puesto | Cadenas representando cargos | Coincidir (trim, case‑insensitive) con `Mensajes.CARGOS` | Válido: exacto/variant case/espacios; Inválido: no listado, vacío, nulo | Límite aceptado: "desarrollador" / "  Desarrollador  " |
| salario | double | Finito, > 0.00 (y >= convenio si aplica) | Válido: >0 (0.01, 15000.50); Inválido: 0, negativo, NaN, Infinity, >tope (si existe) | Límite aceptado: 0.01 ; Si convenio: 15_000.00 aceptado, 14_999.99 rechazado |

---

## Tabla 2 — Casos válidos (deben ser aceptados)

| ID | Descripción | Entrada (nombre, puesto, salario) | Resultado esperado |
|---:|---|---|---|
| TC01 | Nombre válido, puesto válido, salario válido | ("Juan Pérez", "Desarrollador", 50_000.0) | Aceptar — `altaEmpleado` devuelve true; empleado añadido |
| TC04 | Nombre con dos palabras mínimas | ("A B", "Analista", 1_000.0) | Aceptar — límite inferior de palabras |
| TC06 | Puesto con distinto case / espacios | ("María López", " desarrollador ", 45_000.0) | Aceptar — normaliza puesto y añade |
| TC11 | Salario decimal pequeño positivo | ("Luis Ruiz", "Soporte", 0.01) | Aceptar — salario > 0 |
| TC13 | Nombre con múltiples espacios internos | ("  Ana   María Torres  ", "Administrativo", 30_000.0) | Aceptar — trim/norm espacios |
| TC14 | Nombre con acentos y caracteres especiales | ("José Ángel", "Analista", 32_000.0) | Aceptar — UTF‑8 y acentos soportados |
| TC12* | Salario extremadamente grande | ("CEO Max", "Gerente", 10_000_000_000.0) | Aceptar o rechazar según política de tope; documentar resultado |

\*TC12 depende de la política sobre tope superior (recomendar: documentar).

---

## Tabla 3 — Casos no válidos (deben ser rechazados)

| ID | Descripción | Entrada (nombre, puesto, salario) | Resultado esperado (rechazo) |
|---:|---|---|---|
| TC02 | Nombre 1 palabra | ("Juan", "Desarrollador", 50_000.0) | Rechazar — no cumple 2+ palabras |
| TC03 | Nombre vacío | ("   ", "Desarrollador", 50_000.0) | Rechazar — nombre vacío |
| TC05 | Nombre nulo | (null, "Gerente", 60_000.0) | Rechazar — nombre nulo |
| TC07 | Puesto no listado | ("María López", "DevOps", 45_000.0) | Rechazar — puesto inválido |
| TC08 | Puesto vacío | ("María López", "", 45_000.0) | Rechazar — puesto vacío |
| TC09 | Salario = 0 | ("Pedro Gómez", "Gerente", 0.0) | Rechazar — salario no > 0 |
| TC10 | Salario negativo | ("Pedro Gómez", "Gerente", -100.0) | Rechazar — salario negativo |
| TC06b | Salario NaN/Infinity | ("Pedro Gómez", "Gerente", NaN) | Rechazar — valor no finito |
| TC15 | Múltiples errores | ("Juan", "DevOps", -10.0) | Rechazar — informar de todos los errores detectados |
| TC12b | Salario extremadamente grande (si hay tope) | ("CEO Max", "Gerente", 10_000_000_000.0) | Rechazar si supera tope documentado |

---

## Criterios de aceptación global para un caso de prueba
- Pass: todas las validaciones devuelven el resultado esperado (aceptar/rechazar) y el estado del sistema coincide (empleado añadido/no añadido). Si procede, comprobar mensaje de error y contenido de `Empleados.lista`.
- Fail: se acepta entrada inválida o se rechaza entrada válida.

---

## Procedimiento de ejecución de pruebas (recomendado)
1. Preparar entorno con JUnit (o driver de pruebas).  
2. Para cada TC:
   - Ejecutar `altaEmpleado(...)` con la entrada definida.
   - Comprobar retorno booleano (true = aceptado, false = rechazado).
   - Verificar `Empleados.lista` para presencia/ausencia del empleado.
   - Verificar mensajes de error (si la implementación los expone, usar constantes `Mensajes.*`).
3. Registrar resultados y comparar con expectativas.

---

## Observaciones y recomendaciones
- Implementar validaciones en `altaEmpleado` antes de insertar (nombre, puesto, salario).
- Usar `Cargos.normalizar(...)` para validar/normalizar el puesto.
- Definir política sobre tope máximo salarial y documentarla en TESTING.
- Mensajes de error claros y reutilizar constantes de `Mensajes`.
- Añadir tests JUnit automatizados que cubran los TC01–TC15, incluyendo combinaciones y fronteras.
- Verificar codificación UTF‑8 para nombres con acentos.
- Mantener consistentemente la lógica de validación entre producción y tests (evitar duplicación de lógica).

