package Entities

class Transaction {
    private var _id: String = ""
    private var _type: String = "" // "Income" o "Expense"
    private var _amount: Double = 0.0
    private var _category: String = ""
    private var _description: String = ""
    private var _date: String = "" // Formato: "YYYY-MM-DD"

    constructor()

    constructor(id: String, type: String, amount: Double, category: String, description: String, date: String) {
        this._id = id
        this._type = type
        this._amount = amount
        this._category = category
        this._description = description
        this._date = date
    }

    var id: String
        get() = this._id
        set(value) { this._id = value }

    var type: String
        get() = this._type
        set(value) { this._type = value }

    var amount: Double
        get() = this._amount
        set(value) { this._amount = value }

    var category: String
        get() = this._category
        set(value) { this._category = value }

    var description: String
        get() = this._description
        set(value) { this._description = value }

    var date: String
        get() = this._date
        set(value) { this._date = value }
}
