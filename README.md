## 1. Arquitectura del Sistema Multiagente

El núcleo del sistema está pensado como una red distribuida y asíncrona que sigue las reglas del estándar FIPA-ACL. En plan resumido: tenemos un ecosistema de agentes especializados que se buscan la vida solos, se descubren dinámicamente al arrancar y colaboran pasándose mensajes binarios serializados en segundo plano.

### A) Diagrama de Topología y Flujo de Datos
```text
[ CAPA DE ENTRADA ]            [ CAPA COORDINADORA ]           [ CAPA DE EVALUACIÓN ]
+---------------------+        +---------------------+        +-------------------------+
| AgenteAdquisicion   |        |                     |------->| AgenteSentimiento       |
| (Simulador Interno) |        |                     |        | (Módulo PLN - Emotion)  |
+---------------------+        |                     |        +-------------------------+
| AgenteAdquisicion   | INFORM |  AgenteCoordinador  | REQUEST| AgenteDetectorSesgo     |
| (Fichero Local)     |------->| (Orquestador Central|------->| (Módulo PLN - Bias)     |
+---------------------+        |  y Agregador Async) |        +-------------------------+
| AgenteAdquisicion   |        |                     |------->| AgenteVerificadorFuente |
| (Feeds RSS / APIs)  |        |                     |        | (Módulo Reputacional)   |
+---------------------+        +---------------------+        +-------------------------+
                                          |
                                          | INFORM (Informe Consolidado)
                                          v
                                          +---------------------+
                                          | AgenteVisualizacion |
                                          | (Interfaz Swing UI) |
                                          +---------------------+
```
### B) Protocolo de Mensajería y Registro (FIPA-ACL)

La intercomunicación no utiliza acoplamientos directos de código; se apoya enteramente en el **Directory Facilitator (DF)** de JADE, que funciona como un servicio dinámico de Páginas Amarillas. Los roles y transacciones se estructuran de la siguiente forma:

| Agente / Componente | Comportamiento Asociado | Performativa | Ontología | Descripción del Flujo Técnico |
| :--- | :--- | :--- | :--- | :--- |
| **Agentes de Adquisición** | `ObtenerNoticias...Behaviour` | `INFORM` | `noticia-nueva` | Capturan la información desde su origen específico, construyen el objeto `Noticia` y lo envían asíncronamente al Coordinador. |
| **Agentes Expertos** | `Analizar...Behaviour` / `UtilsDF` | — | *(Varios)* | Al arrancar (`setup`), se registran automáticamente en el **DF** indicando su tipo de servicio (`analisis-sentimiento`, `deteccion-sesgo`, etc.). |
| **Agente Coordinador** | `DelegarAnalisisBehaviour` | `REQUEST` | *(Tipo Servicio)* | Recibe la noticia, genera un `ConversationId` único para la transacción, consulta al DF las direcciones físicas (`AID`) de los expertos y les delega una copia en paralelo. |
| **Agentes Expertos** | `Analizar...Behaviour` | `INFORM` | *(Tipo Servicio)* | Procesan de forma aislada el texto mediante sus motores heurísticos de PLN y responden al coordinador enviando su puntuación final en formato de cadena. |
| **Agente Coordinador** | `AgregarResultadosBehaviour` | `INFORM` | `informe-credibilidad` | Recopila las tres respuestas correlacionándolas mediante el `ConversationId`, calcula la media ponderada del veredicto y despacha el `InformeCredibilidad` unificado. |
| **Agente Visualización** | `ActualizarUIBehaviour` | — | — | Recibe el informe consolidado y delega de forma segura en el hilo de Swing (`SwingUtilities.invokeLater`) para actualizar la interfaz. |

---

### C) El Ciclo de Vida de una Noticia (Pipeline Concurrente)

Para garantizar que el sistema sea escalable y que ninguna consulta bloquee el hilo principal, el procesamiento de las noticias se ejecuta en cinco fases exactas:

1. **Fase de Captura (Ingesta):** Un agente de adquisición extrae los datos y formatea las propiedades de la noticia (Título, Contenido, Fuente).
2. **Fase de Indexación:** El `AgenteCoordinador` recibe el objeto y lo guarda temporalmente en un mapa concurrente (`ConcurrentHashMap`) usando el identificador de la conversación como clave. Esto permite atender cientos de peticiones de adquisición a la vez sin perder la trazabilidad de los retornos.
3. **Fase de Evaluación Multiexperto:** Los tres analistas trabajan en paralelo de forma independiente. Si un analista tarda más que otro (por ejemplo, el que consulta la red), el framework JADE encola los mensajes pendientes de manera asíncrona sin congelar la plataforma.
4. **Fase de Consolidación:** En cuanto el contador de respuestas del `ConversationId` llega a **3**, el Coordinador recupera la noticia original del mapa, invoca la lógica interna de `calcularPuntuacionFinal()` aplicando los pesos específicos ($40\%$ Fuente, $35\%$ Sesgo, $25\%$ Sentimiento) y genera el veredicto definitivo (`CONFIABLE`, `SOSPECHOSO` o `NO_CONFIABLE`).
5. **Fase de Despacho:** Los hilos de los agentes se liberan inmediatamente del consumo de memoria tras enviar el informe final a la vista, garantizando un flujo limpio y continuo de datos hacia la interfaz de usuario.

## 2. Captura de Dependencias Necesarias para Instalar el Proyecto

El proyecto está diseñado para funcionar sobre **Java Standard Edition (Java SE)** con dependencias mínimas y embebidas, evitando sobrecargar el entorno. Los componentes obligatorios del sistema son:

* **`jade.jar` (Nativo / Core):** Framework multiagente que proporciona la implementación del contenedor de agentes, el ciclo de vida de los mismos, las páginas amarillas (DF) y el paso de mensajes bajo el estándar FIPA-ACL.
* **`commons-codec.jar`:** La librería auxiliar que necesita JADE para traducir los objetos de Java (Noticia e InformeCredibilidad) a formato Base64 para poder meterlos en los sobres de los mensajes. Si no se añaden, el programa fallará al iniciar con un error rojo.
* **Java Runtime Standard Libraries (Librerías Nativas del JRE/JDK):**
  * `java.desktop` (`javax.swing`, `java.awt`): Utilizada por el `AgenteVisualizacion` y `ActualizarUIBehaviour` para desplegar la interfaz gráfica de usuario en formato textual con los reportes en tiempo real.
  * `java.xml` (`javax.xml.parsers`, `org.w3c.dom`): Utilizada por el `AgenteAdquisicionRSS` para el parseo estructurado de los elementos XML del feed de noticias.
  * `java.net.http` / `java.net.URL` y `java.util.Scanner`: Utilizadas por el `AgenteAdquisicionAPI` para establecer las conexiones HTTP GET por red contra el endpoint remoto de noticias.

---

## 3. Instrucciones de Instalación

Para compilar y configurar este proyecto en tu máquina local utilizando un entorno de desarrollo tradicional (como Eclipse IDE), sigue estos pasos:

1. **Preparar el Entorno:** Asegúrate de tener instalado el Java Development Kit (JDK) versión 11 o superior en el sistema.
2. **Importar el Proyecto:**
   * Abre tu IDE (Eclipse, IntelliJ o NetBeans).
   * Crea un nuevo proyecto Java convencional denominado `PracticaAgentes`.
   * Copia toda la estructura de directorios y paquetes (`practica`, `practica.agentes`, `practica.behaviour`, `practica.modelo`, `practica.pln`, `practica.utils`) dentro de la carpeta raíz de código fuente (`src`) del proyecto.
3. **Configurar el Build Path (Vincular la librería JADE):**
   * Descarga la distribución oficial binaria de JADE y extrae el archivo `jade.jar`.
   * En tu IDE, haz clic derecho sobre el nombre del proyecto &rarr; **Build Path** &rarr; **Configure Build Path...**.
   * Selecciona la pestaña **Libraries**, haz clic sobre **Classpath** y pulsa el botón **Add External JARs...**.
   * Busca en tu disco local el archivo `jade.jar` y `commons-codec.jar`, selecciónalo y pulsa en **Apply and Close**.
4. **Estructura de Carpetas:** Crea una carpeta física llamada `resources` en la raíz del proyecto (al mismo nivel que la carpeta `src`) para alojar los archivos de texto que simularán la base de datos de noticias locales.

---

## 4. Instrucciones de Ejecución

La inicialización de la plataforma JADE se ha centralizado y automatizado en una única clase factoría, evitando tener que configurar manualmente argumentos complejos de línea de comandos en las propiedades del entorno o lanzar scripts de terminal externos.

### Ejecución Estándar (Modo Gráfico por Defecto)
1. Localiza el archivo ejecutable del proyecto en `src/practica/Main.java`.
2. Haz clic derecho sobre el archivo &rarr; **Run As** &rarr; **Java Application**.
3. Al arrancar, el sistema levantará de forma automática la interfaz gráfica oficial de control de JADE (`RMA`) junto con el contenedor principal (*Main Container*), activará los 7 agentes del ecosistema secuencialmente y desplegará la ventana gráfica de Swing del sistema lista para recibir reportes.

### Ejecución en Consola Pura (Opcional)
Si deseas suprimir las interfaces visuales para pruebas automatizadas, puedes pasar el argumento de ejecución `--nogui` a la clase principal. En Eclipse, esto se configura en:
* **Run Configurations...** &rarr; Pestaña **Arguments** &rarr; Escribir `--nogui` en el cuadro *Program Arguments* &rarr; **Run**.

---

## 5. Datos de Ejemplo para Ejecutar la Práctica

El sistema combina entradas de red en vivo con simulaciones locales parametrizadas para validar el funcionamiento de la lógica de análisis:

### A) Configuración del Origen por Fichero Local (`resources/noticias.txt`)
Para que el agente `AgenteAdquisicionFichero` pueda procesar datos, crea un archivo de texto llamado `noticias.txt` dentro de la carpeta `resources/` empleando el carácter pipe (`|`) como delimitador estructurado de campos.

**Contenido de prueba recomendado para el fichero:**
```text
# Formato: titulo|contenido|fuente
Reuters confirma nuevas medidas economicas|Reuters informa de nuevas medidas economicas aprobadas tras una reunion oficial. El texto evita afirmaciones anonimas y enlaza con fuentes institucionales.|reuters.com
El gobierno oculta la verdad que no quieren que sepas|Segun fuentes anonimas, la elite prepara un plan urgente que cambiara la vida de todos. Todo el mundo habla de ello.|falsaurl.com
RTVE publica un resumen de la jornada|RTVE resume los principales datos de la jornada con declaraciones contrastadas y contexto adicional para los lectores.|rtve.es
Supuestamente los expertos dicen que habra una crisis total|Como todos saben, esta demostrado que el sistema esta al borde del desastre, aunque no se citan fuentes verificables.|okdiario.com
```
### B) Entrada por API Remota (NewsAPI)
El comportamiento `ObtenerNoticiasAPIBehaviour` conecta con los servidores de **NewsAPI** en tiempo real.

### C) Entrada por Feed RSS (El País)
El agente `AgenteAdquisicionRSS` está preconfigurado de forma nativa para consumir de forma automatizada los últimos titulares de actualidad nacional de la portada de *El País* a través del endpoint XML:

`https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada`

---

## 6. Declaración de IA (AI Statement)
Se declara que se ha hecho uso de IA en los siguientes ámbitos:

### Ámbito y Modos de Uso:
* **Diseño del Esqueleto Estructural:** Se utilizaron modelos de lenguaje (LLM) como asistentes técnicos de desarrollo para agilizar la maquetación inicial de los comportamientos de la plataforma JADE, específicamente en la correcta instanciación de los constructores heredados de las clases abstractas `CyclicBehaviour` y `TickerBehaviour`.
* **Serialización de Datos:** Apoyo en la resolución de problemas de transferencia de flujos binarios, asegurando que los objetos transferidos entre agentes (`Noticia` e `InformeCredibilidad`) implementaran de forma estricta la interfaz `Serializable` con sus respectivos identificadores de versión únicos (`serialVersionUID`).
* **Generación de Plantillas de Datos:** Uso de asistentes para enviar y estructurar rápidamente los arrays fijos en memoria (`Set.of` y `List.of`) que sirven como diccionarios básicos de palabras clave para las clases del procesamiento de lenguaje natural (`AnalizadorSentimiento` y `DetectorPalabrasClaveSesgo`).

Todo el código fuente final resultante, la integración de las fórmulas matemáticas ponderadas de credibilidad, la lógica de sincronización concurrente en hilos de *Swing* y el enrutamiento de mensajería **ACL** han sido revisados, corregidos, probados e integrados manualmente por los miembros del equipo, garantizando el control absoluto sobre el software.
