# Navieer 🚀⚡

> **Navieer Cyber Edition** é um navegador Android de alto desempenho com interface moderna e imersiva inspirada no universo **Cyberpunk / Gamer HUD / Esports Dashboards**, incorporando nativamente o motor web de ponta **Servo** (escrito em Rust com SpiderMonkey JavaScript), eliminando 100% qualquer dependência de Chromium (Blink/V8) e Gecko.

[![Build Android APK (Servo)](https://github.com/HOWCKs/Navieer/actions/workflows/build-apk.yml/badge.svg)](https://github.com/HOWCKs/Navieer/actions/workflows/build-apk.yml)

---

## 🎮 Destaques do Design System Cyber-Gamer HUD

- **Identidade Visual Gamer Sofisticada & Cyber HUD**:
  - Paleta com fundo profundo de obsidiana (`#0B0E14`), superfícies de painel tecnológico (`#141923`) e tema AMOLED absoluto (`#000000`).
  - Destaques em neon cibernético: **Electric Cyan** (`#00F0FF`), **Neon Violet** (`#A855F7`), **Matrix Green** (`#22C55E`), **Cyber Amber** (`#F59E0B`) e **Overheat Red** (`#EF4444`).
  - Bordas com iluminação sutil (glowing borders) e cantos chamfered / squircle arredondados (`RoundedCornerShape` de 12.dp a 24.dp).
  - Tipografia tática em estilo telemetria com fontes monoespaçadas legíveis e de alto contraste.
  - Zero poluição visual: elementos limpos, sem animações contínuas que degradem bateria ou desempenho da GPU.

- **Central de Comando & Speed Dial Deck**:
  - Anel duplo de telemetria holográfica em degradê ciano/violeta exibindo status do runtime do Servo e arquitetura de 64 bits.
  - Barra de telemetria em tempo real com indicadores de proteção (`ESCUDO ATIVO`, `SERVO RUST`, `60+ FPS LOCK`).
  - Módulos táticos de lançamento rápido organizados por pods (`SERVO RUNTIME`, `RUST CRATES`, `GITHUB CORE`, `WIKIPEDIA`, `DUCKDUCKGO`, `CYBER NEWS`).
  - Botão de desdobramento de novos alvos (`+ DEPLOY POD`).

- **Cápsula de Navegação Omnibox Flutuante**:
  - Cápsula flutuante HUD com borda iluminada em degradê e indicador de encriptação em tempo real (Verde Matriz para TLS/HTTPS, Ciano para sistema interno e Vermelho Alerta para tráfego puro).
  - Dropdown tático de predição de busca (`// RADAR QUERY PREDICTIONS`) com tags de identificação (`[SEARCH]`, `[SAVED]`, `[HISTORY]`, `[WEB POD]`).
  - Indicador numérico de módulos ativos em formato de munição (`[01]`, `[02]`, etc.).

- **Matriz Tática de Abas (Modules vs Stealth Ops)**:
  - Seletor segmentado com alternância rápida entre módulos normais e operações secretas (`STEALTH`).
  - Visualização em cartões de circuito cibernético com botão de encerramento rápido e botão de expansão inferior em degradê tático.

- **Terminal de Telemetria e Defesa do Site**:
  - Terminal de diagnóstico de segurança com status do túnel criptografado, cópia de payload, transmissão e edição com um toque.
  - Painel de expurgo seguro de cookies e armazenamento local por domínio.
  - Chaves de alternância com iluminação de neon para cada protocolo: Localização, Câmera, Microfone e Notificações Push.

- **Sheets de Histórico, Favoritos, Downloads e Flags**:
  - **Histórico**: Registro tático de telemetria com busca em tempo real e botão de expurgo instantâneo.
  - **Favoritos**: Targets fixados com badges em Cyber Amber.
  - **Downloads**: Monitor de transferência de pacotes de dados com progresso em neon.
  - **Servo Flags**: Painel de overclocking do motor com flags de hardware (WebGPU wgpu, dark mode forçado, etc.).

---

## 🌟 Recursos do Motor Servo (Rust + SpiderMonkey)

- **Rolagem Nativa Fluida no Motor Servo**:
  - Eventos de toque (`touchDown`, `touchMove`, `touchUp`, pinch e drag) transmitidos diretamente para o backend EGL/Rust do Servo sem interceptação de ponteiros.
  - Navegação suave por gestos de voltar integrados ao sistema operacional Android (`Predictive Back / BackHandler`).

- **100% Livre de Chromium e Gecko**:
  - Paralelismo avançado de layout e renderização com segurança de memória garantida em Rust.
  - Aceleração por hardware OpenGL ES 3.0 e suporte a WebGPU.

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
