# Navieer 🚀

> **Navieer** é um navegador Android moderno de alta performance baseado fielmente nas diretrizes de design do **Material 3 Expressive** e que incorpora nativamente o motor web **Servo** (desenvolvido em Rust com SpiderMonkey JavaScript), eliminando 100% qualquer dependência de Chromium (Blink/V8) e Gecko.

[![Build Android APK (Servo)](https://github.com/HOWCKs/Navieer/actions/workflows/build-apk.yml/badge.svg)](https://github.com/HOWCKs/Navieer/actions/workflows/build-apk.yml)

---

## 🌟 Destaques da Arquitetura e Recursos

- **Design System Material 3 Expressive Real**:
  - Aplicação estrita das diretrizes do Material 3 Expressive em toda a interface e experiência do usuário (UI/UX).
  - Cantos suaves e arredondados (`RoundedCornerShape(20.dp)` a `28.dp` e botões em formato de pílula) sem recortes ou toques com aparência quadrada.
  - Ícones funcionais e oficiais em vetor Compose (sem emojis como ícones).
  - Tema AMOLED Dark puro (`#000000`) para máxima economia de energia em telas OLED/AMOLED, além de tema dinâmico e Expressive Light.

- **Rolagem Nativa Fluida no Motor Servo**:
  - Eventos de toque (`touchDown`, `touchMove`, `touchUp`, pinch e drag) transmitidos diretamente para o backend EGL/Rust do Servo sem interceptação de ponteiros.
  - Navegação suave por gestos de voltar integrados ao sistema operacional Android (`Predictive Back / BackHandler`).

- **Escrita Inteligente e Sugestões na Omnibox**:
  - Autocompletar inteligente em tempo real enquanto o usuário digita na barra de endereços.
  - Sugestões integradas de sites populares, termos de busca, favoritos salvos e histórico recente.

- **Painel de Segurança e Permissões do Site**:
  - Acesso direto com 1 toque ao ícone de cadeado/segurança na barra de pesquisa.
  - Detalhes de criptografia e status HTTPS/TLS ou HTTP.
  - Visualização, cópia com 1 toque, compartilhamento e edição da URL completa.
  - Gerenciamento e limpeza de cookies e dados de armazenamento local por domínio.
  - Controle granular de permissões por site: Localização, Câmera, Microfone, Notificações e Navieer Shield (Bloqueio de Anúncios e Rastreadores).

- **Motor Servo Nativo (Rust + SpiderMonkey)**:
  - 100% livre de Chromium e Gecko.
  - Paralelismo avançado de layout e renderização com segurança de memória em Rust.
  - Suporte a flags experimentais em `navieer://flags` (WebGPU wgpu, modo escuro forçado, etc.).

---

## 📥 Como Baixar o APK Compilado

1. Acesse a aba **[Actions](../../actions)** deste repositório no GitHub.
2. Clique na execução mais recente com status verde do fluxo **"Build Android APK (Servo)"**.
3. Na seção **Artifacts**, clique em **`Navieer-Servo-arm64-v8a-apk`**.
4. O arquivo ZIP contém o instalável `Navieer-Servo-arm64-v8a.apk`, pronto para instalação em dispositivos Android modernos (arquitetura ARM64 / `arm64-v8a`).

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
│   │       │   ├── MainActivity.kt # Atividade principal, renderização ServoView e navegação
│   │       │   ├── data/
│   │       │   │   ├── AdBlockRules.kt      # Regras de bloqueio de anúncios e telemetria
│   │       │   │   ├── BrowserPreferences.kt# DataStore Preferences (buscadores, AMOLED, etc.)
│   │       │   │   └── SmartSuggestions.kt  # Motor de sugestões e autocompletar da Omnibox
│   │       │   ├── model/
│   │       │   │   └── Tab.kt               # Modelos de Abas, Favoritos, Histórico e Permissões
│   │       │   ├── ui/
│   │       │   │   ├── components/
│   │       │   │   │   ├── BookmarksSheet.kt   # Modal de favoritos
│   │       │   │   │   ├── DownloadsSheet.kt   # Modal de downloads
│   │       │   │   │   ├── FlagsSheet.kt       # Servo Flags (navieer://flags)
│   │       │   │   │   ├── HistorySheet.kt     # Modal de histórico com busca
│   │       │   │   │   ├── OmniboxFloatingBar.kt # Barra de busca e endereço Material 3
│   │       │   │   │   ├── ReaderView.kt       # Modo de leitura simplificado
│   │       │   │   │   ├── SettingsSheet.kt    # Configurações do navegador
│   │       │   │   │   ├── SiteInfoSheet.kt    # Informações de segurança, cookies e permissões
│   │       │   │   │   ├── SpeedDialView.kt    # Página inicial com atalhos em vetores
│   │       │   │   │   └── TabGridView.kt      # Grade de cartões de abas normais e privadas
│   │       │   │   └── theme/
│   │       │   │       └── Theme.kt            # Paleta Material 3 Expressive e AMOLED puro
│   │       └── res/                            # Recursos de UI, strings, ícones adaptativos
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
