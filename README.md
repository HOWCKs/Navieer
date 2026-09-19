# Navieer 🚀

> **Navieer** é um navegador Android moderno de alta performance que incorpora diretamente o motor de renderização **Servo** (desenvolvido em Rust com JavaScript SpiderMonkey da Mozilla), eliminando completamente dependências de Chromium (Blink/V8) e Gecko.

[![Build Android APK (Servo)](https://github.com/HOWCKs/Navieer/actions/workflows/build-apk.yml/badge.svg)](https://github.com/HOWCKs/Navieer/actions/workflows/build-apk.yml)

---

## 🌟 Destaques da Arquitetura

- **Motor Servo Nativo (Rust)**:
  - Totalmente livre de código Chromium e Blink.
  - Runtime JavaScript **SpiderMonkey** de alto desempenho.
  - Segurança de memória intrínseca garantida pelo Rust.
  - Renderização acelerada por hardware via EGL / OpenGL ES 3.0 no Android.
- **Frontend Moderno com Jetpack Compose**:
  - Interface declarativa reativa com Material 3 / Material You.
  - Integração contínua entre `SurfaceView` nativo do Servo e a árvore de UI do Jetpack Compose via `AndroidView`.
  - Suporte completo a tema Claro / Escuro dinâmico.
  - Gerenciamento de abas moderno com Bottom Sheet interativo.
  - Histórico de navegação, Favoritos locais e Omnibox inteligente com busca rápida.
- **CI/CD Automatizado com GitHub Actions**:
  - Compilação automatizada do APK instalável a cada `push`, `pull_request` ou `workflow_dispatch`.
  - Geração e upload do artefato `.apk` pronto para download direto na aba **Actions** do GitHub.

---

## 📥 Como Baixar o APK Compilado

1. Acesse a aba **[Actions](../../actions)** deste repositório no GitHub.
2. Clique na execução mais recente do fluxo **"Build Android APK (Servo)"**.
3. Na seção inferior intitulada **Artifacts**, clique em **`Navieer-Servo-arm64-v8a-apk`**.
4. O arquivo ZIP baixado contém o instalável `Navieer-Servo-arm64-v8a.apk`, pronto para instalação em dispositivos Android modernos (arquitetura ARM64 / `arm64-v8a`).

---

## 🛠️ Estrutura do Projeto

```
Navieer/
├── .github/
│   └── workflows/
│       └── build-apk.yml          # Pipeline de CI/CD para compilação do APK
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── AndroidManifest.xml # Permissões, intents do navegador, OpenGL ES 3.0
│   │       ├── java/com/navieer/browser/
│   │       │   ├── MainActivity.kt # Atividade principal, integração ServoView e UI
│   │       │   ├── model/Tab.kt    # Modelos de Abas, Favoritos e Histórico
│   │       │   └── ui/theme/       # Tema Material 3 e paleta de cores
│   │       └── res/                # Recursos de UI, strings, ícones adaptativos
│   ├── build.gradle.kts           # Configurações de compilação do módulo Android
│   └── proguard-rules.pro         # Regras de preservação de símbolos JNI do Servo
├── gradle/
│   └── wrapper/                   # Wrapper do Gradle
├── build.gradle.kts               # Configuração raiz de plugins
├── gradle.properties              # Otimizações de JVM e flags AndroidX
└── settings.gradle.kts            # Módulos e repositórios Maven
```

---

## 💻 Compilação Local

### Pré-requisitos
- JDK 17 (Temurin recomendado)
- Android SDK instalado com Build Tools 34+ e plataformas Android API 34+
- Dispositivo Android físico ou emulador com arquitetura ARM64 ou x86_64

```bash
# Baixar o AAR oficial do Servo se ainda não estiver em app/libs
mkdir -p app/libs
curl -fL -o app/libs/servo-aarch64-android.aar "https://github.com/servo/servo/releases/download/v0.5.0/servo-aarch64-android.aar"

# Compilar o APK Release
./gradlew assembleRelease
```

O arquivo gerado estará localizado em:
`app/build/outputs/apk/release/app-release.apk`
