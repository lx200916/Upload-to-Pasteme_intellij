# Upload-to-Pasteme_intellij

PasteMe.cn的IntelliJ插件版本.可以将IntelliJ的大部分输出上传到PasteMe.cn<br>
      <em>您可能需要开发者秘钥才能使用</em>

## Why This

* 从`Intellij` 的`Project`视图\`Editor`视图\甚至`Compiler Output`中直接上传文件内容\错误信息(或选定部分),而无需离开IDE
* 与`Intellij`协调的UI设计与内建通知
* 借助`Intellij PSI FILE`API获取更准确的语言信息以供高亮
* 当然,以上所述特性可适用于2020.1以后的全系Jetbrains IDE.

## ToDo list

- [x] Public / Private Paste Upload
- [x] Burn After Read
- [x] IntelliJ Built-in Notification
- [x] Auto Identify Language
- [ ] Insert File Name into Paste
- [ ] Download Paste By ID
- [ ] Auth Service

<!-- Plugin description -->
PasteMe.cn的IntelliJ插件版本.可以将IntelliJ的大部分输出上传到PasteMe.cn<br>
      <em>您可能需要开发者秘钥才能使用</em>
<!-- Plugin description end -->

## Credits

本项目在编写时参考了以下项目的源代码或设计思想:

* [Luogu-Intellij](https://github.com/HoshinoTented/luogu-intellij)
* [Use Gists ](https://github.com/silvafabio/use-gists)
* [PasteMe Backend](https://github.com/PasteUs/PasteMeGoBackend)
* [Intellij 官方GitHub拓展](https://github.com/JetBrains/intellij-community/tree/master/plugins/github)

## Screenshots

![image-20210104185415898](https://tva1.sinaimg.cn/large/0081Kckwly1gmbv0lrd38j30dq0flq4v.jpg)



## Installation

- Using IDE built-in plugin system:
  
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Upload-to-Pasteme_intellij"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/lx200916/Upload-to-Pasteme_intellij/releases/latest) and install it manually using
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
