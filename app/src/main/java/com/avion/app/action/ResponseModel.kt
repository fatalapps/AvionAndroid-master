package com.avion.app.action

data class RegionResponse(
        var id: Int = 1,
        var name: String
)

data class TarifPricesResponse(
        var price: TarifPriceObj? = null
)

data class TarifPriceObj(
        var econom: String? = null,
        var comfort: String? = null,
        var premium: String? = null,
        var universal: String? = null,
        var business: String? = null,
        var businessPlus: String? = null,
        var minivan: String? = null,
        var minibus: String? = null,
        var minibusPlus: String? = null,
        var minibusVip: String? = null
)