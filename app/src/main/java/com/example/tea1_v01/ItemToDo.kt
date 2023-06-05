package com.example.tea1_v01

//La classe ItemToDo représente les tâches à réaliser dans une ToDoList
class ItemToDo {
    private var description: String = ""
    private var fait: Boolean = false

    constructor()

    constructor(description: String) {
        this.description = description
    }

    constructor(description: String, fait: Boolean) {
        this.description = description
        this.fait = fait
    }

    fun setDescription(uneDescription: String) {
        description = uneDescription
    }

    fun getDescription(): String {
        return description
    }

    fun setFait(estFait: Boolean) {
        fait = estFait
    }

    override fun toString(): String {
        return "'$description' : fait=$fait)"
    }
}

