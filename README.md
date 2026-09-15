# TopPGSQL

**TopPGSQL** es una aplicación de escritorio Java/JavaFX orientada a la monitorización y diagnóstico de bases de datos **PostgreSQL**.

La aplicación permite observar sesiones activas, consumo de CPU e I/O, consultar históricos de actividad, analizar planes de ejecución mediante `EXPLAIN`, probar índices hipotéticos con **HypoPG**, detectar árboles de bloqueos y obtener sugerencias de índices a partir de las estadísticas de PostgreSQL.

> **Estado del proyecto:** esta documentación está basada en el código fuente incluido en `source.zip`. El archivo proporcionado contiene el código Java, FXML, CSS y scripts SQL, pero no contiene un `pom.xml`, `build.gradle`, instalador ni una documentación previa. Por lo tanto, la compilación debe realizarse dentro del entorno Java/Speco en el que están disponibles las clases `speco.cat.*` utilizadas por el proyecto.

---

## 1. Funcionalidades

TopPGSQL dispone de las siguientes funciones principales:

| Función | Descripción |
|---|---|
| **Monitor de actividad** | Muestra las sesiones activas de PostgreSQL. |
| **CPU / I/O** | Grafica una ventana de muestras recientes de CPU e I/O. |
| **Detalle de sesión** | Permite abrir el detalle de una sesión mediante doble clic. |
| **Kill** | Envía una terminación de backend al PID seleccionado. |
| **ASH / History** | Consulta el histórico de sesiones almacenado en `pg_stat_activity_history`. |
| **Explain** | Ejecuta `EXPLAIN (FORMAT JSON)` sobre una consulta. |
| **HypoPG** | Permite crear temporalmente un índice hipotético y volver a calcular el plan. |
| **Locks** | Muestra el árbol recursivo de bloqueos. |
| **Advisor** | Busca tablas con una proporción significativa de `Seq Scan` y asocia consultas candidatas. |
| **DDL sugerido** | Genera un `CREATE INDEX CONCURRENTLY` de referencia que debe completarse manualmente con las columnas adecuadas. |

---

## 2. Arquitectura

La aplicación está organizada principalmente en:

```text
speco.toppgsql
├── TopPGSQL.java
├── FXMLTopPgSqlController.java
├── FXMLHistoryController.java
├── FXMLExplainController.java
├── FXMLExplainHistoryController.java
├── FXMLPgActivityFullController.java
├── FXMLLockController.java
├── FXMLIndexAdvisorController.java
├── ReadProperties.java
├── PgBases.java
├── *.fxml
├── estilos.css
└── om/
    ├── ModelPg.java
    ├── ModelPgStatActivityHistory.java
    ├── ModelIndexAdvisor.java
    ├── ModelVLockRecursive.java
    ├── PgActivity.java
    ├── PgActivityFull.java
    ├── PgStatActivityHistory.java
    ├── VIndexAdvisor.java
    ├── VLockRecursive.java
    ├── VwaitEvents.java
    ├── VcpuTime.java
    ├── Pghypopg.java
    └── KillSession.java
```

La aplicación utiliza:

- Java.
- JavaFX.
- PostgreSQL.
- Clases de acceso a datos del paquete `speco.cat`.
- JDBC a través de `speco.cat.Tx`.
- `pg_stat_activity`.
- `pg_stat_statements`.
- `pg_stat_kcache`.
- HypoPG.
- pg_cron para la captura periódica del histórico.

---

# 3. Requisitos

## 3.1. Cliente

Se necesita un entorno Java compatible con la versión de JavaFX utilizada por el proyecto.

El código incluido utiliza:

- `javafx.application.Application`
- JavaFX FXML
- JavaFX Controls
- JavaFX Charts

Algunos FXML declaran JavaFX 25 y otros mantienen el namespace histórico de JavaFX 8. Se recomienda utilizar una versión de JavaFX coherente con el JDK instalado.

## 3.2. PostgreSQL

La base monitorizada debe ser PostgreSQL.

Para disponer de todas las funciones de TopPGSQL se recomienda instalar/configurar:

- `pg_stat_statements`
- `pg_stat_kcache`
- `hypopg`
- `pg_cron`

No todas las funciones de PostgreSQL son necesarias para arrancar el monitor básico, pero las funciones avanzadas dependen de estas extensiones.

---

# 4. Extensiones PostgreSQL

Antes de ejecutar los scripts del proyecto, comprobar qué extensiones están disponibles:

```sql
SELECT name, default_version, installed_version
FROM pg_available_extensions
WHERE name IN (
    'pg_stat_statements',
    'pg_stat_kcache',
    'hypopg',
    'pg_cron'
)
ORDER BY name;
```

Las extensiones que se utilicen deben instalarse en la base correspondiente, por ejemplo:

```sql
CREATE EXTENSION IF NOT EXISTS pg_stat_statements;
CREATE EXTENSION IF NOT EXISTS pg_stat_kcache;
CREATE EXTENSION IF NOT EXISTS hypopg;
```

Para `pg_cron`, además de la extensión, PostgreSQL debe estar configurado para cargarla como extensión compartida y disponer del servicio de background worker correspondiente.

La configuración exacta de `pg_cron` depende de la distribución de PostgreSQL utilizada.

---

# 5. Configuración de PostgreSQL

## 5.1. pg_stat_statements

TopPGSQL utiliza `pg_stat_statements` principalmente en:

- Index Advisor.
- Asociación de consultas con tablas.
- Histórico de actividad.

Normalmente es necesario incluir:

```text
shared_preload_libraries = 'pg_stat_statements'
```

Si se utilizan varias extensiones que requieren `shared_preload_libraries`, deben aparecer juntas, por ejemplo:

```text
shared_preload_libraries = 'pg_stat_statements,pg_stat_kcache,pg_cron'
```

La sintaxis exacta puede variar según la instalación.

Después de modificar `postgresql.conf`, reiniciar PostgreSQL.

Comprobar:

```sql
SHOW shared_preload_libraries;
```

Y:

```sql
SELECT *
FROM pg_stat_statements
LIMIT 1;
```

---

# 6. Instalación de los objetos SQL

Los scripts incluidos en el proyecto están organizados de la siguiente manera:

```text
sql/
├── IndexAdvisor.sql
├── IndexAdvisorSinrestriccion.sql
├── explain.sql
├── cron.sql
├── lock.sql
├── stat/
│   └── statistics.sql
└── logger/
    ├── 00create_view_col.sql
    ├── 01create_logger_tbl.sql
    ├── 02_pkg_prod_insert_logger.sql
    ├── 03_pkg_old_new_vs.sql
    ├── 04_pkg_create_trg_insert.sql
    ├── 04_pkg_check.sql
    ├── 05_pkg_insert_tabla.sql
    ├── readme
    └── test.sql
```

## 6.1. Objetos principales

Los scripts crean o utilizan, entre otros, estos objetos:

```text
v_index_advisor
v_lock_recursive
vcpu_time
vwait_events
explain(sql text)
```

Además, el histórico utiliza:

```text
pg_stat_activity_history
```

Esta tabla debe existir antes de ejecutar el job de `cron.sql`.

---

# 7. Instalación recomendada de los objetos

Ejecutar los scripts en la base que se desea monitorizar.

Por ejemplo:

```bash
psql -h HOST -p 5432 -U USUARIO -d BASE -f sql/stat/statistics.sql
psql -h HOST -p 5432 -U USUARIO -d BASE -f sql/explain.sql
psql -h HOST -p 5432 -U USUARIO -d BASE -f sql/lock.sql
psql -h HOST -p 5432 -U USUARIO -d BASE -f sql/IndexAdvisor.sql
```

**Importante:** el orden puede necesitar adaptarse a la instalación, especialmente si todavía no existe la tabla de histórico o alguna extensión requerida.

Antes de ejecutar:

```sql
SELECT current_database();
```

para confirmar que se está trabajando sobre la base correcta.

---

# 8. Tabla de histórico

La función **ASH / History** depende de una tabla llamada:

```text
pg_stat_activity_history
```

El script `cron.sql` inserta periódicamente información en esta tabla.

El código espera columnas como:

```text
snapshot_time
pid
usename
datname
state
wait_event_type
wait_event
cpu_user_seconds
cpu_system_seconds
io_reads_bytes
io_writes_bytes
shared_blks_hit
shared_blks_read
shared_blks_dirtied
temp_blks_read
temp_blks_written
query_id
query
```

El proyecto incluido no contiene en los scripts principales una sentencia `CREATE TABLE pg_stat_activity_history`. Por lo tanto, **hay que crear esa tabla en el entorno de instalación si todavía no existe**.

Una definición concreta debe ajustarse a la versión de PostgreSQL y a las necesidades de retención de cada instalación.

---

# 9. Configuración de conexiones

TopPGSQL no tiene las conexiones escritas directamente en el código principal.

`ReadProperties` obtiene el nombre del archivo desde:

```java
System.getProperty("cat.file")
```

Por lo tanto, la aplicación debe arrancarse proporcionando:

```text
-Dcat.file=/ruta/al/archivo.properties
```

## 9.1. Archivo de propiedades

El código espera una estructura basada en:

```properties
pool=2

url_1=jdbc:postgresql://localhost:5432/postgres
user_1=postgres
rdbms_1=postgresql
nombre_1=postgres

url_2=jdbc:postgresql://servidor:5432/produccion
user_2=monitor
rdbms_2=postgresql
nombre_2=produccion
```

### Propiedades

| Propiedad | Significado |
|---|---|
| `pool` | Cantidad de conexiones/bases configuradas. |
| `url_N` | URL JDBC de la conexión N. |
| `user_N` | Usuario de PostgreSQL. |
| `rdbms_N` | Identificador del motor, normalmente `postgresql`. |
| `nombre_N` | Nombre mostrado en el selector de bases. |

El código utiliza `nombre_N` como identificador para construir el objeto `Tx`, por lo que **el significado exacto de `nombre_N` depende de la implementación de `speco.cat.Tx`**.

---

# 10. Ejemplo de arranque

El principio de ejecución es:

```bash
java -Dcat.file=/etc/toppgsql/toppgsql.properties \
     ... \
     speco.toppgsql.TopPGSQL
```

Los argumentos concretos de módulos JavaFX y classpath dependen de cómo se haya construido el proyecto.

---

# 11. Pantalla principal

Al iniciar TopPGSQL aparece la ventana:

```text
SQL command Center for Postgresql
```

La pantalla principal contiene:

```text
┌─────────────────────────────────────────────────────────────┐
│ Timeout │ Base │ Kill │ PID │ ASH │ Explain │ Locks │ Advisor│
├─────────────────────────────────────────────────────────────┤
│                                                             │
│                  Sesiones PostgreSQL                        │
│                                                             │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│                  Gráfico CPU / I/O                          │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

# 12. Timeout

El campo:

```text
Timeout
```

define el intervalo de actualización.

El valor inicial configurado por el programa es:

```text
5
```

El código interpreta este valor como segundos.

Ejemplo:

```text
5
```

significa aproximadamente:

```text
actualizar cada 5 segundos
```

La actualización se realiza mediante un hilo que consulta las actividades y un `Timeline` de JavaFX que actualiza el gráfico.

> Se recomienda utilizar valores razonables en servidores de producción. Un intervalo demasiado pequeño aumenta la carga sobre PostgreSQL y sobre la propia aplicación.

---

# 13. Selector de base

El `ComboBox`:

```text
Base
```

se carga desde el archivo de propiedades.

Cada entrada representa una conexión configurada mediante:

```properties
url_N
user_N
rdbms_N
nombre_N
```

Al cambiar la selección, la aplicación utiliza la nueva base para las consultas de monitorización.

---

# 14. Tabla de actividad

La tabla principal muestra las sesiones activas de PostgreSQL.

Las columnas son:

| Columna | Significado |
|---|---|
| `pid` | PID del backend PostgreSQL. |
| `user` | Usuario de PostgreSQL. |
| `wet` | Tipo de evento de espera (`wait_event_type`). |
| `we` | Evento de espera (`wait_event`). |
| `state` | Estado de la sesión. |
| `cpu` | Métrica de CPU obtenida por el modelo de actividad. |
| `ioread` | Lecturas de I/O. |
| `iowrite` | Escrituras de I/O. |
| `query` | Consulta asociada a la sesión. |

Los datos proceden principalmente de `pg_stat_activity` y de información adicional utilizada por el modelo `PgActivity`.

---

# 15. Ver detalle de una sesión

Para consultar el detalle de una sesión:

1. Seleccionar una fila de la tabla.
2. Hacer **doble clic** sobre la fila.

Se abre una ventana:

```text
Activity
```

El detalle muestra información como:

- dirección del cliente;
- hostname;
- puerto;
- usuario;
- PID;
- nombre de aplicación;
- comienzo de la consulta;
- tipo de espera;
- evento de espera;
- estado;
- consulta SQL;
- plan `EXPLAIN`.

---

# 16. Selección del PID

Cuando se selecciona una fila de la tabla principal, el PID se copia al campo:

```text
PID
```

También puede utilizarse el campo manualmente.

Ejemplo:

```text
PID: 18452
```

---

# 17. Kill

El botón:

```text
Kill
```

actúa sobre el PID seleccionado.

### Atención

La interfaz utiliza términos como:

```text
Kill
Cancelar PID
```

y en una pantalla de Locks el mensaje de confirmación habla de:

```text
pg_cancel_backend()
```

Sin embargo, la implementación actual de `KillSession.java` ejecuta:

```sql
pg_terminate_backend(?)
```

Por lo tanto, **la operación implementada actualmente es `pg_terminate_backend`, no `pg_cancel_backend`**.

Esto es importante en producción:

- `pg_cancel_backend(pid)` solicita cancelar la consulta actual.
- `pg_terminate_backend(pid)` termina la sesión/backend.

Se recomienda verificar este comportamiento antes de utilizar el botón sobre sesiones críticas.

---

# 18. ASH / Active Session History

El botón:

```text
ASH
```

abre:

```text
History - Active Session History
```

La ventana permite consultar la información almacenada en:

```text
pg_stat_activity_history
```

Muestra:

- timestamp;
- PID;
- usuario;
- wait event type;
- wait event;
- estado;
- CPU;
- I/O de lectura;
- I/O de escritura;
- consulta.

---

# 19. Consulta por fecha

En la ventana de histórico aparecen:

```text
fecha desde:
fecha hasta:
buscar
```

Las fechas se convierten mediante:

```java
Timestamp.valueOf(...)
```

Por ello deben utilizar el formato aceptado por `java.sql.Timestamp`.

Ejemplo:

```text
2026-09-15 10:00:00
```

y:

```text
2026-09-15 11:00:00
```

El código consulta:

```sql
snapshot_time > ?
AND snapshot_time <= ?
```

Por lo tanto, el límite inferior es exclusivo y el superior es inclusivo.

---

# 20. Gráfico del histórico

El histórico incluye un gráfico:

```text
Rendimiento del Sistema (CPU vs Tiempo)
```

El código crea una serie denominada:

```text
CPU Usage
```

y acumula el valor de CPU mientras recorre los registros recuperados.

> El gráfico actual no constituye una métrica CPU porcentual normalizada; debe interpretarse de acuerdo con la forma en que se almacenan los valores en `pg_stat_activity_history`.

---

# 21. Explain

El botón:

```text
Explain
```

abre:

```text
Explain
```

La ventana contiene:

```text
[ hypopg ]

[ Índice hipotético ]

[ Consulta SQL ]

[ Resultado EXPLAIN ]
```

La consulta se envía a la función PostgreSQL:

```sql
explain(sql text)
```

que fue definida en:

```text
sql/explain.sql
```

---

# 22. Función explain()

La función devuelve:

```sql
EXPLAIN (FORMAT JSON)
```

como texto.

Para consultas que contienen parámetros `$1`, `$2`, etc., el código intenta crear un `PREPARE` con parámetros `unknown` y obtener el plan utilizando:

```sql
SET plan_cache_mode = force_generic_plan
```

Esto permite analizar determinados casos de consultas parametrizadas.

---

# 23. Uso de HypoPG

HypoPG permite probar un índice hipotético sin crear físicamente el índice.

En la ventana Explain:

1. Introducir una sentencia de índice en el campo **hypopg**.
2. Introducir la consulta SQL.
3. Ejecutar el botón `hypopg`.
4. La aplicación crea el índice hipotético.
5. Ejecuta nuevamente `EXPLAIN`.
6. Finalmente ejecuta:

```sql
hypopg_reset()
```

para eliminar los índices hipotéticos de la sesión.

Ejemplo conceptual:

```sql
CREATE INDEX ON public.clientes (apellido)
```

El índice es hipotético; no se crea físicamente en la tabla.

---

# 24. Advertencia sobre HypoPG

El código construye la llamada a HypoPG concatenando directamente el contenido introducido por el usuario:

```java
hypopg_create_index('...')
```

Por ello debe utilizarse únicamente con SQL controlado y correctamente formado.

No se recomienda copiar consultas o entradas no confiables directamente en este campo.

---

# 25. Explain desde el histórico

En:

```text
ASH → doble clic sobre una actividad
```

se abre:

```text
Activity
```

La aplicación muestra:

- usuario;
- PID;
- wait event type;
- wait event;
- consulta histórica;
- plan EXPLAIN.

También dispone del campo para introducir un índice hipotético y probar nuevamente el plan mediante HypoPG.

---

# 26. Locks

El botón:

```text
Locks
```

abre:

```text
Locks Tree
```

Esta pantalla consulta:

```text
v_lock_recursive
```

creada por:

```text
sql/lock.sql
```

---

# 27. Árbol de bloqueos

El árbol permite identificar relaciones:

```text
PID bloqueador
      │
      └─ PID bloqueado
             │
             └─ PID bloqueado
```

La vista utiliza una consulta recursiva (`WITH RECURSIVE`) para seguir cadenas de bloqueos.

Columnas:

| Columna | Descripción |
|---|---|
| Árbol Visual | Representación jerárquica. |
| PID Blocker | PID que mantiene el bloqueo. |
| PID Blocked | PID que espera. |
| Usuario Blocker | Usuario de la sesión bloqueadora. |
| Usuario Blocked | Usuario de la sesión bloqueada. |
| Tipo Espera | `wait_event_type`. |
| Evento Espera | `wait_event`. |
| Tiempo (s) | Tiempo calculado desde el cambio de estado. |
| Consulta Bloqueadora | SQL de la sesión bloqueadora. |
| Consulta Bloqueada | SQL de la sesión bloqueada. |

---

# 28. Refrescar Locks

El botón:

```text
Refrescar
```

vuelve a consultar:

```text
v_lock_recursive
```

Esto permite verificar si un bloqueo continúa activo.

---

# 29. Cancelar un bloqueo

Para cancelar un proceso desde Locks:

1. Seleccionar una fila.
2. Pulsar:
   ```text
   Cancelar PID
   ```
3. Confirmar la operación.

La aplicación obtiene:

```text
blocking_pid
```

y llama a `KillSession`.

### Importante

Como se explicó anteriormente, el código de `KillSession` utiliza actualmente:

```sql
pg_terminate_backend(pid)
```

No asumir que el botón solamente cancela la consulta.

---

# 30. Index Advisor

El botón:

```text
Advisor
```

abre:

```text
Index Advisor
```

La pantalla consulta:

```text
v_index_advisor
```

creada mediante:

```text
sql/IndexAdvisor.sql
```

---

# 31. Cómo funciona Index Advisor

El Advisor analiza estadísticas de:

```text
pg_stat_user_tables
```

y las relaciona con:

```text
pg_stat_statements
```

La versión principal de `IndexAdvisor.sql` considera como candidatas las tablas que:

- tienen más de 25 operaciones combinadas de scan;
- presentan más de un 10 % de scans secuenciales.

Después busca consultas de `pg_stat_statements` relacionadas con la tabla.

Finalmente selecciona la consulta con mayor:

```text
mean_exec_time
```

por tabla.

---

# 32. Datos mostrados por Index Advisor

La pantalla muestra:

| Campo | Descripción |
|---|---|
| Tabla / Esquema | Tabla detectada. |
| Tamaño | Tamaño de la relación. |
| Seq Scans | Número de scans secuenciales. |
| Seq Scan | Porcentaje estimado de scans secuenciales. |
| Tiempo Medio (ms) | Tiempo medio de ejecución de la consulta candidata. |
| Consulta Frecuente Candidate | Consulta relacionada encontrada. |
| Sugerencia DDL | DDL de referencia para un posible índice. |

---

# 33. DDL sugerido

El Advisor genera una sentencia de este estilo:

```sql
CREATE INDEX CONCURRENTLY idx_clientes_advisor
ON public.clientes (/* columna_filtro */);
```

### Muy importante

La aplicación **no conoce automáticamente la columna que debe indexarse**.

El comentario:

```sql
/* columna_filtro */
```

es un marcador.

Por ejemplo, si el análisis demuestra que una consulta utiliza:

```sql
WHERE apellido = 'Gomez'
```

podría evaluarse:

```sql
CREATE INDEX CONCURRENTLY idx_clientes_apellido_advisor
ON public.clientes (apellido);
```

Pero la columna correcta debe determinarse examinando:

- `WHERE`;
- `JOIN`;
- `ORDER BY`;
- selectividad;
- cardinalidad;
- índices existentes;
- plan de ejecución.

No ejecutar automáticamente todas las sugerencias.

---

# 34. Copiar DDL

En Index Advisor:

1. Seleccionar una fila.
2. Pulsar:
   ```text
   Copiar DDL Sugerido
   ```
3. El DDL se copia al portapapeles.

Luego puede pegarse en `psql`, pgAdmin u otra herramienta.

---

# 35. Dos variantes de Index Advisor

El proyecto contiene:

```text
IndexAdvisor.sql
```

y:

```text
IndexAdvisorSinrestriccion.sql
```

## IndexAdvisor.sql

Es la versión más restrictiva:

```text
(seq_scan + idx_scan) > 25
```

y:

```text
seq_scan / (seq_scan + idx_scan) > 10%
```

## IndexAdvisorSinrestriccion.sql

Es más amplia y considera:

```text
seq_scan > 0
```

Además utiliza `LEFT JOIN` con `pg_stat_statements`.

Esto permite detectar tablas aunque no exista una consulta correspondiente en `pg_stat_statements`.

### Recomendación

Para producción, comenzar con:

```text
IndexAdvisor.sql
```

y utilizar la versión sin restricción para investigaciones más amplias.

---

# 36. Vistas estadísticas

El archivo:

```text
sql/stat/statistics.sql
```

crea:

## vcpu_time

Agrupa sesiones activas por:

```text
wait_event_type
```

y devuelve:

```text
tipo_espera
total_sesiones
```

## vwait_events

Muestra sesiones no idle:

```text
pid
usuario
base_datos
estado
wait_event_type
wait_event
consulta_actual
duracion
```

Estas vistas son parte de la infraestructura de monitorización del proyecto.

---

# 37. Captura automática mediante pg_cron

El archivo:

```text
sql/cron.sql
```

programa un job:

```text
snapshot_pg_stat_history
```

con:

```text
* * * * *
```

Es decir, una ejecución cada minuto.

El job inserta información de las sesiones activas en:

```text
pg_stat_activity_history
```

y utiliza:

```text
pg_stat_activity
pg_stat_statements
pg_stat_kcache_detail
```

para recopilar actividad, CPU e I/O.

---

# 38. Verificar el job de pg_cron

Después de instalarlo, comprobar:

```sql
SELECT *
FROM cron.job;
```

Y las ejecuciones:

```sql
SELECT *
FROM cron.job_run_details
ORDER BY start_time DESC
LIMIT 20;
```

Si no aparecen ejecuciones correctas, revisar la configuración de `pg_cron`, permisos y conexión de la base.

---

# 39. Verificar la captura histórica

Comprobar:

```sql
SELECT COUNT(*)
FROM pg_stat_activity_history;
```

Y:

```sql
SELECT *
FROM pg_stat_activity_history
ORDER BY snapshot_time DESC
LIMIT 10;
```

Si no aparecen datos, revisar:

1. existencia de la tabla;
2. existencia de `pg_stat_kcache`;
3. existencia de `pg_stat_statements`;
4. configuración de `pg_cron`;
5. permisos del usuario que ejecuta el job.

---

# 40. Permisos

TopPGSQL consulta información sensible de actividad de PostgreSQL.

Dependiendo de la versión de PostgreSQL y del usuario utilizado, puede ser necesario conceder permisos adicionales.

Para monitorización se recomienda utilizar un usuario específico con los privilegios mínimos necesarios.

En particular, las operaciones administrativas como:

```sql
pg_cancel_backend(...)
pg_terminate_backend(...)
```

deben reservarse a usuarios autorizados.

**No se recomienda ejecutar la aplicación con el superusuario `postgres` salvo que exista una razón administrativa concreta.**

---

# 41. Seguridad

TopPGSQL muestra:

- consultas SQL;
- usuarios;
- PID;
- aplicaciones;
- direcciones de clientes;
- información de espera;
- actividad de sesiones.

Por lo tanto, debe considerarse una herramienta administrativa.

Recomendaciones:

- utilizar conexiones TLS cuando corresponda;
- limitar el acceso de red a PostgreSQL;
- utilizar un usuario dedicado;
- evitar almacenar contraseñas en texto plano;
- no exponer el puerto PostgreSQL públicamente;
- restringir quién puede ejecutar la aplicación;
- auditar el uso de `Kill`.

---

# 42. Flujo recomendado de diagnóstico

Un flujo práctico para investigar una degradación de rendimiento es:

```text
1. Seleccionar la base
          ↓
2. Revisar sesiones activas
          ↓
3. Identificar PID / consulta
          ↓
4. Abrir detalle de sesión
          ↓
5. Revisar EXPLAIN
          ↓
6. Probar índice hipotético con HypoPG
          ↓
7. Revisar Locks
          ↓
8. Revisar ASH / histórico
          ↓
9. Ejecutar Index Advisor
          ↓
10. Validar manualmente cualquier índice
```

---

# 43. Diagnóstico de una consulta lenta

## Paso 1

Seleccionar la base afectada.

## Paso 2

Buscar la sesión en la tabla principal.

## Paso 3

Hacer doble clic.

## Paso 4

Analizar:

```text
state
wait_event_type
wait_event
query
```

## Paso 5

Revisar el `EXPLAIN`.

## Paso 6

Determinar si el problema parece relacionado con:

- Sequential Scan;
- índice inexistente;
- join costoso;
- sort;
- bloqueo;
- I/O;
- CPU.

## Paso 7

Utilizar HypoPG para evaluar una posible solución sin crear inicialmente el índice.

---

# 44. Diagnóstico de bloqueos

Si una aplicación parece congelada:

1. Abrir `Locks`.
2. Pulsar `Refrescar`.
3. Buscar el PID bloqueador.
4. Analizar:
   ```text
   Consulta Bloqueadora
   ```
5. Analizar:
   ```text
   Consulta Bloqueada
   ```
6. Determinar si el bloqueo es esperado.
7. Solo si corresponde, cancelar/terminar la sesión.

No matar sesiones automáticamente.

---

# 45. Diagnóstico histórico

Para investigar un problema que ocurrió anteriormente:

1. Abrir `ASH`.
2. Introducir:
   ```text
   fecha desde
   fecha hasta
   ```
3. Pulsar `buscar`.
4. Revisar las sesiones capturadas.
5. Buscar patrones de:
   - CPU;
   - I/O;
   - waits;
   - consultas repetitivas.
6. Hacer doble clic sobre una actividad para revisar su `EXPLAIN`.

---

# 46. Rendimiento de la herramienta

El monitor actual trabaja con consultas periódicas.

El intervalo predeterminado es:

```text
5 segundos
```

La tabla principal puede solicitar hasta:

```text
1024
```

actividades según el tamaño de página utilizado por el modelo.

El gráfico mantiene aproximadamente:

```text
15
```

muestras visibles.

Para servidores con gran cantidad de sesiones se recomienda:

- aumentar el intervalo;
- evitar abrir varias instancias simultáneas;
- controlar el impacto de `pg_stat_statements`;
- controlar el crecimiento del histórico.

---

# 47. Retención del histórico

El script de `pg_cron` realiza una captura cada minuto.

Eso significa aproximadamente:

```text
60 registros/hora por sesión activa
```

No necesariamente exactamente, porque depende de las sesiones activas en cada captura.

En instalaciones de larga duración, la tabla:

```text
pg_stat_activity_history
```

puede crecer rápidamente.

Se recomienda definir una política de retención.

Ejemplo conceptual:

```sql
DELETE FROM pg_stat_activity_history
WHERE snapshot_time < now() - interval '30 days';
```

La política definitiva debe adaptarse al tamaño de la base y a los requisitos de auditoría.

---

# 48. Problemas frecuentes

## 48.1. La aplicación no arranca

Comprobar:

- JDK instalado;
- JavaFX disponible;
- módulos JavaFX;
- classpath/module-path;
- recursos FXML;
- archivo indicado por `-Dcat.file`;
- clases `speco.cat.*`.

---

## 48.2. No aparecen bases

Comprobar:

```bash
-Dcat.file=/ruta/archivo.properties
```

y:

```properties
pool=1
url_1=...
user_1=...
rdbms_1=...
nombre_1=...
```

También comprobar que `nombre_1` no esté vacío.

---

## 48.3. No aparecen sesiones

Comprobar:

```sql
SELECT *
FROM pg_stat_activity;
```

y los permisos del usuario utilizado.

---

## 48.4. EXPLAIN falla

Comprobar que exista:

```sql
SELECT proname
FROM pg_proc
WHERE proname = 'explain';
```

También probar manualmente:

```sql
SELECT explain('SELECT 1');
```

---

## 48.5. HypoPG falla

Comprobar:

```sql
SELECT extname
FROM pg_extension
WHERE extname = 'hypopg';
```

Debe estar instalada en la base utilizada.

---

## 48.6. Advisor no devuelve información

Comprobar:

```sql
SELECT *
FROM pg_stat_user_tables
ORDER BY seq_scan DESC
LIMIT 20;
```

y:

```sql
SELECT *
FROM pg_stat_statements
LIMIT 10;
```

La versión normal del Advisor aplica filtros relativamente restrictivos.

---

## 48.7. Locks no muestra información

Comprobar:

```sql
SELECT *
FROM pg_locks
WHERE NOT granted;
```

Si no existen locks no concedidos, el árbol estará vacío.

---

## 48.8. ASH está vacío

Comprobar:

```sql
SELECT COUNT(*)
FROM pg_stat_activity_history;
```

Después:

```sql
SELECT *
FROM cron.job
WHERE jobname = 'snapshot_pg_stat_history';
```

Y revisar:

```sql
SELECT *
FROM cron.job_run_details
ORDER BY start_time DESC
LIMIT 20;
```

---

# 49. Verificación rápida de instalación

Después de instalar los objetos, ejecutar:

```sql
SELECT current_database();

SELECT extname
FROM pg_extension
ORDER BY extname;
```

Comprobar las vistas:

```sql
SELECT * FROM vcpu_time;
SELECT * FROM vwait_events;
SELECT * FROM v_lock_recursive;
SELECT * FROM v_index_advisor;
```

Comprobar la función:

```sql
SELECT explain('SELECT 1');
```

Comprobar HypoPG:

```sql
SELECT *
FROM hypopg();
```

Si todos los objetos requeridos están disponibles, se puede pasar a la configuración de la aplicación.

---

# 50. Estructura de archivos del proyecto

```text
source/
├── sql/
│   ├── IndexAdvisor.sql
│   ├── IndexAdvisorSinrestriccion.sql
│   ├── explain.sql
│   ├── cron.sql
│   ├── lock.sql
│   ├── stat/
│   │   └── statistics.sql
│   └── logger/
│       ├── 00create_view_col.sql
│       ├── 01create_logger_tbl.sql
│       ├── 02_pkg_prod_insert_logger.sql
│       ├── 03_pkg_old_new_vs.sql
│       ├── 04_pkg_create_trg_insert.sql
│       ├── 04_pkg_check.sql
│       ├── 05_pkg_insert_tabla.sql
│       ├── readme
│       └── test.sql
│
└── src/
    └── speco/
        └── toppgsql/
            ├── TopPGSQL.java
            ├── FXMLTopPgSqlController.java
            ├── FXMLTopPGSQL.fxml
            ├── FXMLHistoryController.java
            ├── FXMLActivityHistory.fxml
            ├── FXMLExplainController.java
            ├── FXMLExplain.fxml
            ├── FXMLExplainHistoryController.java
            ├── FXMLExplainHistory.fxml
            ├── FXMLPgActivityFullController.java
            ├── FXMLPgActivityFull.fxml
            ├── FXMLLockController.java
            ├── FXMLLock.fxml
            ├── FXMLIndexAdvisorController.java
            ├── FXMLIndexAdvisor.fxml
            ├── estilos.css
            ├── ReadProperties.java
            ├── PgBases.java
            └── om/
                ├── ModelPg.java
                ├── ModelPgStatActivityHistory.java
                ├── ModelIndexAdvisor.java
                ├── ModelVLockRecursive.java
                ├── ModelVwaitEvents.java
                ├── ModelVcpuTime.java
                ├── PgActivity.java
                ├── PgActivityFull.java
                ├── PgStatActivityHistory.java
                ├── VIndexAdvisor.java
                ├── VLockRecursive.java
                ├── VwaitEvents.java
                ├── VcpuTime.java
                ├── Pghypopg.java
                └── KillSession.java
```

---

# 51. Dependencias que no están incluidas en el archivo fuente

El código hace referencia a clases externas:

```java
speco.cat.Tx
speco.cat.Rdbms
speco.cat.util.Log
speco.cat.om.Vom
```

Estas clases **no están incluidas en el `source.zip` analizado**.

Por lo tanto, el proyecto depende de una biblioteca/proyecto externo denominado `speco.cat` o de un módulo equivalente.

Para compilar TopPGSQL es necesario disponer de esas clases.

---

# 52. Compilación

El archivo entregado no incluye:

```text
pom.xml
build.gradle
build.xml
module-info.java
```

por lo que no es posible indicar un comando Maven/Gradle único basado únicamente en este archivo.

La compilación requiere configurar:

```text
JDK
JavaFX
PostgreSQL JDBC Driver
speco.cat
```

y empaquetar los recursos:

```text
FXML
CSS
```

dentro del classpath.

La clase principal es:

```text
speco.toppgsql.TopPGSQL
```

---

# 53. Clase principal

La aplicación se inicia mediante:

```java
public static void main(String[] args) {
    launch(args);
}
```

y la clase:

```text
speco.toppgsql.TopPGSQL
```

crea la ventana JavaFX principal.

Título de la aplicación:

```text
SQL command Center for Postgresql
```

---

# 54. Recomendaciones para producción

Antes de instalar en un servidor productivo:

- probar en un ambiente de staging;
- utilizar un usuario PostgreSQL dedicado;
- verificar permisos;
- revisar el impacto de la frecuencia de polling;
- revisar la retención de `pg_stat_activity_history`;
- revisar el impacto de `pg_stat_statements`;
- validar HypoPG;
- probar el comportamiento de Kill;
- no ejecutar automáticamente los DDL sugeridos;
- monitorizar el tamaño del histórico.

---

# 55. Limitaciones conocidas del código actual

Esta documentación refleja el comportamiento del código entregado. Se identifican algunas características que conviene conocer:

### 55.1. Kill vs Cancel

La interfaz habla de cancelar, pero:

```java
KillSession.callKillSession()
```

ejecuta:

```sql
pg_terminate_backend()
```

Debe considerarse una operación de terminación de sesión.

### 55.2. Index Advisor no determina automáticamente la columna

La sugerencia contiene:

```text
/* columna_filtro */
```

Por lo que el DBA debe determinar la columna o columnas correctas.

### 55.3. El histórico depende de infraestructura externa

El proyecto presupone:

```text
pg_stat_activity_history
pg_cron
pg_stat_kcache
pg_stat_statements
```

### 55.4. Configuración de conexión externa

El formato de conexión se procesa mediante:

```text
speco.cat.Tx
```

por lo que la semántica completa de `url`, `user`, `rdbms` y `nombre` depende también de esa biblioteca.

### 55.5. No existe sistema de autenticación de la aplicación

El acceso a las funciones de TopPGSQL depende de las credenciales y permisos de PostgreSQL.

---

# 56. Checklist de instalación

```text
[ ] Instalar JDK
[ ] Instalar JavaFX compatible
[ ] Instalar PostgreSQL JDBC Driver
[ ] Disponer de speco.cat
[ ] Configurar PostgreSQL
[ ] Activar pg_stat_statements
[ ] Activar pg_stat_kcache si se desea CPU/I/O
[ ] Instalar HypoPG si se desea análisis de índices hipotéticos
[ ] Instalar/configurar pg_cron si se desea histórico automático
[ ] Crear pg_stat_activity_history
[ ] Ejecutar statistics.sql
[ ] Ejecutar explain.sql
[ ] Ejecutar lock.sql
[ ] Ejecutar IndexAdvisor.sql
[ ] Configurar toppgsql.properties
[ ] Arrancar con -Dcat.file=...
[ ] Seleccionar una base
[ ] Verificar sesiones
[ ] Probar EXPLAIN
[ ] Probar Locks
[ ] Probar Advisor
[ ] Verificar ASH
```

---

# 57. Checklist de diagnóstico de rendimiento

```text
[ ] ¿La sesión está activa?
[ ] ¿Qué wait_event_type tiene?
[ ] ¿Qué wait_event tiene?
[ ] ¿Cuánto CPU utiliza?
[ ] ¿Cuánto I/O realiza?
[ ] ¿Cuál es la consulta?
[ ] ¿Existe un Sequential Scan?
[ ] ¿Qué dice EXPLAIN?
[ ] ¿Qué ocurre con un índice hipotético?
[ ] ¿Hay bloqueos?
[ ] ¿Existe histórico del problema?
[ ] ¿Index Advisor encuentra la tabla?
[ ] ¿Existe ya un índice equivalente?
[ ] ¿La sugerencia mejora realmente el plan?
```

---

# 58. Licencia

El código fuente incluido contiene una declaración de licencia:

```text
GNU General Public License
```

indicando que el software puede redistribuirse y modificarse bajo los términos de la GPL.

La copia entregada no contiene un archivo `LICENSE` independiente. Para una distribución formal del proyecto se recomienda incluir la licencia completa correspondiente y conservar los avisos de copyright del código fuente.

---

# 59. Autor

Los archivos fuente identifican como autor a:

```text
Adrian Tabak
```

---

# 60. Resumen

TopPGSQL funciona como un **SQL Command Center para PostgreSQL**, combinando monitorización en tiempo real con herramientas de diagnóstico:

```text
                  ┌──────────────────┐
                  │    TopPGSQL      │
                  └────────┬─────────┘
                           │
          ┌────────────────┼────────────────┐
          │                │                │
          ▼                ▼                ▼
 pg_stat_activity   pg_stat_statements   pg_stat_kcache
          │                │                │
          └────────────────┼────────────────┘
                           │
             ┌─────────────┼──────────────┐
             ▼             ▼              ▼
          Activity       Explain        Advisor
             │             │              │
             ▼             ▼              ▼
          History       HypoPG       Index analysis
             │
             ▼
          pg_cron
             │
             ▼
 pg_stat_activity_history
```

La herramienta está especialmente orientada a administradores y desarrolladores PostgreSQL que necesiten identificar:

- sesiones activas;
- consultas costosas;
- esperas;
- bloqueos;
- consumo de CPU;
- consumo de I/O;
- problemas históricos;
- oportunidades de indexación;
- posibles mejoras de planes de ejecución.

---

## Licencia

Este README documenta el funcionamiento observado en el código fuente proporcionado. Para redistribución del proyecto, incluir la licencia GPL correspondiente y revisar las licencias de todas las dependencias externas.
