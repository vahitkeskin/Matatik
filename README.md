<div align="center">

# 🧮 Matatik

### Yapay zekâ standartlarında, AST tabanlı **adım adım sembolik matematik çözüm motoru**
#### Kotlin Multiplatform · Compose Multiplatform · Clean Architecture · MVI

<br/>

![Kotlin](https://img.shields.io/badge/Kotlin-2.4.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Compose Multiplatform](https://img.shields.io/badge/Compose%20MP-1.11.1-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Platforms](https://img.shields.io/badge/Platforms-Android%20%7C%20iOS%20%7C%20Desktop-0A84FF?style=for-the-badge)
![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVI-00BFA5?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-FF6D00?style=for-the-badge)
![Tests](https://img.shields.io/badge/TDD-passing-success?style=for-the-badge)

<br/>

*Photomath, Microsoft Math Solver ve MATLAB'in sembolik motorlarından ilham alan;*
*matematiği **ham metin değil, tipli bir Soyut Sözdizimi Ağacı (AST)** olarak işleyen,*
*her dönüşümü açık bir log adımı olarak üreten elit bir CAS (Computer Algebra System) çekirdeği.*

</div>

---

## 📑 İçindekiler

1. [Vizyon ve Felsefe](#-vizyon-ve-felsefe)
2. [Öne Çıkan Özellikler](#-öne-çıkan-özellikler)
3. [Ekran Akışı](#-ekran-akışı)
4. [Teknoloji Yığını](#-teknoloji-yığını)
5. [Mimari Genel Bakış](#-mimari-genel-bakış)
6. [Modül ve Paket Yapısı](#-modül-ve-paket-yapısı)
7. [CAS Çekirdeği Derinlemesine](#-cas-çekirdeği-derinlemesine)
   - [Soyut Sözdizimi Ağacı (AST)](#1-soyut-sözdizimi-ağacı-ast)
   - [Tokenizer & Parser Dilbilgisi](#2-tokenizer--parser-dilbilgisi)
   - [LaTeX Renderer](#3-latex-renderer)
   - [Çözücüler ve Algoritmalar](#4-çözücüler-ve-algoritmalar)
   - [Veri Kontratları](#5-veri-kontratları)
8. [Sunum Katmanı (MVI)](#-sunum-katmanı-mvi)
9. [Glassmorphic Tasarım Sistemi](#-glassmorphic-tasarım-sistemi)
10. [Yerelleştirme (Kotlin Tabanlı)](#-yerelleştirme-kotlin-tabanlı)
11. [Dinamik Sürümleme](#-dinamik-sürümleme)
12. [Test ve Kalite (TDD)](#-test-ve-kalite-tdd)
13. [Kurulum ve Çalıştırma](#-kurulum-ve-çalıştırma)
14. [Yeni Bir Çözücü Ekleme](#-yeni-bir-çözücü-ekleme)
15. [Yeni Bir Dil Ekleme](#-yeni-bir-dil-ekleme)
16. [Yol Haritası](#-yol-haritası)
17. [Katkıda Bulunma](#-katkıda-bulunma)
18. [Geliştirici](#-geliştirici)
19. [Lisans](#-lisans)

---

## 🎯 Vizyon ve Felsefe

> **"Bitmiş olan, mükemmel olandan iyidir."** Tüm kod derlenebilir, sözdizimsel olarak kusursuz ve production-ready'dir. `TODO()` bloğu, atlanmış mantık veya placeholder yoktur.

Matatik üç temel ilke üzerine kuruludur:

| İlke | Açıklama |
|------|----------|
| **Cebir = AST** | Matematik asla string veya regex olarak ele alınmaz. Her denklem, tipli ve yapısal bir ifade ağacına ayrıştırılır. Adımlar bu ağaç üzerinde atomik yeniden-yazma işlemleridir (`Sₙ ⟹ Sₙ₊₁`). |
| **Decoupled Çekirdek** | İstemci yüksek performanslı reaktif bir durum makinesidir; matematik motoru ise platformdan tamamen bağımsız, standalone (sunucusuz) çalışan bir CAS tasarımıdır. |
| **Açık Dönüşüm Logları** | Her adım; uygulanan kuralı, lokalize açıklamayı, kullanılan formülü ve adım sonu LaTeX ifadesini taşır. Çözüm bir "kara kutu" değil, şeffaf bir öğretici akıştır. |

---

## ✨ Öne Çıkan Özellikler

- 🌳 **AST Tabanlı Motor** — Tokenizer → Recursive-Descent Parser → Tipli ifade ağacı → Çözücü → LaTeX log
- ➗ **Lineer Denklem Çözücü** — `ax + b = cx + d` için terim taşıma, sadeleştirme ve izolasyon adımları
- 🔢 **Logaritma Çözücü** — `log_b(n)` sayısal hesaplama (taban değiştirme) + `log_b(x) = c` üstel tanım
- 🧩 **Genişletilebilir** — Yeni konular (türev, integral, matris) tek bir `Solver` implementasyonuyla eklenir
- 🎨 **Glassmorphic UI** — Canvas ile çizilmiş yarı saydam cam yüzeyler, özel buton/kart efektleri
- 🌀 **Özel Canvas Splash** — Android native launcher-ikonlu splash kapatıldı; yerine dönen yörünge animasyonu
- 🌗 **Light / Dark / System** teması + **anlık system bar senkronu** (expect/actual)
- 🌐 **19 Dilli, Kotlin Tabanlı Yerelleştirme** — `strings.xml` yok; çalışma zamanında anlık dil değişimi
- 🔁 **MVI Reaktif Durum Makinesi** — `State / Intent / Effect` ile tek doğruluk kaynağı
- 🔢 **Dinamik Sürümleme** — Hardcoded sürüm metni yasak; Gradle kod-üretimiyle tek doğruluk kaynağı
- ✅ **TDD** — Parser, çözücüler, engine ve ViewModel için kapsamlı birim testleri
- 🖼 **Her Bileşende `@Preview`** — Hem Light hem Dark mod için

---

## 📱 Ekran Akışı

```
┌────────────────────┐      animasyon       ┌────────────────────────────┐
│   SplashScreen     │   bitince geçiş      │        SolverScreen        │
│  (Canvas anim.)    │  ───────────────▶    │  • Girdi alanı (cam kart)  │
│  dönen yörünge +   │                      │  • Örnek denklem çipleri   │
│  atan çekirdek     │                      │  • Çöz / Temizle butonları │
└────────────────────┘                      │  • Adım adım çözüm kartları │
                                            │  • Nihai cevap kartı       │
                                            └────────────────────────────┘
```

---

## 🛠 Teknoloji Yığını

| Katman | Teknoloji |
|--------|-----------|
| **Dil** | Kotlin 2.4.0 (KMP) |
| **UI** | Compose Multiplatform 1.11.1, Material 3 |
| **Hedefler** | Android (minSdk 24, compileSdk 36), iOS (arm64 + simulatorArm64), Desktop/JVM |
| **Asenkron** | Kotlinx Coroutines 1.11.0 |
| **Serileştirme** | Kotlinx Serialization 1.9.0 (JSON) |
| **Durum Yönetimi** | Jetpack ViewModel (Multiplatform) + MVI |
| **Mimari** | Clean Architecture + MVI |
| **Test** | kotlin-test, kotlinx-coroutines-test |
| **Build** | Gradle (Kotlin DSL), Version Catalog (`libs.versions.toml`) |

> **Modern kod garantisi:** Deprecated API kullanılmaz. Sistem barı kontrolü dahi
> `window.statusBarColor` (deprecated) yerine `WindowInsetsControllerCompat` ile yapılır.

---

## 🏛 Mimari Genel Bakış

Clean Architecture katmanları, paket düzeyinde kesin olarak ayrılmıştır. Bağımlılık oku
**daima içe doğrudur** — `presentation` ve `ui`, `domain`'e bağımlıdır; `domain` hiçbir
şeye bağımlı değildir (saf Kotlin + serialization).

```
                          ┌─────────────────────────────────────────┐
                          │                  UI                      │
                          │  Compose · Glassmorphism · Splash · Theme │
                          └───────────────────┬─────────────────────┘
                                              │ gözlemler / intent gönderir
                          ┌───────────────────▼─────────────────────┐
                          │              PRESENTATION                │
                          │   MVI: State / Intent / Effect + VM      │
                          └───────────────────┬─────────────────────┘
                                              │ çağırır
              ┌───────────────────────────────▼─────────────────────────────┐
              │                          DOMAIN (saf)                        │
              │  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────────┐  │
              │  │  model   │  │   ast    │  │  parser  │  │    engine    │  │
              │  │ kontrat  │  │  Expr    │  │ Tokenizer│  │  Solver'lar  │  │
              │  │ (serial.)│  │ Renderer │  │  Parser  │  │   + Facade   │  │
              │  └──────────┘  └──────────┘  └──────────┘  └──────────────┘  │
              └─────────────────────────────────────────────────────────────┘
                                              ▲
                          ┌───────────────────┴─────────────────────┐
                          │              LOCALIZATION                │
                          │  Language · LocalizedStrings · TR/EN ... │
                          └─────────────────────────────────────────┘
```

**Çözüm akışı (uçtan uca):**

```
"2x + 3 = 7"
     │  Tokenizer
     ▼
[NUMBER(2), IDENT(x), PLUS, NUMBER(3), EQUALS, NUMBER(7), EOF]
     │  Parser (recursive-descent + örtük çarpım)
     ▼
Equation( Add(Mul(Num 2, Var x), Num 3) = Num 7 )
     │  MathSolverEngine → uygun Solver seçimi
     ▼
LinearEquationSolver  →  a=2, c=4  →  x = 2
     │  her adım LaTeX + vurgu + kural anahtarı ile loglanır
     ▼
MathematicalSolution( steps = [1..5], finalAnswer = "x = 2" )
     │  LatexDisplay (LaTeX → Unicode)
     ▼
UI: animasyonlu adım kartları
```

---

## 📦 Modül ve Paket Yapısı

```
Matatik/
├── androidApp/                 # Android uygulama giriş noktası (MainActivity)
│   └── src/main/res/
│       ├── values/themes.xml   # Özel tema — native splash kapatıldı
│       └── values/colors.xml   # splash_background (Compose splash ile eşleşir)
├── iosApp/                     # iOS giriş noktası (SwiftUI host)
├── desktopApp/                 # Desktop/JVM giriş noktası
└── shared/                     # ⭐ Tüm ortak kod
    └── src/
        ├── commonMain/kotlin/com/vahitkeskin/matatik/
        │   ├── core/
        │   │   ├── domain/
        │   │   │   ├── model/          # @Serializable veri kontratları
        │   │   │   ├── ast/            # Expr (AST) + LatexRenderer
        │   │   │   ├── parser/         # Tokenizer + Parser
        │   │   │   └── engine/         # Solver'lar + MathSolverEngine
        │   │   └── localization/       # Language, LocalizedStrings, TrString, EnString
        │   ├── presentation/solver/    # MVI: State/Intent/Effect + SolverViewModel
        │   ├── ui/
        │   │   ├── theme/              # Color, Theme, SystemBars (expect)
        │   │   ├── components/         # GlassCard, GlassButton, SolutionStepCard
        │   │   ├── math/               # LatexDisplay (LaTeX → Unicode)
        │   │   ├── splash/             # Canvas SplashScreen
        │   │   └── solver/             # SolverScreen
        │   └── App.kt                  # Kök composable (splash → solver)
        ├── androidMain/  ...ui/theme/SystemBars.android.kt   # WindowInsetsController
        ├── iosMain/      ...ui/theme/SystemBars.ios.kt       # no-op
        ├── jvmMain/      ...ui/theme/SystemBars.jvm.kt       # no-op
        └── commonTest/                 # Parser/Solver/Engine/ViewModel testleri
```

---

## 🧠 CAS Çekirdeği Derinlemesine

### 1. Soyut Sözdizimi Ağacı (AST)

`Expr` mühürlü (sealed) bir arayüzdür; her matematiksel yapı tipli bir düğümdür:

```kotlin
sealed interface Expr {
    data class Num(val value: Double) : Expr
    data class Variable(val name: String) : Expr
    data class Add(val left: Expr, val right: Expr) : Expr
    data class Sub(val left: Expr, val right: Expr) : Expr
    data class Mul(val left: Expr, val right: Expr) : Expr
    data class Div(val left: Expr, val right: Expr) : Expr
    data class Pow(val base: Expr, val exponent: Expr) : Expr
    data class Neg(val arg: Expr) : Expr
    data class Log(val base: Expr, val arg: Expr) : Expr   // log_base(arg)
    data class Ln(val arg: Expr) : Expr                    // ln(arg)
}

data class Equation(val lhs: Expr, val rhs: Expr)
```

### 2. Tokenizer & Parser Dilbilgisi

**Tokenizer** Unicode operatörleri de tanır (`−`, `×`, `⋅`, `÷`). **Parser** öncelik
artan sırada özyinelemeli iniş kullanır ve **örtük çarpmayı** destekler (`2x`, `3(x+1)`,
`(x+1)(x-1)`):

```ebnf
equation := expr ('=' expr)?
expr     := term (('+' | '-') term)*
term     := unary ( ('*' | '/') unary | implicitMul )*
unary    := '-' unary | power
power    := atom ('^' unary)?              // sağ-ilişkili
atom     := NUMBER | function | IDENT | '(' expr ')'
function := 'ln' '(' expr ')'
          | 'log' ('_' NUMBER)? '(' expr ')'   // taban verilmezse 10
```

### 3. LaTeX Renderer

`LatexRenderer`, AST'yi **öncelik-duyarlı, en sade parantezleme** ile LaTeX'e çevirir.
Katsayı-değişken çarpımlarında `\cdot` yerine bitişik gösterim (`2x`) kullanır,
bölmeyi `\frac{...}{...}` olarak yazar. UI tarafında `LatexDisplay`, harici bağımlılık
olmadan bu LaTeX'i okunur **Unicode** matematiğe dönüştürür (`x^{2}` → `x²`, `\frac{4}{2}` → `(4)/(2)`).

### 4. Çözücüler ve Algoritmalar

Her çözücü `Solver` arayüzünü uygular:

```kotlin
interface Solver {
    val topic: MathTopic
    fun canSolve(equation: Equation): Boolean   // yan etkisiz
    fun solve(equation: Equation): MathematicalSolution
}
```

#### ➗ `LinearEquationSolver` (Temel Cebir)
İfadeyi `a·x + b` lineer biçimine indirger (`AlgebraUtils.toLinearForm`). Lineer olmayan
yapı (örn. `x²`, `x·x`, `log(x)`) görülürse `NotLinearException` ile reddedilir. Üretilen adımlar:

1. Verilen denklem
2. Değişkenleri sola, sabitleri sağa taşı
3. Benzer terimleri sadeleştir → `A·x = C`
4. Her iki tarafı katsayıya böl → `x = C / A` *(katsayı 1 ise atlanır)*
5. Sonuç → `x = değer`

#### 🔢 `LogarithmSolver` (Logaritma)
İki kalıbı tanır:
- **Hesaplama** `log_b(n)` → taban değiştirme `\log_b(a) = \frac{\ln a}{\ln b}` → sayısal değer
- **Denklem** `log_b(x) = c` → üstel tanım `\log_b(x) = c \iff x = b^c`

#### 🎛 `MathSolverEngine` (Facade)
Ham metni ayrıştırır, kayıtlı çözücüler arasından ilk `canSolve` dönenini seçer, eşleşme
yoksa `UnsupportedProblemException` fırlatır. Tamamen standalone çalışır.

### 5. Veri Kontratları

Tüm çözüm logları `@Serializable`'dır — platformlar arası ve istemci-sunucu sınırında kayıpsız taşınır:

```kotlin
@Serializable
data class SolutionStep(
    val stepNumber: Int,
    val ruleApplied: String,
    val descriptionLocalizationKey: String,   // örn. "rules.log.change_of_base"
    val formulaUsedLatex: String,
    val currentExpressionLatex: String,
    val highlightedParts: List<HighlightedCoordinate>
)

@Serializable
data class MathematicalSolution(
    val id: String,
    val topic: MathTopic,                  // BASIC_ALGEBRA, LOGARITHM, ...
    val baseDifficulty: DifficultyLevel,   // EASY, MEDIUM, HARD
    val rawEquationLatex: String,
    val steps: List<SolutionStep>,
    val finalAnswerLatex: String
)
```

---

## 🔁 Sunum Katmanı (MVI)

`SolverViewModel` reaktif bir durum makinesidir. Tek bir `onIntent` girişi, tek bir
`StateFlow` çıkışı ve tek seferlik olaylar için bir `SharedFlow` (`SolverEffect`) yayar.

```kotlin
sealed interface SolverIntent {
    data class InputChanged(val value: String) : SolverIntent
    data object Solve : SolverIntent
    data object Clear : SolverIntent
    data class ChangeLanguage(val language: Language) : SolverIntent
    data class LoadExample(val expression: String) : SolverIntent
}
```

| MVI Bileşeni | Sınıf |
|--------------|-------|
| **State** | `SolverState` (girdi, dil, yükleniyor, çözüm, hata, örnekler) |
| **Intent** | `SolverIntent` |
| **Effect** | `SolverEffect` (ShowError, Solved) |
| **Reducer** | `SolverViewModel.onIntent(...)` |

---

## 🎨 Glassmorphic Tasarım Sistemi

iOS'un son sürümlerinden ilham alan, **Canvas tabanlı** yarı saydam cam yüzeyler:

- **`GlassCard`** — `drawWithCache` ile degrade dolgu + üst kenar parlaması (specular highlight) + ince alfa kenarlığı
- **`GlassButton`** — degrade dolgu + cam parlaması
- **`SolutionStepCard`** — numara rozeti, kural başlığı, lokalize açıklama, formül çipi, vurgulu ifade; kademeli (staggered) giriş animasyonu
- **`SplashScreen`** — tamamen Canvas: dönen sweep-gradient yörünge halkaları, ters dönen iç halka, sinüs ile atan çekirdek, yörüngedeki operatör noktaları

**Tema:** `MatatikTheme(themeMode)` — `SYSTEM / LIGHT / DARK`. `SyncSystemBars` expect/actual'ı,
StatusBar ve NavigationBar ikon kontrastını arka planla anlık eşitler. Her özel bileşenin
hem Light hem Dark `@Preview`'ı vardır.

| Renk | Hex | Kullanım |
|------|-----|----------|
| Violet | `#7C4DFF` | Birincil aksan / değişken vurgusu |
| Teal | `#00BFA5` | İkincil / sabit vurgusu |
| Orange | `#FF6D00` | Sonuç vurgusu |
| Blue | `#2979FF` | İşlem vurgusu |

---

## 🌐 Yerelleştirme (Kotlin Tabanlı)

`strings.xml` **kullanılmaz**. Her dil, camelCase anahtarlı bir Kotlin `object`'idir.
`Language` enum'u 19 dili (`code`, `nativeName`, `flag`, `isRtl`) ile tanımlar. ViewModel
ile çalışma zamanında anlık değişim.

```kotlin
enum class Language(val code, val nativeName, val flag, val isRtl) {
    TR, EN, JA, DE, RU, FR, ES, HI, AR(isRtl=true), AZ,
    ZH, PT, ID, KO, IT, NL, VI, TH, PL
}
```

> **Durum:** TR ve EN tamamen çevrilidir. Henüz çevrilmemiş diller, uluslararası taban
> olan İngilizce'ye düşer (`Localization` kayıt defteri). Yeni bir dili aktive etmek için
> yalnızca bir `*String.kt` object'i yazıp kayıt defterine eklemek yeterlidir.

---

## 🔢 Dinamik Sürümleme

Hardcoded `text = "v1.0"` **yasaktır**. Sürüm, `gradle.properties` içindeki tek doğruluk
kaynağından (`matatik.versionName`) okunur ve bir Gradle kod-üretim göreviyle
(`generateAppVersion`) `AppVersion.NAME` sabitine yazılır:

```kotlin
// shared/build.gradle.kts
val generateAppVersion = tasks.register("generateAppVersion") { ... }
kotlin.sourceSets.named("commonMain") { kotlin.srcDir(generateAppVersion) }
```

UI yalnızca `AppVersion.NAME` referansını kullanır.

---

## 🧪 Test ve Kalite (TDD)

Proje Test-Driven Development ile geliştirilir. `commonTest` altında izole birim testleri:

| Test | Kapsam |
|------|--------|
| `ParserTest` | Örtük çarpım, denklemsiz girdi, log ayrıştırma, üs önceliği, hatalı karakter |
| `LinearEquationSolverTest` | Basit denklem, iki taraflı değişken, kesirli sonuç, lineer-olmayan reddi, adım numarası sırası |
| `LogarithmSolverTest` | Sayısal hesaplama, üstel tanımla denklem, doğal logaritma |
| `MathSolverEngineTest` | Doğru çözücü seçimi, desteklenmeyen problem, boş girdi, güvenli `canSolve` |
| `SolverViewModelTest` | Geçerli/geçersiz girdi akışı, dil değişimi, temizleme (coroutines-test) |

### Testleri Çalıştırma

```bash
# Tüm ortak birim testleri (JVM hedefinde)
./gradlew :shared:jvmTest

# Tüm test lifecycle'ı
./gradlew test

# Android tarafının derlendiğini doğrula
./gradlew :androidApp:assembleDebug
```

---

## 🚀 Kurulum ve Çalıştırma

### Önkoşullar
- JDK 17+
- Android Studio (en güncel) / IntelliJ IDEA
- iOS için: macOS + Xcode

### Klonlama
```bash
git clone https://github.com/vahitkeskin/Matatik.git
cd Matatik
```

### Platform Bazında Çalıştırma

| Platform | Komut |
|----------|-------|
| **Android** | `./gradlew :androidApp:assembleDebug` *(veya IDE run widget)* |
| **Desktop (hot reload)** | `./gradlew :desktopApp:hotRun --auto` |
| **Desktop (standart)** | `./gradlew :desktopApp:run` |
| **iOS** | `iosApp/` dizinini Xcode'da açıp çalıştırın |

---

## ➕ Yeni Bir Çözücü Ekleme

```kotlin
class DerivativeSolver : Solver {
    override val topic = MathTopic.CALCULUS_DERIVATIVE
    override fun canSolve(equation: Equation): Boolean { /* ... */ }
    override fun solve(equation: Equation): MathematicalSolution { /* adımları üret */ }
}
```

Ardından `MathSolverEngine.defaultSolvers()` listesine ekleyin — facade otomatik olarak
uygun girdilerde bu çözücüyü seçecektir. Yeni kural anahtarlarını `RuleKeys`'e ve
çevirilerini `TrString`/`EnString`'e ekleyin.

---

## 🌍 Yeni Bir Dil Ekleme

1. `core/localization/` altında `JaString.kt` gibi bir `object` oluşturun, `LocalizedStrings`'i uygulayın.
2. `Localization.registry` haritasına `Language.JA to JaString` satırını ekleyin.
3. Tamamlandı — dil seçici otomatik olarak yeni dili gösterir.

---

## 🗺 Yol Haritası

- [x] Lineer denklem çözücü
- [x] Logaritma çözücü (hesaplama + denklem)
- [x] Glassmorphic UI + Canvas splash
- [x] MVI + ViewModel
- [x] TR / EN yerelleştirme + 19 dilli altyapı
- [ ] Kalan 17 dilin tam çevirisi
- [ ] Türev çözücü (`CALCULUS_DERIVATIVE`)
- [ ] İntegral çözücü (`CALCULUS_INTEGRAL`)
- [ ] Matris işlemleri (`MATRIX_OPERATIONS`)
- [ ] İkinci dereceden denklem (diskriminant)
- [ ] Tam LaTeX dizgici (gerçek matematik gösterimi)
- [ ] Çözüm geçmişi (Room Multiplatform) ve ayarlar (DataStore)
- [ ] Kamera ile denklem tanıma (OCR)

---

## 🤝 Katkıda Bulunma

1. Fork'layın ve bir özellik dalı açın: `git checkout -b feature/harika-ozellik`
2. TDD ile geliştirin; `./gradlew :shared:jvmTest` yeşil kalmalı.
3. Deprecated API kullanmayın, Kotlin 2.x pratiklerine uyun.
4. Her yeni UI bileşeni için Light + Dark `@Preview` ekleyin.
5. PR açın.

---

## 👨‍💻 Geliştirici

<div align="center">

### Vahit Keskin
**Senior Android & Computer Engineer**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/vahit-keskin/)
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/vahitkeskin)
[![Instagram](https://img.shields.io/badge/Instagram-E4405F?style=for-the-badge&logo=instagram&logoColor=white)](https://www.instagram.com/keskin.vahit/)
[![Play Store](https://img.shields.io/badge/Play%20Store-414141?style=for-the-badge&logo=googleplay&logoColor=white)](https://play.google.com/store/apps/developer?id=vahitkeskin)

📧 **Mail:** vahitkeskin07@gmail.com  ·  📱 **Tel:** +90 551 044 43 06

**Öne çıkan uygulama — iRadix:**
[Google Play'de Görüntüle](https://play.google.com/store/apps/details?id=com.vahitkeskin.iradix)

</div>

---

## 📄 Lisans

Bu proje MIT Lisansı ile lisanslanmıştır.

```
MIT License · Copyright (c) 2026 Vahit Keskin
```

<div align="center">
<br/>
<sub>⭐ Beğendiyseniz yıldız vermeyi unutmayın — Kotlin Multiplatform ve Compose ile ❤️ ile geliştirildi.</sub>
</div>
