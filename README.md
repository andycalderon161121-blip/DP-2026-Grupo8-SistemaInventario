# Sistema de control de inventario — Grupo 8

Aplicación de escritorio (Java Swing) que permite registrar **entradas y salidas** de productos de un inventario y avisa automáticamente cuando un producto queda con **stock bajo**.

Proyecto final del curso **Diseño de Patrones (100000SI47)** — UTP, 2026 Ciclo 2 Agosto.

## Integrantes

| Nombre | Código | Usuario de GitHub |
|---|---|---|
| _Andy Cesar Duque Calderon_ | _U23314767_ | [andycalderon161121-blip] |
| _Chumacero Chuquicusma Jampier Smith_ | _U23330025_ | [chumacerochjampier]|
| _Juliano Josue Cordova Valdiviezo_ | _u24267471_ | [julianojosue]|
| _Andy Abel Rumiche Lalupu_ | _U23206853_ | [andylalupu07-crear]|

## Problema que resuelve

Un negocio pequeño (bodega, minimarket) necesita saber cuánto stock tiene de cada producto y enterarse a tiempo cuando alguno se está acabando. Hacerlo a mano genera errores y quiebres de stock.

El sistema permite registrar entradas y salidas, actualiza la tabla de inventario en el momento y muestra una alerta cuando el stock de un producto cae por debajo de su mínimo. Además, valida que no se registren cantidades inválidas ni salidas mayores al stock disponible.

## Patrones aplicados

### 1. Factory Method (creacional)

| Campo | Detalle |
|---|---|
| Clasificación | Creacional (GoF) |
| Problema | El sistema debe crear movimientos de **entrada** y de **salida**, cada uno con un comportamiento distinto. Si la ventana decidiera qué clase instanciar con `if/else`, tendría que modificarse cada vez que aparezca un tipo de movimiento nuevo. |
| Solución aplicada | `MovimientoFactory` declara el método `crearMovimiento()`; cada fábrica concreta decide qué movimiento crear. La ventana solo trabaja con la abstracción. |

**Clases participantes**

| Rol en el patrón | Clase |
|---|---|
| Creator | `MovimientoFactory` (clase abstracta) |
| ConcreteCreator | `EntradaFactory`, `SalidaFactory` |
| Product | `MovimientoStock` (interfaz) |
| ConcreteProduct | `EntradaStock`, `SalidaStock` |

**Ventajas**
- Agregar un tipo de movimiento (por ejemplo, merma o devolución) solo requiere una clase producto y una fábrica nuevas, sin tocar el código existente.
- La creación de movimientos no se concentra en la ventana.

**Desventajas**
- Aumenta el número de clases (una fábrica por cada tipo de movimiento).

### 2. Observer (comportamiento)

| Campo | Detalle |
|---|---|
| Clasificación | De comportamiento (GoF) |
| Problema | Después de cada movimiento, la tabla de productos y el módulo de alertas deben enterarse del nuevo stock. Si `Inventario` los actualizara directamente, quedaría acoplado a la interfaz gráfica. |
| Solución aplicada | `Inventario` mantiene una lista de `ObservadorInventario` y los notifica tras cada movimiento. Cada observador reacciona a su manera. |

**Clases participantes**

| Rol en el patrón | Clase |
|---|---|
| Subject | `Inventario` (`agregarObservador()`, `notificarObservadores()`) |
| Observer | `ObservadorInventario` (interfaz, método `actualizar()`) |
| ConcreteObserver | `PanelInventarioObserver` (actualiza la tabla), `AlertaStockObserver` (muestra alertas de stock bajo) |

**Ventajas**
- Los módulos se actualizan sin que `Inventario` conozca sus detalles.
- Se pueden agregar observadores nuevos (por ejemplo, un registro en archivo o un correo) sin modificar `Inventario`.

**Desventajas**
- Con muchos observadores, las notificaciones son más difíciles de rastrear.
- Los observadores se notifican en el orden en que se registraron; no hay control de prioridad.

### Cómo trabajan juntos

La ventana pide a una **fábrica** que cree el movimiento (Factory Method) y se lo entrega a `Inventario`. `Inventario` ejecuta el movimiento y luego **notifica** a sus observadores (Observer).

## Comparación antes / después

### Factory Method

**Sin el patrón:** la ventana decide qué clase concreta instanciar.

```java
private void realizarMovimiento(String tipo, int cantidad) {
    MovimientoStock movimiento;
    if (tipo.equals("entrada")) {
        movimiento = new EntradaStock();
    } else if (tipo.equals("salida")) {
        movimiento = new SalidaStock();
    } else {
        return; // cada tipo nuevo obliga a editar este método
    }
    inventario.registrarMovimiento(producto, movimiento, cantidad);
}
```

**Con el patrón:** la ventana solo conoce la abstracción.

```java
private void realizarMovimiento(MovimientoFactory factory, int cantidad) {
    MovimientoStock movimiento = factory.crearMovimiento();
    inventario.registrarMovimiento(producto, movimiento, cantidad);
}
```

### Observer

**Sin el patrón:** `Inventario` conoce y actualiza directamente la interfaz.

```java
public void registrarMovimiento(Producto p, MovimientoStock m, int cantidad) {
    m.ejecutar(p, cantidad);
    actualizarTabla(p);          // acoplado a la tabla Swing
    if (p.tieneStockBajo()) {
        mostrarAlerta(p);        // acoplado al área de alertas
    }
}
```

**Con el patrón:** `Inventario` solo notifica; cada observador decide qué hacer.

```java
public void registrarMovimiento(Producto producto, MovimientoStock movimiento, int cantidad) {
    movimiento.ejecutar(producto, cantidad);
    notificarObservadores(producto, movimiento.getTipo(), cantidad);
}
```

## Principios SOLID aplicados

- **S (Responsabilidad Única):** `Producto` maneja su stock, `Inventario` coordina los movimientos, cada observador atiende una sola reacción y `VentanaPrincipal` solo se ocupa de la interfaz.
- **O (Abierto/Cerrado):** se pueden agregar nuevos movimientos (con su fábrica) y nuevos observadores sin modificar `Inventario`.
- **D (Inversión de Dependencias):** `Inventario` depende de las interfaces `MovimientoStock` y `ObservadorInventario`, no de clases concretas.

## Flujo del programa

1. `Main` inicia la aplicación y abre `VentanaPrincipal`.
2. `VentanaPrincipal` crea el `Inventario` y registra dos observadores: `PanelInventarioObserver` y `AlertaStockObserver`.
3. El usuario elige un producto y pulsa **Registrar entrada** o **Registrar salida**.
4. La ventana crea la fábrica correspondiente (`EntradaFactory` o `SalidaFactory`) y llama a `crearMovimiento()`.
5. `Inventario.registrarMovimiento()` ejecuta el movimiento sobre el `Producto`.
6. `Inventario` notifica a los observadores: la tabla se actualiza y, si el stock quedó bajo el mínimo, aparece una alerta.

## Diagrama de clases

![Diagrama de clases](docs/diagrama-clases.png)

## Estructura del proyecto

```
├── README.md
├── .gitignore
├── build.xml
├── manifest.mf
├── nbproject/
├── docs/
│   ├── diagrama-clases.png
│   ├── diagrama-clases.puml
│   └── presentacion.pdf
└── src/inventario/
    ├── Main.java
    ├── Producto.java
    ├── Inventario.java
    ├── VentanaPrincipal.java
    ├── MovimientoFactory.java
    ├── EntradaFactory.java
    ├── SalidaFactory.java
    ├── MovimientoStock.java
    ├── EntradaStock.java
    ├── SalidaStock.java
    ├── ObservadorInventario.java
    ├── PanelInventarioObserver.java
    └── AlertaStockObserver.java
```

## Cómo compilar y ejecutar

**Requisito:** JDK 8 o superior. No requiere librerías externas.

```bash
git clone https://github.com/andycalderon161121-blip/DP-2026-Grupo8-SistemaInventario
cd DP-2026-Grupo8-SistemaInventario
```

**Opción 1: NetBeans**
1. **File > Open Project** y elegir la carpeta del proyecto.
2. Ejecutar con **Run Project (F6)**.

**Opción 2: terminal**
```bash
mkdir out
javac -encoding UTF-8 -d out src/inventario/*.java
java -cp out inventario.Main
```

> Nota de configuración: en `nbproject/project.properties` se define `dist.javadoc.dir` para evitar el error de Ant `Must set dist.javadoc.dir` al ejecutar desde NetBeans.

## Capturas de pantalla

| Pantalla principal | Entrada registrada | Alerta de stock bajo |
|---|---|---|
| ![Principal](docs/captura-principal.png) | ![Entrada](docs/captura-entrada.png) | ![Alerta](docs/captura-alerta.png) |
