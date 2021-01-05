package com.github.lx200916.uploadtopastemeintellij.services

import PasteMeSettings
import PrivatePasteDialog
import PublicPasteDialog
import com.github.lx200916.uploadtopastemeintellij.getParentWindow
import com.github.lx200916.uploadtopastemeintellij.listeners.OnPrivateDialogListener
import com.github.lx200916.uploadtopastemeintellij.listeners.OnPublicDialogListener
import com.intellij.ide.BrowserUtil
import com.intellij.notification.*
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.ThrowableComputable
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.codeStyle.CodeStyleManager
import java.awt.EventQueue
import java.awt.datatransfer.StringSelection
import java.io.IOException
import javax.swing.JDialog


private fun getText(anActionEvent: AnActionEvent, file: VirtualFile): String {
    val editor = anActionEvent.getData(CommonDataKeys.EDITOR)

    val content = ReadAction.compute<String, RuntimeException> {
        try {
            return@compute FileDocumentManager.getInstance().getDocument(file)?.text
                    ?: String(file.contentsToByteArray(), file.charset)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return@compute null
        }
    }
//    println("file\n$content")
//    println("select\n" + editor?.selectionModel?.selectedText)
//    println("document\n" + editor?.document?.text)

    return (editor?.selectionModel?.selectedText ?: content).orEmpty()
}
private fun copyToClip(id: String){
    val copyPasteManager = CopyPasteManager.getInstance()
    val stringSelection = StringSelection("https://pasteme.cn/$id")
    copyPasteManager.setContents(stringSelection)
}
private fun Browseropen(id: String){
    BrowserUtil.browse("https://pasteme.cn/$id")
}
private fun createNoti(id: String, e: AnActionEvent, settings: PasteMeSettings) {
    val project = e.project
    if (settings.IsCopy){
        copyToClip(id)
    }


    println("ID:$id")
    run {
        val notification = NotificationGroup("Pasteme.Paste", NotificationDisplayType.BALLOON).createNotification("Paste Created", "PasteID is $id\n <a href= 'https://pasteme.cn/$id'>https://pasteme.cn/$id</a>", NotificationType.INFORMATION, NotificationListener.URL_OPENING_LISTENER)
        notification.addAction(object : NotificationAction("Copy To ClipBroad") {
            override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                copyToClip(id)
            }

        })
        notification.addAction(object : NotificationAction("Open in Browser") {
            override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                Browseropen(id)
            }

        })
        notification.notify(project)

    }
}

private fun createPaste(e: AnActionEvent, private: Boolean, content: String, lang: String) {
    val pasteMeSettings: PasteMeSettings = ServiceManager.getService(PasteMeSettings::class.java)

    val publicPasteDialog: JDialog
    if (private) {
        println(lang)

            publicPasteDialog = PrivatePasteDialog(lang, content, pasteMeSettings.defaultPass
                    ?: "", object : OnPrivateDialogListener {
                override fun onOkClicked(PrivatePasteDialog: PrivatePasteDialog) {
                    println("Private Ok ")
                    with(PrivatePasteDialog) {
                        println("Burn:$burnAfterRead")
                        if (burnAfterRead) {
                            val pasteIDstr = pasteID.text
                            if (pasteIDstr.length < 3) {
                                dispose()
                                PasteMeAPI.createPasteOnce(pasteContent, pasteLang, pastePass) { id ->
                                    createNoti(id, e, pasteMeSettings)
                                }
                            } else {
                                object : Backgroundable(e.project, "Checking PasteID") {
                                    override fun run(indicator: ProgressIndicator) {
//                                  Check if PasteID exists
                                        when (PasteMeAPI.testPasteID(pasteIDstr)) {
                                            1 -> {
                                                EventQueue.invokeLater { //
                                                    dispose()
                                                }
                                                PasteMeAPI.createPasteOncewithID(pasteContent, pasteLang, pastePass, pasteIDstr) { id: String ->
                                                    run {
                                                        createNoti(id, e, pasteMeSettings)
                                                    }
                                                }

                                            }
                                            0 -> {
                                                EventQueue.invokeLater { //
                                                    invalidPasteid()
                                                }
                                            }
                                            else -> {

                                                EventQueue.invokeLater {
                                                    Messages.showMessageDialog("Oops..Check Your Content or Your Token..", "Network Error", Messages.getErrorIcon())

                                                }
                                            }

                                        }
                                    }

                                    override fun onSuccess() {

                                    }
                                }.queue()
                            }


                        } else {
                            dispose()
                            PasteMeAPI.createPaste(pasteContent, pasteLang, pastePass) { id ->
                                run {
                                    createNoti(id, e, pasteMeSettings)
                                    if (pasteMeSettings.IsOpenB) {
                                        Browseropen(id);
                                    }
                                }
                            }
                        }


                    }
                }

                override fun onCancelClicked(PrivatePasteDialog: PrivatePasteDialog) {
                    with(PrivatePasteDialog) {
//                    Messages.showMessageDialog(pasteLang, pasteLang, com.intellij.openapi.ui.Messages.getErrorIcon())
                        dispose()

                    }
                }
            })

    } else {
        publicPasteDialog = PublicPasteDialog(lang, content, object : OnPublicDialogListener {
            override fun onOkClicked(PublicPasteDialog: PublicPasteDialog) {
                with(PublicPasteDialog) {
//                    Messages.showMessageDialog(pasteLang, pasteLang, com.intellij.openapi.ui.Messages.getWarningIcon())
                    println("Burn:$burnAfterRead")
                    if (burnAfterRead) {
                        val pasteIDstr = pasteID.text
                        if (pasteIDstr.length < 3) {
                            dispose()
                            PasteMeAPI.createPasteOnce(pasteContent, pasteLang, "") { id ->
                                createNoti(id, e, pasteMeSettings)
                            }
                        } else {
                            object : Backgroundable(e.project, "Checking PasteID") {
                                override fun run(indicator: ProgressIndicator) {
//                                  Check if PasteID exists
                                    when (com.github.lx200916.uploadtopastemeintellij.services.PasteMeAPI.testPasteID(pasteIDstr)) {
                                        1 -> {
                                            java.awt.EventQueue.invokeLater {
                                                dispose()
                                            }
                                            PasteMeAPI.createPasteOncewithID(pasteContent, pasteLang, "", pasteIDstr) { id: String ->
                                                run {
                                                    createNoti(id, e, pasteMeSettings)
                                                }
                                            }

                                        }
                                        0 -> {
                                            java.awt.EventQueue.invokeLater {
                                                invalidPasteid()
                                            }
                                        }
                                        else -> {

                                            EventQueue.invokeLater { //
                                                Messages.showMessageDialog("Oops..Check Your Content or Your Token..", "Network Error", Messages.getErrorIcon())
                                            }

                                        }

                                    }
                                }

                                override fun onSuccess() {

                                }
                            }.queue()
                        }


                    } else {
                        dispose()
                        PasteMeAPI.createPaste(pasteContent, pasteLang, "") { id ->
                            run {
                                createNoti(id, e, pasteMeSettings)
                                if (pasteMeSettings.IsOpenB) {
                                    Browseropen(id);
                                }
                            }
                        }
                    }


                }

            }


            override fun onCancelClicked(PublicPasteDialog: PublicPasteDialog) {
                with(PublicPasteDialog) {
//                    Messages.showMessageDialog(pasteLang, pasteLang, com.intellij.openapi.ui.Messages.getErrorIcon())
                    dispose()

                }
            }
        }

        )
    }


    with(publicPasteDialog) {
//                pack()
        setLocationRelativeTo(e.project?.getParentWindow())
        isVisible = true
    }
}


fun newPaste(e: AnActionEvent, private: Boolean) {
    val pasteMeSettings: PasteMeSettings = ServiceManager.getService(PasteMeSettings::class.java)
    val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
    val files = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
    val editor = e.getData(CommonDataKeys.EDITOR)
    if (editor == null && file == null && files == null) {
        println("22222")

        return
    }
    file?.let {

        if (file.isDirectory or (files!!.size != 1)) {
            Messages.showMessageDialog("Sorry, Only Single Text File is Supported.", "Error", Messages.getErrorIcon())
            return
        }
        if (file.fileType.isBinary) {
            Messages.showMessageDialog("Sorry, Only Single Text File is Supported.", "Error", Messages.getErrorIcon())
            return
        }
        if (file.length >= 102400 || file.length == 0L) {
            Messages.showMessageDialog("Sorry, Only Single Text File (<100KB) is Supported.", "Error", Messages.getErrorIcon())
            return
        }

//            Messages.showMessageDialog(file.name, "info", Messages.getInformationIcon());
        val type = file.fileType.defaultExtension
        val psi=e.getData(CommonDataKeys.PSI_FILE)!!
        val lang: String = psi.language.displayName
//        e.getData(CommonDataKeys.PSI_FILE)!!.language
        if (pasteMeSettings.IsFormat){
            WriteCommandAction.runWriteCommandAction(e.project!!) {
                val codeStyleManager: CodeStyleManager = CodeStyleManager.getInstance(e.project!!)
                println(e.project!!)
                try {
                    codeStyleManager.reformat(psi).text

                }
                catch (e:Exception){
                    e.printStackTrace()
            }
//
            }

        }

        val content = getText(e, file)
//            println(content)
        if (StringUtil.isEmptyOrSpaces(content)) {
            Messages.showMessageDialog("Sorry, No Content.", "Error", Messages.getErrorIcon())
            return
        }
        createPaste(e, private, content, lang)
        return


    }
    editor?.let {
        var text = ReadAction.compute(ThrowableComputable { editor.selectionModel.selectedText })
        if (text == null) {
            text = editor.document.text
        }

        if (StringUtil.isEmptyOrSpaces(text)) {
            return
        }
        createPaste(e, private, text, "Plain")
        return
    }
}
