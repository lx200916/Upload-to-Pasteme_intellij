package com.github.lx200916.uploadtopastemeintellij.services

import com.intellij.openapi.project.Project
import com.github.lx200916.uploadtopastemeintellij.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
