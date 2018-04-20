package com.jd.jdbc.util;

import com.jd.jdbc.schedule.EsClient;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * @author wanghong12
 * @date 2018-4-20
 */
public class EsUtil {
    public static Logger logger = LoggerFactory.getLogger(EsUtil.class);
    private static EsClient client;

    private static EsClient createClient(String jdbcUrl) {
        if (StringUtils.isBlank(jdbcUrl)) {
            jdbcUrl = PropertiesUtils.getValue("jdbc.url");
        }
        TransportClient transportClient = null;
        String clusterName = PropertiesUtils.getValue("cluster.name");

        Settings settings = Settings.builder()
                .put("cluster.name", clusterName).build();
        try {
            transportClient = new PreBuiltTransportClient(settings);

            String hostAndPortArrayStr = jdbcUrl.split("/")[2];
            String[] hostAndPortArray = hostAndPortArrayStr.split(",");

            for (String hostAndPort : hostAndPortArray) {
                String host = hostAndPort.split(":")[0];
                String port = hostAndPort.split(":")[1];
                transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), Integer.parseInt(port)));
            }
        } catch (UnknownHostException e) {
            logger.error("", e);
        }
        return new EsClient(transportClient);
    }

    public static EsClient getClient() {
        return getClient(null);
    }

    public static EsClient getClient(String jdbcUrl) {
        if (client == null) {
            client = createClient(jdbcUrl);
        }
        return client;
    }

    public static EsClient getNewClient(String jdbcUrl) {
        EsClient esClient = createClient(jdbcUrl);
        return esClient;
    }

    /**
     * 插入数据
     *
     * @param index  索引
     * @param type   类型
     * @param params
     * @return
     */
    public static IndexResponse insert(String index, String type, Map params) {
        return getClient().getClient().prepareIndex(index, type).setSource(params).get();
    }

    /**
     * 插入数据,使用指定id作为文档id
     *
     * @param index  索引
     * @param type   类型
     * @param id     文档id
     * @param params
     * @return
     */
    public static IndexResponse insert(String index, String type, String id, Map params) {
        return getClient().getClient().prepareIndex(index, type).setId(id).setSource(params).get();
    }

    /**
     * 批量插入数据
     *
     * @param index  索引
     * @param type   类型
     * @param params
     * @return BulkResponse
     */
    public static BulkResponse insert(String index, String type, Map... params) {
        Client client = getClient().getClient();
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        for (Map param : params) {
            IndexRequestBuilder indexBuilder = client.prepareIndex(index, type).setSource(param);
            bulkRequestBuilder.add(indexBuilder);
        }
        return bulkRequestBuilder.get();
    }

    /**
     * 删除数据
     *
     * @param index 索引
     * @param type  类型
     * @param id    记录id
     * @return
     */
    public static DeleteResponse delete(String index, String type, String id) {
        return getClient().getClient().prepareDelete(index, type, id).get();
    }

    /**
     * 批量删除数据
     *
     * @param index 索引
     * @param type  类型
     * @param ids   文档id
     * @return
     */
    public static BulkResponse delete(String index, String type, String... ids) {
        Client client = getClient().getClient();
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        for (String id : ids) {
            DeleteRequestBuilder delBuilder = client.prepareDelete(index, type, id);
            bulkRequestBuilder.add(delBuilder);
        }
        return bulkRequestBuilder.get();
    }

    /**
     * 更新，如果id不存在，抛出异常
     *
     * @param index  索引
     * @param type   类型
     * @param id     文档id
     * @param params
     * @return
     */
    public static UpdateResponse update(String index, String type, String id, Map params) {
        return update(index, type, id, params, false);
    }

    /**
     * 批量更新,用作批量更新一组文档的某个字段，如更改state等等
     *
     * @param index  索引
     * @param type   类型
     * @param ids    文档id数组
     * @param params
     * @return
     */
    public static BulkResponse update(String index, String type, String[] ids, Map params) {
        Client client = getClient().getClient();
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        for (String id : ids) {
            UpdateRequestBuilder update = client.prepareUpdate(index, type, id);
            bulkRequestBuilder.add(update);
        }
        return bulkRequestBuilder.get();
    }

    /**
     * 更新
     *
     * @param index  索引
     * @param type   类型
     * @param params
     * @param upsert 是否更新插入，如果为true，在指定的_id不存在时执行插入操作,否则抛出异常
     * @return
     */
    public static UpdateResponse update(String index, String type, String id, Map params, boolean upsert) {
        return getClient().getClient().prepareUpdate(index, type, id)
                .setDocAsUpsert(upsert)
                .setDoc(params).get();
    }

    /**
     * 通过文档id查询
     *
     * @param index 索引
     * @param type  类型，可以为null
     * @param id    文档id
     * @return
     */
    public static GetResponse get(String index, String type, String id) {
        return getClient().getClient().prepareGet(index, type, id).get();
    }

}
