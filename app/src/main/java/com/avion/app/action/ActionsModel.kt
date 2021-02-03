package com.avion.app.action

import com.avion.app.models.Payment
import com.avion.app.models.TarifObj
import java.io.Serializable

data class DriverRequest(val action: String = "getDriverInfo", val driverid: String)


data class RateDriverRequsest(val action: String, val id: String, val rating: Rating)

data class Rating(val comment: String, val rate: Double)

data class SimpleResponse(val success: Boolean)

data class OrderCalcPrice(
        val action: String = "calc",
        var region: Int,
        var from: Address,
        var to: Address
)

data class GetRegions(
        val action: String = "getRegions"
)

class MakeOrder : Serializable {
    constructor(user_phone: String) {
        this.user_phone = user_phone
    }

    constructor()

    var user_phone: String = ""
    var id: String? = null
    var driver_id: String? = null
    var status: Int = 0
    val action: String = "addOrder"
    var region: Any? = null
    var cartotime: Boolean? = false
    var from: Address? = null
    var to: Address? = null
    var date: String? = null
    var time: String? = null
    var tarif: TarifObj? = null
    var options: Option? = Option(ChildSeat(0, 0, 0),
            null,
            false,
            false,
            Language(false, false, false),
            false,
            false,
            false)
    var comments_to_the_driver: String? = null
    var train_number: String? = null
    var carriage_number: String? = null
    var flight_number: String? = null
    var departing_from: String? = null
    var total_price: Double? = 0.0
    var parking_price: Double? = null
    var sent_car: String? = null
    var nameOrder: String? = null
    var payment: Payment? = null
    var bonusPayment: Boolean? = null

}

class EmailConf : Serializable {
    var email: String = ""
    var phone: String = ""
}


class Address : Serializable {
    var fullAddress: String = ""
    var type: String = ""
    var geo: Geo = Geo()

    constructor(fullAddress: String, type: String, geo: Geo) {
        this.fullAddress = fullAddress
        this.type = type
        this.geo = geo
    }

    constructor()


}


class Geo : Serializable {
    var longitude: Double = 0.0
    var latitude: Double = 0.0

    constructor(longitude: Double, latitude: Double) {
        this.longitude = longitude
        this.latitude = latitude
    }

    constructor()

}


class Option : Serializable {
    constructor(child_seat: ChildSeat, meeting_with_a_table: String?, invoice_for_company: Boolean, car_with_yellow_plates: Boolean, driver_language: Language, smoking_salon: Boolean, pet_transportation: Boolean, oversized_baggage: Boolean) {
        this.child_seat = child_seat
        this.meeting_with_a_table = meeting_with_a_table
        this.invoice_for_company = invoice_for_company
        this.car_with_yellow_plates = car_with_yellow_plates
        this.driver_language = driver_language
        this.smoking_salon = smoking_salon
        this.pet_transportation = pet_transportation
        this.oversized_baggage = oversized_baggage
    }

    constructor()

    var child_seat: ChildSeat = ChildSeat()
    var meeting_with_a_table: String? = null
    var invoice_for_company: Boolean = false
    var car_with_yellow_plates: Boolean = false
    var driver_language: Language = Language()
    var smoking_salon: Boolean = false
    var pet_transportation: Boolean = false
    var oversized_baggage: Boolean = false
}

class ChildSeat : Serializable {
    var car_cradle: Int = 0
    var child_safety_seat: Int = 0
    var booster_seat: Int = 0

    constructor(car_cradle: Int, child_safety_seat: Int, booster_seat: Int) {
        this.car_cradle = car_cradle
        this.child_safety_seat = child_safety_seat
        this.booster_seat = booster_seat
    }

    constructor()

}


class Language : Serializable {
    var english: Boolean = false
    var german: Boolean = false
    var spain: Boolean = false

    constructor(english: Boolean, german: Boolean, spain: Boolean) {
        this.english = english
        this.german = german
        this.spain = spain
    }

    constructor()

}


data class CalcPrice(
        var from: Address? = null,
        var to: Address? = null,
        var region: Int = 0
) : Serializable {
    val action = "calc"
}