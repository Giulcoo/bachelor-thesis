# Vorlage für Dokumentation und Abschlussarbeiten

## Installation

### Visual Studio Code + TeX Live
- [TeX Live](https://www.tug.org/texlive/) installieren
- Erweiterung für VSCode: [LaTex-Workshop](https://github.com/James-Yu/LaTeX-Workshop/wiki)

Folgende Packages müssen zunächst installiert werden:

#### Windows
```bash
$ tlmgr install footmisc siunitx paralist wrapfig minted acronym xstring bigfoot csquotes din1505
```


#### MAC
```bash
$ sudo tlmgr install \
  multirow footmisc siunitx paralist wrapfig minted acronym xstring bigfoot csquotes din1505
```

### IntelliJ + MiKTex
- Plugin für IntelliJ: [TeXiFy IDEA](https://plugins.jetbrains.com/plugin/9473-texify-idea)
- Weitere Schritte die nötig sind für das Plugin: https://github.com/Hannah-Sten/TeXiFy-IDEA/wiki/Installation
- Für die Darstellung von Code wird das "minted"-Package verwendet. Damit dies kompiliert muss folgendes erledigt werden:
https://alipourmousavi.com/blog/index.php/2018/02/08/using-minted-package-in-latex-to-format-codes/
