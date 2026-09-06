package com.example.demo.cdc;

import com.fasterxml.jackson.annotation.JsonProperty;


public record OrderCdcEvent(
        Payload payload
) {

    public record Payload(
            OrderData before,
            OrderData after,
            Source source,
            Transaction transaction,
            String op,

            @JsonProperty("ts_ms")
            Long timestampMillis,

            @JsonProperty("ts_us")
            Long timestampMicros,

            @JsonProperty("ts_ns")
            Long timestampNanos
    ) {
    }

    public record OrderData(
            Long id,
            String name,
            String address,
            Integer age,

            @JsonProperty("created_date")
            Long createdDate
    ) {
    }

    public record Source(
            String version,
            String connector,
            String name,

            @JsonProperty("ts_ms")
            Long timestampMillis,

            String snapshot,
            String db,
            String sequence,

            @JsonProperty("ts_us")
            Long timestampMicros,

            @JsonProperty("ts_ns")
            Long timestampNanos,

            String table,

            @JsonProperty("server_id")
            Long serverId,

            String gtid,
            String file,
            Long pos,
            Integer row,
            Long thread,
            String query
    ) {
    }

    public record Transaction(
            String id,

            @JsonProperty("total_order")
            Long totalOrder,

            @JsonProperty("data_collection_order")
            Long dataCollectionOrder
    ) {
    }
}
