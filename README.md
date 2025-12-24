# ✈️ API de Vuelos — (Spring Boot)

Este proyecto implementa una API para gestionar vuelos sin usar una base de datos (Usando almacenamiento en memoria).

Incluye un CRUD completo, validaciones, filtros, ordenamiento y manejo de errores.

---

## 📌 Tecnologías usadas
- Java 21 ♨️
- SpringBoot 🍃

  > Spring Web + Validation.
- Maven 🪶
- Lombok 🫑
- Postman 🚀

  > Para comprobar los endpoints y guardar la collection.
---

## 📁 Estructura del proyecto.
- `controllers/` → Endpoints REST, validación de entrada y respuestas.
- `services/` → Logica de la APP (reglas, filtros, orden, duplicados).
- `repositories/` → Persistencia en memoria con `Map` (simula una DB).
- `dtos/` → DTOs de entrada/salida + Mapper (no se expone el models directamente).
- `models/` → Clases base como `Vuelo` y `ApiResponse`
- `utils/` → Utilidades de fecha y normalización de parametros (parseo/validaciones)
- `exceptions/` → Excepciones propias + handler para respuestas de errores generales.
- `postman/` → Aquí se guarda el **export** de la colección Postman (lo haces desde Postman)

---

## ✅ Requisitos

* **Java 21** (recomendado)
* **Maven**
### Lombok (imprescindible)
1. `Settings → Plugins` → instalar **Lombok**
2. `Settings → Build, Execution, Deployment → Compiler → Annotation Processors`
   - Activa el `Enable annotation processing`

---

## ⚙️ ¿Como arrancarlo?

Para ejecturar el programa, confirma que los siguientes puntos estan correctos.

1. **Dependencias y plugins correctos**

   * Compruebalos en `pom.xml`.

2. **Arrancar APP**

   * Ejecuta `VuelosApplication.java` en el proyecto de IntelliJ
   * O desde la terminal (raíz del proyecto)
```
mvn spring-boot:run
```
### La API queda disponible en:
- `http://localhost:8080`
---

## 🚀 Probar con Postman
### Importar la colección incluida en el repo
> La colección exportada se encuentra en `postman/` (archivo `.json`).

1. **Abrir Postman.**

2. **Click en Import (arriba a la izquierda).**
 
3. **Seleccionar File y elegir el archivo de la carpeta `postman/` del proyecto.**

   (`postman/vuelosCRUD.postman_collection.json`).
   
5. **Confirmar importación. La colección aparecerá en Collections.**

---

## 🦜 Persistencia en memoria

- No hay base de datos
- Se usa una estructura en memoria `(Map<Integer, Vuelo>)` para guardar los vuelos
- Al iniciar, se cargan 10 vuelos.
- Si se reinicia la APP, se reinicia el estado y vuelve al seed

---

## 🧰 End-Points

### 1. GET `/vuelos` — Listar vuelos
**Query params** (opcionales, combinables):

| Parámetro      | Tipo   | Ejemplo      | Descripción |
|---------------|--------|--------------|-------------|
| `empresa`      | String | `Air Europa`    | Filtra por empresa (case-insensitive). |
| `lugarLlegada` | String | `New York`   | Filtra por lugar de llegada (case-insensitive). |
| `fechaSalida`  | String | `2025-03-10` | Filtra por fecha de salida (formato `yyyy-MM-dd`). |
| `ordenarPor`   | String | `empresa`    | `fechaSalida` (default), `empresa`, `lugarLlegada`. |
| `ordenar`      | String | `DESC`       | `ASC` (default) o `DESC`. |

**Ejemplos:**
```text
/vuelos
/vuelos?empresa=Air Europa
/vuelos?lugarLlegada=New%20York&fechaSalida=2025-03-10
/vuelos?ordenarPor=empresa&ordenar=DESC
```

---

### 2. GET `/vuelos/{id}` — Listar por ID
```text
/vuelos/1
```

---

### 3. POST `/vuelos` — Crear vuelo
Body JSON:
```json
{
  "nombreVuelo": "QA001-V",
  "empresa": "Iberia",
  "lugarSalida": "Madrid",
  "lugarLlegada": "Tokyo",
  "fechaSalida": "2025-03-16",
  "fechaLlegada": "2025-03-17"
}
```

- Campos obligatorios: `nombreVuelo`, `empresa`, `lugarSalida`, `lugarLlegada`, `fechaSalida`, `fechaLlegada`
- `fechaSalida` no puede ser posterior a `fechaLlegada` (400)
- `nombreVuelo` debe ser único (409)

---

### 4. PUT `/vuelos/{id}` — Actualizar vuelo
Body igual al POST.
```text
/vuelos/11
```

---

### 5. DELETE `/vuelos/{id}` — Eliminar vuelo
```text
/vuelos/11
```

---

## ✏️ Formato de respuestas
Todas las respuestas usan `ApiResponse`:

```json
{
  "success": true,
  "message": "Listado de vuelos",
  "data": [],
}
```

Al listar los vuelos o buscar por ID, cada vuelo incluye el campo extra:
- `duracionDias` = los días entre `fechaSalida` y `fechaLlegada`

---

## 🦺 Casos de prueba sugeridos

### Listado y filtros
- `GET /vuelos` → 200 OK y vuelos listados ordenados por `fechaSalida` ASC.
- `GET /vuelos?empresa=Air Europa` → 200 OK y todos los vuelos con empresa Air Europa.
- `GET /vuelos?lugarLlegada=New York&fechaSalida=2025-03-10` → 200 OK (Usando varios filtros en un mismo GET).

### Ordenamiento
- `GET /vuelos?ordenarPor=empresa&ordenar=DESC` → 200 OK.
- `GET /vuelos?ordenarPor=stefano` → 400.

### Validaciones
- `POST /vuelos` con campo faltante → 400 Bad Request.
- `POST /vuelos` con `fechaSalida > fechaLlegada` → 400 Bad Request.
- `POST /vuelos` duplicando `nombreVuelo` → 409 Conflict.

### CRUD completo
- `POST` para crear un vuelo con ID → 201 Created.
- `PUT` a un ID creado → 200 OK.
- `DELETE` a un id creado → 200 OK.
- `GET` a un ID eliminado → 404 Not Found.

---

## ✖️ Errores y códigos HTTP

| Código | Cuándo ocurre | Ejemplo |
|------:|---------------|---------|
| 400 | Validación, formato fecha inválido, ordenarPor inválido, fechas incoherentes | `fechaSalida=10-03-2025` |
| 404 | No existe el recurso | `GET /vuelos/50` |
| 409 | Duplicado de `nombreVuelo` | Intentar crear un vuelo con el mismo "nombreVuelo" |

---

## 🧟 Posibles errores

### Lombok no funciona (no reconoce getters/setters)
- Instalar plugin Lombok en IntelliJ
- Activar annotation processing ✅

### Fecha inválida
- Formato válido: `yyyy-MM-dd` (ej: `2025-03-10`)

### 404 al probar endpoints
- Verificar que la app corre en `localhost:8080`
- Verificar ruta exacta `/vuelos`

---

```xml
<!-- dependencias y plugins correctos en el pom.xml -->

<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webmvc</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webmvc-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
```
