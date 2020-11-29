package com.example.first

import kotlin.properties.Delegates
//enum Category

class UnitCategory(name: String, baseFirstUnit : String, baseSecondUnit: String, coef: Double) {
    private val _name = name
    private val _firstUnits = mutableMapOf<String, Double>(Pair(baseFirstUnit, 1.0))
    private val _secondUnits = mutableMapOf<String, Double>(Pair(baseSecondUnit, 1.0))
    private val _baseCoef : Double = coef

    public fun addFirstSetUnit(unitName : String, coefToBase : Double){
        _firstUnits[unitName] = coefToBase
    }
    public fun addSecondSetUnit(unitName : String, coefToBase : Double){
        _secondUnits[unitName] = coefToBase
    }
    public fun getCategoryName(): String {
        return _name
    }
    public fun getFirstUnits(): MutableSet<String> {
        return _firstUnits.keys
    }
    public fun getSecondUnits(): MutableSet<String> {
        return _secondUnits.keys
    }
    public fun getBase(unit : String) : Double? {
        if(_firstUnits.containsKey(unit))
            return _firstUnits[unit]
        if(_secondUnits.containsKey(unit))
            return _secondUnits[unit]
        else
            throw ArrayStoreException("There is no unit whit that name")
    }
    public fun getCoef(baseUnit : String) : Double {
        if(_firstUnits.containsKey(baseUnit)){
            return _baseCoef
        }
        if(_secondUnits.containsKey(baseUnit)){
            return 1/_baseCoef
        }
        else
            throw ArrayStoreException("There is no unit whit that name")
    }

}

class UnitManager{
    private val _categories = mutableListOf<UnitCategory>()

    public fun getCategories()
    : List<String> {
        return _categories.map{it.getCategoryName()}
    }

    public fun isEmpty() : Boolean = _categories.isEmpty()

    public fun getCategoriesFirstUnits(id : Int): List<String> {
        return _categories[id].getFirstUnits().toList()
    }
    public fun getCategoriesSecondUnits(id : Int): List<String> {
        return _categories[id].getSecondUnits().toList()
    }
    public fun addCategory(category: UnitCategory){
        _categories.add(category)
    }
    public fun translate(value: Double,
                         firstUnit : String,
                         secondUnit : String,
                         catId : Int
                         )
            : Double {
        val cat = _categories[catId]
        val result = value * cat.getBase(firstUnit)!! * cat.getCoef(firstUnit) / cat.getBase(secondUnit)!!
        return result
    }
}