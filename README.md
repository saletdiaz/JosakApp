<!-- --- HECHO POR ANTIGRAVITY --- -->

# JosakApp — Memoria Final (Hito 3 + Hito 4)

> Aplicación móvil Android nativa en **Kotlin + Jetpack Compose** que gamifica la gestión de hábitos mediante el cuidado de una mascota virtual (pingüino).

---

## 1. Estado Final del Proyecto

| Módulo | Estado |
| Autenticación (Login/Registro/Recuperación) |  Completo |
| Gestión de Hábitos (CRUD + Toggle diario) |  Completo |
| Temporizador de Hábitos por frecuencia |  Completo  |
| Mascota Virtual (Pingüino + sed offline) |  Completo |
| Tienda JSON con rotación diaria |  Completo  |
| Tienda de Ropa (accesorios pingüino) |  Completo |
| Sistema de Monedas, XP y Puntos |  Completo |
| Ranking Real desde Firestore |  Completo  |
| Red Social (Seguir/Buscar usuarios) |Completo |
| Perfil de otros usuarios |  Completo  |
| Sincronización Cloud (Firestore) |  Completo |
| Widget de Escritorio |  Completo |
| Estadísticas, Calendario y Rachas |  Completo |
| Perfil de Usuario con Foto Base64 |  Completo |
| Check-in Diario y Regalo Aleatorio | Completo |
| Modo Oscuro / Claro |  Completo |
| Splash Screen |  Completo |

---

## 2. Requisitos Previos

| Requisito | Versión |
 Android Studio 
 JDK - 11 
 compileSdk / targetSdk - 36 
 minSdk - 29 

---

## 3. Tecnologías Utilizadas

 Capa - Tecnología 
 UI - Jetpack Compose 
 Persistencia Local  -Room Database 
 Red - Retrofit 2.9.0 + OkHttp 
 Backend Cloud - Firebase Auth, Firestore, Storage 
 Arquitectura - MVVM + Repository 
 Asincronía - Kotlin Coroutines, StateFlow, Flow 
 Imágenes - Glide Compose + Coil Compose 
 Crashlytics - Firebase Crashlytics 

---

## 4. Funcionalidades Implementadas

### 4.1 Autenticación y Registro
- Login con email/contraseña vía Firebase Authentication.
- Registro con validación. Recuperación de contraseña.
- Persistencia de sesión (auto-login con `LaunchedEffect`).

### 4.2 Gestión de Hábitos
- CRUD completo: crear, editar, eliminar y listar hábitos.
- Cada hábito: nombre, descripción, XP, frecuencia, icono y color.
- Toggle diario con registro histórico (`HabitoRegistro`).
- Sincronización bidireccional con Firestore (`users/{uid}/habitos`).

### 4.3 Temporizador de Hábitos (Hito 4)
- `HabitoTimerUtils.kt`: cálculo del tiempo restante según frecuencia (cada hora, diario, semanal, mensual).
- Almacenamiento del último completado en SharedPreferences.
- Formato legible: "X días", "X horas", "X minutos" o "Ahora".

### 4.4 Mascota Virtual (Pingüino)
- Nivel de sed que se degrada un 10% por día de inactividad offline.
- Mochila de bebidas: comprar con monedas, usar para hidratar (+0.1/unidad).
- Tienda de ropa: 8 sombreros con precios variables (10–60 monedas).
- Equipar/desequipar ropa con relación N:M en Room.
- Pingüino arrastrable e interactivo (`DraggablePenguin`).

### 4.5 Tienda Mejorada con JSON (Hito 4)
- Catálogo leído desde `assets/tienda.json` con 7 artículos.
- Categorías: ropa, cuerpo, accesorio, efecto, consumible.
- Rarezas: common, rare, epic.
- **Rotación diaria** de 4 artículos (algoritmo basado en `dayOfYear`).
- Persistencia de compras en SharedPreferences (`store_owned_items`).
- Nuevo paquete `data/store/` con `StoreItem`, `StoreRepository`, `StoreViewModel`.

### 4.6 Sistema Económico
- Monedas de oro e intercambio XP ↔ Monedas.
- Cada 100 XP otorga 10 puntos automáticamente.
- Check-in diario semanal (10→100 monedas, reset los lunes).
- Regalo aleatorio diario (1% → 1000, 60% → 10 monedas).
- Misiones con protección anti-duplicados por sesión.

### 4.7 Estadísticas y Calendario
- Calendario interactivo (`CalendarCard`).
- Racha actual (streak) y total de días activos.
- Gráficos avanzados (`StatsScreen`).

### 4.8 Red Social
- Búsqueda de usuarios en Firestore (prefijo con `\uf8ff`).
- Seguir usuarios (colección `social`).
- Contadores seguidores/siguiendo en tiempo real.
- Lista de amigos local en Room para filtrar ranking.
- Nuevos métodos `addFriendLocal()` y `getLocalFriendNames()` 

### 4.9 Ranking Real con Firestore 
- **Eliminado** el ranking estático vía Retrofit/JSON de GitHub.
- Ahora sincroniza **todos los usuarios reales** desde Firestore (`getAllUsers()`).
- Los usuarios remotos se insertan en Room local para consulta offline.
- Filtro amigos vs. global con `recomputeRanking()`.
- `UserRanking` ahora incluye `id_usuario` para navegación al perfil.

### 4.10 Perfil de Otros Usuarios 
- Nueva ruta `NavPerfilUserScreen` → `perfil_user/{userId}`.
- Al tocar un usuario en el ranking o búsqueda, se navega a su perfil.
- Carga asíncrona del usuario por ID con `LaunchedEffect`.

### 4.11 Perfil de Usuario
- Nivel, XP, monedas, seguidores y siguiendo.
- Foto de perfil: comprimida 300×300 JPEG 70%, Base64, sincronizada con Firestore.
- Completar perfil con formulario secundario.

### 4.12 Widget de Escritorio
- `HabitoWidget`: muestra el primer hábito con botón toggle.
- `NotificationReceiver`: BroadcastReceiver que actualiza Room y refresca el widget.

### 4.13 Configuración
- Modo Oscuro/Claro dinámico (`ThemeViewModel`).
- Pantallas de ajustes, preferencias, privacidad y recordatorios.

---

## 5. Arquitectura Real del Sistema

```
┌─────────────────────────────────────────────────────┐
│                   CAPA DE VISTA (UI)                │
│  21 Screens + 10 Components + Navigation            │
│  ↕ observa StateFlow / SharedFlow                   │
├─────────────────────────────────────────────────────┤
│               CAPA DE LÓGICA (ViewModel)            │
│  UserVM · HabitosVM · PinguinoVM · RankingVM        │
│  LoginVM · RegisterVM · ThemeVM · StoreVM           │
│  ↕ llama a Repository                               │
├─────────────────────────────────────────────────────┤
│               CAPA DE DATOS (Repository)            │
│  UserRepository · HabitosRepository                 │
│  PinguinoRepository · StoreRepository               │
│  ↕ decide fuente de datos                           │
├─────────────────────────────────────────────────────┤
│                FUENTES DE DATOS                     │
│  ┌──────────────┐  ┌────────────────────────────┐   │
│  │ Room (SQLite)│  │ Firebase Firestore/Auth    │   │
│  │ DAOs         │  │ SharedPreferences          │   │
│  │ LocalDatasrc │  │ Assets (tienda.json)       │   │
│  └──────────────┘  └────────────────────────────┘   │
└─────────────────────────────────────────────────────┘
```

### Inyección de Dependencias
Service Locator manual (`AppModule`) con inicialización lazy. Nuevo método `getContext()`  para módulos sin DI como `StoreRepository`.

### Navegación
21+ rutas en `NavScreens` . `MainContainerScreen` como contenedor con BottomNavigation. Nueva ruta parametrizada `perfil_user/{userId}`.

---

## 6. Modelo de Datos Definitivo

### 6.1 Entidades Room (`habitos.db` v6)

| Entidad | Tabla | Campos Clave |
 `User` - `user` - id_usuario(PK), uid, nombre_usuario, email, esPremium, monedas, xp_total, fotoPerfil, nivel, puntos 
 `Habito` - `habito` - id_habito(PK), nombre, descripcion, exp_habito, frecuencia, estado, icono, id_usuario(FK), colorHex 
 `HabitoRegistro` - `habito_registro` - id_habito+fecha (PK compuesta), completado 
 `Pinguino` - `pinguino`- idPinguino(PK), nivel, xp_actual, estado, nombre, id_usuario, nivelSed, ultimaVezSed 
 `Accesorios` - `accesorios` - id_accesorio(PK), nombre, descripcion, imagen, precio, tipo, id_pinguino 
 `PinguinoAccesoriosCrossRef` - `PinguinoAccesoriosCrossRef` - idPinguino+id_accesorio (PK compuesta) 
 `Amigo` - `amigos` - nombre(PK) 

### 6.2 Modelos no-Room

 Modelo - Uso 
 `StoreItem` (Hito 4) - Artículo de tienda JSON (id, name, description, price, rarity, category, imageResName) 
 `StoreItemUi` (Hito 4) - Wrapper con estado de propiedad (`isOwned`) 
 `UserRanking` - Ranking (id_usuario, nombre_usuario, puntos, nivel, fotoPerfil) 
 `UserRemote` / `HabitoRemote` - Modelos Firestore 
 `Calendar` / `TiendaItem` - Data classes auxiliares de UI 
 `Invitacion` / `Racha` / `Suscripcion` - Entidades modeladas pendientes de implementación completa 

### 6.3 Relaciones Room

| Relación - Tipo - Clase 
 User ↔ Habitos - 1:N - `UserWithHabito` 
 User ↔ Pinguino - 1:1 - `UserWithPinguino` 
 Pinguino ↔ Accesorios - N:M - `PinguinoWithAccesorios`  
 User ↔ Invitaciones - 1:N - `UserWithInvitacion` 
 User ↔ Rachas  1:N - `UserWithRacha` 
 User ↔ Suscripciones - 1:N - `UserWithSuscripcion` 


---

## 7. Estructura del Repositorio

```
📂 app/src/main/java/edu/josakapp/proyectoJosakapp
 ├── 📄 MainActivity.kt / SplashActivity.kt
 ├── 📂 converter
 │    └── 📄 base64ToBitmap.kt
 ├── 📂 data
 │    ├── 📂 datasource
 │    │    ├── 📄 AppDatabase.kt          # Room Singleton (v6)
 │    │    └── 📄 RemoteDatasource.kt     # Ranking data source
 │    ├── 📂 di
 │    │    └── 📄 Appmodule.kt            # Service Locator + getContext()
 │    ├── 📂 local
 │    │    ├── 📄 UserDao / HabitosDao / AmigosDao / PinguinoDao
 │    │    └── 📄 LocalDatasource.kt
 │    ├── 📂 model                        # 22 entidades y modelos
 │    ├── 📂 network
 │    │    ├── 📄 AuthService.kt / FirebaseClient.kt
 │    ├── 📂 remote
 │    │    ├── 📄 RetrofitClient.kt / RankingApi.kt / UserApi.kt
 │    │    ├── 📄 UserRemoteRepository.kt # + getAllUsers() (Hito 4)
 │    │    └── 📄 HabitoRemoteRepository.kt
 │    ├── 📂 repository
 │    │    ├── 📄 UserRepository.kt       # + addFriendLocal() (Hito 4)
 │    │    ├── 📄 HabitosRepository.kt / PinguinoRepository.kt
 │    │    └── 📄 RankingRepository.kt
 │    ├── 📂 store                        #  NUEVO (Hito 4)
 │    │    ├── 📄 StoreItem.kt
 │    │    ├── 📄 StoreRepository.kt      # Lee tienda.json + SharedPrefs
 │    │    └── 📄 StoreViewModel.kt       # Rotación diaria
 │    └── 📂 util                         #  NUEVO (Hito 4)
 │         └── 📄 HabitoTimerUtils.kt     # Timer por frecuencia
 └── 📂 ui
      ├── 📂 components (10 archivos)
      ├── 📂 navigation
      │    ├── 📄 NavScreens.kt           # + NavPerfilUserScreen (Hito 4)
      │    ├── 📄 NavigationHost.kt       # + ruta perfil_user/{userId}
      │    └── 📄 NotificationReceiver.kt
      ├── 📂 theme (Color / Theme / Type)
      ├── 📂 view (21 pantallas)
      └── 📂 viewmodel
           ├── 📄 RankingViewModel.kt     # Reescrito con Firestore real
           └── 📄 (6 ViewModels más)

📂 app/src/main/assets
 └── 📄 tienda.json                       #  NUEVO (Hito 4) — Catálogo
```

---

## 8. Plan de Pruebas — Resultado Final

### 8.1 Pruebas Funcionales
 Caso de Prueba | Resultado 
 Registro nuevo usuario → Firebase Auth + Firestore + Room 
 Login válido → sesión persistente 
 Login inválido → mensaje de error 
 Auto-login al reiniciar 
 Crear hábito → Room + Firestore 
 Marcar hábito completado → XP sumada 
 Desmarcar hábito → registro eliminado 
 Editar / Eliminar hábito 
 Timer de hábito muestra tiempo restante correcto 
 Comprar bebida → mochila actualizada 
 Usar bebida → +0.1 sed (máx 1.0) 
 Degradación offline → -10%/día 
 Comprar accesorio ropa → CrossRef Room 
 Equipar/desequipar ropa 
 Tienda JSON → carga 7 artículos desde assets 
 Rotación diaria → 4 artículos distintos por día 
 Check-in diario semanal 
 Regalo aleatorio (cooldown 24h) 
 Búsqueda usuarios Firestore 
 Seguir usuario → colección `social` 
 Contadores seguidores/siguiendo 
 Ranking real con usuarios Firestore 
 Filtro ranking solo amigos 
 Navegar al perfil de otro usuario 
 Subir foto perfil Base64 
 Widget escritorio → toggle hábito 
 Cambio tema claro/oscuro 
 Splash screen (2s) 
 Racha (streak) calculada 

### 8.2 Pruebas de Integración
 Escenario | Resultado 
 Room ↔ Firestore: sincronización usuario 
 Room ↔ Firestore: sincronización hábitos 
 Firestore → Room: sincronización ranking real (Hito 4) 
 XP → Nivel → Puntos automáticos 
 Widget ↔ Room 
 StoreRepository ↔ assets/tienda.json (Hito 4) 

---

## 9. Problemas Encontrados y Soluciones

| Problema | Solución |
 **Sincronización Room ↔ Firestore**: hábitos no coincidían entre dispositivos -`HabitoRemoteRepository` con sync bidireccional en `loadUser()` 
 **Degradación offline del pingüino**: sed no se actualizaba tras días sin abrir - Cálculo con `ultimaVezSed` timestamp: `diasPasados × 0.1f`, mínimo 0.2f 
 **Foto de perfil > 1MB Firestore**: imágenes originales demasiado grandes -Compresión 300×300 JPEG 70% + Base64 
 **XP duplicada por misiones**: navegación entre pantallas repetía recompensas - Set en memoria `misionesEntregadasEnSesion` + tope XP 450 
 **Widget no se actualizaba**: al marcar desde la app seguía estado anterior - Broadcast `ACTION_APPWIDGET_UPDATE` en `refreshWidget()` 
 **Migraciones Room**: crashes al cambiar esquema - `fallbackToDestructiveMigration()` (datos críticos en Firestore) 
 **Búsqueda parcial Firestore**: no soporta LIKE  `whereGreaterThanOrEqualTo` + sufijo Unicode `\uf8ff` 
 **Ranking estático desactualizado** (Hito 4): JSON no reflejaba usuarios reales - Reescrito `RankingViewModel` para sincronizar desde Firestore con `getAllUsers()` 
 **StoreRepository sin contexto** (Hito 4): no tenía acceso a `assets/` - Añadido `AppModule.getContext()` como accessor público 

---

## 10. Futuras Mejoras

### 10.1 Funcionalidades Previstas No Implementadas


 **Notificaciones push (FCM)** Estructura preparada pero no integrada por falta de tiempo 
 **Suscripciones premium**  Entidad `Suscripcion` modelada, lógica de pago no implementada 
 **Invitaciones bidireccionales**  Entidad `Invitacion` modelada, flujo enviar/aceptar/rechazar pendiente 
 **Racha persistente en Room**  Entidad `Racha` existe pero el cálculo se hace en memoria 

### 10.2 Posibles Ampliaciones

- Múltiples mascotas desbloqueables
- Sistema de logros/achievements (insignias por hitos)
- Chat entre amigos vía Firestore Realtime
- Eventos temporales en la tienda (Navidad, Halloween)
- Hábitos compartidos / retos grupales
- Exportar estadísticas a PDF/CSV
- Categorías de hábitos (salud, estudio, deporte)

### 10.3 Mejoras Técnicas

- **Estado del pinguino**: Cambiar el estado de ánimo del pinguino dependiendo de si esta cuidado o no.
- **Caché de imágenes**: evitar decodificar Base64 en cada renderizado
- **Índices Firestore**: optimizar consultas compuestas

---

## 11. Cómo Ejecutar

1. Clonar el repositorio
2. Abrir en Android Studio 
3. Sincronizar Gradle
4. Configurar `google-services.json`
5. Ejecutar en emulador o dispositivo API 29+

---
