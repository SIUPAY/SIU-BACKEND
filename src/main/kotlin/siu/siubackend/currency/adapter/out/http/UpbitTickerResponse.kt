package siu.siubackend.currency.adapter.out.http

import com.fasterxml.jackson.annotation.JsonProperty

data class UpbitTickerResponse(
    @JsonProperty("market")
    val market: String,
    
    @JsonProperty("trade_date")
    val tradeDate: String,
    
    @JsonProperty("trade_time")
    val tradeTime: String,
    
    @JsonProperty("trade_date_kst")
    val tradeDateKst: String,
    
    @JsonProperty("trade_time_kst")
    val tradeTimeKst: String,
    
    @JsonProperty("trade_timestamp")
    val tradeTimestamp: Long,
    
    @JsonProperty("opening_price")
    val openingPrice: Double,
    
    @JsonProperty("high_price")
    val highPrice: Double,
    
    @JsonProperty("low_price")
    val lowPrice: Double,
    
    @JsonProperty("trade_price")
    val tradePrice: Double,
    
    @JsonProperty("prev_closing_price")
    val prevClosingPrice: Double,
    
    @JsonProperty("change")
    val change: String,
    
    @JsonProperty("change_price")
    val changePrice: Double,
    
    @JsonProperty("change_rate")
    val changeRate: Double,
    
    @JsonProperty("signed_change_price")
    val signedChangePrice: Double,
    
    @JsonProperty("signed_change_rate")
    val signedChangeRate: Double,
    
    @JsonProperty("trade_volume")
    val tradeVolume: Double,
    
    @JsonProperty("acc_trade_price")
    val accTradePrice: Double,
    
    @JsonProperty("acc_trade_price_24h")
    val accTradePrice24h: Double,
    
    @JsonProperty("acc_trade_volume")
    val accTradeVolume: Double,
    
    @JsonProperty("acc_trade_volume_24h")
    val accTradeVolume24h: Double,
    
    @JsonProperty("highest_52_week_price")
    val highest52WeekPrice: Double,
    
    @JsonProperty("highest_52_week_date")
    val highest52WeekDate: String,
    
    @JsonProperty("lowest_52_week_price")
    val lowest52WeekPrice: Double,
    
    @JsonProperty("lowest_52_week_date")
    val lowest52WeekDate: String,
    
    @JsonProperty("timestamp")
    val timestamp: Long
)
