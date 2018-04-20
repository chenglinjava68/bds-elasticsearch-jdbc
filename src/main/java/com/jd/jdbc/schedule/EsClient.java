package com.jd.jdbc.schedule;

import org.elasticsearch.client.Client;

/**
 * @author wanghong12
 * @date 2018/4/20 11:56
 */
public class EsClient {
    private Client client;

    public EsClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void close() {
        this.client.close();
    }
}
