package Entities

import android.graphics.Bitmap
import java.util.Date

class FinanceTransaction {
    private var _id: Int = 0
    private var _type: String = ""
    private var _category: String = ""
    private var _amount: Double = 0.0
    private var _description: String = ""
    private var _date: Date = Date()
    private var _photo: Bitmap? = null

    constructor()
    constructor(
        id: Int,
        type: String,
        category: String,
        amount: Double,
        description: String,
        date: Date,
        photo: Bitmap? = null
    ) {
        this._id = id
        this._type = type
        this._category = category
        this._amount = amount
        this._description = description
        this._date = date
        this._photo = photo
    }

    var id: Int
        get() = this._id
        set(value) {
            this._id = value
        }

    var type: String
        get() = this._type
        set(value) {
            this._type = value
        }

    var category: String
        get() = this._category
        set(value) {
            this._category = value
        }

    var amount: Double
        get() = this._amount
        set(value) {
            this._amount = value
        }

    var description: String
        get() = this._description
        set(value) {
            this._description = value
        }

    var date: Date
        get() = this._date
        set(value) {
            this._date = value
        }

    var photo: Bitmap?
        get() = this._photo
        set(value) {
            this._photo = value
        }
}
