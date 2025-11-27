# Sudoku App - Examen Práctico de Desarrollo Móvil

## Información del Estudiante

- **Nombre Completo**: Jorge Manuel Oyoqui Aguilera
- **Matrícula**: A01711783
- **Plataforma**: Android
- **Lenguaje**: Kotlin
- **Framework**: Jetpack Compose

---

## Descripción

Aplicación móvil educativa de Sudoku que permite generar, resolver y verificar puzzles de diferentes tamaños (4x4 y 9x9) y niveles de dificultad (fácil, medio, difícil). Desarrollada siguiendo arquitectura MVVM con Clean Architecture.

---

## Características Implementadas

### Requerimientos Funcionales

1. **Generación de Puzzles** ✅
   - Integración con API Ninjas para generar puzzles
   - Soporte para 4x4 y 9x9
   - Tres niveles de dificultad (easy, medium, hard)
   - Modo offline con JSON simulado

2. **Interfaz Interactiva** ✅
   - Tablero Sudoku responsivo
   - Celdas pistas diferenciadas visualmente
   - Celdas editables para el usuario
   - Selección de número por click

3. **Verificación de Solución** ✅
   - Validación local sin depender de API
   - Mensajes claros de éxito/error
   - Opción para continuar jugando

4. **Control del Puzzle** ✅
   - Reiniciar puzzle (limpiar solo celdas del usuario)
   - Generar nuevo puzzle
   - Interfaz clara

5. **Guardado y Carga de Partidas** ✅
   - Persistencia en SharedPreferences
   - Cargar partida guardada desde pantalla inicial
   - Recupera estado exacto del juego

6. **Modo Offline** ✅
   - Funciona sin conexión una vez cargado el puzzle
   - Indica cuando está en modo simulado
   - Manejo de errores de conexión

7. **Manejo de Estados** ✅
   - **Estado Cargando**: Muestra spinner mientras se solicita puzzle al API
   - **Estado Éxito**: Muestra el puzzle cargado, solución correcta, progreso guardado
   - **Estado Error**: Muestra mensajes claros al usuario con botón "Reintentar"
   - **Errores entendibles**: Incluye detalles de conexión y validación
   - **Modo offline**: Indica cuando funciona con datos simulados

### Requerimientos No Funcionales

1. **Arquitectura MVVM** ✅
   - ViewModel con estados reactivos (StateFlow)
   - Patrón Repository
   - Separación clara de responsabilidades

2. **Clean Architecture** ✅
   - **data**: Repositorio, API, Preferences
   - **domain**: Use cases, Modelos
   - **presentation**: UI, ViewModels, Navegación

3. **UI Moderna** ✅
   - Jetpack Compose
   - Diseño responsivo para 4x4 y 9x9
   - Material Design 3
   - Accesibilidad básica

4. **Inyección de Dependencias** ✅
   - Hilt configurado
   - Inyección en ViewModel, Repository
   - Modules en `di/AppModule.kt`

---

## Estructura del Proyecto

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

---

## Tecnologías Utilizadas

- **Kotlin** 2.0.0
- **Jetpack Compose** 2025.01.00
- **Hilt** 2.52 (Inyección de Dependencias)
- **Retrofit** 2.11.0 (Cliente HTTP)
- **Gson** (Serialización JSON)
- **Android Navigation** 2.8.5
- **Material Design 3**

---

## Manejo de Estados en Detalle

La aplicación implementa un sistema robusto de manejo de estados basado en `StateFlow`:

### Estados Principales

1. **Loading (Cargando)**
   - Se muestra un `CircularProgressIndicator`
   - Aparece cuando se solicita un puzzle al API
   - Presenta un mensaje "Generando puzzle..." al usuario

2. **Success (Éxito)**
   - Puzzle cargado y listo para jugar
   - Solución verificada correctamente
   - Progreso guardado exitosamente
   - Mensajes verdes indicando operación completada

3. **Error (Error)**
   - Muestra un `ErrorView` con icono de advertencia
   - Mensaje descriptivo del problema (conexión, API, validación)
   - Botón **"Reintentar"** que reintenta la operación fallida
   - Permite al usuario tomar acción sin perder el contexto

### Flujo de Estados en Acciones

#### Generación de Puzzle
```
Esperando → [Usuario selecciona tamaño/dificultad y presiona "Generar"]
    ↓
Loading → [Llamada al API]
    ↓
Success → [Puzzle mostrado] O Error → [Mostrar ErrorView con Reintentar]
```

#### Verificación de Solución
```
Completando puzzle → [Usuario presiona "Verificar"]
    ↓
Validating → [Validación local]
    ↓
✅ Correcto [Mensaje: "¡Solución correcta! Felicidades"] 
O ❌ Incorrecto [Mensaje: "Solución inválida, intenta de nuevo"]
```

### Errores Entendibles al Usuario

- **"No se pudo generar el puzzle. Asegúrate de tener conexión..."**: Error de API/conexión
- **"Error de respuesta del API"**: Formato inválido en la respuesta
- **"Error de validación"**: Datos inconsistentes
- **"Solución inválida"**: Violación de reglas Sudoku

Cada error incluye opción de reintentar sin perder el estado actual.

---

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
La aplicación está configurada con una API Key válida para API Ninjas. En caso de no funcionar, reemplazar en `SudokuRepositoryImpl.kt`:

```kotlin
private val apiKey = "eVmtNoI1b2vpfsW0tyBo3Q==3X7j0mrCq4sbcIiV"
```

---

## Características Especiales

### Validación Local
No depende de la API para verificar soluciones. Implementa validación completa de reglas Sudoku:
- Filas sin duplicados
- Columnas sin duplicados
- Cuadrantes sin duplicados (2x2 para 4x4, 3x3 para 9x9)

### Modo Simulado
Si no hay conexión al generar un nuevo puzzle, carga automáticamente un JSON local (`assets/200_sudoku.json`) e indica al usuario que está en "Modo sin conexión / datos simulados".

### Persistencia
Guarda y recupera estado completo de la partida:
- Tablero base (pistas)
- Valores ingresados
- Tamaño y dificultad
- Solución

---

## Notas de Implementación

- El proyecto sigue la estrategia de versionado con commits funcionales
- Las decisiones de arquitectura prioriza mantenibilidad y escalabilidad
- Se enfatiza en experiencia de usuario clara con mensajes informativos
- Código Kotlin idiomático con null safety
- Sistema de estados reactivo con `StateFlow` y `collectAsStateWithLifecycle`

---

## Entrega

**Fecha**: Domingo 30 de noviembre 2025, 23:59 CST

---

**Desarrollo Completado**: Noviembre 2025
