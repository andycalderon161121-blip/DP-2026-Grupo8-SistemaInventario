# Sistema de control de inventario - Grupo 8

Aplicación de escritorio Java Swing para registrar entradas y salidas de productos y detectar existencias bajas.

## Patrones GoF aplicados

### Factory Method (creacional)
- Problema: el sistema necesita crear movimientos de entrada y salida con comportamientos diferentes.
- Solución: `MovimientoFactory` declara la creación; `EntradaFactory` y `SalidaFactory` crean `EntradaStock` y `SalidaStock`.
- Participantes: `MovimientoFactory`, `EntradaFactory`, `SalidaFactory`, `MovimientoStock`, `EntradaStock`, `SalidaStock`.
- Ventaja: se pueden agregar nuevos tipos de movimiento sin concentrar toda la creación en la ventana.
- Desventaja: incorpora clases adicionales.

### Observer (comportamiento)
- Problema: después de cada movimiento, la tabla y las alertas deben enterarse del nuevo stock.
- Solución: `Inventario` notifica a los objetos que implementan `ObservadorInventario`.
- Participantes: `Inventario` (sujeto), `ObservadorInventario` (observador), `PanelInventarioObserver` y `AlertaStockObserver` (observadores concretos).
- Ventaja: los módulos se actualizan sin que Inventario conozca sus detalles.
- Desventaja: si hay muchos observadores, las notificaciones pueden ser más difíciles de rastrear.

## Ejecutar en NetBeans
1. Descomprimir el proyecto.
2. Abrir NetBeans y seleccionar **File > Open Project**.
3. Elegir la carpeta `DP-2026-Grupo8-SistemaInventario`.
4. Ejecutar el proyecto con **Run Project (F6)**.

No requiere instalar librerías externas.


## Nota de configuración de NetBeans
Se define `dist.javadoc.dir` en `nbproject/project.properties` para evitar el error de Ant `Must set dist.javadoc.dir` al ejecutar el proyecto.
