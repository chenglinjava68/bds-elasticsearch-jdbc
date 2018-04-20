package com.jd.jdbc.schedule;

import com.jd.jdbc.util.EsUtil;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author wangjiajun
 * @date 2017/9/16 11:01
 * 心跳监测连接状态
 */
public class HeartBeatsSchedule {
    public static Logger logger = LoggerFactory.getLogger(HeartBeatsSchedule.class);
    private static ScheduledExecutorService heartBeatsSchedule = Executors.newSingleThreadScheduledExecutor();
    private static Set<EsClient> clientSet = new HashSet<>();
    private static String jdbcUrl = "";
    private static volatile boolean isScheduled = false;

    private HeartBeatsSchedule() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("active client size:" + clientSet.size());
                if (clientSet.size() > 0) {
                    for (EsClient client : clientSet) {
                        client.getClient().close();
                    }
                }
                logger.info("all active client connection had been colsed");
                heartBeatsSchedule.shutdown();
            }
        });
    }

    public void startSchedule() {
        isScheduled = true;
        heartBeatsSchedule.scheduleAtFixedRate(new HeartBeatsTask(), 2, 2, TimeUnit.SECONDS);
    }

    public static synchronized void registryClient(EsClient client, String jdbcUrl) {
        HeartBeatsSchedule.jdbcUrl = jdbcUrl;
        clientSet.add(client);
        if (!isScheduled) {
            new HeartBeatsSchedule().startSchedule();
        }
    }

    public static void unRegistryClient(EsClient client) {
        clientSet.remove(client);
    }

    private class HeartBeatsTask implements Runnable {
        @Override
        public void run() {
            for (EsClient client : HeartBeatsSchedule.clientSet) {
                try {
                    ClusterHealthResponse healths = client.getClient().admin().cluster().prepareHealth().get();
                    logger.debug("elasticsearch status:" + ClusterHealthStatus.fromValue(healths.getStatus().value()));
                } catch (Exception e) {
                    logger.error("an error occurs when TransportClient connect status heartbeats checking", e);
                    logger.info("trying to create a new TransportClient for the connection");
                    try {//尝试将原来的旧客户端连接关闭一次
                        client.getClient().close();
                    } catch (Exception ex) {

                    }
                    EsClient newClient = EsUtil.getNewClient(HeartBeatsSchedule.jdbcUrl);
                    client.setClient(newClient.getClient());
                    logger.info("a new TransportClient has been created after error occurs");
                }
            }
        }
    }

}
