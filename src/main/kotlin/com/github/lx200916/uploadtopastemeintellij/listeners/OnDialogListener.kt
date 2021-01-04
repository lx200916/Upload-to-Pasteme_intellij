package com.github.lx200916.uploadtopastemeintellij.listeners

import PrivatePasteDialog
import PublicPasteDialog

interface OnPublicDialogListener {
    fun onOkClicked(PublicPasteDialog: PublicPasteDialog)

    fun onCancelClicked(PublicPasteDialog: PublicPasteDialog)
}

interface OnPrivateDialogListener {
    fun onOkClicked(PrivatePasteDialog: PrivatePasteDialog)

    fun onCancelClicked(PrivatePasteDialog: PrivatePasteDialog)
}