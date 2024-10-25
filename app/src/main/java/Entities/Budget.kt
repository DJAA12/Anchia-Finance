package Entities

class Budget {
    private var _initialAmount: Double = 0.0
    private var _currentAmount: Double = 0.0

    constructor()

    constructor(initialAmount: Double, currentAmount: Double) {
        this._initialAmount = initialAmount
        this._currentAmount = currentAmount
    }

    var initialAmount: Double
        get() = this._initialAmount
        set(value) { this._initialAmount = value }

    var currentAmount: Double
        get() = this._currentAmount
        set(value) { this._currentAmount = value }
}
