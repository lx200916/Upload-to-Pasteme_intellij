package com.github.lx200916.uploadtopastemeintellij.privatepaste

import com.github.lx200916.uploadtopastemeintellij.services.newPaste
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class NewPaste : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        newPaste(e, private = true)
    }
}