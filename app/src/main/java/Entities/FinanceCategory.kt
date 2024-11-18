package Entities

import android.graphics.Bitmap

class FinanceCategory {
    private var _id: Int = 0
    private var _name: String = ""
    private var _photo: Bitmap? = null

    constructor()
    constructor(id: Int, name: String, photo: Bitmap?) {
        this._id = id
        this._name = name
        this._photo = photo
    }

    var id: Int
        get() = this._id
        set(value) { this._id = value }

    var name: String
        get() = this._name
        set(value) { this._name = value }

    var photo: Bitmap?
        get() = this._photo
        set(value) { this._photo = value }
}
