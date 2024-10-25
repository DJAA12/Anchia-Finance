package Entities

class Category {
    private var _id: String = ""
    private var _name: String = ""

    constructor()

    constructor(id: String, name: String) {
        this._id = id
        this._name = name
    }

    var id: String
        get() = this._id
        set(value) { this._id = value }

    var name: String
        get() = this._name
        set(value) { this._name = value }
}
