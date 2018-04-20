# elasticsearch-jdbc

####  maven依赖

上面是在GitHub上搭建的私人仓库
```xml
<dependency>
  <groupId>com.jd</groupId>
  <artifactId>bds-elasticsearch-jdbc</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

####  application.xml

```xml
<bean id="defaulteDataSource-es" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
		<property name="driverClassName" value="ElasticSearchDriver" />
		<property name="url" value="${jdbc.url}" />
		<property name="initialSize" value="2" />
		<property name="minIdle" value="2" />
		<property name="maxActive" value="${maxActive}" />
		<property name="maxWait" value="60000" />
		<property name="timeBetweenEvictionRunsMillis" value="60000" />
		<property name="minEvictableIdleTimeMillis" value="300000" />
		<property name="validationQuery" value="select 1 from dual" />
		<property name="testWhileIdle" value="true" />
		<property name="testOnBorrow" value="false" />
		<property name="testOnReturn" value="false" />
		<property name="poolPreparedStatements" value="false" />
		<property name="maxPoolPreparedStatementPerConnectionSize" value="20" />
		<property name="removeAbandoned" value="true" />
		<property name="removeAbandonedTimeout" value="1800" />
		<property name="logAbandoned" value="true" />
		<property name="filters" value="stat,log4j" />
		<property name="connectionProperties" value="druid.stat.slowSqlMillis=5000" />
	</bean>
	<bean id="defaultSQLSessionFactory-es" class="org.mybatis.spring.SqlSessionFactoryBean">
		<property name="dataSource" ref="defaulteDataSource-es" />
		<property name="configLocation" value="classpath:mybatis-config.xml"></property>
		<property name="mapperLocations">
			<array>
				<value>classpath*:com/**/model/es/*.xml</value>
			</array>
		</property>
	</bean>
	<bean id="defaultSQLSessionTemplate-es" class="org.mybatis.spring.SqlSessionTemplate">
		<constructor-arg index="0" ref="defaultSQLSessionFactory-es" />
	</bean>
	<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
		<property name = "dataSource" ref="defaulteDataSource-es"/>
	</bean>
```


#### 添加elasticsearch.properties 配置文件到classpath
elasticsearch.properties配置文件是必须的，可以直接如上面代码引入到spring配置文件中。java.url是TransportClient的地址，http.url是REST接口地址


```properties
#es java TransportClient address，split by "," if have multi address,eg.jdbc:elasticsearch://ip1:port,ip2:port
jdbc.url=jdbc:elasticsearch://192.168.70.128:9300
#es rest http address，split by "," if have multi address.eg http://ip1:port,http://ip2:port
http.url=http://192.168.70.128:9200
#max connection number
maxActive=20
#cluster name，ignored if you not setting
cluster.name=elasticsearch
```

