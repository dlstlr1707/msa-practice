package com.example.msaboardproject.database;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private final RoutingReplicas<String> routingReplicas;

    public RoutingDataSource(List<String> routingReplicas) {
        this.routingReplicas = new RoutingReplicas<>(routingReplicas);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (isReadOnly) {
            return routingReplicas.get();
        } else {
            return "master";
        }
    }
}
