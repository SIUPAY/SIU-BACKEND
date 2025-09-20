package siu.siubackend.common.sui.domain

import java.util.*

/**
 * Sui 체인에서 발생하는 PaidEvent 도메인 모델
 * 모든 금액 필드는 MIST 단위로 저장됨 (Sui 네트워크에서 제공하는 원본 단위)
 */
data class SuiPaidEvent(
    val transactionDigest: String,
    val eventId: String,
    val orderIdentifier: UUID,
    val fromWalletAddress: String,
    val toWalletAddress: String,
    val totalCryptoAmount: Double, // MIST 단위
    val totalBrokerageFee: Double, // MIST 단위
    val blockNumber: Long,
    val timestamp: Long
) {
    
    companion object {
        /**
         * Sui RPC 응답에서 PaidEvent 파싱
         */
        fun fromSuiEvent(eventData: Map<String, Any>): SuiPaidEvent {
            val parsedJson = eventData["parsedJson"] as? Map<String, Any>
                ?: throw IllegalArgumentException("parsedJson not found in event data")
            
            val idMap = eventData["id"] as? Map<String, Any>
            val transactionDigest = idMap?.get("txDigest") as? String
                ?: throw IllegalArgumentException("Transaction digest not found")
                
            val eventSeq = idMap?.get("eventSeq") ?: "0"
            val eventId = "${transactionDigest}_${eventSeq}"
            
            return SuiPaidEvent(
                transactionDigest = transactionDigest,
                eventId = eventId,
                orderIdentifier = UUID.fromString(parsedJson["order_identifier"] as String),
                fromWalletAddress = parsedJson["from_wallet"] as String,
                toWalletAddress = parsedJson["to_wallet"] as String,
                totalCryptoAmount = (parsedJson["amount"] as Number).toDouble(), // MIST 단위
                totalBrokerageFee = (parsedJson["fee"] as Number).toDouble(), // MIST 단위
                blockNumber = (eventData["timestampMs"] as Number).toLong(),
                timestamp = (eventData["timestampMs"] as Number).toLong()
            )
        }

        // 필드명이 다른 경우를 위한 유연 파서 (amount_sent/fee_amount, sender/recipient 등)
        fun fromSuiEventFlexible(eventData: Map<String, Any>): SuiPaidEvent? {
            try {
                val parsedJson = eventData["parsedJson"] as? Map<String, Any>
                if (parsedJson == null) {
                    println("DEBUG: parsedJson is null in event: $eventData")
                    return null
                }
                
                val idMap = eventData["id"] as? Map<String, Any>
                if (idMap == null) {
                    println("DEBUG: id map is null in event: $eventData")
                    return null
                }
                
                val transactionDigest = idMap["txDigest"] as? String
                if (transactionDigest == null) {
                    println("DEBUG: txDigest is null in event: $eventData")
                    return null
                }
                
                val eventSeq = idMap["eventSeq"] ?: "0"
                val eventId = "${transactionDigest}_${eventSeq}"

                val orderIdRaw = (parsedJson["order_identifier"] as? String)
                if (orderIdRaw == null) {
                    println("DEBUG: order_identifier is null in parsedJson: $parsedJson")
                    return null
                }

                // 금액/수수료 필드 유연 매핑
                val amountAny = parsedJson["amount"]
                    ?: parsedJson["amount_sent"]
                    ?: parsedJson["net_amount"]
                val feeAny = parsedJson["fee"] ?: parsedJson["fee_amount"]

                val sender = (parsedJson["from_wallet"] as? String)
                    ?: (parsedJson["sender"] as? String)
                val recipient = (parsedJson["to_wallet"] as? String)
                    ?: (parsedJson["recipient"] as? String)

                if (amountAny == null || feeAny == null || sender == null || recipient == null) {
                    println("DEBUG: Missing required fields - amount: $amountAny, fee: $feeAny, sender: $sender, recipient: $recipient")
                    println("DEBUG: Full parsedJson: $parsedJson")
                    return null
                }

                val amount = when (amountAny) {
                    is Number -> amountAny.toDouble()
                    is String -> amountAny.toDoubleOrNull() ?: return null
                    else -> return null
                }
                val fee = when (feeAny) {
                    is Number -> feeAny.toDouble()
                    is String -> feeAny.toDoubleOrNull() ?: return null
                    else -> return null
                }

                val timestamp = (eventData["timestampMs"] as? Number)?.toLong() ?: System.currentTimeMillis()

                println("DEBUG: Successfully parsed SuiPaidEvent - tx: $transactionDigest, order: $orderIdRaw")

                return SuiPaidEvent(
                    transactionDigest = transactionDigest,
                    eventId = eventId,
                    orderIdentifier = UUID.fromString(orderIdRaw),
                    fromWalletAddress = sender,
                    toWalletAddress = recipient,
                    totalCryptoAmount = amount, // MIST 단위
                    totalBrokerageFee = fee, // MIST 단위
                    blockNumber = timestamp, // 블록넘버 정보 없으면 timestamp로 대체
                    timestamp = timestamp
                )
            } catch (e: Exception) {
                println("DEBUG: Exception in fromSuiEventFlexible: ${e.message}")
                println("DEBUG: Event data: $eventData")
                return null
            }
        }
    }
}
