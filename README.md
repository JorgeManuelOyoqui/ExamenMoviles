# Examen de Desarrollo Móvil 27/11/2025

- **Nombre**: Jorge Manuel Oyoqui Aguilera
- **Matrícula**: A01711783
- **Plataforma**: Android
- **Lenguaje**: Kotlin
- **Framework**: Jetpack Compose

## Descripción
Generé una aplicación móvil educativa de Sudoku que permite generar, resolver y verificar puzzles de diferentes tamaños (4x4 y 9x9) y niveles de dificultad (fácil, medio, difícil). Fue desarrollada siguiendo los estándares de la arquitectura MVVM y usando Clean Architecture.

## Características e Implementaciones

### Requerimientos Funcionales

- **Generación del Sudoku**: 
Se hace conexión con API Ninjas para generar sudokus/puzzles, donde se pueden crear Sudokus para 4x4 y 9x9 (Pero el 9x9 no salió del todo) y hay tres niveles de dificultad (easy, medium, hard)
Igualmente hay un modo offline con un JSON con un sudoku simulado

- **Interfaz Interactiva**: 
El tablero Sudoku es responsivo a los clics que le haces, donde las celdas pistas están diferenciadas visualmente y no se pueden modificar y las celdas editables están disponibles y funcionales para el usuario. Se hace la selección del número por click (se empieza por uno y sigue creciendo hasta el máximo núemr permitido)

- **Verificación de Solución**: 
Hay validación local sin depender de API. También se meustran mensajes claros de éxito o de error segun el resultado del sudoku. Para error, está la opción para continuar jugando

- **Control del Sudoku**: 
Hay una interfaz clara donde se puede reiniciar el sudoku (donde se limpian solo las celdas del usuario), también se puede generar un nuevo sudoku, guardarlo para seguir jugando después, verificar la respuesta o ver la solución (si es que no es el caso de prueba que no está conectado a la API).

- **Guardado y Carga de Partidas**: 
 La carga de partidas guardadas se encuentra en la pantalla inicial, y se recupera estado exacto del juego, incluyendo las jugadas del usuario.

- **Modo Offline**: 
Funciona sin conexión una vez cargado el puzzle, también se indica cuando está en modo simulado y hay manejo de errores de conexión

- **Manejo de Estados**: 
Se pueden encontrar los siguientes estados dentro de la aplciación para los diferentes escenarios que pueda llegar a presnetar:
   - **Estado Cargando**: donde se muestra un spinner mientras se solicita un sudoku a la API
   - **Estado Éxito**: donde se muestra el sudoku cargado, solución correcta y progreso guardado
   - **Estado Error**: donde se muestran mensajes claros al usuario con botón "Reintentar" cuando se equivoca en su solución
   - **Errores entendibles**: se incluyen detalles de conexión y validación
   - **Modo offline**: indica cuando el juego funciona con datos simulados locales

### Requerimientos No Funcionales

- **Arquitectura MVVM**: 
Se trabaja con ViewModel y con estados reactivos (StateFlow). Hay un patrón Repository y hay separación clara de responsabilidades entre los arcivos del sistema.

- **Clean Architecture**: 
Se trabaja con:
   - **data**: para el repositorio, API, Preferences
   - **domain**: para los use cases, Modelos
   - **presentation**: para la UI, ViewModels, Navegación

- **UI Moderna**: 
Se usa Jetpack Compose, los diseños son responsivos para 4x4 y 9x9 (a pesar del que el diseño del 9x9 sea incorrecto pues la API sólo eprmite de 2x2 a 4x4), se trabaja con Material Design 3 y la accesibilidad a las distintas funciones es clara y simple.

- **Inyección de Dependencias**: 
Se usa Hilt configurado, de modo que hay Inyección en ViewModel, Repository y en los modules en AppModule.kt

- ## Estructura del Proyecto
La estructura final del poryecto quedó de la siguiente manera:
```
app/src/main/java/com/app/sudokuapp/
├── data/
│   ├── local/preferences/
│   │   ├── SudokuPreferences.kt
│   │   └── SudokuPreferencesConstants.kt
│   ├── remote/
│   │   ├── api/SudokuApi.kt
│   │   └── dto/
│   │       ├── SudokuGenerateDto.kt
│   │       └── SudokuSolveDto.kt
│   └── repository/SudokuRepositoryImpl.kt
├── domain/
│   ├── model/SudokuPuzzle.kt
│   ├── repository/SudokuRepository.kt
│   └── usecase/
│       ├── GenerateSudokuUseCase.kt
│       └── SolveSudokuUseCase.kt
├── presentation/
│   ├── MainActivity.kt
│   ├── navigation/SudokuNavGraph.kt
│   ├── screens/sudoku/
│   │   ├── SudokuHomeScreen.kt
│   │   ├── SudokuBoardScreen.kt
│   │   ├── SudokuViewModel.kt
│   │   ├── SudokuUiState.kt
│   │   └── components/
│   │       ├── SudokuBoard.kt
│   │       ├── SudokuCell.kt
│   │       └── SudokuToolbar.kt
│   ├── common/components/ErrorView.kt
│   └── theme/
│       ├── Theme.kt
│       ├── Color.kt
│       └── Type.kt
├── di/AppModule.kt
└── SudokuApplication.kt
```

## Manejo de Estados en Detalle
La aplicación implementa un sistema robusto de manejo de estados basado en "StateFlow", donde se tiene:

### Estados Principales

- **Loading (Cargando)**
Se muestra un "CircularProgressIndicator", que aparece cuando se solicita un puzzle al API. También le presenta un mensaje "Generando puzzle..." al usuario

- **Success (Éxito)**
Se activas cuando el puzzle está cargado y listo para jugar, cuando la solución verificada está correcta y cuando el progreso de guardado es exitoso. Los mensajes verdes indicando operación completada

- **Error (Error)**
Muestra un "ErrorView" con icono de advertencia. Es un mensaje descriptivo del problema (ya sea conexión, API, validación, etc.)
Se activa también cuando el botón **"Reintentar"** reintenta una operación fallida
Esto le permite al usuario tomar acción sin perder el contexto

### Flujo de Estados en Acciones

#### Generación de Puzzle

Esperando -> [El usuario selecciona el tamaño y dificultad y da clic en "Generar"]
    |
    V
Loading → [Llamada al API]
    |
    V
Success → [Se muestra el Sudoku] o Error → [Se muestra el ErrorView con Reintentar]


#### Verificación de Solución

Completando puzzle -> [El usuario presiona "Verificar"]
    |
    V
Validating -> [Se hace la validación local]
    |
    V
Correcto [Mensaje: "¡Solución correcta! Felicidades"]  O Incorrecto [Mensaje: "Solución inválida, intenta de nuevo"]


### Errores Entendibles al Usuario

- **"No se pudo generar el puzzle. Asegúrate de tener conexión..."**: donde se da un error de API o de conexión
- **"Error de respuesta del API"**: doned el formato es inválido en la respuesta
- **"Error de validación"**: donde los datos son inconsistentes
- **"Solución inválida"**: donde hay al menos una violación de  las reglas de Sudoku

Cada error incluye opción de reintentar sin perder el estado actual.

## Configuración y Ejecución

### Requisitos
- Android Studio Ladybug o superior
- Android SDK 24+
- JDK 11+
- Gradle 8.8.0+

### Pasos
1. Clonar el repositorio
2. Abrir en Android Studio
3. Sincronizar Gradle
4. Ejecutar en emulador o dispositivo físico

### API Key
La aplicación está configurada con una API Key válida para API Ninjas. En caso de no funcionar, reemplazala en el archivo "SudokuRepositoryImpl.kt" en la línea:
"private val apiKey = "eVmtNoI1b2vpfsW0tyBo3Q==3X7j0mrCq4sbcIiV"

## Características Especiales

### Validación Local
No depende de la API para verificar soluciones. Implementa validación completa de las reglas de Sudoku, donde:
- No pueden haber filas duplicadas
- No pueden haber columnas duplicadas
- No pueden haber cuadrantes duplicados

### Modo Simulado
Si no hay conexión al generar un nuevo puzzle, se carga automáticamente un JSON local ("assets/200_sudoku.json") e indica al usuario que está en "Modo sin conexión / datos simulados".

### Persistencia
Guarda y recupera el estado completo de la partida:
- Tablero base (pistas)
- Valores ingresados
- Tamaño y dificultad
- Y solución
